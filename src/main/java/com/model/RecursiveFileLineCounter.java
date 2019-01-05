package com.model;

import com.utils.FilesUtils;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

public class RecursiveFileLineCounter extends AFileLineCounter {
    private final boolean isVerbose;

    RecursiveFileLineCounter(final File file, final boolean isVerbose) {
        super(file);
        this.isVerbose = isVerbose;
    }

    @Override
    public int count() {
        if(!FilesUtils.fileExists(file)) {
            return 0;
        }
        if(file.isFile()) {
            return FilesUtils.getFileLinesCount(file);
        }
        final Collection<File> dirCollection = FilesUtils.subDirs(file);
        if(dirCollection == null) {
            return 0;
        }
        int count = 0;
        for(final File aDirFile : dirCollection) {
            if (isVerbose) {
                System.out.println("Entering dir : " + aDirFile.getAbsolutePath() + " ...");
                System.out.println("Counted " + count + " lines.");
            }
            final AFileLineCounter subCounter = new RecursiveFileLineCounter(aDirFile, isVerbose);
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
        return (filesIterator.hasNext()) ? directoryLinesHelper(filesIterator, currentCount + FilesUtils.getFileLinesCount(filesIterator.next())) : currentCount;
    }
}
