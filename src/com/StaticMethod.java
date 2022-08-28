package com;


import com.util.DatabaseLayer;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

public class StaticMethod {
    static void addCSS (Control node, String path, String className){
        node.getStyleClass().clear();
        node.getStyleClass().addAll("text-field", "text-input");
        node.getStylesheets().add(path);
        node.getStyleClass().add(className);
    }

    static void removeCSS(Control node){
        node.getStyleClass().clear();
        node.getStyleClass().addAll("text-field", "text-input");
    }

    static boolean lengthController(Control node , String t1,int max,int min,String error,String notError){
        if (t1.length()<=max && t1.length()>=min){
            addCSS(node,"com/view/css/mainsc.css",notError);
            return true;
        }else if (t1.length() == 0){
            removeCSS(node);
            return false;
        }else{
            addCSS(node,"com/view/css/mainsc.css",error);
            return false;
        }
    }



    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public static String IBANCalculator(){
        DatabaseLayer layer = new DatabaseLayer();
        Random random = new Random();
        String countryCode = "TR";
        int controlDigit = random.nextInt(90)+10;
        int bankNo = 14058;
        int rezDigit = random.nextInt(10);
        int accountNo1 = random.nextInt(90000000)+10000000;
        int accountNo2 = random.nextInt(90000000)+10000000;
        String IBAN = countryCode + controlDigit + bankNo + rezDigit + accountNo1 +accountNo2;
        while (layer.IBANConflictControl(IBAN)){
            accountNo1 = random.nextInt(90000000)+10000000;
        }
        return IBAN;
    }

    public static String API(String base,String symbol){
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL("https://api.exchangeratesapi.io/latest?base="+base+"&symbols="+symbol);
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null){
                result.append(line);
            }
            reader.close();
            JSONObject object = new JSONObject(result.toString());
            NumberFormat format = new DecimalFormat("#0.0000");
            return format.format(Double.parseDouble(object.getJSONObject("rates").get(symbol).toString()));
        } catch (JSONException | IOException e) {
            return null;
        }
    }
    static double dollar = 0;
    static double euro = 0;

    public static void setCurrencies(){
        if (dollar == 0 || euro ==0){
            String dolarS = API("USD","TRY");
            String euroS = API("EUR","TRY");
            for (int i = 0;i<dolarS.length();i++){
                if (dolarS.charAt(i) == ','){
                    dolarS= dolarS.substring(0,i) + "." + dolarS.substring(i+1);
                }
            }
            for (int i = 0;i<euroS.length();i++){
                if (euroS.charAt(i) == ','){
                    euroS=euroS.substring(0,i) + "." + euroS.substring(i+1);
                }
            }
             dollar = Double.parseDouble(dolarS);
             euro = Double.parseDouble(euroS);
        }
    }

    public static double makeExcCalc(String currency , Double money){
        if (currency.equals("TL")){
            return money;
        }else if(currency.equals("Dollar")){
            return money/dollar;
        }else{
            return money/euro;
        }
    }

    public static double makeExcCalcWithBase(String baseCurrency ,String toCurrency, Double money){
        String moneyRate;
        if (toCurrency.equals("TL")||toCurrency.equals("Gold")){
            moneyRate = API(baseCurrency,"TRY");
        }else if(toCurrency.equals("Dollar")){
            moneyRate = API(baseCurrency,"USD");
        }else{
            moneyRate = API(baseCurrency,"EUR");
        }
        System.out.println(moneyRate);
        for (int i = 0;i<moneyRate.length();i++){
            if (moneyRate.charAt(i) == ','){
                moneyRate= moneyRate.substring(0,i) + "." + moneyRate.substring(i+1);
            }
        }
        return money * Double.parseDouble(moneyRate);
    }

    static void imageLoader(ImageView image, String path){
        File file = new File("./"+path);
        Image imageFile = new Image(file.toURI().toString());
        image.setImage(imageFile);
    }

}
