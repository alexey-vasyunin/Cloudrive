package com.cloudrive.common;

import com.cloudrive.common.interfaces.TransferCommon;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class DirMessage implements TransferCommon, Serializable {

    private ArrayList<FileItem> files;

    public ArrayList<FileItem> getFiles() {
        return files;
    }

    @Override
    public TransferObjectType getType() {
        return TransferObjectType.DIRLIST;
    }

    public DirMessage(String path) {
        try {
            files = new ArrayList<>();
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path));
            for (Path p : stream) {
                files.add(new FileItem(p.getFileName().toString(), Files.size(p), Files.isDirectory(p)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DirMessage(FileItem[] files) {
        this.files = new ArrayList<>();
        this.files.addAll(Arrays.asList(files));
    }

    public void addFileItem(FileItem file) {
        files.add(file);
    }


    public class FileItem implements Serializable {
        private static final long serialVersionUID = -9069891434256916773L;
        private String filename;
        private long size;
        private boolean isDirectory;

        public FileItem(String filename, long size, boolean isDirectory) {
            this.filename = filename;
            this.size = size;
            this.isDirectory = isDirectory;
        }

        public String getFilename() {
            return filename;
        }

        public long getSize() {
            return size;
        }

        public boolean isDirectory() {
            return isDirectory;
        }
    }

}
