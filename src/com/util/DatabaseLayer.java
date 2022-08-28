package com.util;
import com.*;
import com.mysql.cj.jdbc.ClientPreparedStatement;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class DatabaseLayer {
    Dotenv env = Dotenv.configure().directory("./././..env").ignoreIfMalformed().ignoreIfMissing().load();
    private final String url = env.get("DBurl");
    private final String username = env.get("DBusername");
    private final String password = env.get("DBpassword");
    Connection connection;
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+3"));
    java.sql.Timestamp currentDate = new java.sql.Timestamp(new java.util.Date().getTime());


    public DatabaseLayer() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setContentText("Database connection error!\nPlease check your connection.");
            alert.showAndWait();
            System.exit(1);
        }
    }



    public void createTables() {
        String userTable = "CREATE TABLE IF NOT EXISTS users(\n" +
                "    F_Name VARCHAR(20),\n" +
                "    L_Name VARCHAR(20),\n" +
                "    TC BIGINT(11) PRIMARY KEY,\n" +
                "    address VARCHAR(100),\n" +
                "    mail VARCHAR(320),\n" +
                "    password VARCHAR(50),\n" +
                "    B_Date DATE,\n" +
                "    admin_F BOOL default false\n" +
                ");";

        String accountTable = "CREATE TABLE IF NOT EXISTS accounts(\n" +
                "    TC bigint(11) NOT NULL,\n" +
                "    IBAN VARCHAR(26) PRIMARY KEY,\n" +
                "    amount decimal(28,3) default 0,\n" +
                "    currency varchar(30) default 'TL',\n" +
                "    mainAccF bool default false,\n" +
                "    openDate date,\n" +
                "    interestDate date default null,\n" +
                "    interest int default 15,\n" +
                "    depositAccF bool default false,\n" +
                "    goldGram decimal(15,3) default 0,\n" +
                "    foreign key (TC) REFERENCES users(TC)\n" +
                ");";

        String transactionTable = "CREATE TABLE IF NOT EXISTS transactions(\n" +
                "     notID int not null auto_increment PRIMARY KEY,"+
                "    senderIBAN VARCHAR(26) NOT NULL,\n" +
                "    receiverIBAN VARCHAR(26),\n" +
                "    amount decimal(28,3),\n" +
                "    T_date datetime,\n" +
                "    isRead bool default false,\n" +
                "    foreign key (senderIBAN) REFERENCES accounts(IBAN)\n" +
                ")";

        String creditTable = "CREATE TABLE IF NOT EXISTS credits(\n" +
                "     creditID int not null auto_increment PRIMARY KEY,\n" +
                "     TC bigint(11) not null,\n" +
                "     amount int,\n" +
                "     creditMonths tinyint,\n" +
                "     withInterest decimal(14,2),\n" +
                "     getCreditDate date,\n" +
                "     paidAmount int, \n" +
                "     paymentDate int, \n" +
                "     isPaid boolean default false, \n"+
                "     restMonth int, \n" +
                "     confirmation bool default false,\n" +
                "     foreign key (TC) REFERENCES users(TC)\n" +

                ")";

        String creditPayment= "CREATE TABLE IF NOT EXISTS creditPayment (\n" +
                "    amount decimal(28,2),\n" +
                "    paymentDate date,\n" +
                "    creditID int,\n" +
                "    isPaid boolean default false, \n"+
                "    foreign key (creditID) references  credits(creditID)\n" +
                ")";


        try {
                Statement statement = connection.createStatement();
                        statement.execute(userTable);
                        statement.execute(accountTable);
                        statement.execute(transactionTable);
                        statement.execute(creditTable);
                        statement.execute(creditPayment);
        }catch (SQLException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setContentText("Database connection error!\nPlease check your connection.");
            alert.showAndWait();
            System.exit(1);
        }



    }

    public void interestQuery(){
        try {
            PreparedStatement statement = connection.prepareStatement("update accounts set amount = amount + (((amount*15)/36500)*DATEDIFF(now(),interestDate))," +
                                                                            "interestDate = now() where depositAccF = true");
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void goldUpdateQuery(){
        try {
            PreparedStatement statement = connection.prepareStatement("update accounts set goldGram = (amount/460) where currency = 'Gold'");
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateCredit(){
        try {
            PreparedStatement statement =  connection.prepareStatement("update credits set isPaid = true where withInterest = paidAmount");
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean insertUser(String FName, String LName, Double TC, String eMail, String password, Date BDate, String address, String IBAN, Double moneyAmount){
        try {
            PreparedStatement statement = connection.prepareStatement("insert into users (F_Name, L_Name, TC, address, mail, password, B_Date) VALUES (?,?,?,?,?,?,?)");
            PreparedStatement statement2 = connection.prepareStatement("insert into accounts (TC,IBAN,amount,openDate,mainAccF) value (?,?,?,?,?)");
            statement.setString(1,FName);
            statement.setString(2,LName);
            statement.setDouble(3,TC);
            statement.setString(4,address);
            statement.setString(5,eMail);
            statement.setString(6,password);
            statement.setDate(7,BDate);
            statement2.setDouble(1,TC);
            statement2.setString(2,IBAN);
            statement2.setDouble(3,moneyAmount);
            statement2.setTimestamp(4,currentDate,cal);
            statement2.setBoolean(5,true);
            statement.execute();
            statement2.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }


    }

    public Double getMainMoney(String TC){
        try {
            PreparedStatement statement =  connection.prepareStatement("select amount from accounts where TC = ? and mainAccF = true");
            statement.setString(1,TC);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return rs.getDouble("amount");
        } catch (SQLException | RuntimeException throwables) {
            throwables.printStackTrace();
            return 0.0;
        }
    }



    public boolean loginUserControl(Double TC , String password){
        try {
            PreparedStatement statement =  connection.prepareStatement("select password,TC from users where password = ? AND TC = ? AND admin_F = false");
            statement.setString(1,password);
            statement.setDouble(2,TC);
            ResultSet rs = statement.executeQuery();
            rs.next();
            rs.getString("TC");
            return true;
        } catch (SQLException | RuntimeException throwables) {
            return false;
        }

    }

    public boolean loginAdminControl(Double TC , String password){
        try {
            PreparedStatement statement =  connection.prepareStatement("select password,TC from users where password = ? AND TC = ? AND admin_F = true");
            statement.setString(1,password);
            statement.setDouble(2,TC);
            ResultSet rs = statement.executeQuery();
            rs.next();
            rs.getString("TC");
            return true;
        } catch (SQLException | RuntimeException throwables) {
            return false;
        }

    }

    public void updateCreditControl(String TC,boolean confirmation){
        try {
            PreparedStatement statement;
            if (confirmation){
                statement = connection.prepareStatement("UPDATE credits set confirmation = true where TC = ?");
            }else{
                statement = connection.prepareStatement("UPDATE credits set confirmation = true, isPaid= true where TC = ?");
            }
            statement.setString(1,TC);
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



    public boolean updatePassword(Double TC, String password){
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE users set password = ? where TC = ?");
            statement.setString(1,password);
            statement.setDouble(2,TC);
            statement.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean updateAddress(Double TC, String address){
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE users set address = ? where TC = ?");
            statement.setString(1,address);
            statement.setDouble(2,TC);
            statement.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean updateMail(Double TC, String mail){
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE users set mail = ? where TC = ?");
            statement.setString(1,mail);
            statement.setDouble(2,TC);
            statement.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean IBANConflictControl(String IBAN){
        try {
            PreparedStatement statement =  connection.prepareStatement("select IBAN from accounts where IBAN = ?");
            statement.setString(1,IBAN);
            ResultSet rs = statement.executeQuery();
            rs.next();
            rs.getString(1);
            return true;
        } catch (SQLException | RuntimeException throwables) {
            return false;
        }
    }

    public String[] getUserInfo(String TC){
        try {
            PreparedStatement statement1 = connection.prepareStatement("select F_Name,L_Name,IBAN,amount,mail,address from accounts,users where users.TC=accounts.TC and mainAccF = true and users.TC = ?");
            statement1.setString(1,TC);
            ResultSet rs = statement1.executeQuery();
            rs.next();
            return new String[]{rs.getString("F_Name"),
            rs.getString("L_Name"),
            rs.getString("IBAN"),
            String.valueOf(rs.getDouble("amount")),
            rs.getString("mail"),
            rs.getString("address")};

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }


    public List<String[]> getAccountData(String TC){
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT IBAN,amount,currency,depositAccF from accounts where TC = ? AND mainAccF = false");
            statement.setString(1,TC);
            ResultSet rs = statement.executeQuery();
            List<String[]> data = new ArrayList<>();
            while (rs.next()){
                data.add(new String[]{rs.getString("IBAN"),
                        String.valueOf(rs.getDouble("amount")),
                        rs.getString("currency"),
                        String.valueOf(rs.getBoolean("depositAccF"))
                });
            }
            return data;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }


    }

    public boolean addNewDrawAccount(Double TC,Double money,Double currAmount,String currency,boolean deposit){
        try {
            PreparedStatement statement1 = connection.prepareStatement("insert into accounts (TC,IBAN,amount,currency,depositAccF,openDate) value (?,?,?,?,?,?)");
            PreparedStatement statement2 = connection.prepareStatement("UPDATE accounts set amount = amount - ? where TC = ? AND mainAccF = true ");
            statement1.setDouble(1,TC);
            statement1.setString(2, StaticMethod.IBANCalculator());
            statement1.setDouble(3,currAmount);
            statement1.setString(4,currency);
            statement1.setBoolean(5,deposit);
            statement1.setTimestamp(6,currentDate,cal);
            statement2.setDouble(1,money);
            statement2.setDouble(2,TC);
            statement1.execute();
            statement2.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
    public boolean addNewDepositAccount(Double TC,Double money,Double currAmount,String currency,boolean deposit){
        try {
            PreparedStatement statement1 = connection.prepareStatement("insert into accounts (TC,IBAN,amount,currency,depositAccF,openDate,interestDate) value (?,?,?,?,?,?,?)");
            PreparedStatement statement2 = connection.prepareStatement("UPDATE accounts set amount = amount - ? where TC = ? AND mainAccF = true ");
            statement1.setDouble(1,TC);
            statement1.setString(2, StaticMethod.IBANCalculator());
            statement1.setDouble(3,currAmount);
            statement1.setString(4,currency);
            statement1.setBoolean(5,deposit);
            statement1.setTimestamp(6,currentDate,cal);
            statement1.setTimestamp(7,currentDate,cal);
            statement2.setDouble(1,money);
            statement2.setDouble(2,TC);
            statement1.execute();
            statement2.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }


    public boolean addNewGoldAccount(Double TC, double money){
        try {
            PreparedStatement statement1 = connection.prepareStatement("insert into accounts (TC,IBAN,currency,openDate,amount) value (?,?,?,?,?)");
            PreparedStatement statement2 = connection.prepareStatement("UPDATE accounts set amount = amount - ? where TC = ? AND mainAccF = true ");
            statement1.setDouble(1,TC);
            statement1.setString(2, StaticMethod.IBANCalculator());
            statement1.setString(3,"Gold");
            statement1.setTimestamp(4,currentDate,cal);
            statement1.setDouble(5,money);
            statement2.setDouble(1,money);
            statement2.setDouble(2,TC);
            statement1.execute();
            statement2.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public void transaction( String sendIBAN,String recevIBAN,double amount){
        try{
            PreparedStatement statement1=connection.prepareStatement("insert into transactions (senderIBAN,receiverIBAN,amount,T_date) value (?,?,?,?)");
            statement1.setString(1,sendIBAN);
            statement1.setString(2,recevIBAN);
            statement1.setDouble(3,amount);
            statement1.setTimestamp(4,currentDate,cal);

            statement1.execute();



        }catch (SQLException throwables){
            throwables.printStackTrace();

        }

    }



    public boolean transactionAmountControl(String IBAN,double transAmount){
        try{
            PreparedStatement statement=connection.prepareStatement("select amount from accounts where IBAN = ?" );
            statement.setString(1,IBAN);
            ResultSet rs=statement.executeQuery();
            rs.next();
            double amount=  rs.getDouble("amount");
            if(transAmount<=amount){
                return  true;
            }else {
                return false;
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
            return false ;
        }

    }


    public void transactionAmountSameCur(String sendIBAN, String recevIBAN, double value) {

        try{
            PreparedStatement statement=connection.prepareStatement("UPDATE accounts set amount=amount - ? where IBAN = ?");
            PreparedStatement statement1=connection.prepareStatement("UPDATE accounts set amount=amount + ? where IBAN = ?");
            statement.setDouble(1,value);
            statement.setString(2,sendIBAN);
            statement1.setDouble(1,value);
            statement1.setString(2,recevIBAN);
            statement.execute();
            statement1.execute();

        }catch (SQLException throwables){
            throwables.printStackTrace();

        }

    }

    public void transactionAmountDiffCur(String sendIBAN, String recevIBAN, double value , double APIValue) {
        try{
            PreparedStatement statement=connection.prepareStatement("UPDATE accounts set amount=amount - ? where IBAN = ?");
            PreparedStatement statement1=connection.prepareStatement("UPDATE accounts set amount=amount + ? where IBAN = ?");
            statement.setDouble(1,value);
            statement.setString(2,sendIBAN);
            statement1.setDouble(1,APIValue);
            statement1.setString(2,recevIBAN);
            statement.execute();
            statement1.execute();

        }catch (SQLException throwables){
            throwables.printStackTrace();

        }

    }


    public boolean currencyAccountControl(String senderIBAN,String recevIBAN){
        try{
            PreparedStatement statement=connection.prepareStatement("select currency from accounts where IBAN = ? and currency = (select currency from accounts where IBAN = ?) and TC != (select TC from accounts where IBAN = ?)" );
            statement.setString(1,senderIBAN);
            statement.setString(2,recevIBAN);
            statement.setString(3,recevIBAN);
            ResultSet rs=statement.executeQuery();
        if(rs.next()){
            return true;
       }
            return false;
        }catch (SQLException throwables){
            throwables.printStackTrace();
            return false ;
        }

    }

    public double totalAmount(Double TC){
        try {

            PreparedStatement statement=connection.prepareStatement("select SUM(amount) from accounts where TC = ? and currency = 'TL'");
            statement.setDouble(1,TC);
            ResultSet rs=statement.executeQuery();
            rs.next();
            double totalAmo=rs.getDouble("SUM(amount)");
            return  totalAmo;

        }catch (SQLException throwables){
            throwables.printStackTrace();
            return  0;
        }
    }

    public void creditApply(String TC, double amount, int creditMonths, double withInterest,int paymentDate){
        try {

            PreparedStatement statement=connection.prepareStatement("insert into credits (TC,amount,creditMonths,withInterest,getCreditDate,paidAmount,paymentDate,restMonth) value (?,?,?,?,?,?,?,?)");
            statement.setString(1,TC);
            statement.setDouble(2,amount);
            statement.setInt(3,creditMonths);
            statement.setDouble(4,withInterest);
            statement.setTimestamp(5,currentDate,cal);
            statement.setInt(6,0);
            statement.setInt(7,paymentDate);
            statement.setInt(8,creditMonths);

            statement.execute();


        }catch (SQLException throwables){
            throwables.printStackTrace();

        }
    }

    public void creditPaymentCreate(int paymentDate,int creditMonths,int creditID){
        Date date=new Date(currentDate.getYear(),currentDate.getMonth(),paymentDate);
        PreparedStatement statement1;
        for(int i=1;i<=creditMonths;i++){
            try {
                statement1=connection.prepareStatement("insert into creditPayment(amount, paymentDate, creditID) values (" +
                                                           "(select withInterest from credits where creditID = ?)/(select creditMonths from credits where creditID = ?)," +
                                                                "date_add(?,INTERVAL ? MONTH ),?)");
                statement1.setInt(1,creditID);
                statement1.setInt(2,creditID);
                statement1.setTimestamp(3,new Timestamp(date.getTime()),cal);
                statement1.setInt(4,i);
                statement1.setInt(5,creditID);
                statement1.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
    }

    public String[] getCreditInfo(String TC){
        try {
            PreparedStatement statement = connection.prepareStatement("select creditID,withInterest,paidAmount,getCreditDate,paymentDate,restMonth,creditMonths from credits where TC = ? and isPaid = false");
            statement.setString(1, TC);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return new String[]{
                        rs.getString("withInterest"),
                        rs.getString("paidAmount"),
                        rs.getString("getCreditDate"),
                        rs.getString("paymentDate"),
                        rs.getString("restMonth"),
                        rs.getString("creditMonths"),
                        rs.getString("creditID")};

            }catch (SQLException throwables){
                throwables.printStackTrace();
            return  null;
        }
    }

    public int controlConfirmation(String TC){
        try {
        PreparedStatement statement=connection.prepareStatement("select confirmation from credits where TC = ? and isPaid = false");
        statement.setString(1,TC);
        ResultSet rs=statement.executeQuery();
            rs.next();

            if(rs.getBoolean("confirmation")) {
               return 1; //accepted
            }else
            return 2; //waiting credit

        }catch (SQLException throwables){

            return 0; //no applied credit
        }
    }

    public ObservableList<ModelTable> fillTableCol (String TC,boolean query1){
        try {
            ObservableList<ModelTable> data = FXCollections.observableArrayList();
            PreparedStatement statement;
            if(query1) {
                statement = connection.prepareStatement("select F_Name,L_Name,receiverIBAN,pro2.amount,pro2.T_date from users,(select TC,receiverIBAN,pro.amount,T_date from accounts,(select receiverIBAN,transactions.amount,T_date from transactions,accounts where IBAN = transactions.senderIBAN and TC = ?)pro where IBAN=pro.receiverIBAN)pro2 where pro2.TC = users.TC");
            }
            else{
                statement = connection.prepareStatement("select F_Name,L_Name,receiverIBAN,amount,T_date from users," +
                                                                "(select TC,receiverIBAN,prod2.amount,T_date from accounts," +
                                                                    "(select senderIBAN,receiverIBAN,amount,T_date from transactions, " +
                                                                        "(select IBAN from accounts where TC = ?)pro where receiverIBAN = pro.IBAN)prod2 " +
                                                            "where IBAN = prod2.senderIBAN)pro3 where pro3.TC = users.TC;");
            }
            statement.setString(1,TC);
            ResultSet rs = statement.executeQuery();
            SimpleDateFormat fromDB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            while (rs.next()){
                String date = myFormat.format(fromDB.parse(rs.getString("T_Date")));
                if(query1){
                    data.add(new ModelTable(rs.getString("F_Name") + " " + rs.getString("L_Name"), rs.getString("receiverIBAN"),"-" + rs.getString("amount"),date));
                }
                else{
                    data.add(new ModelTable(rs.getString("F_Name") + " " + rs.getString("L_Name"), rs.getString("receiverIBAN"),"+" + rs.getString("amount"),date));
                }
            }
            return data;
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
            return null;
        }

    }


    public ObservableList<PieChart.Data> fillPieChart (String TC){
        try {
            ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
            PreparedStatement statement = connection.prepareStatement("select currency,SUM(amount) from accounts where TC = ? group by currency");
            statement.setString(1,TC);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                data.add(new PieChart.Data(rs.getString("currency"),rs.getDouble("SUM(amount)")));
                }
            return data;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    public List<String[]> getAccountDataForTrans(String TC){
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT IBAN,amount,currency from accounts where TC = ? and depositAccF = false and currency != 'Gold'");
            statement.setString(1,TC);
            ResultSet rs = statement.executeQuery();
            List<String[]> accountsData = new ArrayList<>();

            while (rs.next()){
                accountsData.add(new String[]{rs.getString("IBAN"),
                        String.valueOf(rs.getInt("amount")),
                        rs.getString("currency"),
                });
            }
            return accountsData;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }


    }
    public List<String[]> getNotificationData(String TC){
        try {
            PreparedStatement statement = connection.prepareStatement("select F_Name,L_Name,amount,T_date,notID from users," +
                                                                            "(select TC,prod2.amount,T_date,notID from accounts," +
                                                                                "(select senderIBAN,amount,T_date,notID from transactions, " +
                                                                                    "(select IBAN from accounts where TC = ?)pro " +
                                                                                "where receiverIBAN = pro.IBAN and isRead = false)prod2 " +
                                                                          "where IBAN = prod2.senderIBAN)pro3 where pro3.TC = users.TC");
            statement.setString(1,TC);
            ResultSet rs = statement.executeQuery();
            List<String[]> data = new ArrayList<>();
            SimpleDateFormat fromDB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            while (rs.next()){
                String date = myFormat.format(fromDB.parse(rs.getString("T_Date")));
                data.add(new String[]{rs.getString("F_Name"),
                        rs.getString("L_Name"),
                        rs.getString("amount"),
                        date,
                        rs.getString("notID")
                });
            }
            return data;
        }catch (SQLException | ParseException e){
            e.printStackTrace();
            return null;
        }
    }

    public void notificationUpdate(int notID){
        try {
            PreparedStatement statement = connection.prepareStatement("update transactions set isRead = true where notID = ?");
            statement.setInt(1,notID);
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public List<String[]> getAccountDataForChange(String TC,String IBAN){
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT IBAN,amount,currency from accounts where TC = ? and depositAccF = false and IBAN != ?");
            statement.setString(1,TC);
            statement.setString(2,IBAN);
            ResultSet rs = statement.executeQuery();
            List<String[]> accountsData = new ArrayList<>();
            while (rs.next()){
                accountsData.add(new String[]{rs.getString("IBAN"),
                        String.valueOf(rs.getInt("amount")),
                        rs.getString("currency"),
                });
            }
            return accountsData;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }


    }

    public ObservableList<PaymentTable> paymentTable(String TC){
        try {
            PreparedStatement statement=connection.prepareStatement("Select IBAN,currency,amount from accounts where TC = ? and depositAccF=false and currency='TL' ");
            statement.setString(1,TC);
            ResultSet rs=statement.executeQuery();
            ObservableList<PaymentTable> data=FXCollections.observableArrayList();
            while (rs.next()){
                data.add(new PaymentTable(rs.getString("IBAN"),rs.getString("currency"),rs.getDouble("amount")));
            }
            return data;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public ObservableList<CreditTable> creditTable(String TC){
        try {

          PreparedStatement  statement = connection.prepareStatement("Select creditPayment.amount,creditPayment.paymentDate," +
                                                                            "creditPayment.amount*0.02*DATEDIFF(now(),creditPayment.paymentDate) as fee  " +
                                                                         "from creditPayment,credits " +
                                                                         "where credits.creditID=creditPayment.creditID and TC = ? and creditPayment.isPaid = false");
            statement.setString(1,TC);
            ResultSet rs=statement.executeQuery();
            ObservableList<CreditTable> data=FXCollections.observableArrayList();
            while(rs.next()){
                double fee=rs.getDouble("fee");
                if(fee<0) fee=0;
                data.add(new CreditTable(rs.getDouble("amount"),fee,rs.getDate("paymentDate")));
            }
            return data;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }


    }

    public void paymentCredit(String TC,Date date){
        try {
            PreparedStatement statement=connection.prepareStatement("update creditPayment set isPaid=true where paymentDate = ? and creditID =(select creditID from credits where TC = ? and isPaid = false);");
            statement.setDate(1,date);
            statement.setString(2,TC);
            statement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void payCredit(String TC ,double amount,String IBAN,int creditID,double latefee){
        try {
            PreparedStatement statement1 = connection.prepareStatement("update accounts set amount = amount - ? where IBAN =?");
            PreparedStatement statement2 = connection.prepareStatement("update credits set paidAmount = paidAmount + ? , withInterest = withInterest + ? where creditID =?");

            statement1.setDouble(1,amount);
            statement1.setString(2,IBAN);
            statement2.setDouble(1,amount);
            statement2.setDouble(2,latefee);
            statement2.setInt(3,creditID);
            statement1.execute();
            statement2.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ObservableList<AdminTable> adminTable(){
        try {

            PreparedStatement  statement = connection.prepareStatement("select TC,amount,withInterest,creditMonths,getCreditDate,paymentDate,creditID from credits where confirmation = false");
            ResultSet rs=statement.executeQuery();
            ObservableList<AdminTable> data=FXCollections.observableArrayList();
            while(rs.next()){
                data.add(new AdminTable(rs.getString("TC"),rs.getDouble("amount"),rs.getDouble("withInterest"),rs.getInt("creditMonths"),rs.getInt("paymentDate"),rs.getDate("getCreditDate"),rs.getInt("creditID")));
            }
            return data;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }


    }

    public void creditConfirmationAdmin(String TC,double amount,int paymentDate, int creditMonths, int creditID){
        try {
            PreparedStatement statement = connection.prepareStatement("update accounts set amount = amount + ? where TC = ? and mainAccF = true");
            statement.setDouble(1,amount);
            statement.setString(2,TC);
            statement.execute();
            creditPaymentCreate(paymentDate,creditMonths,creditID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }




    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }




}
