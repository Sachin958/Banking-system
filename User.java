import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    Connection connection;
    Scanner sc;
    User(Connection connection , Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }

    public void register(){
        System.out.print("Enter you full name : ");
        String name = sc.next();
        System.out.print("Enter your email : ");
        String email = sc.next();
        System.out.println("Enter your password : ");
        String password = sc.next();

        if(user_exist(email)){
            System.out.println("User Already Exists...");
            return;
        }else{
        String query = "insert into user(full_name, email , password)values(?,?,?);";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1 , name);
            ps.setString(2 , email);
            ps.setString(3 , password);

            int row_effected = ps.executeUpdate();
            if(row_effected > 0){
                System.out.println("Registration Successfully !!");
            }else{
                System.out.println("Registration failed!!!");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        }

    }
    public String login(){
        System.out.print("Enter your email : ");
        String email = sc.next();
        System.out.println("Enter your password : ");
        String password = sc.next();

        String query = "select * from user where email = ? and password = ?;";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1 , email);
            ps.setString(2 , password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return email;
            }
//            while(rs.next()){
//              return rs.getString("email");
//            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
    public boolean user_exist(String email){
        String query = "select * from user where email = ?;";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1 , email);
            ResultSet rs = ps.executeQuery();
//            if(rs.next()){
//                return true;
//            }
            return rs.next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
//    return false;
}