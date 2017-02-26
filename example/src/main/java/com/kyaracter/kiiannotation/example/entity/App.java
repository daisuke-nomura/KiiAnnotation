package com.kyaracter.kiiannotation.example.entity;

import com.kyaracter.kiiannotation.annotations.ApplicationScope;
import com.kyaracter.kiiannotation.annotations.Key;


@ApplicationScope(name = "app", simplify = false, builder = false, suffix = "Data")
public class App {
    @Key(key = "app")
    private String name;

    @Key(key = "count")
    private int count;
}
