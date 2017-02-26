package com.kyaracter.kiiannotation.processor;


import java.util.regex.Pattern;

public class Restriction {
    private static boolean isReservedBucketName(String name) {
        switch (name) {
            case "users": return true;
            case "devices": return true;
            case "internal": return true;
            case "things": return true;
        }

        return false;
    }

    private static boolean isStartUnderScore(String name) {
        return Pattern.compile("^_").matcher(name).find();
    }

    private static boolean isCollectNameContentAndLength(String name) {
        return Pattern.compile("^[A-Za-z0-9]{2,64}$").matcher(name).find();
    }

    public static boolean checkKii(String name) {
        return !isReservedBucketName(name) && !isStartUnderScore(name) && isCollectNameContentAndLength(name);
    }

    private static boolean isReservedMethodName(String name) {
        switch (name) {
            case "this": return true;
            case "kiiObject": return true;
            case "getKiiObject": return true;
            case "create": return true;
            case "from": return true;
        }

        return false;
    }

    public static boolean checkJava(String name) {
        return !isReservedMethodName(name);
    }
}
