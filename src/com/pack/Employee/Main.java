package com.pack.Employee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {

    public static void main(String[] args)
    {
        System.out.println("jjj");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded Successfully");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "tiger12345");
            System.out.println("Connection Established!");
            Statement statement = con.createStatement();
            String query = "Select * from testdb.new_table";
            ResultSet rs = statement.executeQuery(query);

            while(rs.next() != false)
            {
                System.out.println(rs.getInt(1));
                System.out.println(rs.getString(2));

            }
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
    }
}
