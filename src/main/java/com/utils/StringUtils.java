package com.utils;

import org.jetbrains.annotations.Contract;

public class StringUtils {

    @Contract("null -> true")
    static public final boolean isNullOrEmpty(final String string) {
        return string == null || string.trim().isEmpty();
    }
}
