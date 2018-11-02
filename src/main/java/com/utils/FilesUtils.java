package com.utils;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Contract;

public class FilesUtils {

    @Contract("null -> !null")
    static public final Collection<File> subDirs(final File file) {
        return file == null ? Collections.emptyList() : subDirs(file.getAbsolutePath());
    }

    static public final Collection<File> subDirs(final String path) {
        if(!fileExists(path)) {
            return Collections.emptyList();
        }
        final File file = new File(path);
        return Arrays.stream(file.listFiles())
                        .filter(File::isDirectory)
                        .collect(Collectors.toList());
    }

    @Contract("null -> !null")
    static public final Collection<File> dirFiles(final File file) {
        return file == null ? Collections.emptyList() : dirFiles(file.getAbsolutePath());
    }

    static public final Collection<File> dirFiles(final String path) {
        if(!fileExists(path)) {
            return Collections.emptyList();
        }
        final File file = new File(path);
        return Arrays.stream(file.listFiles())
                        .filter(File::isFile)
                        .collect(Collectors.toList());
    }

    static public final boolean fileExists(final String filePath) {
        return !StringUtils.isNullOrEmpty(filePath) && fileExists(new File(filePath));
    }

    @Contract("null -> false")
    static public final boolean fileExists(final File file) {
        return file != null && file.exists();
   }

    static public final int getFileLines(final File file) {
        int count = 0;
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            for(;reader.readLine() != null; ++count);
            reader.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return count;
    }
}
