package com.mycompany.app;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {
    static Scanner scanner = new Scanner(System.in);
    static WeatherServiceAPI serviceAPI = new WeatherServiceAPI();

    public static void main(String[] args) throws SQLException {
        printMenu();
    }

    public static void printMenu() throws SQLException {
        System.out.println("1. Add location");
        System.out.println("2. Display location");
        System.out.println("3. Get weather");
        System.out.println("4. Get weather from API");
        System.out.print("Choose: ");
        String input = scanner.nextLine();
        switch (input) {
            case "1":
                addLocationMenu();
                break;
            case "2":
                displayLocation();
                break;
            case "3":
                getWeather();
                break;
            case "4":
                getWeatherFromAPI();
                break;
        }
    }

    public static void getWeatherFromAPI() throws SQLException {
        System.out.print("Please select city: ");
        String qytet = scanner.nextLine();
        MainResponse response = serviceAPI.getWeatherDataFromApi(qytet);
        System.out.println("Temperature: " + response.getTemp());
        System.out.println("Humidity: " + response.getHumidity());
        printMenu();
    }

    public static void getWeather() throws SQLException {
        Connection con = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/weatherlady", "root", "2009");
            statement = (Statement) con.createStatement();
            String sql;
            System.out.println("Please select city: ");
            String qytet = scanner.nextLine();
            sql = "select info from weather where city='" + qytet + "'";
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            System.out.println(resultSet.getString("info"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayLocation() throws SQLException {
        Connection con = null;
        Statement statement = null;
        try {
            ArrayList<ArrayList<String>> outer = new ArrayList<ArrayList<String>>();
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/weatherlady", "root", "2009");
            statement = (Statement) con.createStatement();
            String sql;
            sql = "select city from weather";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                ArrayList<String> inner = new ArrayList<String>();
                inner.add(resultSet.getString("city"));
                outer.add(inner);
            }
            System.out.println("The cities are as follows:");
            for (int i = 0; i < outer.size(); i++) {
                System.out.println(outer.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        printMenu();
    }

    public static void addLocationMenu() {
        String urlDb = "jdbc:mysql://localhost:3306/weatherlady";
        String user = "root";
        String password = "2009";
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));
            System.out.println("Please enter city: ");
            String city = reader.readLine();
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=7edda0bea071419a1e1da9c91ac2d4c6");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                scanner.close();
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(urlDb, user, password);
                String query = "Insert into weather (city,info) values('" + city + "', '" + informationString + "')";
                Statement statement = connection.createStatement();
                statement.execute(query);
                System.out.println("Inserted into database");
                printMenu();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}