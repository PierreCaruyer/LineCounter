package com.model;

import com.utils.FilesUtils;

import java.io.File;
import java.util.Collection;

public class BasicLineCounter extends AFileLineCounter {
    public BasicLineCounter(final File file) {
        super(file);
    }

    @Override
    public int count() {
        if(!FilesUtils.fileExists(file)) {
            return 0;
        }
        if(file.isFile()) {
            return FilesUtils.getFileLines(file);
        }
        final Collection<File> dirCollection = FilesUtils.subDirs(file);
        if(dirCollection == null) {
            return 0;
        }
        int count = 0;
        for(final File aDirFile : dirCollection) {
            System.out.println("Entering dir : " + aDirFile.getAbsolutePath() + " ...");
            count += FilesUtils.getFileLines(aDirFile);
            System.out.println("Counted " + count + " lines.");
        }
        return count;
    }
}
