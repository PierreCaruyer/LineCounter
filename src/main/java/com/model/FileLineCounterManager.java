package com.model;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FileLineCounterManager {

    @NotNull
    public static final AFileLineCounter getInstance(final File file, final boolean recursive, final boolean verbose) {
        return recursive ? new RecursiveFileLineCounter(file, verbose) : new BasicLineCounter(file);
    }
}
