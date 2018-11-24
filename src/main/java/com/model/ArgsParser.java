package com.model;

import java.util.Arrays;
import java.util.List;

public class ArgsParser {

    private final List<String> args;
    private boolean timer = false, verbose = false, helpActive = false, nullOrEmpty = false;

    public ArgsParser(final String ... args) {
        this(Arrays.asList(args));
    }

    public ArgsParser(final List<String> args) {
        this.args = args;
    }

    public void parse() {
        if(args == null || args.size() == 0) {
            nullOrEmpty = true;
        }
        if(args.contains("-h") || args.contains("--help")) {
            helpActive = true;
        }
        if(args.contains("--verbose") || args.contains("-v")) {
            verbose = true;
        }
        if(args.contains("-t") || args.contains("--timer")) {
            timer = true;
        }
    }

    public final boolean isNullOrEmpty() {
        return nullOrEmpty;
    }

    public final boolean isHelpActive() {
        return helpActive;
    }

    public final boolean isTimerActive() {
        return timer;
    }

    public final boolean isVerboseActive() {
        return verbose;
    }
}
