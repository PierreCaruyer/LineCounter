package com.model;

import java.io.File;

public class FileLineCounterManager {

    private final File file;
    private final boolean recursive, verbose;

    public final AFileLineCounter getInstance() {
        final AFileLineCounter lineCounter = recursive ?
                new RecursiveFileLineCounter(file, verbose) :
                new BasicLineCounter(file);
        return lineCounter;
    }

    public FileLineCounterManager(final File file, final boolean recursive, final boolean verbose) {
        this.file = file;
        this.recursive = recursive;
        this.verbose = verbose;
    }
}
