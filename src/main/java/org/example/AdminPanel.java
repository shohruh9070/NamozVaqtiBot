package org.example;

import java.sql.*;

public class AdminPanel {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:users.db")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            System.out.println("Foydalanuvchilar:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getLong("user_id") +
                        ", Username: " + rs.getString("username") +
                        ", Location: " + rs.getString("location") +
                        ", Created: " + rs.getString("created_at"));
            }

            rs = stmt.executeQuery("SELECT * FROM donations");
            System.out.println("\nDonatsiyalar:");
            while (rs.next()) {
                System.out.println("User ID: " + rs.getLong("user_id") +
                        ", Amount: " + rs.getDouble("amount") +
                        ", Created: " + rs.getString("created_at"));
            }
        } catch (SQLException e) {
            System.err.println("Xatolik: Ma'lumotlarni olishda muammo yuz berdi: " + e.getMessage());
        }
    }
}