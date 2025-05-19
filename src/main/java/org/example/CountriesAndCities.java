package org.example;

import java.util.*;

public class CountriesAndCities {
    private static final Map<String, List<String>> countriesAndCities = new LinkedHashMap<>();

    static {
        countriesAndCities.put("Uzbekistan", Arrays.asList(
                "Toshkent", "Andijon", "Angren", "Bekobod", "Buxoro", "Denov", "Fargʻona",
                "Guliston", "Jizzax", "Kattaqoʻrgʻon", "Qoʻqon", "Margʻilon", "Namangan",
                "Navoiy", "Nukus", "Olmaliq", "Qarshi", "Samarqand", "Termiz", "Urganch",
                "Xiva", "Chirchiq", "Zarafshon", "Yangiyer", "Shahrisabz", "Asaka", "Quva"
        ));
        countriesAndCities.put("Kazakhstan", Arrays.asList(
                "Astana", "Almaty", "Shymkent", "Karaganda", "Aktobe", "Taraz", "Atyrau", "Kostanay"
        ));
        countriesAndCities.put("Kyrgyzstan", Arrays.asList(
                "Bishkek", "Osh", "Jalal-Abad", "Karakol", "Tokmok", "Naryn"
        ));
        countriesAndCities.put("Tajikistan", Arrays.asList(
                "Dushanbe", "Khujand", "Kulob", "Qurghonteppa", "Istaravshan", "Vahdat"
        ));
        countriesAndCities.put("Turkmenistan", Arrays.asList(
                "Ashgabat", "Turkmenabat", "Mary", "Dashoguz", "Balkanabat", "Türkmenbasy"
        ));
        countriesAndCities.put("Russia", Arrays.asList("Moscow", "Saint Petersburg", "Novosibirsk", "Kazan", "Yekaterinburg"));
        countriesAndCities.put("United States", Arrays.asList("New York", "Los Angeles", "Chicago", "Houston", "Phoenix"));
        countriesAndCities.put("Turkey", Arrays.asList("Istanbul", "Ankara", "Izmir", "Bursa", "Antalya"));
        countriesAndCities.put("Saudi Arabia", Arrays.asList("Riyadh", "Jeddah", "Mecca", "Medina", "Dammam"));
        countriesAndCities.put("India", Arrays.asList("New Delhi", "Mumbai", "Hyderabad", "Chennai", "Bangalore"));
        countriesAndCities.put("Pakistan", Arrays.asList("Karachi", "Lahore", "Islamabad", "Rawalpindi", "Peshawar"));
        countriesAndCities.put("Egypt", Arrays.asList("Cairo", "Alexandria", "Giza", "Shubra El Kheima", "Port Said"));
        countriesAndCities.put("Indonesia", Arrays.asList("Jakarta", "Surabaya", "Bandung", "Medan", "Semarang"));
        countriesAndCities.put("Malaysia", Arrays.asList("Kuala Lumpur", "Johor Bahru", "Shah Alam", "Ipoh", "George Town"));
        countriesAndCities.put("United Arab Emirates", Arrays.asList("Dubai", "Abu Dhabi", "Sharjah", "Al Ain", "Ajman"));
        countriesAndCities.put("Bangladesh", Arrays.asList("Dhaka", "Chittagong", "Khulna", "Rajshahi", "Sylhet"));
        countriesAndCities.put("Nigeria", Arrays.asList("Lagos", "Abuja", "Kano", "Ibadan", "Port Harcourt"));
        countriesAndCities.put("United Kingdom", Arrays.asList("London", "Manchester", "Birmingham", "Leeds", "Glasgow"));
        countriesAndCities.put("Germany", Arrays.asList("Berlin", "Hamburg", "Munich", "Cologne", "Frankfurt"));
        countriesAndCities.put("France", Arrays.asList("Paris", "Lyon", "Marseille", "Toulouse", "Nice"));
    }

    public static List<String> getCountries() {
        return new ArrayList<>(countriesAndCities.keySet());
    }

    public static List<String> getCities(String country) {
        return countriesAndCities.getOrDefault(country, List.of());
    }

    public static int getMethodForCountry(String country) {
        return switch (country) {
            case "Saudi Arabia", "United Arab Emirates", "Qatar" -> 4; // Umm al-Qura University
            case "Turkey" -> 13; // Diyanet İşleri Başkanlığı
            case "Pakistan" -> 1;  // University of Islamic Sciences, Karachi
            case "Iran" -> 7;       // Institute of Geophysics, University of Tehran
            case "Egypt" -> 5;      // Egyptian General Authority of Survey
            case "Malaysia" -> 11;  // JAKIM
            case "Indonesia" -> 12; // Kemenag
            case "Uzbekistan", "Kazakhstan", "Kyrgyzstan", "Tajikistan", "Turkmenistan" -> 14; // Custom: namozvaqti.uz method
            default -> 5;            // Default: Egypt method
        };
    }
}
