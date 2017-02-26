package com.kyaracter.kiiannotation.processor.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;

import javax.lang.model.element.Modifier;


public class FieldGenerator {
    public static FieldSpec fieldKiiObject() {
        ClassName className = ClassName.get("com.kii.cloud.storage", "KiiObject");
        return FieldSpec
                .builder(className, "kiiObject")
                .addModifiers(Modifier.PRIVATE)
                .build();
    }
}
