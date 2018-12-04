package com.model;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FileLineCounterManager {

    private final File file;
    private final boolean recursive, verbose;

    @NotNull
    public final AFileLineCounter getInstance() {
        return recursive ?
                new RecursiveFileLineCounter(file, verbose) :
                new BasicLineCounter(file);
    }

    public FileLineCounterManager(final File file, final boolean recursive, final boolean verbose) {
        this.file = file;
        this.recursive = recursive;
        this.verbose = verbose;
    }
}
