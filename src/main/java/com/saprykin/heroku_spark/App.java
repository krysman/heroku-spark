package com.saprykin.heroku_spark;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//import spark.template.freemarker.FreeMarkerRoute;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import static spark.Spark.*;
import spark.*;

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

        //staticFileLocation("/spark"); // Static files

        /*get("/", (request, response) -> {

            // The hello.ftl file is located in directory:
            // src/test/resources/spark/template/freemarker
            return new ModelAndView(null, "login.ftl.html");
        }, new FreeMarkerEngine());*/

        get("/hello", (request, response) -> {
            return "<html><head><h1>Hello World!</h1></head><body></body></html>";
        });

        /*get(new FreeMarkerRoute("/") {
            @Override
            public ModelAndView handle(Request request, Response response) {
                return modelAndView(null, "login.ftl.html");
            }
        });*/


        /*post("/", (request, response) -> {
            String username = request.queryParams("username");
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Welcome " + username + ", Time now is: " + new Date());

            return new ModelAndView(attributes, "home.ftl.html");
        }, new FreeMarkerEngine());*/

        /*post(new FreeMarkerRoute("/") {
            @Override
            public ModelAndView handle(Request request, Response response) {
                String username = request.queryParams("username");
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("message", "Welcome " + username + ", Time now is: " + new Date());
                return modelAndView(attributes, "home.ftl.html");
            }
        });*/

    }

}
