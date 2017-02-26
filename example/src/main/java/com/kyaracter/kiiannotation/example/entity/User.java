package com.kyaracter.kiiannotation.example.entity;


import com.kyaracter.kiiannotation.annotations.Key;
import com.kyaracter.kiiannotation.annotations.UserScope;

@UserScope(name = "user", builder = false)
public class User {
    @Key(key = "user")
    private String name;
}
