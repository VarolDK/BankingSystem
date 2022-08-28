package com;

import com.util.DatabaseLayer;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class NotificationController {
    public VBox notificationVBox;
    public ImageView notificationIV;
    DatabaseLayer layer =  new DatabaseLayer();

    private MainScreenController parentController;
    public void setParentController(MainScreenController parentController) {
        this.parentController = parentController;
    }

    public void initialize(){
        StaticMethod.imageLoader(notificationIV,"images/notification.png");
    }

    List<Integer> ID =  new ArrayList<>();
    void setData(List<String[]> data){
       for (int i=0; i<data.size();i++){
           Label dateLabel = new Label(data.get(i)[3]);
           Label dataLabel1 = new Label(data.get(i)[0]+" "+data.get(i)[1] +" +"+data.get(i)[2]);
           Label empty =  new Label();
           notificationVBox.getChildren().addAll(dateLabel,dataLabel1,empty);
           ID.add(Integer.parseInt(data.get(i)[4]));
       }

    }

    public void viewedAllHandle(){
        ((Stage)(notificationVBox.getScene().getWindow())).close();
        for(int i : ID){
            layer.notificationUpdate(i);
        }
        parentController.notifTimeline.play();
    }
}
