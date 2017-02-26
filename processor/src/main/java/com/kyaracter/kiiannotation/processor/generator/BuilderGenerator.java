package com.kyaracter.kiiannotation.processor.generator;


import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;

public class BuilderGenerator {
    public static TypeSpec.Builder builderInit() {
        return TypeSpec
                .classBuilder("Builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
    }

    public static FieldSpec builderField(Element element) {
        Name name = element.getSimpleName();

        return FieldSpec
                .builder(TypeName.get(element.asType()), name.toString())
                .addModifiers(Modifier.PRIVATE)
                .build();
    }

    public static MethodSpec builderMethod(ClassName className, Element element) {
        Name name = element.getSimpleName();

        ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.get(element.asType()), name.toString()).build();

        return MethodSpec
                .methodBuilder(name.toString())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(parameterSpec)
                .addStatement("this.$N = $N", name.toString(), name.toString())
                .returns(className)
                .addStatement("return this")
                .build();
    }

    public static MethodSpec builderBuild(ClassName className, List<MethodSpec> methodSpecList, List<FieldSpec> fieldSpecList) {
        String name = className.simpleName();
        CodeBlock.Builder codeBlock = CodeBlock.builder();

        for (int i = 0; i < methodSpecList.size(); i++) {
            codeBlock.add("$N.$N(this.$N);\n", name.toLowerCase(), methodSpecList.get(i).name, fieldSpecList.get(i).name);
        }

        return MethodSpec
                .methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$N $N = new $N()", name, name.toLowerCase(), name)
                .addCode(codeBlock.build())
                .returns(className)
                .addStatement("return $N", name.toLowerCase())
                .build();
    }

    public static FieldSpec defaultGroupField() {
        ClassName className = ClassName.get("com.kii.cloud.storage", "KiiGroup");

        return FieldSpec
                .builder(className, "kiiGroup")
                .addModifiers(Modifier.PRIVATE)
                .build();
    }

    public static MethodSpec constructorGroupMethod() {
        ClassName className = ClassName.get("com.kii.cloud.storage", "KiiGroup");
        ParameterSpec parameterSpec = ParameterSpec.builder(className, "kiiGroup").build();

        return MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(parameterSpec)
                .addStatement("this.kiiGroup = kiiGroup")
                .build();
    }

    public static MethodSpec builderGroupBuild(ClassName className, List<MethodSpec> methodSpecList, List<FieldSpec> fieldSpecList) {
        String name = className.simpleName();
        CodeBlock.Builder codeBlock = CodeBlock.builder();

        for (int i = 0; i < methodSpecList.size(); i++) {
            codeBlock.add("$N.$N(this.$N);\n", name.toLowerCase(), methodSpecList.get(i).name, fieldSpecList.get(i).name);
        }

        return MethodSpec
                .methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$N $N = new $N($N)", name, name.toLowerCase(), name, "this.kiiGroup")
                .addCode(codeBlock.build())
                .returns(className)
                .addStatement("return $N", name.toLowerCase())
                .build();
    }
}
