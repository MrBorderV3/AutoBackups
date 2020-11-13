package me.border.autobackups.module.backup;

import me.border.utilities.utils.AsyncScheduler;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents a backup
 */
public class Backup {

    private int interval;
    private long intervalInMillis;

    private double whenOver;

    private boolean active;

    private File folder;
    private File drive;

    private final Timer timer = new Timer();

    /**
     * Create a new backup
     *
     * @param folder Folder to backup.
     * @param drive Drive to backup the folder on.
     * @param interval The interval for the backups (in hours)
     */
    public Backup(File folder, File drive, int interval){
        this.interval = interval;
        this.whenOver = -1;
        setInterval(interval);

        this.folder = folder;
        this.drive = drive;

        this.active = true;

        setupTimer();
    }

    /**
     * Create a new backup object from a serialized data.
     *
     * @param pathToFolder Path to the folder of the backup.
     * @param pathToDrive Path to the drive to backup the folder on.
     * @param interval The interval for the backups (in hours).
     * @param whenOver When the backup should happen (in minutes).
     * @param active Whether the backup is active.
     */
    public Backup(String pathToFolder, String pathToDrive, int interval, int whenOver, boolean active){

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


    public void enable(){
        if (!active){
            this.active = true;
            setupTimer();
        }
    }

    public void disable(){
        this.active = false;
    }

    /**
     * Set the backup interval.
     *
     * @param interval The interval to set it for.
     */
    public void setInterval(int interval) {
        this.interval = interval;
        this.intervalInMillis = interval * 3600000;
    }

    /**
     * Setup the backup internal count
     */
    private void setupTimer(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (whenOver == -1){
                    whenOver = interval * 60;
                } else {
                    if (whenOver == 1){
                        whenOver = interval * 60;
                        backup();
                    } else {
                        whenOver = whenOver - 1;
                    }
                }
            }
        }, 60000, 60000);
    }

    /**
     * Perform the backup async
     */
    private void backup(){
        AsyncScheduler.runTaskAsyncDaemon(() -> {
            // RUN BACKUP
            //TODO USE FILES API TO COPY CONTENTS OF <@code>folder<@code> to <@code>drive<@code>
            notifyBackup();
        });
    }

    private void notifyBackup(){

    }

    /**
     * Delete this backup
     */
    public void delete(){

    }
}
