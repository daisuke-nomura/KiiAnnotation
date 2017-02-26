package com.kyaracter.kiiannotation.example.entity;

import com.kyaracter.kiiannotation.annotations.GroupScope;
import com.kyaracter.kiiannotation.annotations.Key;


@GroupScope(name = "hoge")
public class Group {
    @Key(key = "fuga")
    private String name;
}
