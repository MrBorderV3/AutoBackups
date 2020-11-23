package me.border.autobackups.utils;

import me.border.autobackups.Main;

import java.awt.*;

public class NotificationUtils {

    /**
     * Show a system tray notification (if supported).
     *
     * @param caption Title of the notification.
     * @param text Message of the notification.
     * @param type Type of the notification.
     */
    public static void showNotification(String caption, String text, TrayIcon.MessageType type){
        if (SystemTray.isSupported())
            Main.getTrayIcon().displayMessage(caption, text, type);
    }
}

