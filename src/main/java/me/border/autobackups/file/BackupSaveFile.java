package me.border.autobackups.file;

import me.border.utilities.file.AbstractYamlFile;

import java.io.File;

public class BackupSaveFile extends AbstractYamlFile {

    /*public BackupSaveFile(File file){
        this(file.getName().substring(0, file.getName().length()-4), file.getParentFile());
    }*/

    public BackupSaveFile(String fileName, File path) {
        super(fileName, path);
    }
}
