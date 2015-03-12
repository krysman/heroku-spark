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
        if(process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 8080;
        }
        setPort(port);

        DaoTest daoTest = new DaoTest();
        final String dbTestString = "dbTestString\n" + daoTest.testDb();

        get("/hello", (request, response) -> "<html><head><h1>Hello World!</h1></head><body>" + "<h2>" + dbTestString + "</h2>" + "</body></html>");
    }


}
