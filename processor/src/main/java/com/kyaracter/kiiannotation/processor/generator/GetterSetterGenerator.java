package com.kyaracter.kiiannotation.processor.generator;

import com.kyaracter.kiiannotation.annotations.Key;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import java.util.Locale;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;


public class GetterSetterGenerator {
    public static MethodSpec getKiiObject(boolean simplify) {
        ClassName className = ClassName.get("com.kii.cloud.storage", "KiiObject");
        String getter = simplify ? "kiiObject" : getNotSimplifyName("get", "kiiObject");

        return MethodSpec
                .methodBuilder(getter)
                .addModifiers(Modifier.PUBLIC)
                .returns(className)
                .addStatement("return this.kiiObject")
                .build();
    }

    public static MethodSpec setter(Element element, boolean simplify) {
        Key key = element.getAnnotation(Key.class);
        String name = element.getSimpleName().toString();

        ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.get(element.asType()), name).build();

        String setter = simplify ? name : getNotSimplifyName("set", name);

        return MethodSpec
                .methodBuilder(setter)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(parameterSpec)
                .addStatement("this.kiiObject.set(\"$N\", $N)", key.key(), name)
                .build();
    }

    public static MethodSpec getter(Element element, boolean simplify) {
        Key key = element.getAnnotation(Key.class);
        String name = element.getSimpleName().toString();
        TypeName typeName = TypeName.get(element.asType());

        String getter = simplify ? name : getNotSimplifyName("get", name);

        String type = getKiiGetter(typeName);

        return MethodSpec
                .methodBuilder(getter)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.get(element.asType()))
                .addStatement("return this.kiiObject.get$N(\"$N\")", type, key.key())
                .build();
    }

    public static String getKiiGetter(TypeName typeName) {
        if (typeName.isPrimitive()) {
            return String.format(Locale.US, "%s%s", typeName.toString().substring(0, 1).toUpperCase(), typeName.toString().substring(1, typeName.toString().length()));
        } else {
            String[] strings = typeName.toString().split("\\.");
            return strings[strings.length - 1];
        }
    }

    private static String getNotSimplifyName(String prefix, String name) {
        return String.format(Locale.US, "%s%s%s", prefix, name.substring(0, 1).toUpperCase(), name.substring(1, name.length()));
    }
}
