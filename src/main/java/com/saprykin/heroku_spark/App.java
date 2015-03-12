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

        get("/hello", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                DaoTest daoTest = new DaoTest();

                return "<html><head><h1>Hello World!</h1></head><body>" + "<h2>" + daoTest.testDb() + "</h2>" + "</body></html>";
            }
        });
    }


}
