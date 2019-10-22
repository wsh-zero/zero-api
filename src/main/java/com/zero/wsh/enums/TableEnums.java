package com.zero.wsh.enums;

public enum TableEnums {
    YES("YES"),
    NO("NO");
    private String key;

    TableEnums(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
    }
