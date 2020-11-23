package me.border.autobackups.module.backupmodule;

import me.border.autobackups.file.BackupSaveFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages all the backups
 *
 * @see Backup
 */
public class BackupManager {
    private static final BackupManager instance = new BackupManager();

    public static final File backupDir = new File(System.getProperty("user.home") + File.separator + "AutoBackups" + File.separator + "Backups");

    private final Map<String, Backup> backupMap = new HashMap<>();

    /**
     * Starts up the backup manager and loads all backups from the backups directory.
     */
    private BackupManager(){
        for (File backupFile : backupDir.listFiles()){
            String fullName = backupFile.getName();
            if (fullName.endsWith(".yml")){
                String name = fullName.substring(0, fullName.length()-4);
                BackupSaveFile saveFile = new BackupSaveFile(name, backupDir);
                backupMap.put(name, new Backup(saveFile));
            }
        }
    }

    /**
     * Get a backup.
     *
     * @param name Name of the backup.
     * @return The backup.
     */
    public Backup getBackup(String name){
        return backupMap.get(name);
    }

    /**
     * Delete a backup.
     *
     * @param name Name of the backup.
     */
    public void deleteBackup(String name){
        Backup backup = getBackup(name);
        backup.delete();
    }

    /**
     * Get an instance of the {@code BackupManager}
     *
     * @return The instance.
     */
    public static BackupManager getInstance() {
        return instance;
    }
}
