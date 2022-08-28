package com;

import com.util.DatabaseLayer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.commons.validator.routines.EmailValidator;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MainScreenController {

    public AnchorPane accountPage;
    public AnchorPane transactionPage;
    public AnchorPane settingsPage;
    public AnchorPane historyPage;
    public VBox accountVBox;
    public Label moneyLabel;
    public Label IBANLabel;
    public ImageView currencyIV;
    public Button accountButton;
    public Button addAccountButton;
    public Label welcomeLabel;
    public ImageView acc_img;
    public ImageView trans_img;
    public ImageView credit_img;
    public ImageView settings_img;
    public TextField currentPasswordTF;
    public TextField newPasswordTF;
    public Label currentMailLabel;
    public Label currentAddressLabel;
    public TextArea newAddressTF;
    public TextField newMailTF;
    public TextField recevIBAN;
    public TextField recevNameSurname;
    public Button sendButton;
    public TextField recevAmount;
    public VBox currencyVBox;
    public TextField baseTF;
    public TextField toTF;
    public Button currencySearchButton;
    public PieChart myMoneyPC;
    public VBox transAccount;
    public Label passwordInf;
    public Label mailInf;
    public Label addressInf;
    public ImageView logo;
    public Label yourIBAN;
    public ImageView newAccountIV;
    public Button refreshButton;
    public ImageView refreshIV;
    public Label IBANInf;
    public Label amountInf;
    public Label sendInf;
    public AnchorPane creditPage;
    public Label nameSurInf;
    public Label interestCredit;
    public Label getCreditDate;
    public DatePicker paymentDate;
    public Button sendButtonCredit;
    public Label creditName;
    public ComboBox monthCredit;
    public Label totalAmount;
    public TextField creditAmount;
    public ComboBox monthlyPayment;
    public VBox creditApplyVbox;
    public Label amountPaidLayer;
    public Label creditAmountLayer;
    public Label restAmountLayer;
    public Label waitingAcceptLabel;
    public Label getCreditLayer;
    public Label monthlyPayLabel;
    public Label titleCreditLabel;
    public HBox showCreditDetail;
    public Button changeMoneyMain;
    public ImageView changeMoneyIV;
    public Label successCredit;
    public TableView<ModelTable> table1;
    public TableColumn<ModelTable, String> col_sender1;
    public TableColumn<ModelTable, String> col_receiver1;
    public TableColumn<ModelTable, String> col_amount1;
    public TableColumn<ModelTable, String> col_Tdate1;
    public ImageView historyIV;
    public Label creditMonthsLabel;
    public VBox paymentVbox;
    public TableView<PaymentTable> selectAccountTable;
    public TableColumn<PaymentTable, String> ibanTable;
    public TableColumn<PaymentTable, Double> amountAcTable;
    public TableColumn<PaymentTable, String> currencyTable;
    public TableView<CreditTable> selectCreditTable;
    public TableColumn<CreditTable, Date> paymentDateCol;
    public TableColumn<CreditTable, Double> amountCol;
    public TableColumn<CreditTable, Double> feeCol;
    public Label warText;
    public Label successText;
    public ImageView logoutIV;
    public ImageView logoIV;
    @FXML
    private TableView<ModelTable> table;
    @FXML
    private TableColumn<ModelTable, String> col_sender;
    @FXML
    private TableColumn<ModelTable, String> col_receiver;
    @FXML
    private TableColumn<ModelTable, String> col_amount;
    @FXML
    private TableColumn<ModelTable, String> col_Tdate;








    DatabaseLayer layer = new DatabaseLayer();
    public String currentUserTC;
    boolean[] searchControl = {false,false};
    int currentUserCreditID;

    public void initialize() {
        passScreenHandle(true,false,false,false,false);
        passwordInf.setText("");
        mailInf.setText("");
        addressInf.setText("");
        currencyVBox.getChildren().addAll(new Label("USD/TRY: "+StaticMethod.API("USD","TRY")),new Label("EUR/TRY: "+StaticMethod.API("EUR","TRY")));
        addListener();
        loadImages();

    }



    void loadImages(){
        StaticMethod.imageLoader(acc_img,"images/account.png");
        StaticMethod.imageLoader(trans_img,"images/transaction.png");
        StaticMethod.imageLoader(credit_img,"images/credit.png");
        StaticMethod.imageLoader(settings_img,"images/settings.png");
        StaticMethod.imageLoader(logo,"images/logo.png");
        StaticMethod.imageLoader(newAccountIV,"images/newAccount.png");
        StaticMethod.imageLoader(refreshIV,"images/refresh.png");
        StaticMethod.imageLoader(historyIV,"images/transaction-history.png");
        StaticMethod.imageLoader(logoutIV,"images/exit.png");
        StaticMethod.imageLoader(logoIV,"images/logo.png");
    }


    public void setCurrentUserTC(String currentUserTC) {
        this.currentUserTC = currentUserTC;
        listAccounts();
        fillMainAccountInfo();
        myMoneyPC.getData().addAll(layer.fillPieChart(currentUserTC));
        listTransAccounts();
        creditScreen();
        controlCredit();
        fillTable();
        notificationHandle();
        notificationScreen();
    }


    void fillTable(){
        col_sender.setCellValueFactory(new PropertyValueFactory<>("senderName"));
        col_receiver.setCellValueFactory(new PropertyValueFactory<>("receiverIBAN"));
        col_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        col_Tdate.setCellValueFactory(new PropertyValueFactory<>("T_date"));

        col_sender1.setCellValueFactory(new PropertyValueFactory<>("senderName"));
        col_receiver1.setCellValueFactory(new PropertyValueFactory<>("receiverIBAN"));
        col_amount1.setCellValueFactory(new PropertyValueFactory<>("amount"));
        col_Tdate1.setCellValueFactory(new PropertyValueFactory<>("T_date"));

        table.setItems(layer.fillTableCol(currentUserTC,true));
        table1.setItems(layer.fillTableCol(currentUserTC,false));

        ibanTable.setCellValueFactory(new  PropertyValueFactory<>("IBAN"));
        amountAcTable.setCellValueFactory(new PropertyValueFactory<>("amount"));
        currencyTable.setCellValueFactory(new PropertyValueFactory<>("currency"));


        paymentDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        feeCol.setCellValueFactory(new PropertyValueFactory<>("lateFee"));

        selectAccountTable.setItems(layer.paymentTable(currentUserTC));
        selectCreditTable.setItems(layer.creditTable(currentUserTC));
        selectCreditTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        selectAccountTable.setPlaceholder(new Label("You don't have TL Account!"));
        selectCreditTable.setPlaceholder(new Label("You don't have credit payment"));
        table.setPlaceholder(new Label("You don't have transaction history"));
        table1.setPlaceholder(new Label("You don't have transaction history"));
    }



    public void passScreenHandle(boolean account, boolean trans,boolean credit, boolean history, boolean settings){
        accountPage.setVisible(account);
        transactionPage.setVisible(trans);
        creditPage.setVisible(credit);
        historyPage.setVisible(history);
        settingsPage.setVisible(settings);
    }

    void fillMainAccountInfo(){
        String[] infos = layer.getUserInfo(currentUserTC);
        welcomeLabel.setText("Welcome "+infos[0]+" "+infos[1]);
        IBANLabel.setText("IBAN: "+ infos[2]);
        moneyLabel.setText("Money : " + infos[3] + " TL");
        StaticMethod.imageLoader(currencyIV,"images/turkish-lira.png");
        currentMailLabel.setText(infos[4]);
        currentAddressLabel.setText(infos[5]);

        accountButton.setOnAction(actionEvent -> {
            StringSelection selection =  new StringSelection(infos[2]);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection,null);
            IBANLabel.setText("IBAN Copied to Clipboard !");
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> IBANLabel.setText("IBAN: " + infos[2])));
            timeline.play();
        });

        accountButton.setOnMouseEntered(mouseEvent -> {
            accountButton.setStyle("-fx-background-color: #1761A0; -fx-border-radius: 20");
            IBANLabel.setTextFill(Color.WHITE);
            moneyLabel.setTextFill(Color.WHITE);
        });

        accountButton.setOnMouseExited(mouseEvent -> {
            accountButton.setStyle("-fx-background-color: #419A1C; -fx-border-radius: 20");
            IBANLabel.setTextFill(Color.BLACK);
            moneyLabel.setTextFill(Color.BLACK);
        });

        changeMoneyMain.setOnMouseEntered(mouseEvent -> changeMoneyMain.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 10"));

        changeMoneyMain.setOnMouseExited(mouseEvent -> changeMoneyMain.setStyle("-fx-background-color: transparent; -fx-border-width: 0"));

        changeMoneyMain.setOnAction(actionEvent -> {
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
                controller.setCurrentUserData(infos[2],currentUserTC,"TL",infos[3]);
                refreshButton.fire();
                controller.setData();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        StaticMethod.imageLoader(changeMoneyIV,"images/moneyChange.png");
    }



    public void accountsButtonAction(){
        passScreenHandle(true,false,false,false,false);
    }

    public void transactionButtonAction(){
        passScreenHandle(false,true,false,false,false);
        autoRefreshForTrans();
    }
    public void creditButtonAction(){
        passScreenHandle(false,false,true,false,false);
    }

    public void historyButtonAction() { passScreenHandle(false,false,false,true,false);
    table.getItems().clear();
    table1.getItems().clear();
    fillTable();}

    public void settingsButtonAction(){
        passScreenHandle(false,false,false,false,true);
    }



    public void exitButtonAction(){
        System.exit(1);
        layer.closeConnection();
    }

    public void minimizeButtonAction(){
        Stage stage = (Stage) accountPage.getScene().getWindow();
        stage.setIconified(true);
    }

    void addListener(){
        baseTF.textProperty().addListener((observableValue, s, t1) -> {
           searchControl[0]= StaticMethod.lengthController(baseTF,t1,3,3,"text-fieldError","text-field");
           searchExcButDisable();
        });

        toTF.textProperty().addListener((observableValue, s, t1) -> {
            searchControl[1]= StaticMethod.lengthController(toTF,t1,3,3,"text-fieldError","text-field");
            searchExcButDisable();
        });

        creditAmount.textProperty().addListener((observableValue, s, t1) -> {
            if(StaticMethod.isDouble(t1))
            interestCredit.setText(Double.parseDouble(t1)*115/100+"");
        });
    }



    public void addAccountHandle() throws IOException {
        if (accountVBox.getChildren().size()<7) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/NewAccount.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(accountPage.getScene().getWindow());
            NewAccountController controller = loader.getController();
            controller.setCurrentUserData(currentUserTC);
            controller.setParentController(this);
            stage.show();
        }
    }
    public void searchExcButDisable(){
        for (boolean i : searchControl){
            if (!i){
                currencySearchButton.setDisable(true);
                return;
            }else{
                currencySearchButton.setDisable(false);
            }
        }
    }

    public void searchExcButton(){
        String search = StaticMethod.API(baseTF.getText().toUpperCase(),toTF.getText().toUpperCase());
        if (search !=null){
            currencyVBox.getChildren().add(new Label(baseTF.getText().toUpperCase()+"/"+toTF.getText().toUpperCase()+": "+search));
        }else{
            baseTF.setText("Wrong unit");
            toTF.setText("Wrong unit");
        }
    }

    public void sendTransactionButtonHandle() {

        boolean check=true;
        if(!layer.IBANConflictControl(recevIBAN.getText())){
            timeLineError(IBANInf,"This IBAN not found.",Color.RED,recevIBAN);
            check=false;
        }
         else if (!layer.currencyAccountControl(yourIBAN.getText(), recevIBAN.getText())){
            timeLineError(IBANInf,"The currency of Receiver IBAN and your IBAN is not match.",Color.RED,recevIBAN);
            check=false;
        }
        if(!StaticMethod.isDouble(recevAmount.getText())){
            timeLineError(amountInf,"Invalid Amount.",Color.RED,recevAmount);
            check=false;
        }else if(!layer.transactionAmountControl(yourIBAN.getText(), Double.parseDouble(recevAmount.getText()))){
                timeLineError(amountInf,"This amount is too much.", Color.RED,recevAmount);
            check=false;
            }

        if(!StaticMethod.lengthController(recevNameSurname, recevNameSurname.getText(), 60, 6, "text-fieldError", "text-field")){
            timeLineError(nameSurInf,"Invalid Name/Surname",Color.RED,recevNameSurname);
            check=false;
        }
        if(check){ layer.transaction(yourIBAN.getText(),recevIBAN.getText(),Double.parseDouble(recevAmount.getText()));
            layer.transactionAmountSameCur(yourIBAN.getText(),recevIBAN.getText(),Double.parseDouble(recevAmount.getText()));
            timeLineError(sendInf,"Successful!",Color.web("#419A1C"),recevNameSurname);
            recevIBAN.setText("");
            recevAmount.setText("");
        }
            autoRefreshForTrans();


    }

    void timeLineError(Label node, String msg, Color color,TextField tf){
        node.setVisible(true);
        node.setText(msg);
        node.setTextFill(color);
        tf.setText("");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> node.setVisible(false)));
        timeline.play();
    }

    Timeline notifTimeline;
    boolean firsOpen = true;
    void notificationHandle(){
        notifTimeline = new Timeline(new KeyFrame(Duration.seconds(10), actionEvent -> {
           if (!firsOpen) notificationScreen();
       }));
        notifTimeline.setCycleCount(Animation.INDEFINITE);
        notifTimeline.play();
    }

    void notificationScreen(){
        try {
            List<String[]> data = layer.getNotificationData(currentUserTC);
            if (data.size()!=0){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Notification.fxml"));
                Parent root =loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(accountPage.getScene().getWindow());
                NotificationController controller = loader.getController();
                controller.setData(data);
                controller.setParentController(this);
                stage.show();
                notifTimeline.stop();
            }
            firsOpen=false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void autoRefreshForTrans(){
        transAccount.getChildren().clear();
        listTransAccounts();

    }
    void listAccounts()  {
        List<String[]> data = layer.getAccountData(currentUserTC);
        for (int i = 0 ; i <data.size();i++){
            AccountViewController control = new AccountViewController(data.get(i)[0],data.get(i)[1],data.get(i)[2],data.get(i)[3],currentUserTC);
            control.setParentController(this);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/AccountView.fxml"));
            loader.setController(control);
            try {
                accountVBox.getChildren().add(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void refreshButtonHandle(){
        int count = accountVBox.getChildren().size();
        for (int i = 2 ; i < count  ; i++){
            accountVBox.getChildren().remove(2);
        }
        listAccounts();
        myMoneyPC.getData().clear();
        myMoneyPC.getData().addAll(layer.fillPieChart(currentUserTC));
        fillMainAccountInfo();
    }

    void listTransAccounts()  {
        List<String[]> accountsData = layer.getAccountDataForTrans(currentUserTC);

        for (String[] accountsDatum : accountsData) {
            TransactionAccountController controller = new TransactionAccountController(accountsDatum[0], accountsDatum[1], accountsDatum[2],yourIBAN);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/TransactionAccount.fxml"));
            loader.setController(controller);
            try {
                transAccount.getChildren().add(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        sendButton.setOnMouseEntered(mouseEvent -> {
            sendButton.setStyle("-fx-background-color: #419A1C; -fx-background-radius:  10");
        });

        sendButton.setOnMouseExited(mouseEvent -> {
            sendButton.setStyle("-fx-background-color: #1761A0; -fx-background-radius: 10");
        });


    }

    public void creditScreen(){
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
        String[] info=  layer.getUserInfo(currentUserTC);

        creditName.setText(info[0]+" "+info[1]);
        getCreditDate.setText(timeStamp);
        monthCredit.getItems().addAll("1 Month","3 Month","6 Month","12 Month","18 Month","24 Month");
        totalAmount.setText("Total Amount="+layer.totalAmount(Double.parseDouble(currentUserTC)) +"\n"+"You can withdraw up to %30 of your money.");
        monthlyPayment.getItems().addAll(1,5,10,15,20,25);
        monthCredit.getSelectionModel().select(0);
        monthlyPayment.getSelectionModel().select(0);


    }

    public void applyCreditHandle(){

      int save1=0;
      int save2=0;
      int mC= monthCredit.getSelectionModel().getSelectedIndex();
      int mP= monthlyPayment.getSelectionModel().getSelectedIndex();
      monthCredit.getSelectionModel().getSelectedItem();
      switch (mC){
          case 0: save1=1;  break;
          case 1: save1=3;  break;
          case 2: save1=6;  break;
          case 3: save1=12; break;
          case 4: save1=18; break;
          case 5: save1=24; break;
      }
        switch (mP){
            case 0: save2=1;  break;
            case 1: save2=5;  break;
            case 2: save2=10;  break;
            case 3: save2=15; break;
            case 4: save2=20; break;
            case 5: save2=25; break;
        }
        if( StaticMethod.isDouble(creditAmount.getText())) {
            if(Double.parseDouble(creditAmount.getText())<=(layer.totalAmount(Double.parseDouble(currentUserTC))*3/10) ) {
                layer.creditApply(currentUserTC, Double.parseDouble(creditAmount.getText()), save1, Double.parseDouble(interestCredit.getText()), save2);
                setCreditInfo();
                creditApplyVbox.setDisable(true);
                creditApplyVbox.setVisible(true);
                showCreditDetail.setVisible(true);
                titleCreditLabel.setVisible(true);
                titleCreditLabel.setText("Current Label");
            }
            else{

                timeLineError(totalAmount,"You can withdraw up to thirty percent of your money.",Color.RED,creditAmount);
              //  totalAmount.setText("Total Amount="+layer.totalAmount(Double.parseDouble(currentUserTC)) +"\n"+"You can withdraw up to thirty percent of your money.");

            }
        }else{
            timeLineError(totalAmount,"Invalid value.",Color.RED,creditAmount);
           // totalAmount.setText("Total Amount="+layer.totalAmount(Double.parseDouble(currentUserTC)) +"\n"+"You can withdraw up to thirty percent of your money.");
        }
   }

   void controlCredit(){
        int control =layer.controlConfirmation(currentUserTC);
        System.out.println(control);
        if (control == 0){

            titleCreditLabel.setVisible(false);
            showCreditDetail.setVisible(false);
            paymentVbox.setVisible(false);
            creditApplyVbox.setVisible(true);
            creditApplyVbox.setDisable(false);

        }else if (control == 1){
            creditApplyVbox.setDisable(true);
            creditApplyVbox.setVisible(false);
            setCreditInfo();
            titleCreditLabel.setText("Accepted Credit");
            paymentVbox.setVisible(true);

        }else{
            creditApplyVbox.setDisable(true);
            creditApplyVbox.setVisible(false);
            paymentVbox.setVisible(false);
            setCreditInfo();
            titleCreditLabel.setText("Waiting Credit");

        }

   }

    void setCreditInfo(){
     String[] info = layer.getCreditInfo(currentUserTC);
        creditAmountLayer.setText("Amount: "+ info[0]);
        amountPaidLayer.setText("Amount Paid: "+info[1]);
        double creditAmountLayer=Double.parseDouble(info[0]);
        double amountPaidLayer=Double.parseDouble(info[1]);
        restAmountLayer.setText("Rest of Amount: "+(creditAmountLayer-amountPaidLayer));
        getCreditLayer.setText(info[2]);
        monthlyPayLabel.setText(info[3]+". Days");
        creditMonthsLabel.setText("Credit Months: "+info[4]);
        currentUserCreditID = Integer.parseInt(info[6]);
    }

    public void paymentButtonHandle(){
        Double amount = null;
        String IBAN = null;
        try{
            amount= selectAccountTable.getSelectionModel().getSelectedItem().amount;
            IBAN = selectAccountTable.getSelectionModel().getSelectedItem().IBAN;
        }catch (NullPointerException e){
            timeLineError(warText ,"Please select account",Color.RED,recevIBAN);
        }
        ObservableList<CreditTable> data=selectCreditTable.getSelectionModel().getSelectedItems();

        double sumAmount=0;
        double sumLatefee=0;
        for(CreditTable i:data){
            sumAmount+=i.amount + i.lateFee;
            sumLatefee = i.lateFee;
        }
        if(amount>=sumAmount && !IBAN.isEmpty()){
            for (CreditTable i:data){
                layer.paymentCredit(currentUserTC, (java.sql.Date) i.date);
            }
            layer.payCredit(currentUserTC,sumAmount,IBAN,currentUserCreditID,sumLatefee);
            timeLineError(successText,"Success!",Color.GREEN,recevIBAN);
            fillTable();
            setCreditInfo();
        }else{
            timeLineError(warText,"Please select another account. Your amount is not enough!",Color.RED,recevIBAN);
        }

    }







    public void changePasswordHandle(){
        if(layer.loginUserControl(Double.parseDouble(currentUserTC),currentPasswordTF.getText())){
            if(StaticMethod.lengthController(newPasswordTF,newPasswordTF.getText(),30,5,"error","notError")){
                layer.updatePassword(Double.parseDouble(currentUserTC),newPasswordTF.getText());
                StaticMethod.addCSS(currentPasswordTF,"com/view/css/mainsc.css","notError");
                StaticMethod.addCSS(newPasswordTF,"com/view/css/mainsc.css","notError");
                currentPasswordTF.clear();
                newPasswordTF.clear();
                passwordInf.setText("Successful!");
                passwordInf.setTextFill(Color.web("#419A1C"));
            }
            else{
                StaticMethod.addCSS(newPasswordTF,"com/view/css/mainsc.css","error");
                passwordInf.setText("New password is too long or too short!");
                passwordInf.setTextFill(Color.web("#FF0000"));
            }
            StaticMethod.addCSS(currentPasswordTF,"com/view/css/mainsc.css","notError");
        }
        else{
            StaticMethod.addCSS(currentPasswordTF,"com/view/css/mainsc.css","error");
            passwordInf.setText("Current password is wrong!");
            passwordInf.setTextFill(Color.web("#FF0000"));
        }
    }

    public void changeAddressHandle() {
        if (StaticMethod.lengthController(newAddressTF,newAddressTF.getText(),70,5,"error","notError")) {
            layer.updateAddress(Double.parseDouble(currentUserTC), newAddressTF.getText());
            StaticMethod.addCSS(newAddressTF, "com/view/css/mainsc.css", "notError");
            newAddressTF.clear();
            addressInf.setText("Successful!");
            addressInf.setTextFill(Color.web("#419A1C"));
        }
        else {
            StaticMethod.addCSS(newAddressTF, "com/view/css/mainsc.css", "error");
            addressInf.setText("New address is too long or too short!");
            addressInf.setTextFill(Color.web("#FF0000"));
        }
    }

    public void changeMailHandle(){
        if(EmailValidator.getInstance().isValid(newMailTF.getText())){
            layer.updateMail(Double.parseDouble(currentUserTC),newMailTF.getText());
            StaticMethod.addCSS(newMailTF,"com/view/css/mainsc.css", "notError");
            newMailTF.clear();
            mailInf.setText("Successful!");
            mailInf.setTextFill(Color.web("#419A1C"));
        }
        else {
            StaticMethod.addCSS(newMailTF,"com/view/css/mainsc.css", "error");
            mailInf.setText("Enter a valid mail!!");
            mailInf.setTextFill(Color.web("#FF0000"));

        }
    }

    public void logoutButton(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log out");
        alert.setHeaderText("Are you sure log out");
        alert.setContentText(null);
        alert.initOwner(refreshButton.getScene().getWindow());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Main main = new Main();
            try {
                main.myLoader("view/LoginScreen.fxml");
                ((Stage)refreshButton.getScene().getWindow()).close();
            } catch (IOException e) {
                System.exit(1);
            }
        }
    }



}

