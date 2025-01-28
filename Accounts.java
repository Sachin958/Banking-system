import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Accounts {
    Connection connection;
    Scanner sc ;
    Accounts(Connection connection , Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }

    public long getAccount_number(String email){
        System.out.print("Enter your name : ");
        String name = sc.next();

        System.out.print("Enter your security pin : ");
        int pin = sc.nextInt();

        String query = "select account_number from account where email = ? and security_pin = ? ;";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1 , email);
            ps.setInt(2 , pin);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                return rs.getLong("account_number");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return 0;
    }

    public boolean account_exist(String email){
            String query = "select account_number from account where email = '?';";
            try{
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
//                while(rs.next()){
////                    if(rs.getString("email") == email){
////                        return true;
////                    }
//                }
                return rs.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
//        return false;
    }

    public void open_account(){
        System.out.print("Enter your full name : ");
        String name = sc.next();

        System.out.print("Enter your email : ");
        String email = sc.next();

        System.out.print("Enter initial Balance : ");
        double balance = sc.nextDouble();

        System.out.print("Enter security pin : ");
        int pin = sc.nextInt();

        String query = "insert into account(full_name , email, balance , security_pin)values(?,?,?,?);";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1 , name);
            ps.setString(2 , email);
            ps.setDouble(3 , balance);
            ps.setInt(4 , pin);
            int row_effected = ps.executeUpdate();
            if(row_effected > 0){
                System.out.println("Account created successfully ....");
            }else{
                System.out.println("Account creation failed ....");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


    }

}
