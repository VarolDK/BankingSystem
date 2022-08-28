package com;
import com.util.DatabaseLayer;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Optional;
import java.util.Random;

public class RegisterScreenController {

    public Button minimizeButton;
    public TextField FNameTF;
    public TextField LNameTF;
    public TextField TCTF;
    public TextField mailTF;
    public PasswordField passwordPF;
    public DatePicker BDateDP;
    public TextField addressTF;
    public ImageView logo;
    public Button registerButton;
    private boolean disableButton[] ={false,false,false,false,false,false,false};
    DatabaseLayer layer = new DatabaseLayer();

    public void initialize(){
        StaticMethod.imageLoader(logo,"images/logo.png");
        lengthListeners();
        emailTFListener();
        BDateController();
        makeDisableButton();

    }


    public void signInButtonHandle() throws IOException {
        Main loader = new Main();
        loader.myLoader("view/LoginScreen.fxml");
        ((Stage)registerButton.getScene().getWindow()).close();
    }




    void makeDisableButton(){
        for (boolean a : disableButton){
            if(!a){
                registerButton.setDisable(true);
                break;
            }else{
                registerButton.setDisable(false);
            }
        }
    }

   void lengthListeners(){
        FNameTF.textProperty().addListener((observableValue, s,t1) ->{
           disableButton[0] = StaticMethod.lengthController(FNameTF,t1,20,3,"text-fieldError","text-field");
           makeDisableButton();
        } );

        LNameTF.textProperty().addListener((observableValue, s,t1) ->{
            disableButton[1] = StaticMethod.lengthController(LNameTF,t1,20,3,"text-fieldError","text-field");
            makeDisableButton();
        } );

            passwordPF.textProperty().addListener((observableValue, s, t1) -> {
            disableButton[2] = StaticMethod.lengthController(passwordPF,t1,30,5,"text-fieldError","text-field");
            makeDisableButton();
        });

        addressTF.textProperty().addListener((observableValue, s, t1) ->{
            disableButton[3] = StaticMethod.lengthController(addressTF,t1,100,5,"text-fieldError","text-field");
            makeDisableButton();
        } );

        TCTF.textProperty().addListener((observableValue, s, t1) -> {
            if (StaticMethod.lengthController(TCTF,t1,11,11,"text-fieldError","text-field")){
                if(StaticMethod.isDouble(t1)){
                    StaticMethod.addCSS(TCTF,"com/view/css/mainsc.css","text-field");
                    disableButton[4]= true;
                }else{
                    StaticMethod.addCSS(TCTF,"com/view/css/mainsc.css","text-fieldError");
                    disableButton[4] = false;
                }
            }
            makeDisableButton();
        });
    }


    void emailTFListener(){
        mailTF.textProperty().addListener((observableValue, s, t1) -> {
            if (EmailValidator.getInstance().isValid(t1)){
                StaticMethod.addCSS(mailTF,"com/view/css/mainsc.css","text-field");
                disableButton[5] = true;
            }else if(t1.length()==0){
                StaticMethod.removeCSS(mailTF);
                disableButton[5] = false;
            }else{
                StaticMethod.addCSS(mailTF,"com/view/css/mainsc.css","text-fieldError");
                disableButton[5] = false;
            }
            makeDisableButton();
        });

    }

    void BDateController(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        BDateDP.valueProperty().addListener((observableValue, localDate, t1) -> {
            int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime()));
            int BDateYear = Integer.parseInt(formatter.format(t1));
            if (currentYear-BDateYear < 18){
                BDateDP.setStyle("-fx-border-color: red");
                disableButton[6] = false;
            }else{
                BDateDP.setStyle("-fx-border-color: #419A1C");
                disableButton[6] = true;
            }
            makeDisableButton();
        });
    }

    public void registerButtonHandle(){
        java.sql.Date gotDate = java.sql.Date.valueOf(BDateDP.getValue());
        Random rand =  new Random();
        boolean succes = layer.insertUser(FNameTF.getText(),
                LNameTF.getText(),
                Double.parseDouble(TCTF.getText()),
                mailTF.getText(),
                passwordPF.getText(),
                gotDate,
                addressTF.getText(),
                StaticMethod.IBANCalculator(),
                (double)rand.nextInt(1000000));


        Alert alert ;
        if (succes){
            alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Your registration is succeed");
            alert.initOwner(registerButton.getParent().getScene().getWindow());
            alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    Main loader = new Main();
                    loader.myLoader("view/LoginScreen.fxml");
                    ((Stage) registerButton.getScene().getWindow()).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setContentText("Something went wrong\nPlease try again");
            alert.initOwner(registerButton.getParent().getScene().getWindow());
            alert.setHeaderText(null);
            alert.showAndWait();
        }

    }





    public void exitButton(){
        System.exit(1);
        layer.closeConnection();
    }

    public void minimizeButton(){
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }
}
