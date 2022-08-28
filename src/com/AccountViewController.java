package com;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class AccountViewController {
    public Label IBANLabel;
    public Label MoneyLabel;
    public Button accountButton;
    public ImageView currencyImage;
    public Label depositLabel;
    public Button changeMoney;
    public ImageView changeMoneyIV;

    String money;
    String IBAN;
    String currency;
    String deposit;
    String TC;

    public AccountViewController(String IBAN,String money,String currency,String deposit,String TC) {
        this.money = money;
        this.IBAN = IBAN;
        this.currency=currency;
        this.deposit=deposit;
        this.TC = TC;
    }

    private MainScreenController parentController;
    public void setParentController(MainScreenController parentController) {
        this.parentController = parentController;
    }

    void setCurrencyImage() {
        if (currency.equals("TL")){
            StaticMethod.imageLoader(currencyImage, "images/turkish-lira.png");
        }
        if (currency.equals("Dollar")){
            StaticMethod.imageLoader(currencyImage, "images/dollar.png");
        }
        if (currency.equals("Euro")){
            StaticMethod.imageLoader(currencyImage, "images/euro.png");
        }
        if (currency.equals("Euro")){
            StaticMethod.imageLoader(currencyImage, "images/euro.png");
        }
        if (currency.equals("Gold")){
            StaticMethod.imageLoader(currencyImage, "images/gold.png");
        }

    }

    public void initialize(){
        if (currency.equals("Gold")){
            double conMoney = Double.parseDouble(money)/460;
            NumberFormat format = new DecimalFormat("#0.0000");
            MoneyLabel.setText("Money: "+ money + " TL Gram: "+ format.format(conMoney));
        }else{
            MoneyLabel.setText("Money: "+ money + " " +currency);
        }

        if (deposit.equals("true")){
            depositLabel.setVisible(true);
            changeMoney.setDisable(true);
            changeMoney.setVisible(false);
        }

        IBANLabel.setText("IBAN: " + IBAN);
        IBANLabel.setTextFill(Color.BLACK);
        MoneyLabel.setTextFill(Color.BLACK);
        setActions();
        setCurrencyImage();
        StaticMethod.imageLoader(changeMoneyIV,"images/moneyChange.png");
    }

    public void setActions(){
        accountButton.setOnAction(actionEvent -> {
            StringSelection selection =  new StringSelection(IBAN);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection,null);
            IBANLabel.setText("IBAN Copied to Clipboard !                 ");
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e ->{
                IBANLabel.setText("IBAN: " + IBAN);
            }));
            timeline.play();
        });

        accountButton.setOnMouseEntered(mouseEvent -> {
            accountButton.setStyle("-fx-background-color: #1761A0");
            IBANLabel.setTextFill(Color.WHITE);
            MoneyLabel.setTextFill(Color.WHITE);
        });

        accountButton.setOnMouseExited(mouseEvent -> {
            accountButton.setStyle("-fx-background-color: #419A1C");
            IBANLabel.setTextFill(Color.BLACK);
            MoneyLabel.setTextFill(Color.BLACK);
        });

        changeMoney.setOnMouseEntered(mouseEvent -> {
            changeMoney.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 10");

        });

        changeMoney.setOnMouseExited(mouseEvent -> {
            changeMoney.setStyle("-fx-background-color: transparent; -fx-border-width: 0");
        });

        changeMoney.setOnAction(actionEvent -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("view/changeMoney.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(IBANLabel.getParent().getScene().getWindow());
                ChangeMoneyController controller = loader.getController();
                controller.setParentController(parentController);
                controller.setCurrentUserData(IBAN,TC,currency,money);
                controller.setData();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }




}
