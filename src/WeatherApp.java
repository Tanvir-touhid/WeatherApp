import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherApp {
    private static final String API_KEY = "Your API-Key";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Provide the name of the city: ");
        String city = sc.nextLine();

        try {
            // Fetch the weather data from the API
            String weatherData = getWeatherData(city);
            // Display the weather information
            displayWeather(weatherData);
        } catch (Exception e) {
            System.out.println("Error fetching weather data: " + e.getMessage());
        }
    }

    // Method to fetch weather data from the OpenWeatherMap API
    private static String getWeatherData(String city) throws Exception {
        String urlString = BASE_URL + city + "&appid=" + API_KEY;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Read the response from the API
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();

        return content.toString();
    }

    // Method to parse and display the weather data
    private static void displayWeather(String weatherData) {
        // Parse the JSON data using Gson
        JsonObject jsonObject = JsonParser.parseString(weatherData).getAsJsonObject();

        // Extract data like city name, temperature, and weather description
        String cityName = jsonObject.get("name").getAsString();
        double temperature = jsonObject.get("main").getAsJsonObject().get("temp").getAsDouble();
        String weatherDescription = jsonObject.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString();

        // Display the extracted weather information
        System.out.println("Weather in " + cityName + ":");
        System.out.println("Temperature: " + (temperature - 273.15) + "°C"); // Convert Kelvin to Celsius
        System.out.println("Condition: " + weatherDescription);
    }
}
