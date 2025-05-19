package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class NamozVaqtiBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(NamozVaqtiBot.class);
    private static final String BOT_TOKEN = "TOKEN_HERE";
    private static final String BOT_USERNAME = "YOUR_BOT_USERNAME";
    private static final long ADMIN_CHAT_ID = 123456789L;

    private final OkHttpClient httpClient = new OkHttpClient();
    private Connection db;

    public NamozVaqtiBot() {
        initDatabase();
    }

    private void initDatabase() {
        try {
            db = DriverManager.getConnection("jdbc:sqlite:users.db");
            try (Statement st = db.createStatement()) {
                st.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, name TEXT, location TEXT, created_at TEXT)");
                st.execute("CREATE TABLE IF NOT EXISTS donations (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, amount REAL, created_at TEXT)");
            }
        } catch (SQLException e) {
            LOGGER.error("DB xatosi: {}", e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            String name = Optional.ofNullable(update.getMessage().getFrom().getUserName()).orElse("NoName");
            saveUser(chatId, name);

            switch (text) {
                case "/start" -> sendStartMenu(chatId);
                case "🕌 Namoz vaqtlari" -> {
                    String[] loc = getUserLocation(chatId);
                    if (loc == null) sendMessage(chatId, "Joylashuv tanlanmagan. 📍 Joylashuvni tanlang");
                    else sendMessage(chatId, fetchPrayerTimes(loc[0], loc[1]));
                }
                case "📍 Joylashuvni tanlash" -> sendCountrySelection(chatId);
                case "💰 Donatsiya" -> sendMessage(chatId, "💳 Donat: 9860 0201 0280 7821 (Shohruh Mirzamuxamedov)");
                case "⚙️ Admin Panel" -> {
                    if (chatId == ADMIN_CHAT_ID) sendAdminPanel(chatId);
                }
                default -> {
                    if (CountriesAndCities.getCountries().contains(text)) {
                        updateUserLocation(chatId, null, text);
                        sendCitySelection(chatId, text);
                    } else {
                        String[] loc = getUserLocation(chatId);
                        if (loc != null && CountriesAndCities.getCities(loc[1]).contains(text)) {
                            updateUserLocation(chatId, text, loc[1]);
                            sendMessage(chatId, "📍 Joylashuv saqlandi: " + text + ", " + loc[1]);
                        } else sendMessage(chatId, "Noma'lum buyruq. Tugmalardan foydalaning.");
                    }
                }
            }
        }
    }

    private void saveUser(long id, String name) {
        try (PreparedStatement ps = db.prepareStatement("INSERT OR IGNORE INTO users (id, name, created_at) VALUES (?, ?, datetime('now'))")) {
            ps.setLong(1, id);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("User saqlashda xato: {}", e.getMessage());
        }
    }

    private void updateUserLocation(long chatId, String city, String country) {
        String loc = city != null ? city + "," + country : "null," + country;
        try (PreparedStatement ps = db.prepareStatement("UPDATE users SET location = ? WHERE id = ?")) {
            ps.setString(1, loc);
            ps.setLong(2, chatId);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Joylashuv saqlash xatosi: {}", e.getMessage());
        }
    }

    private String[] getUserLocation(long chatId) {
        try (PreparedStatement ps = db.prepareStatement("SELECT location FROM users WHERE id = ?")) {
            ps.setLong(1, chatId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String loc = rs.getString("location");
                if (loc != null && loc.contains(",")) return loc.split(",");
            }
        } catch (SQLException e) {
            LOGGER.error("Joylashuv olishda xato: {}", e.getMessage());
        }
        return null;
    }

    private void sendAdminPanel(long chatId) {
        try (Statement st = db.createStatement()) {
            ResultSet rs1 = st.executeQuery("SELECT COUNT(*) FROM users");
            int users = rs1.next() ? rs1.getInt(1) : 0;
            ResultSet rs2 = st.executeQuery("SELECT SUM(amount) FROM donations");
            double total = rs2.next() ? rs2.getDouble(1) : 0.0;
            sendMessage(chatId, "👥 Foydalanuvchilar: " + users + "\n💰 Donatsiya: " + total);
        } catch (SQLException e) {
            sendMessage(chatId, "Admin panelida xatolik");
        }
    }

    private void sendStartMenu(long chatId) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText("Assalomu alaykum! Quyidagi menyudan foydalaning:");

        ReplyKeyboardMarkup kbd = new ReplyKeyboardMarkup();
        kbd.setResizeKeyboard(true);
        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("🕌 Namoz vaqtlari");
        row1.add("📍 Joylashuvni tanlash");
        rows.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add("💰 Donatsiya");
        rows.add(row2);

        if (chatId == ADMIN_CHAT_ID) {
            KeyboardRow adminRow = new KeyboardRow();
            adminRow.add("⚙️ Admin Panel");
            rows.add(adminRow);
        }

        kbd.setKeyboard(rows);
        msg.setReplyMarkup(kbd);
        executeMessage(msg);
    }

    private void sendCountrySelection(long chatId) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText("Davlatni tanlang:");

        ReplyKeyboardMarkup kbd = new ReplyKeyboardMarkup();
        kbd.setResizeKeyboard(true);
        List<KeyboardRow> rows = new ArrayList<>();
        for (String c : CountriesAndCities.getCountries()) {
            KeyboardRow row = new KeyboardRow();
            row.add(c);
            rows.add(row);
        }
        kbd.setKeyboard(rows);
        msg.setReplyMarkup(kbd);
        executeMessage(msg);
    }

    private void sendCitySelection(long chatId, String country) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText(country + " uchun shaharni tanlang:");

        ReplyKeyboardMarkup kbd = new ReplyKeyboardMarkup();
        kbd.setResizeKeyboard(true);
        List<KeyboardRow> rows = new ArrayList<>();
        for (String city : CountriesAndCities.getCities(country)) {
            KeyboardRow row = new KeyboardRow();
            row.add(city);
            rows.add(row);
        }
        kbd.setKeyboard(rows);
        msg.setReplyMarkup(kbd);
        executeMessage(msg);
    }

    private void sendMessage(long chatId, String text) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText(text);
        executeMessage(msg);
    }

    private void executeMessage(SendMessage msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            LOGGER.error("Xabar yuborilmadi: {}", e.getMessage());
        }
    }

    private String fetchPrayerTimes(String city, String country) {
        int methodId = CountriesAndCities.getMethodForCountry(country);
        String url = String.format("https://api.aladhan.com/v1/timingsByCity?city=%s&country=%s&method=%d",
                URLEncoder.encode(city, StandardCharsets.UTF_8),
                URLEncoder.encode(country, StandardCharsets.UTF_8),
                methodId);
        try (Response response = httpClient.newCall(new Request.Builder().url(url).build()).execute()) {
            if (!response.isSuccessful()) return "❌ Namoz vaqtlarini olishda muammo.";
            String body = response.body().string();
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();
            if (json.has("code") && json.get("code").getAsInt() != 200)
                return "⚠️ Namoz vaqtlarini topib bo‘lmadi.";
            JsonObject t = json.getAsJsonObject("data").getAsJsonObject("timings");
            return String.format("\uD83C\uDFD4️ *Namoz Vaqtlari* \uD83D\uDD70️ (%s, %s)\n\n" +
                            "\uD83C\uDF05 *Bomdod* — %s\n" +
                            "\uD83C\uDF1E *Peshin* — %s\n" +
                            "\uD83C\uDF07 *Asr* — %s\n" +
                            "\uD83C\uDF06 *Shom* — %s\n" +
                            "\uD83C\uDF03 *Xufton* — %s",
                    city, country, t.get("Fajr").getAsString(), t.get("Dhuhr").getAsString(),
                    t.get("Asr").getAsString(), t.get("Maghrib").getAsString(), t.get("Isha").getAsString());
        } catch (Exception e) {
            return "Xatolik: namoz vaqtlarini hozircha olishning imkoni yo‘q.";
        }
    }

    public void sendDailyPrayerTimesToAllUsers() {
        String sql = "SELECT id, location FROM users";
        try (Statement stmt = db.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                long userId = rs.getLong("id");
                String loc = rs.getString("location");
                if (loc != null && loc.contains(",")) {
                    String[] parts = loc.split(",");
                    String msg = fetchPrayerTimes(parts[0], parts[1]);
                    sendMessage(userId, msg);
                } else {
                    sendMessage(userId, "📍 Iltimos, joylashuvni tanlang: 📍 Joylashuvni tanlash");
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Foydalanuvchilarga xabar yuborishda xatolik: {}", e.getMessage());
        }
    }
}
