package com.model;

import com.utils.ArraysUtils;
import com.utils.CollectionsUtils;
import org.jetbrains.annotations.Contract;

import java.util.List;

public class ArgsParser {

    private final List<String> args;
    private boolean timer = false, verbose = false, helpActive = false, nullOrEmpty = false, recursive = false;
    private String path = null;

    public ArgsParser(final String ... args) {
        this(new ArraysUtils<>(args).asList());
    }

    public ArgsParser(final List<String> args) {
        this.args = args;
    }

    public ArgsParser parse() {
        if(CollectionsUtils.isEmpty(args)) {
            nullOrEmpty = true;
            return this;
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
        path = args.get(args.size() - 1);
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

    public final String getPath() {
        return path;
    }
}
