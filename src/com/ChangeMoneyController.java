package com;

import com.util.DatabaseLayer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class ChangeMoneyController {
    public VBox accountsVbox;
    public Label yourIBAN;
    public TextField amountTF;
    public Button submitButton;
    public Button cancelButton;
    public Label moneyLabel;
    public String toCurreny;
    String IBAN, TC, currency, money;
    DatabaseLayer layer = new DatabaseLayer();

    public void setCurrentUserData(String IBAN, String TC, String currency,String money) {
        this.IBAN = IBAN;
        this.TC = TC;
        this.currency = currency;
        this.money = money;
    }

    private MainScreenController parentController;
    public void setParentController(MainScreenController parentController) {
        this.parentController = parentController;
    }

    public void setData(){
        List<String[]> accountsData = layer.getAccountDataForChange(TC,IBAN);
        for (String[] accountsDatum : accountsData) {
            TransactionAccountController controller = new TransactionAccountController(accountsDatum[0], accountsDatum[1], accountsDatum[2],yourIBAN);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/TransactionAccount.fxml"));
            loader.setController(controller);
            controller.setParentController(this);
            moneyLabel.setText("Money: "+ money);
            try {
                accountsVbox.getChildren().add(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    boolean isDouble = false;
   public void initialize(){
        amountTF.textProperty().addListener((observableValue, s, t1) -> {
           if (StaticMethod.isDouble(t1)){
               StaticMethod.addCSS(amountTF,"com/view/css/mainsc.css","text-field");
               moneyLabel.setText("Money: "+ (Double.parseDouble(money)-Double.parseDouble(t1)));
               isDouble=true;
           }else{
               StaticMethod.addCSS(amountTF,"com/view/css/mainsc.css","text-fieldError");
               isDouble=false;
           }
        });
   }

   public void submitButtonHandle(){
       if (isDouble && yourIBAN.getText().length()>10){
           StaticMethod.setCurrencies();
           Double mainMoney = Double.parseDouble(amountTF.getText());
           Double money = Double.parseDouble(amountTF.getText());
          if (layer.transactionAmountControl(IBAN,money)){
              if (currency.equals("TL") || currency.equals("Gold")){
                  money=StaticMethod.makeExcCalcWithBase("TRY",toCurreny,money);
              }else if(currency.equals("Dollar")){
                  money=StaticMethod.makeExcCalcWithBase("USD",toCurreny,money);
              }else{
                  money=StaticMethod.makeExcCalcWithBase("EUR",toCurreny,money);
              }
              layer.transactionAmountDiffCur(IBAN, yourIBAN.getText(),mainMoney,money);
              layer.transaction(IBAN,yourIBAN.getText(),money);
              submitButton.setDisable(true);
              cancelButton.setDisable(true);
              Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e ->{
                  ((Stage) cancelButton.getScene().getWindow()).close();
                  if(parentController!=null) parentController.refreshButton.fire();
              }));
              timeline.play();
          }
       }
   }

    public void cancelButton(){
        ((Stage)yourIBAN.getScene().getWindow()).close();
    }
}
