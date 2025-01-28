import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    Connection connection;
    Scanner sc;
    AccountManager(Connection connection , Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }


    public void credit_money(){
        System.out.print("Enter your account number : ");
        long account_number =sc.nextLong();

        System.out.print("Enter your security pin");
        int pin = sc.nextInt();

        System.out.print("Enter amount to credit : ");
        double credit_amount = sc.nextDouble();

        String query = "update account set balance = balance + ? where account_number = ? and security_pin = ? ;";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setDouble(1 , credit_amount);
            ps.setLong(2 , account_number);
            ps.setInt(3 , pin);

            int row_effected = ps.executeUpdate();
            if(row_effected > 0){
                System.out.println("Amount credited successfully....");
            }else{
                System.out.println("Failed!!!");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void debit_money(){
        System.out.print("Enter your account number : ");
        long account_number = sc.nextLong();

        System.out.print("Enter your security pin");
        int  pin = sc.nextInt();

        System.out.print("Enter amount to withdraw :");
        double withdraw_amount = sc.nextDouble();

        String query = "select balance from account where account_number = '?' and security_pin = ?;" ;
        String query2 = "update account set balance = balance - ? where account_number = ? and security_pin = ?;";

        try{
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1 , account_number);
            ps.setInt(2 , pin);
            ResultSet rs = ps.executeQuery();
            double current_balance = 0;
            if(rs.next()){
                current_balance = rs.getDouble("balance");
            }
            if(withdraw_amount <= current_balance){
                PreparedStatement ps2 = connection.prepareStatement(query2);
                ps2.setDouble(1 , withdraw_amount);
                ps2.setLong(2 , account_number);
                ps2.setInt(3 , pin);
                connection.commit();
            }
            connection.rollback();



        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void transfer_money(){
        System.out.print("Enter receiver account number : ");
        long receiver_ac = sc.nextLong();

        System.out.print("Enter sender account number : ");
        long sender_ac = sc.nextLong();

        System.out.print("Enter sender's security pin : ");
        int pin = sc.nextInt();

        System.out.print("Enter transfer amount : ");
        double t_money = sc.nextDouble();
        String withdraw_query = "update account set balance = balance - ? where account_number = ? and security_pin = ?;";
        String transfer_query = "update account set balance = balance + ? where account_number = ?;";
        try{
            PreparedStatement wps = connection.prepareStatement(withdraw_query);
            PreparedStatement tps = connection.prepareStatement(transfer_query);

            wps.setDouble(1 , t_money);
            wps.setLong(2 , sender_ac);
            wps.setInt(3 , pin);

            tps.setDouble(1 , t_money);
            tps.setLong(2 , receiver_ac);

            int row_effected_withdraw = wps.executeUpdate();
            int row_effected_deposite = tps.executeUpdate();
            if(row_effected_deposite > 0 && row_effected_withdraw > 0){
                connection.commit();
                System.out.println("Transaction successfull...");
            }else {
                connection.rollback();
                System.out.println("Transation failed!!!");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void getBalance(long accountNumber) {
        System.out.print("Enter your security pin : ");
        int pin = sc.nextInt();

        String query = "select balance from account where security_pin = ? ;";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1 , pin);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getDouble("balance"));
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
