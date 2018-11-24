package com.model;

import com.utils.FilesUtils;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

public class RecursiveFileLineCounter extends AFileLineCounter {
    private final boolean isVerbose;

    public RecursiveFileLineCounter(final File file, final boolean isVerbose) {
        super(file);
        this.isVerbose = isVerbose;
    }

    public RecursiveFileLineCounter(final String filepath, final boolean isVerbose) {
        this(new File(filepath), isVerbose);
    }

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
        final Iterator<File> dir = dirCollection.iterator();
        while(dir.hasNext()) {
            final String name = dir.next().getName();
            final String nextFileAbsolutePath = file.getAbsolutePath() + "/" + name + "/";
            if(isVerbose) {
                System.out.println("Entering dir : " + nextFileAbsolutePath + " ...");
                System.out.println("Counted " + count + " lines.");
            }
            final AFileLineCounter subCounter = new RecursiveFileLineCounter(nextFileAbsolutePath, isVerbose);
            count += subCounter.count();
        }
        return count + directoryLines(FilesUtils.dirFiles(file));
    }

    private static int directoryLines(final Collection<File> files) {
        return directoryLinesHelper(files, 0);
    }

    private static int directoryLinesHelper(final Collection<File> files, final int currentCount) {
        if(files == null) {
            return 0;
        }
        return directoryLinesHelper(files.iterator(), currentCount);
    }

    private static int directoryLinesHelper(final Iterator<File> filesIterator, final int currentCount) {
        if(filesIterator == null) {
            return 0;
        }
        return (filesIterator.hasNext()) ? directoryLinesHelper(filesIterator, currentCount + FilesUtils.getFileLines(filesIterator.next())) : currentCount;
    }
}
