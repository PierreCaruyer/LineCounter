package com.model;

import java.io.File;

abstract public class AFileLineCounter {

    protected final File file;

    public AFileLineCounter(final File file) {
        this.file = file;
    }

    public abstract int count();
}
