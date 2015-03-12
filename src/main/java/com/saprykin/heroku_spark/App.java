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

                final Connection finalConnection = getConnection();

                Statement stmt = finalConnection != null ? finalConnection.createStatement() : null;
                StringBuilder result = new StringBuilder();

                try {
                    if(stmt != null) {
                        stmt.executeUpdate("DROP TABLE IF EXISTS users");
                        stmt.executeUpdate("CREATE TABLE users " +
                                "(id INTEGER NOT NULL AUTO_INCREMENT," +
                                " email VARCHAR(100) NOT NULL " +
                                "PRIMARY KEY (id) )");

                        stmt.executeUpdate("INSERT INTO users (email) VALUES ('foo@bar.com'), ('bar@foo.com'), ('foobar@bf.com') ");
                        ResultSet rs = stmt.executeQuery("SELECT * FROM users");

                        result.append("Read from DB:\n");
                        while(rs.next()) {
                            result.append("id: ");
                            result.append(rs.getInt("id"));
                            result.append("e-mail: ");
                            result.append(rs.getString("email"));
                        }
                    }

                } catch(SQLException e) {
                    e.printStackTrace();
                }

                return "<html><head><h1>Hello World!</h1></head><body>" + "<h2>" + result.toString() + "</h2>" + "</body></html>";
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

}
