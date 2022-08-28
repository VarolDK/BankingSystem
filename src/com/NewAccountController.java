package com;

import com.util.DatabaseLayer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NewAccountController{
    public VBox goldAccountVBox;
    public ComboBox<String> selectCBox;
    public ComboBox<String> currencyCBox;
    public TextField moneyTF;
    public Label dailyEarningLabel;
    public Label yearlyEarningLabel;
    public Button submitButton;
    public Button cancelButton;
    public TextField goldMoneyTF;
    public Label boughtGoldLabel;
    public VBox AccountVBox;
    public Label interestLabel;
    public static String currentUserTC;
    static Double currentUserMoney;
    public Label yourMoney;
    public Label successfulInfo;

    DatabaseLayer layer = new DatabaseLayer();

    private MainScreenController parentController;
    public void setParentController(MainScreenController parentController) {
        this.parentController = parentController;
    }
    public void setCurrentUserData(String currentUser) {
        currentUserTC = currentUser;
        currentUserMoney = layer.getMainMoney(currentUser);
        yourMoney.setText("Your Money: "+ currentUserMoney);
    }


    public void initialize(){
        selectCBox.getItems().addAll("Draw Account","Deposit Account","Gold Account");
        currencyCBox.getItems().addAll("TL","Dollar","Euro");
        currencyCBox.getSelectionModel().select(0);
        addComboBoxListener();
        addListener();

    }


    void addComboBoxListener(){
        selectCBox.valueProperty().addListener((observableValue, o, t1) -> {
            if (t1.equals("Draw Account")){
                interestLabel.setVisible(false);
                AccountVBox.setVisible(true);
                goldAccountVBox.setVisible(false);
                dailyEarningLabel.setVisible(false);
                yearlyEarningLabel.setVisible(false);
            }else if(t1.equals("Deposit Account")){
                interestLabel.setVisible(true);
                AccountVBox.setVisible(true);
                goldAccountVBox.setVisible(false);
                dailyEarningLabel.setVisible(true);
                yearlyEarningLabel.setVisible(true);
            }else{
                goldAccountVBox.setVisible(true);
                AccountVBox.setVisible(false);
            }
        });

    }

    void makeInterestCalculation(String moneyy){
        NumberFormat format = new DecimalFormat("#0.00");
        StaticMethod.setCurrencies();
        double money = StaticMethod.makeExcCalc(currencyCBox.getSelectionModel().getSelectedItem(),Double.parseDouble(moneyy));
        yearlyEarningLabel.setText("Yearly Earning: "+(format.format((money*15)/100))+ " " +currencyCBox.getSelectionModel().getSelectedItem());
        dailyEarningLabel.setText("Daily Earning: " +(format.format((money*15)/36500))+" " +currencyCBox.getSelectionModel().getSelectedItem());
        boughtGoldLabel.setText("Bought Gold: "+ format.format(money/460) + " gram");
    }

    void addListener(){
        StaticMethod.setCurrencies();
        moneyTF.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.length() == 0){
                StaticMethod.addCSS(moneyTF,"com/view/css/mainsc.css","error");
                yearlyEarningLabel.setText("Yearly Earning: ");
                dailyEarningLabel.setText("Daily Earning:");
                yourMoney.setText("Your Money:"+currentUserMoney);
            }else{
                if (StaticMethod.isDouble(t1)){
                    if (StaticMethod.lengthController(moneyTF,t1,10,0,"error","notError")){
                        makeInterestCalculation(t1);
                        yourMoney.setText("Your Money:" + (currentUserMoney-Integer.parseInt(t1)));
                    }
                }else{
                    yearlyEarningLabel.setText("Yearly Earning: ");
                    dailyEarningLabel.setText("Daily Earning: ");
                    yourMoney.setText("Your Money:"+currentUserMoney);
                }
            }

        } );

        goldMoneyTF.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.length() == 0){
                StaticMethod.addCSS(goldMoneyTF,"com/view/css/mainsc.css","error");
                boughtGoldLabel.setText("Bought Gold: ");
                yourMoney.setText("Your Money:"+currentUserMoney);
            }else{
                if (StaticMethod.isDouble(t1)){
                    if (StaticMethod.lengthController(goldMoneyTF,t1,10,0,"error","notError")){
                        makeInterestCalculation(t1);
                        yourMoney.setText("Your Money:" + (currentUserMoney-Double.parseDouble(t1)));
                    }
                }else{
                    boughtGoldLabel.setText("Bought Gold: ");
                    yourMoney.setText("Your Money:"+currentUserMoney);
                }
            }
        });

        currencyCBox.valueProperty().addListener((observableValue, o, t1) -> {
            if (StaticMethod.lengthController(moneyTF,moneyTF.getText(),10,0,"error","notError") && moneyTF.getText().length()!=0 && StaticMethod.isDouble(moneyTF.getText())){
                makeInterestCalculation(moneyTF.getText());
            }

        });


    }
    public void cancelButton(){
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    public void submitButton(){
        StaticMethod.setCurrencies();
        boolean succes = false;
        if(selectCBox.getSelectionModel().getSelectedItem().equals("Draw Account")){
            if(moneyTF.getText().length()!=0 && Double.parseDouble(moneyTF.getText()) <= currentUserMoney){
                double money = StaticMethod.makeExcCalc(currencyCBox.getSelectionModel().getSelectedItem(),Double.parseDouble(moneyTF.getText()));
                layer.addNewDrawAccount(Double.parseDouble(currentUserTC),Double.parseDouble(moneyTF.getText()),money,currencyCBox.getSelectionModel().getSelectedItem(),false);
                succes = true;

            }else{
                StaticMethod.addCSS(moneyTF,"com/view/css/mainsc.css","error");
                succes = false;
            }
        }else if(selectCBox.getSelectionModel().getSelectedItem().equals("Deposit Account")){
            if (moneyTF.getText().length()!=0 && Double.parseDouble(moneyTF.getText()) <= currentUserMoney){
                double money = StaticMethod.makeExcCalc(currencyCBox.getSelectionModel().getSelectedItem(),Double.parseDouble(moneyTF.getText()));
                layer.addNewDepositAccount(Double.parseDouble(currentUserTC),Double.parseDouble(moneyTF.getText()),money,currencyCBox.getSelectionModel().getSelectedItem(),true);
                succes = true;
            }else{
                StaticMethod.addCSS(moneyTF,"com/view/css/mainsc.css","error");
                succes = false;
            }
        }else if(selectCBox.getSelectionModel().getSelectedItem().equals("Gold Account")){
            if (goldMoneyTF.getText().length()!=0 && Double.parseDouble(goldMoneyTF.getText()) <= currentUserMoney){
                layer.addNewGoldAccount(Double.parseDouble(currentUserTC),Double.parseDouble(goldMoneyTF.getText()));
                layer.goldUpdateQuery();
                succes = true;
            }else{
                StaticMethod.addCSS(goldMoneyTF,"com/view/css/mainsc.css","error");
                succes = false;
            }
        }
        if(succes){
            submitButton.setDisable(true);
            cancelButton.setDisable(true);
            successfulInfo.setVisible(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e ->{
                ((Stage) cancelButton.getScene().getWindow()).close();
                parentController.refreshButton.fire();
            }));
            timeline.play();

        }
    }

}
