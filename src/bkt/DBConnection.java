package bkt;

import java.sql.*;

public class DBConnection {
    private static String DRIVER="com.mysql.cj.jdbc.Driver";
    private static String URL="jdbc:mysql://localhost:3306/ss14?createDatabaseIfNotExist=true";
    private static String USERNAME="root";
    private static String PASSWORD="yen030306.";

    static Connection openConnection(){
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Chua cai dat mysql driver");
        } catch (SQLException e) {
            System.err.println("Loi SQL: Ket noi that bai");
            e.printStackTrace();
        }
        return  null;
    }
    public static void main(String[] args) {
        try(
                Connection conn=openConnection();// mo 1 connection
                Statement stm=conn.createStatement();//tao doi tuong dai dien cho cau lenh sql
        ){
            System.out.println("hahaha");
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}