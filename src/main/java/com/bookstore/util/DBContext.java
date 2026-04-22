package com.bookstore.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBContext {

    private static String url;
    private static String driver;
    private static String username;
    private static String password;

    static {
        try {
            Properties props = new Properties();
            InputStream input = DBContext.class
                    .getClassLoader()
                    .getResourceAsStream("db.properties");

            props.load(input);

            url = props.getProperty("db.url");
            driver = props.getProperty("db.driver");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");

            Class.forName(driver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, username, password);
    }
}