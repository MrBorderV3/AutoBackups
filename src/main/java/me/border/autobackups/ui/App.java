package me.border.autobackups.ui;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main UI class
 */
public class App extends Application {

    public void start(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("AutoBackups");
        stage.setResizable(false);


    }
}
