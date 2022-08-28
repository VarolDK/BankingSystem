package com;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    private  double xOffset = 0;
    private  double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        myLoader("view/LoginScreen.fxml");
    }

    public FXMLLoader myLoader(String url) throws IOException {
        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource(url));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getIcons().add(new Image(new File("./images/logoWin.png").toURI().toString()));
        stage.show();

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        return loader;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

