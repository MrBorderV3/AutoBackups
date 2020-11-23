package me.border.autobackups;

import me.border.autobackups.ui.App;
import me.border.utilities.utils.URLUtils;

import java.awt.*;

public class Main {

    private static TrayIcon trayIcon;

    public static void main(String[] args) {
        App app = new App();
        app.start(args);

        initializeTrayIcon();
    }

    private static void initializeTrayIcon(){
        Image img = Toolkit.getDefaultToolkit().createImage(URLUtils.getURL("/img/trayicon.png"));
        trayIcon = new TrayIcon(img, "AutoBackups");
    }

    public static TrayIcon getTrayIcon(){
        return trayIcon;
    }
}
