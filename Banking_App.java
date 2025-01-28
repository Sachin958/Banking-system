import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Scanner;
import com.sun.security.jgss.GSSUtil;

public class Banking_App {
    private static final String  url = "jdbc:mysql://localhost:3306/banking_system ";
    private static final String username = "root";
    private static final String password = "1230.";

    public static void main(String[] args) throws ClassNotFoundException , RuntimeException {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully...");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection connection = DriverManager.getConnection(url , username , password);
            System.out.println("Connection established successfully...");
            Scanner sc = new Scanner(System.in);
            User user = new User(connection , sc);
            Accounts accounts = new Accounts(connection , sc);
            AccountManager accountManager = new AccountManager(connection , sc);

            String email;
            long account_number;

            while(true){
                System.out.println("--WELCOME TO BANKING APPLICATION--");

                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println(" Enter your choice");
                int choice = sc.nextInt();
                switch (choice){
                    case 1 :
                        user.register();
                        break;
                    case 2 :
                        email = user.login();
                        if(email != null){
                            System.out.println("User logged In..");
                            if(!accounts.account_exist(email)){
                                System.out.println("1. open a new bank account");
                                System.out.println("2. Exit");
                                System.out.print("Enter your choice : ");
                                if(sc.nextInt() == 1){
                                    accounts.open_account();
                                    System.out.println("Account created successfully");
                                }else{
                                    break;
                                }
                            }
                            account_number = accounts.getAccount_number(email);

//                            int choice2 = 0;
                            while (true) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.println("Enter your choice: ");
                                int choice2 = sc.nextInt();
                                switch (choice2) {
                                    case 1:
                                        accountManager.debit_money();
                                        break;
                                    case 2:
                                        accountManager.credit_money();
                                        break;
                                    case 3:
                                        accountManager.transfer_money();
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }

                        }
                        else{
                            System.out.println("Incorrect Email or Password!");
                        }
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Exiting System!");
                        return;
                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}