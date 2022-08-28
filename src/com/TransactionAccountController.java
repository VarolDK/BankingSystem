package com;


import javafx.scene.control.Button;
import javafx.scene.control.Label;



public class TransactionAccountController {
    public Label accIBAN;
    public Label accAmount;
    public Label accCurrency;
    public Button accButton;
    String accIBANN,accAmountt,accCurencyy;
    public Label IBANLabel;


    public void initialize(){
        accIBAN.setText(accIBANN);
        accAmount.setText(accAmountt);
        accCurrency.setText(accCurencyy);
        accButton();
    }
    private ChangeMoneyController parentController;
    public void setParentController(ChangeMoneyController parentController) {
        this.parentController = parentController;
    }


    public TransactionAccountController(String accIBANN, String accAmountt, String accCurencyy,Label label) {
        this.accIBANN = accIBANN;
        this.accAmountt = accAmountt;
        this.accCurencyy = accCurencyy;
        IBANLabel=label;
    }

    public void accButton(){
        accButton.setOnAction(actionEvent -> {
           IBANLabel.setText(accIBANN);
           if (parentController!=null)parentController.toCurreny = accCurencyy;
        });

        accButton.setOnMouseEntered(mouseEvent -> {
            accButton.setStyle("-fx-background-color: #419A1C;-fx-background-radius: 20 20 20 0");


        });

        accButton.setOnMouseExited(mouseEvent -> {
            accButton.setStyle("-fx-background-color: #1761A0;-fx-background-radius: 20 20 20 0");

        });
    }




}
