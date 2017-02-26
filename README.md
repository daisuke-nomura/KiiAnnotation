# KiiAnnotation: Annotation Processor for KiiObject

[![Release](https://jitpack.io/v/daisuke-nomura/kiiannotation.svg)](https://jitpack.io/#daisuke-nomura/kiiannotation)

KiiAnnotation is an Annotation Processor library for [KiiObject][kiiObject] of [Kii Cloud][kii].  
This library generates wrapper methods for KiiObject and builder of itself.  

##Sample usage

First of all, define KiiBucket name and KiiObject details. 

    @GroupScope(name = "hoge")
    public class Group {
        @Key(key = "fuga")
        private String name;
    }

Next, select "Build APK".

    public class GroupBucket {
      private KiiObject kiiObject;
    
      private GroupBucket(KiiGroup kiiGroup) {
        this.kiiObject = kiiGroup.bucket("hoge").object();
      }
    
      private GroupBucket(KiiObject kiiObject) {
        this.kiiObject = kiiObject;
      }
    
      public static GroupBucket create(KiiGroup kiiGroup) {
        return new com.kyaracter.kiiannotation.example.entity.GroupBucket(kiiGroup);
      }
    
      public static GroupBucket from(KiiObject kiiObject) {
        return new com.kyaracter.kiiannotation.example.entity.GroupBucket(kiiObject);
      }
    
      public KiiObject kiiObject() {
        return this.kiiObject;
      }
    
      public void name(String name) {
        this.kiiObject.set("fuga", name);
      }
    
      public String name() {
        return this.kiiObject.getString("fuga");
      }
    
      public static class Builder {
        private String name;
    
        private KiiGroup kiiGroup;
    
        public Builder(KiiGroup kiiGroup) {
          this.kiiGroup = kiiGroup;
        }
    
        public GroupBucket.Builder name(String name) {
          this.name = name;
          return this;
        }
    
        public GroupBucket build() {
          GroupBucket groupbucket = new GroupBucket(this.kiiGroup);
          groupbucket.name(this.name);
          return groupbucket;
        }
      }
    }


After that, write your code.

    GroupBucket groupBucket = new GroupBucket.Builder(Kii.group("aaa"))
            .name("bbb")
            .build();


If you like get/set**** or don't like builder, you could enable or disable it.


###Application Scope
@ApplicationScope

    @ApplicationScope(name = "app", simplify = false, builder = false, suffix = "Data")
    public class App {
        @Key(key = "app")
        private String name;
    
        @Key(key = "count")
        private int count;
    }

then

    public class AppData {
      private KiiObject kiiObject;
    
      private AppData() {
        this.kiiObject = com.kii.cloud.storage.Kii.bucket("app").object();
      }
    
      private AppData(KiiObject kiiObject) {
        this.kiiObject = kiiObject;
      }
    
      public static AppData create() {
        return new com.kyaracter.kiiannotation.example.entity.AppData();
      }
    
      public static AppData from(KiiObject kiiObject) {
        return new com.kyaracter.kiiannotation.example.entity.AppData(kiiObject);
      }
    
      public KiiObject getKiiObject() {
        return this.kiiObject;
      }
    
      public void setName(String name) {
        this.kiiObject.set("app", name);
      }
    
      public void setCount(int count) {
        this.kiiObject.set("count", count);
      }
    
      public String getName() {
        return this.kiiObject.getString("app");
      }
    
      public int getCount() {
        return this.kiiObject.getInt("count");
      }
    }

You could change suffix of class name.  
The default suffix is 'Bucket'.


###Group Scope
@GroupScope

refer to sample usage.

###User Scope
@UserScope

    @UserScope(name = "user", builder = false)
    public class User {
        @Key(key = "user")
        private String name;
    }

then

    public class UserBucket {
      private KiiObject kiiObject;
    
      private UserBucket() {
        this.kiiObject = com.kii.cloud.storage.Kii.user().bucket("user").object();
      }
    
      private UserBucket(KiiObject kiiObject) {
        this.kiiObject = kiiObject;
      }
    
      public static UserBucket create() {
        return new com.kyaracter.kiiannotation.example.entity.UserBucket();
      }
    
      public static UserBucket from(KiiObject kiiObject) {
        return new com.kyaracter.kiiannotation.example.entity.UserBucket(kiiObject);
      }
    
      public KiiObject kiiObject() {
        return this.kiiObject;
      }
    
      public void name(String name) {
        this.kiiObject.set("user", name);
      }
    
      public String name() {
        return this.kiiObject.getString("user");
      }
    }

##Binaries

    allprojects {
        repositories {
            maven { url "https://jitpack.io" }
        }
    }

and

    dependencies {
        annotationProcessor 'com.github.daisuke-nomura.KiiAnnotation:processor:0.1.0'
        compile 'com.github.daisuke-nomura:KiiAnnotation:0.1.0'
    }

If you using Android Studio 2.2 or below, use [android-apt][apt] instead of 'annotationProcessor'.

##Bugs and Feedback

Please use [GitHub Issues][issues].

##License

   Copyright 2017 Daisuke Nomura

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
  
   http://www.apache.org/licenses/LICENSE-2.0
  
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

[kiiObject]: http://docs.kii.com/references/android/storage/latest/com/kii/cloud/storage/KiiObject.html
[kii]: https://jp.kii.com/
[apt]: https://bitbucket.org/hvisser/android-apt
[issues]: https://github.com/daisuke-nomura/KiiAnnotation/issues