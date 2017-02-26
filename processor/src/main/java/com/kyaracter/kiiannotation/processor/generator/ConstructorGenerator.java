package com.kyaracter.kiiannotation.processor.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;

import javax.lang.model.element.Modifier;


public class ConstructorGenerator {
    public static MethodSpec constructorApp(String name) {
        return MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addStatement("this.kiiObject = com.kii.cloud.storage.Kii.bucket(\"$N\").object()", name)
                .build();
    }

    public static MethodSpec constructorGroup(String name) {
        ClassName className = ClassName.get("com.kii.cloud.storage", "KiiGroup");
        ParameterSpec parameterSpec = ParameterSpec.builder(className, "kiiGroup").build();

        return MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(parameterSpec)
                .addStatement("this.kiiObject = kiiGroup.bucket(\"$N\").object()", name)
                .build();
    }

    public static MethodSpec constructor(String name) {
        ClassName className = ClassName.get("com.kii.cloud.storage", "KiiObject");
        ParameterSpec parameterSpec = ParameterSpec.builder(className, "kiiObject").build();

        return MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(parameterSpec)
                .addStatement("this.kiiObject = kiiObject", name)
                .build();
    }

    public static MethodSpec constructorUser(String name) {
        return MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addStatement("this.kiiObject = com.kii.cloud.storage.Kii.user().bucket(\"$N\").object()", name)
                .build();
    }

    public static MethodSpec constructorCreate(ClassName className) {
        return MethodSpec
                .methodBuilder("create")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addStatement("return new $L()", className)
                .returns(className)
                .build();
    }

    public static MethodSpec constructorCreateGroup(ClassName className) {
        ClassName className1 = ClassName.get("com.kii.cloud.storage", "KiiGroup");
        ParameterSpec parameterSpec = ParameterSpec.builder(className1, "kiiGroup").build();

        return MethodSpec
                .methodBuilder("create")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(parameterSpec)
                .addStatement("return new $L($L)", className, "kiiGroup")
                .returns(className)
                .build();
    }

    public static MethodSpec constructorFrom(ClassName className) {
        ClassName className1 = ClassName.get("com.kii.cloud.storage", "KiiObject");
        ParameterSpec parameterSpec = ParameterSpec.builder(className1, "kiiObject").build();

        return MethodSpec
                .methodBuilder("from")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(parameterSpec)
                .addStatement("return new $L($L)", className, "kiiObject")
                .returns(className)
                .build();
    }
}
