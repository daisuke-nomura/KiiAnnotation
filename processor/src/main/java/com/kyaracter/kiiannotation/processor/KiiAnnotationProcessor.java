package com.kyaracter.kiiannotation.processor;


import com.kyaracter.kiiannotation.annotations.ApplicationScope;
import com.kyaracter.kiiannotation.annotations.GroupScope;
import com.kyaracter.kiiannotation.annotations.Key;
import com.kyaracter.kiiannotation.annotations.UserScope;
import com.kyaracter.kiiannotation.processor.exception.IllegalAnnotationException;
import com.kyaracter.kiiannotation.processor.exception.IllegalBucketNameException;
import com.kyaracter.kiiannotation.processor.generator.ConstructorGenerator;
import com.kyaracter.kiiannotation.processor.generator.FieldGenerator;
import com.kyaracter.kiiannotation.processor.generator.GetterSetterGenerator;
import com.kyaracter.kiiannotation.processor.generator.BuilderGenerator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        "com.kyaracter.kiiannotation.annotations.ApplicationScope",
        "com.kyaracter.kiiannotation.annotations.GroupScope",
        "com.kyaracter.kiiannotation.annotations.Key",
        "com.kyaracter.kiiannotation.annotations.UserScope"
})
public class KiiAnnotationProcessor extends AbstractProcessor{

    private Class<? extends Annotation> cls;
    private boolean simplify;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, final RoundEnvironment roundEnvironment) {
        List<Class<? extends Annotation>> list = new ArrayList<>();
        list.add(ApplicationScope.class);
        list.add(GroupScope.class);
        list.add(UserScope.class);

        list.stream()
                .flatMap((Function<Class<? extends Annotation>, Stream<? extends Element>>) aClass -> {
                    cls = aClass;
                    return roundEnvironment.getElementsAnnotatedWith(aClass).stream();
                })
                .forEach(element -> {
                    final TypeElement typeElement = (TypeElement) element;

                    String bucketName;
                    boolean withBuilder;
                    final String packageName = processingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString();
                    final String regularClass = getRegularClassString(cls, typeElement);

                    final ClassName regularClassName = ClassName.get(packageName, regularClass);
                    final ClassName builderClassName = ClassName.get(packageName, regularClass + ".Builder");

                    //create regular class
                    final TypeSpec.Builder regularBuilder = TypeSpec
                            .classBuilder(regularClassName)
                            .addModifiers(Modifier.PUBLIC);

                    regularBuilder.addField(FieldGenerator.fieldKiiObject());

                    if (cls.equals(ApplicationScope.class)) {
                        ApplicationScope appBucketObject = typeElement.getAnnotation(ApplicationScope.class);
                        bucketName = appBucketObject.name();
                        simplify = appBucketObject.simplify();
                        withBuilder = appBucketObject.builder();
                    } else if (cls.equals(GroupScope.class)) {
                        GroupScope groupBucketObject = typeElement.getAnnotation(GroupScope.class);
                        bucketName = groupBucketObject.name();
                        simplify = groupBucketObject.simplify();
                        withBuilder = groupBucketObject.builder();
                    } else if (cls.equals(UserScope.class)) {
                        UserScope userBucketObject = typeElement.getAnnotation(UserScope.class);
                        bucketName = userBucketObject.name();
                        simplify = userBucketObject.simplify();
                        withBuilder = userBucketObject.builder();
                    } else {
                        throw new IllegalAnnotationException();
                    }

                    if (!Restriction.checkKii(bucketName)) {
                        throw new IllegalBucketNameException();
                    }

                    regularBuilder.addMethods(createConstructors(cls, bucketName, regularClassName));

                    regularBuilder.addMethod(GetterSetterGenerator.getKiiObject(simplify));

                    final List<MethodSpec> setterSpecList = new ArrayList<>();
                    final List<MethodSpec> getterSpecList = new ArrayList<>();
                    final List<FieldSpec> builderFieldSpecList = new ArrayList<>();
                    final List<MethodSpec> builderMethodSpecList = new ArrayList<>();

                    typeElement.getEnclosedElements()
                            .stream()
                            .filter((Predicate<Element>) element1 -> element1 instanceof VariableElement)
                            .forEach((Consumer<Element>) element2 -> {
                                if (element2.getAnnotation(Key.class) != null) {
                                    System.out.println("Key");
                                    setterSpecList.add(GetterSetterGenerator.setter(element2, simplify));
                                    getterSpecList.add(GetterSetterGenerator.getter(element2, simplify));
                                    builderFieldSpecList.add(BuilderGenerator.builderField(element2));
                                    builderMethodSpecList.add(BuilderGenerator.builderMethod(builderClassName, element2));
                                } else {
                                    throw new IllegalAnnotationException();
                                }
                            });

                    regularBuilder.addMethods(setterSpecList);
                    regularBuilder.addMethods(getterSpecList);

                    //create builder class in regular class
                    final TypeSpec.Builder builderBuilder = BuilderGenerator.builderInit();

                    if (withBuilder) {
                        builderBuilder.addFields(builderFieldSpecList);
                        builderBuilder.addMethods(builderMethodSpecList);

                        if (cls.equals(GroupScope.class)) {
                            //Group
                            builderBuilder.addField(BuilderGenerator.defaultGroupField());
                            builderBuilder.addMethod(BuilderGenerator.constructorGroupMethod());
                            builderBuilder.addMethod(BuilderGenerator.builderGroupBuild(regularClassName, setterSpecList, builderFieldSpecList));
                        } else {
                            builderBuilder.addMethod(BuilderGenerator.builderBuild(regularClassName, setterSpecList, builderFieldSpecList));
                        }

                        regularBuilder.addType(builderBuilder.build());
                    }

                    //write
                    final JavaFile javaFile = JavaFile.builder(packageName, regularBuilder.build()).build();

                    try {
                        javaFile.writeTo(processingEnv.getFiler());
                    } catch (IOException ioe) {

                    }
                });

        return false;
    }

    private static String getRegularClassString(Class<? extends Annotation> cls, TypeElement typeElement) {
        if (cls.equals(ApplicationScope.class)) {
            ApplicationScope appBucketObject = typeElement.getAnnotation(ApplicationScope.class);
            return typeElement.getSimpleName() + appBucketObject.suffix();
        } else if (cls.equals(GroupScope.class)) {
            GroupScope groupBucketObject = typeElement.getAnnotation(GroupScope.class);
            return typeElement.getSimpleName() + groupBucketObject.suffix();
        } else if (cls.equals(UserScope.class)) {
            UserScope userBucketObject = typeElement.getAnnotation(UserScope.class);
            return typeElement.getSimpleName() + userBucketObject.suffix();
        } else {
            throw new IllegalAnnotationException();
        }
    }

    private static List<MethodSpec> createConstructors(Class<? extends Annotation> cls, String bucketName, ClassName regularClassName) {
        List<MethodSpec> methodSpecList = new ArrayList<>();

        if (cls.equals(ApplicationScope.class)) {
            methodSpecList.add(ConstructorGenerator.constructorApp(bucketName));
            methodSpecList.add(ConstructorGenerator.constructor(bucketName));
            methodSpecList.add(ConstructorGenerator.constructorCreate(regularClassName));
            methodSpecList.add(ConstructorGenerator.constructorFrom(regularClassName));
        } else if (cls.equals(GroupScope.class)) {
            methodSpecList.add(ConstructorGenerator.constructorGroup(bucketName));
            methodSpecList.add(ConstructorGenerator.constructor(bucketName));
            methodSpecList.add(ConstructorGenerator.constructorCreateGroup(regularClassName));
            methodSpecList.add(ConstructorGenerator.constructorFrom(regularClassName));
        } else if (cls.equals(UserScope.class)) {
            methodSpecList.add(ConstructorGenerator.constructorUser(bucketName));
            methodSpecList.add(ConstructorGenerator.constructor(bucketName));
            methodSpecList.add(ConstructorGenerator.constructorCreate(regularClassName));
            methodSpecList.add(ConstructorGenerator.constructorFrom(regularClassName));
        } else {
            throw new IllegalAnnotationException();
        }

        return methodSpecList;
    }
}
