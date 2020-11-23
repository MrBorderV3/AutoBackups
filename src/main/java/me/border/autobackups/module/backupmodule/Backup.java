package me.border.autobackups.module.backupmodule;

import me.border.autobackups.file.BackupSaveFile;
import me.border.autobackups.utils.NotificationUtils;
import me.border.utilities.ui.javafx.fxml.AlertBox;
import me.border.utilities.utils.AsyncScheduler;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents a backup
 */
public class Backup {

    private final String name;

    private int interval;
    private long intervalInMillis;

    private double whenOver;

    private boolean active;

    private final File folder;
    private final File drive;

    private final Timer timer = new Timer();

    private final BackupSaveFile saveFile;

    /**
     * Create a new backup
     *
     * @param name Name of the backup.
     * @param folder Folder to backup.
     * @param drive Drive to backup the folder on.
     * @param interval The interval for the backups (in hours)
     */
    public Backup(String name, File folder, File drive, int interval){
        this.name = name;
        this.interval = interval;
        this.whenOver = -1;
        setInterval(interval);

        this.folder = folder;
        this.drive = drive;

        this.active = true;

        this.saveFile = new BackupSaveFile(name, BackupManager.backupDir);
        this.saveFile.setup();
        setupTimer();
    }

    /**
     * Create a new backup object from a serialized data.
     *
     * @param saveFile The file the backup is saved on.
     */
    protected Backup(BackupSaveFile saveFile){
        this.saveFile = saveFile;
        saveFile.setup();

        this.name = saveFile.getString("name");
        this.active = saveFile.getAs("active");
        this.whenOver = saveFile.getAs("whenOver");
        setInterval(saveFile.getAs("interval"));
        this.folder = new File(saveFile.getString("folder"));
        this.drive = new File(saveFile.getString("drive"));
    }

    /**
     * Activate the backup
     */
    public void enable(){
        if (!active){
            this.active = true;
            setupTimer();
            updateFile();
        }
    }

    /**
     * Disable the backup
     */
    public void disable(){
        if (active) {
            this.active = false;
            updateFile();
        }
    }

    /**
     * Setup the backup internal count
     */
    private void setupTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!active)
                    return;
                if (whenOver == -1) {
                    whenOver = interval * 60;
                } else {
                    if (whenOver == 0.1) {
                        whenOver = interval * 60;
                        backup();
                    } else {
                        whenOver = whenOver - - 0.1;
                    }
                }
                updateFile();
            }
        }, 10000, 10000);
    }

    /**
     * Perform the backup async
     */
    private void backup(){
        AsyncScheduler.runTaskAsync(() -> {
            try {
                if (!folder.exists()){
                    NotificationUtils.showNotification("Backup Failed - Folder doesn't exist", "Backup `" + name + "` has failed to activate due to the folder at path `" + folder.getAbsolutePath() + "` not existing", TrayIcon.MessageType.ERROR);
                    return;
                }
                if (!drive.exists()){
                    NotificationUtils.showNotification("Backup Failed - Drive doesn't exist", "Backup `" + name + "` has failed to activate due to the drive at path `" + drive.getAbsolutePath() + "` not existing", TrayIcon.MessageType.ERROR);
                    return;
                }
                Files.copy(Paths.get(folder.toURI()), Paths.get(drive.toURI()), StandardCopyOption.REPLACE_EXISTING);
                NotificationUtils.showNotification("Backup Success", "Backup `" + name + "` has successfully ran.", TrayIcon.MessageType.INFO);
            } catch (IOException e) {
                e.printStackTrace();
                NotificationUtils.showNotification("Backup Failed", "An unknown error has occurred and backup `" + name + "` has failed", TrayIcon.MessageType.ERROR);
            }
        });
    }

    /**
     * Update the backup settings to the {@code saveFile} and save.
     */
    private void updateFile(){
        AsyncScheduler.runTaskAsync(() -> {
            saveFile.set("name", name);
            saveFile.set("active", active);
            saveFile.set("interval", interval);
            saveFile.set("whenOver", whenOver);
            saveFile.set("folder", folder.getAbsolutePath());
            saveFile.set("drive", folder.getAbsolutePath());

            saveFile.save();
        });

    }

    /**
     * Delete this backup
     */
    public void delete(){
        this.active = false;
        File file = this.saveFile.getFile();
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                boolean isDeleted = f.delete();
                System.out.println(isDeleted ? "Successfully deleted " + f.getName() : "Could not delete " + f.getName());
            }
        }
        boolean isDeleted = file.delete();
        if (!isDeleted)
            AlertBox.showAlert("Error", "Backup `" + name + "` deletion has failed.");
    }

    /*----------------------------------------------------
                            SETTERS
     ----------------------------------------------------*/

    /**
     * Set the backup interval.
     *
     * @param interval The interval to set it for.
     */
    public void setInterval(int interval) {
        this.interval = interval;
        this.intervalInMillis = interval * 3600000;
        updateFile();
    }

    /*----------------------------------------------------
                            Getters
     ----------------------------------------------------*/

    /**
     * Get the name of the backup.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get whether the backup is currently active.
     *
     * @return Whether the backup is currently active.
     */
    public boolean isActive(){
        return active;
    }

    /**
     * Get the backup interval.
     *
     * @return The interval.
     */
    public int getInterval() {
        return interval;
    }

    /**
     * Get the backup interval in millis.
     *
     * @return The interval in millis.
     */
    public long getIntervalInMillis() {
        return intervalInMillis;
    }

    /**
     * Get the folder to backup.
     *
     * @return The folder.
     */
    public File getFolder() {
        return folder;
    }

    /**
     * Get the drive to backup it on.
     *
     * @return The drive.
     */
    public File getDrive() {
        return drive;
    }

}
