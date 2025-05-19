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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class NamozVaqtiBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(NamozVaqtiBot.class);
    private static final String BOT_TOKEN = System.getenv("BOT_TOKEN") != null ? System.getenv("BOT_TOKEN") : "7686090055:AAGB3NkHRGdLL3iLVk244v3wU9tX5IjJUPg";
    private static final String BOT_USERNAME = "@MuslimTimeUzBot";
    private static final long ADMIN_CHAT_ID = System.getenv("ADMIN_CHAT_ID") != null ? Long.parseLong(System.getenv("ADMIN_CHAT_ID")) : 5550676341L;
    private static final int ITEMS_PER_PAGE = 5;

    private final OkHttpClient httpClient = new OkHttpClient();
    private Connection dbConnection;
    private final Set<String> processedCallbacks = new HashSet<>(); // Takroriy callback'larni oldini olish uchun

    public NamozVaqtiBot() {
        initDatabase();
    }

    // --- DATABASE INIT ---
    private void initDatabase() {
        try {
            dbConnection = DriverManager.getConnection("jdbc:sqlite:users.db");
            try (Statement stmt = dbConnection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS users (user_id INTEGER PRIMARY KEY, username TEXT, location TEXT, created_at TEXT)");
                stmt.execute("CREATE TABLE IF NOT EXISTS donations (user_id INTEGER, amount REAL, created_at TEXT)");
                LOGGER.info("Ma'lumotlar bazasi muvaffaqiyatli ishga tushirildi.");
            }
        } catch (SQLException e) {
            LOGGER.error("Ma'lumotlar bazasini ishga tushirishda xatolik: {}", e.getMessage(), e);
        }
    }

    // --- BOT CREDENTIALS ---
    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    // --- MAIN UPDATE HANDLER ---
    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                handleTextMessage(update);
            } else if (update.hasCallbackQuery()) {
                String callbackData = update.getCallbackQuery().getData();
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                String callbackId = chatId + "_" + callbackData + "_" + update.getCallbackQuery().getId();
                synchronized (processedCallbacks) {
                    if (!processedCallbacks.contains(callbackId)) {
                        processedCallbacks.add(callbackId);
                        handleCallback(callbackData, chatId);
                    } else {
                        LOGGER.info("Takroriy callback o'tkazib yuborildi: {}", callbackId);
                        return; // Takroriy callback'larni butunlay e'tiborsiz qoldirish
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Yangilanishni qayta ishlashda xatolik (chatId: {}): {}",
                    update.hasMessage() ? update.getMessage().getChatId() : "noma'lum", e.getMessage(), e);
        }
    }

    // --- HANDLERS ---
    private void handleTextMessage(Update update) {
        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        String userName = Optional.ofNullable(update.getMessage().getChat().getUserName()).orElse("Foydalanuvchi");

        LOGGER.info("Yangi xabar qabul qilindi: {} (chatId: {}): {}", userName, chatId, messageText);

        switch (messageText) {
            case "/start" -> {
                saveUser(chatId, userName);
                sendStartMenu(chatId, userName);
            }
            default -> {
                if (messageText.startsWith("/message")) {
                    handleUserMessage(chatId, userName, messageText);
                } else if (messageText.equals("/admin") && isAdmin(chatId)) {
                    sendAdminPanel(chatId);
                } else {
                    sendMessage(chatId, "Noto‘g‘ri buyruq. Assalomu alaykum, /start tugmasini bosing va tugmalar orqali davom eting.");
                }
            }
        }
    }

    private void handleUserMessage(long chatId, String userName, String messageText) {
        String[] parts = messageText.split(" ", 2);
        if (parts.length < 2) {
            sendMessage(chatId, "Iltimos, xabarni kiriting: /message <xabar>\nMasalan: /message Salom!");
            return;
        }
        String userMessage = parts[1].trim();
        sendMessage(chatId, "Sizning xabaringiz qabul qilindi: " + userMessage);
        if (ADMIN_CHAT_ID != chatId) {
            sendMessage(ADMIN_CHAT_ID, "Yangi xabar:\nFoydalanuvchi: " + userName + "\nXabar: " + userMessage);
        }
    }

    private boolean isAdmin(long chatId) {
        return chatId == ADMIN_CHAT_ID;
    }

    // --- MENU BUTTONS ---
    private void sendStartMenu(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        // Foydalanuvchi ismini Markdown uchun xavfsiz formatga o'tkazamiz
        String safeUserName = escapeMarkdown(userName);

        // Xabar matni
        String text = "*Assalomu alaykum, " + safeUserName + "!* 👋\n" +
                "Namoz Vaqti botiga xush kelibsiz! Quyidagi tugmalardan foydalaning:";
        message.setText(text);
        message.setParseMode("Markdown");

        // Inline tugmalar
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(Arrays.asList(
                createButton("📋 МЕНЮ", "main_menu"),
                createButton("💬 ХАБАР", "send_message")
        ));

        keyboard.add(List.of(createButton("🕌 Namoz vaqtlari", "prayer_times")));
        keyboard.add(List.of(createButton("📍 Joylashuvni tanlash", "select_country_0")));

        keyboard.add(Arrays.asList(
                createButton("💰 Donatsiya", "donation"),
                createButton("ℹ️ Bot haqida", "about")
        ));

        if (isAdmin(chatId)) {
            keyboard.add(List.of(createButton("⚙️ Admin Panel", "admin")));
        }

        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);

        // Xabarni yuboramiz
        executeMessage(message);
    }

    // Markdown belgilarini qochirish funksiyasi
    private String escapeMarkdown(String text) {
        if (text == null) return "";
        return text.replace("_", "\\_")
                .replace("*", "\\*")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("~", "\\~")
                .replace("`", "\\`")
                .replace(">", "\\>")
                .replace("#", "\\#")
                .replace("+", "\\+")
                .replace("-", "\\-")
                .replace("=", "\\=")
                .replace("|", "\\|")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace(".", "\\.")
                .replace("!", "\\!");
    }


    private InlineKeyboardButton createButton(String text, String callbackData) {
        return InlineKeyboardButton.builder().text(text).callbackData(callbackData).build();
    }

    // --- USER SAVE AND LOCATION UPDATE ---
    private void saveUser(long userId, String username) {
        String sql = "INSERT OR IGNORE INTO users (user_id, username, created_at) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.setString(2, username);
            pstmt.setString(3, LocalDateTime.now().toString());
            pstmt.executeUpdate();
            LOGGER.info("Foydalanuvchi saqlandi: {} (userId: {})", username, userId);
        } catch (SQLException e) {
            LOGGER.error("Foydalanuvchini saqlashda xatolik (userId: {}): {}", userId, e.getMessage(), e);
        }
    }

    private void updateLocation(long chatId, String city, String country) {
        String location = city + "," + country;

        String sql = "UPDATE users SET location = ?, created_at = ? WHERE user_id = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, location);
            pstmt.setString(2, LocalDateTime.now().toString());
            pstmt.setLong(3, chatId);
            pstmt.executeUpdate();
            sendMessage(chatId, "Joylashuvingiz yangilandi: " + city + ", " + country);
            LOGGER.info("Joylashuv yangilandi (chatId: {}): {}", chatId, location);
            sendPrayerTimes(chatId);
        } catch (SQLException e) {
            LOGGER.error("Joylashuvni yangilashda xatolik (chatId: {}): {}", chatId, e.getMessage(), e);
            sendMessage(chatId, "Joylashuvni yangilashda xatolik yuz berdi.");
        }
    }

    // --- CALLBACK QUERY HANDLER ---
    private void handleCallback(String data, long chatId) {
        try {
            if (data.startsWith("select_country_")) {
                int page = Integer.parseInt(data.split("_")[2]);
                sendCountries(chatId, page);
            } else if (data.startsWith("select_city_")) {
                String[] parts = data.split("_");
                if (parts.length < 4) {
                    sendMessage(chatId, "Xatolik: Noto'g'ri shahar ma'lumotlari. Qaytadan tanlang.");
                    return;
                }
                String country = parts[2].replace("-", " ");
                int page = Integer.parseInt(parts[3]);
                sendCities(chatId, country, page);
            } else if (data.startsWith("set_location_")) {
                String[] parts = data.split("_", 4);
                if (parts.length < 4) {
                    LOGGER.error("Invalid callback data format: {}", data);
                    sendMessage(chatId, "Xatolik: Noto'g'ri joylashuv ma'lumotlari. Iltimos, qaytadan tanlang.");
                    return;
                }
                String country = parts[2].replace("-", " ");
                String city = parts[3].replace("-", " ");
                // Clean city name by removing country prefix if present
                if (city.startsWith(country.replace(" ", "_") + "_")) {
                    city = city.substring(country.replace(" ", "_").length() + 1);
                }
                if (country.isEmpty() || city.isEmpty()) {
                    LOGGER.error("Empty country or city in callback: country={}, city={}", country, city);
                    sendMessage(chatId, "Xatolik: Davlat yoki shahar noto'g'ri. Iltimos, qaytadan tanlang.");
                    return;
                }
                LOGGER.debug("Callback: country={}, city={}", country, city);
                updateLocation(chatId, city, country);
            } else if (data.equals("prayer_times")) {
                sendPrayerTimes(chatId);
            } else if (data.equals("donation")) {
                sendDonationInfo(chatId);
            } else if (data.equals("about")) {
                sendAboutInfo(chatId);
            } else if (data.equals("admin") && isAdmin(chatId)) {
                sendAdminPanel(chatId);
            } else if (data.equals("main_menu")) {
                String userName = Optional.ofNullable(getUserNameFromChatId(chatId)).orElse("Foydalanuvchi");
                sendStartMenu(chatId, userName);
            } else if (data.equals("send_message")) {
                sendMessage(chatId, "Iltimos, xabaringizni yozing. Masalan: /VaqtiBot yozing. Masalan: /message Salom!");
            }
        } catch (Exception e) {
            LOGGER.error("Callback qayta ishlashda xatolik (chatId: {}, data: {}): {}", chatId, data, e.getMessage(), e);
            sendMessage(chatId, "Xatolik yuz berdi. Iltimos, '📍 Joylashuvni tanlash' tugmasini bosing yoki keyinroq urinib ko‘ring.");
        }
    }

    private String getUserNameFromChatId(long chatId) {
        String sql = "SELECT username FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setLong(1, chatId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Foydalanuvchi nomini olishda xatolik (chatId: {}): {}", chatId, e.getMessage(), e);
        }
        return null;
    }

    // --- PAGINATED COUNTRY LIST ---
    private void sendCountries(long chatId, int page) {
        List<String> countries = CountriesAndCities.getCountries();
        int totalPages = (int) Math.ceil((double) countries.size() / ITEMS_PER_PAGE);

        int start = page * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, countries.size());

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (int i = start; i < end; i++) {
            String country = countries.get(i);
            keyboard.add(List.of(createButton(country, "select_city_" + country.replace(" ", "-") + "_0")));
        }

        List<InlineKeyboardButton> navButtons = new ArrayList<>();
        if (page > 0) navButtons.add(createButton("⬅️ Orqaga", "select_country_" + (page - 1)));
        if (page < totalPages - 1) navButtons.add(createButton("Keyingi ➡️", "select_country_" + (page + 1)));
        if (!navButtons.isEmpty()) keyboard.add(navButtons);

        sendInlineKeyboard(chatId, "Davlatni tanlang:", keyboard);
    }

    // --- PAGINATED CITY LIST ---
    private void sendCities(long chatId, String country, int page) {
        List<String> cities = CountriesAndCities.getCities(country);
        int totalPages = (int) Math.ceil((double) cities.size() / ITEMS_PER_PAGE);

        int start = page * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, cities.size());

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (int i = start; i < end; i++) {
            String city = cities.get(i);
            // Ensure city name is clean (remove country prefix if present)
            String cleanCity = city.startsWith(country.replace(" ", "_") + "_")
                    ? city.substring(country.replace(" ", "_").length() + 1)
                    : city;
            keyboard.add(List.of(createButton(cleanCity, "set_location_" + country.replace(" ", "-") + "_" + cleanCity.replace(" ", "-"))));
        }

        List<InlineKeyboardButton> navButtons = new ArrayList<>();
        if (page > 0) navButtons.add(createButton("⬅️ Orqaga", "select_city_" + country.replace(" ", "-") + "_" + (page - 1)));
        if (page < totalPages - 1) navButtons.add(createButton("Keyingi ➡️", "select_city_" + country.replace(" ", "-") + "_" + (page + 1)));
        navButtons.add(createButton("🔙 Davlatlarga qaytish", "select_country_0"));
        keyboard.add(navButtons);

        sendInlineKeyboard(chatId, country + " shaharlarini tanlang:", keyboard);
    }

    // --- PRAYER TIMES ---
    private void sendPrayerTimes(long chatId) {
        String location = getUserLocation(chatId);
        if (location == null || !location.contains(",")) {
            sendMessage(chatId, "Iltimos, avval joylashuvingizni tanlang. '📍 Joylashuvni tanlash' tugmasini bosing.");
            return;
        }

        String[] parts = location.split(",");
        if (parts.length != 2) {
            sendMessage(chatId, "Joylashuv noto'g'ri formatda. '📍 Joylashuvni tanlash' tugmasini bosing va qayta tanlang.");
            return;
        }
        String city = parts[0].trim();
        String country = parts[1].trim();

        if (city.isEmpty() || country.isEmpty()) {
            sendMessage(chatId, "Joylashuv ma'lumotlari noto'g'ri. '📍 Joylashuvni tanlash' tugmasini bosing va qayta tanlang.");
            return;
        }

        String prayerTimes = fetchPrayerTimes(city, country);
        sendMessage(chatId, prayerTimes);
    }

    private String getUserLocation(long chatId) {
        String sql = "SELECT location FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setLong(1, chatId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("location");
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Foydalanuvchi joylashuvini olishda xatolik (chatId: {}): {}", chatId, e.getMessage(), e);
        }
        return null;
    }

    private String fetchPrayerTimes(String city, String country) {
        try {
            String url = String.format("https://api.aladhan.com/v1/timingsByCity?city=%s&country=%s&method=5",
                    URLEncoder.encode(city, StandardCharsets.UTF_8.toString()),
                    URLEncoder.encode(country, StandardCharsets.UTF_8.toString()));
            LOGGER.info("API so'rov URL: {}", url);
            Request request = new Request.Builder().url(url).build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    LOGGER.warn("API so'rov muvaffaqiyatsiz (shahar: {}, davlat: {}, kod: {}): {}",
                            city, country, response.code(), response.message());
                    return "Namoz vaqtlarini olishda xatolik yuz berdi. Iltimos, to‘g‘ri shahar va davlat tanlang yoki keyinroq urinib ko‘ring.";
                }
                String responseBody = response.body().string();
                LOGGER.debug("API javobi: {}", responseBody);
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

                if (jsonResponse.has("code") && jsonResponse.get("code").getAsInt() != 200) {
                    String status = jsonResponse.has("status") ? jsonResponse.get("status").getAsString() : "Noma'lum xato";
                    LOGGER.warn("API xatosi: {} (shahar: {}, davlat: {})", status, city, country);
                    return "Namoz vaqtlarini topib bo‘lmadi. Iltimos, to‘g‘ri shahar va davlat tanlang.";
                }

                JsonObject data = jsonResponse.getAsJsonObject("data");
                if (!data.has("timings") || !data.get("timings").isJsonObject()) {
                    LOGGER.warn("Noto‘g‘ri timings formati (shahar: {}, davlat: {})", city, country);
                    return "Namoz vaqtlarini olishda xatolik yuz berdi. Iltimos, keyinroq urinib ko‘ring.";
                }

                JsonObject timings = data.getAsJsonObject("timings");
                return "🕌 *Namoz Vaqtlari* 🕰️ (" + city + ", " + country + ")\n\n" +
                        "🌅 *Bomdod* — " + timings.get("Fajr").getAsString() + "\n" +
                        "🌞 *Peshin* — " + timings.get("Dhuhr").getAsString() + "\n" +
                        "🌇 *Asr* — " + timings.get("Asr").getAsString() + "\n" +
                        "🌆 *Shom* — " + timings.get("Maghrib").getAsString() + "\n" +
                        "🌃 *Xufton* — " + timings.get("Isha").getAsString();
            }
        } catch (IOException e) {
            LOGGER.error("Tarmoq xatosi (shahar: {}, davlat: {}): {}", city, country, e.getMessage(), e);
            return "Internet aloqasida xatolik. Iltimos, keyinroq urinib ko‘ring.";
        } catch (Exception e) {
            LOGGER.error("Kutilmagan xatolik (shahar: {}, davlat: {}): {}", city, country, e.getMessage(), e);
            return "Namoz vaqtlarini olishda xatolik yuz berdi.";
        }
    }

    // --- DONATION INFO ---
    private void sendDonationInfo(long chatId) {
        sendMessage(chatId, "Bizni botimiz doimiy ishlash uchun donatsiya qilishingiz mumkin: 9860020102807821 (ism: Shohruh Mirzamuxamedov)");
    }

    private void sendAboutInfo(long chatId) {
        sendMessage(chatId, "Namoz vaqti boti - har bir musulmon uchun aniq vaqtda namoz qilish imkonini beradi. Botni Telegram orqali ishlating.");
    }

    // --- ADMIN PANEL ---
    private void sendAdminPanel(long chatId) {
        try {
            String userCountSql = "SELECT COUNT(*) FROM users";
            String donationSumSql = "SELECT SUM(amount) FROM donations";
            int userCount = 0;
            double totalDonations = 0.0;

            try (Statement stmt = dbConnection.createStatement()) {
                ResultSet rs = stmt.executeQuery(userCountSql);
                if (rs.next()) userCount = rs.getInt(1);
                rs = stmt.executeQuery(donationSumSql);
                if (rs.next()) totalDonations = rs.getDouble(1);
            }

            String message = String.format(
                    "Admin Panel:\n👥 Foydalanuvchilar: %d\n💰 Jami donatsiyalar: %.2f",
                    userCount, totalDonations
            );
            sendMessage(chatId, message);
        } catch (SQLException e) {
            LOGGER.error("Admin panelida xatolik (chatId: {}): {}", chatId, e.getMessage(), e);
            sendMessage(chatId, "Admin panelida xatolik yuz berdi.");
        }
    }

    // --- SEND MESSAGE ---
    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        // Matnni tozalash: Markdown sifatida talqin qilinishi mumkin bo'lgan belgilarni olib tashlash
        text = text.replace("_", "\\_").replace("*", "\\*").replace("[", "\\[").replace("`", "\\`");
        message.setText(text);
        message.setParseMode("Markdown");
        executeMessage(message);
    }

    // --- SEND INLINE KEYBOARD ---
    private void sendInlineKeyboard(long chatId, String text, List<List<InlineKeyboardButton>> keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        // Matnni tozalash
        text = text.replace("_", "\\_").replace("*", "\\*").replace("[", "\\[").replace("`", "\\`");
        message.setText(text);
        message.setParseMode("Markdown");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);
        executeMessage(message);
    }

    // --- EXECUTE MESSAGE ---
    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            LOGGER.error("Xabarni yuborishda xatolik (chatId: {}): {}", message.getChatId(), e.getMessage(), e);
        }
    }
}