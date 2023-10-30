package services;

import java.sql.Connection;
import java.sql.DriverManager;

public class Provider {
    Connection cn;
    public Connection dbconnect() {
        try {
            // below two lines are used for connectivity.
            Class.forName("com.mysql.cj.jdbc.Driver");
            cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/playcrud","root", "");
            System.out.println("--------------------Connection succé------------------------");

        }
        catch (Exception exception) {
            System.out.println("Ereur de connexion ---------------------- " + exception);
        }
        return cn;
    }

}