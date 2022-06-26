package com.mycompany.app;

import com.google.gson.Gson;
import com.squareup.okhttp.*;

public class WeatherServiceAPI {
    private static final String ACCESS_KEY = "0b66a7d3d8a71dd36a55e2f3d12456df";
    private static final String BASE_URL = "https://api.openweathermap.org";
    //?q=Tirana&appid=0b66a7d3d8a71dd36a55e2f3d12456df

    public MainResponse getWeatherDataFromApi(String city) {
        try {
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder
                    = HttpUrl.parse(BASE_URL + "/data/2.5/weather").newBuilder();


            urlBuilder.addQueryParameter("q", city);
            urlBuilder.addQueryParameter("appid", ACCESS_KEY);
            String url = urlBuilder.build().toString();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            String sResponse = response.body().string();
            System.out.println(sResponse);

            Gson gson = new Gson();
            WeatherResponse weatherResponse = gson.fromJson(sResponse, WeatherResponse.class);
            return weatherResponse.getMain();
        } catch (Exception e) {
            System.out.println("* Something went wrong while retrieving weather data. *");
        }
        return null;
    }
}
