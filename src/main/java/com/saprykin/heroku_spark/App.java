package com.saprykin.heroku_spark;


import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;


// heroku auth:token

public class App {

    public static void main(String[] args) {

        //Heroku assigns different port each time, hence reading it from process.
        ProcessBuilder process = new ProcessBuilder();
        Integer port;
        if (process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 8080;
        }
        setPort(port);

        get("/hello", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {

                return "<html><head><h1>Hello World!</h1></head><body>" + "<h2>" + testDb() + "</h2>" + "</body></html>";
            }
        });
    }


    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }

    private static String testDb() {
        String result = "";

        Connection connection = null;
        try {
            connection = getConnection();
        } catch(URISyntaxException | SQLException e) {
            result += "couldn't get connection!";
        }



        try {
            Statement stmt;
            if(connection != null) {
                stmt = connection.createStatement();
            }
            else {
                stmt = null;
                result += "\nconnection is null, and statement couldn't be created";
            }
            StringBuilder res = new StringBuilder();

            if(stmt != null) {
                stmt.executeUpdate("DROP TABLE IF EXISTS users");
                stmt.executeUpdate("CREATE TABLE users " +
                        "(id INTEGER NOT NULL AUTO_INCREMENT," +
                        " email VARCHAR(100) NOT NULL " +
                        "PRIMARY KEY (id) )");

                stmt.executeUpdate("INSERT INTO users (email) VALUES ('foo@bar.com'), ('bar@foo.com'), ('foobar@bf.com') ");
                ResultSet rs = stmt.executeQuery("SELECT * FROM users");

                res.append("Read from DB:\n");
                while(rs.next()) {
                    res.append("id: ");
                    res.append(rs.getInt("id"));
                    res.append("e-mail: ");
                    res.append(rs.getString("email"));
                }
            }

            result = res.toString();

        } catch(SQLException e) {
            result += "\n";
            result += e.toString();
        }

        return result;
    }

}
