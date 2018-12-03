package com.model;

import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public class ArgsParser {

    private final List<String> args;
    private boolean timer = false, verbose = false, helpActive = false, nullOrEmpty = false, recursive = false;

    public ArgsParser(final String ... args) {
        this(Arrays.asList(args));
    }

    public ArgsParser(final List<String> args) {
        this.args = args;
    }

    public ArgsParser parse() {
        if(args == null || args.size() == 0) {
            nullOrEmpty = true;
        }
        if(args.contains("-h") || args.contains("--help")) {
            helpActive = true;
        }
        if(args.contains("-v") || args.contains("--verbose")) {
            verbose = true;
        }
        if(args.contains("-t") || args.contains("--timer")) {
            timer = true;
        }
        if(args.contains("-r") || args.contains("--recursive")) {
            recursive = true;
        }
        return this;
    }

    @Contract(pure = true)
    public final boolean isNullOrEmpty() {
        return nullOrEmpty;
    }

    @Contract(pure = true)
    public final boolean isHelpActive() {
        return helpActive;
    }

    @Contract(pure = true)
    public final boolean isTimerActive() {
        return timer;
    }

    @Contract(pure = true)
    public final boolean isVerboseActive() {
        return verbose;
    }

    @Contract(pure = true)
    public final boolean isRecursive() {
        return recursive;
    }
}
