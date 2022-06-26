package com.mycompany.app;

public class WeatherResponse {
    private String name;
    private MainResponse main;

    public WeatherResponse() {
    }

    public WeatherResponse(String name, MainResponse main) {
        this.name = name;
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MainResponse getMain() {
        return main;
    }

    public void setMain(MainResponse main) {
        this.main = main;
    }
}