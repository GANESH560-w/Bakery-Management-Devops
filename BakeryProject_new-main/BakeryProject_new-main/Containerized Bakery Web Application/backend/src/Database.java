package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = System.getenv("DB_URL") != null ? System.getenv("DB_URL") + "?useSSL=false&allowPublicKeyRetrieval=true" : "jdbc:mysql://db:3306/bakery_db?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
    private static final String PASS = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "rootpassword";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            initDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initDatabase() {
        String baseUri = System.getenv("DB_URL") != null ? System.getenv("DB_URL").substring(0, System.getenv("DB_URL").lastIndexOf("/")) : "jdbc:mysql://db:3306";
        try (Connection conn = DriverManager.getConnection(baseUri + "/?useSSL=false", USER, PASS);
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS bakery_db");
            stmt.executeUpdate("USE bakery_db");

            // Products Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS products (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "category VARCHAR(100), " +
                    "price DOUBLE NOT NULL, " +
                    "image TEXT)");

            // Orders Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS orders (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "email VARCHAR(255), " +
                    "phone VARCHAR(20), " +
                    "address TEXT, " +
                    "payment VARCHAR(50), " +
                    "total DOUBLE, " +
                    "coupon VARCHAR(50), " +
                    "status VARCHAR(50) DEFAULT 'pending', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS contact_messages (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "email VARCHAR(255), " +
                    "message TEXT, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // Initial Products
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM products");
            if (rs.next() && rs.getInt(1) == 0) {
                String[][] initialItems = {
                    {"Sourdough Loaf", "Breads", "6.50", "https://images.unsplash.com/photo-1585478259715-876acc5be8eb?w=500&q=80"},
                    {"Butter Croissant", "Pastries", "3.25", "https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=500&q=80"},
                    {"Chocolate Babka", "Cakes", "12.00", "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=500&q=80"},
                    {"Artisan Baguette", "Breads", "4.00", "https://images.unsplash.com/photo-1597079910443-60c43fc4f729?w=500&q=80"},
                    {"Berry Tart", "Pastries", "5.50", "https://images.unsplash.com/photo-1519915028121-7d3463d20b13?w=500&q=80"},
                    {"Whole Grain Pav", "Breads", "2.00", "https://images.unsplash.com/photo-1606787366850-de6330128bfc?w=500&q=80"},
                    {"Paneer Puff", "Puffs", "1.50", "https://images.unsplash.com/photo-1623334044303-24266e1095bd?w=500&q=80"},
                    {"Veg Puff", "Puffs", "1.00", "https://images.unsplash.com/photo-1606313564200-e75d5e30476c?w=500&q=80"},
                    {"Nan Khatai", "Cookies", "4.00", "https://images.unsplash.com/photo-1499636136210-6f4ee915583e?w=500&q=80"},
                    {"Eggless Chocolate Cake", "Cakes", "15.00", "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=500&q=80"}
                };
                for (String[] item : initialItems) {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO products (name, category, price, image) VALUES (?, ?, ?, ?)")) {
                        ps.setString(1, item[0]);
                        ps.setString(2, item[1]);
                        ps.setDouble(3, Double.parseDouble(item[2]));
                        ps.setString(4, item[3]);
                        ps.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }

    public static String getProductsJson() {
        StringBuilder json = new StringBuilder("[");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {
            while (rs.next()) {
                if (json.length() > 1) json.append(",");
                json.append(String.format("{\"id\":%d,\"name\":\"%s\",\"category\":\"%s\",\"price\":%.2f,\"image\":\"%s\"}",
                        rs.getInt("id"),
                        escapeJson(rs.getString("name")),
                        escapeJson(rs.getString("category")),
                        rs.getDouble("price"),
                        escapeJson(rs.getString("image"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        json.append("]");
        return json.toString();
    }

    public static boolean saveOrder(String json) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement("INSERT INTO orders (name, email, phone, address, payment, total, coupon) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, extract(json, "name"));
            ps.setString(2, extract(json, "email"));
            ps.setString(3, extract(json, "phone"));
            ps.setString(4, extract(json, "address"));
            ps.setString(5, extract(json, "payment"));
            ps.setDouble(6, Double.parseDouble(extract(json, "total")));
            ps.setString(7, extract(json, "coupon"));
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getOrdersJson() {
        StringBuilder json = new StringBuilder("[");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM orders ORDER BY created_at DESC")) {
            while (rs.next()) {
                if (json.length() > 1) json.append(",");
                json.append(String.format("{\"id\":%d,\"name\":\"%s\",\"email\":\"%s\",\"phone\":\"%s\",\"payment\":\"%s\",\"total\":%.2f,\"status\":\"%s\",\"coupon\":\"%s\",\"address\":\"%s\",\"created_at\":\"%s\"}",
                        rs.getInt("id"),
                        escapeJson(rs.getString("name")),
                        escapeJson(rs.getString("email")),
                        escapeJson(rs.getString("phone")),
                        escapeJson(rs.getString("payment")),
                        rs.getDouble("total"),
                        escapeJson(rs.getString("status")),
                        escapeJson(rs.getString("coupon")),
                        escapeJson(rs.getString("address")),
                        escapeJson(String.valueOf(rs.getTimestamp("created_at")))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        json.append("]");
        return json.toString();
    }

    public static String trackOrdersJson(String lookup) {
        StringBuilder json = new StringBuilder("[");
        String sql = "SELECT * FROM orders WHERE email = ? OR CAST(id AS CHAR) = ? ORDER BY created_at DESC";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lookup);
            ps.setString(2, lookup);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (json.length() > 1) json.append(",");
                    json.append(String.format("{\"id\":%d,\"name\":\"%s\",\"email\":\"%s\",\"phone\":\"%s\",\"payment\":\"%s\",\"total\":%.2f,\"status\":\"%s\",\"coupon\":\"%s\",\"address\":\"%s\",\"created_at\":\"%s\"}",
                            rs.getInt("id"),
                            escapeJson(rs.getString("name")),
                            escapeJson(rs.getString("email")),
                            escapeJson(rs.getString("phone")),
                            escapeJson(rs.getString("payment")),
                            rs.getDouble("total"),
                            escapeJson(rs.getString("status")),
                            escapeJson(rs.getString("coupon")),
                            escapeJson(rs.getString("address")),
                            escapeJson(String.valueOf(rs.getTimestamp("created_at")))));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        json.append("]");
        return json.toString();
    }

    public static void updateOrderStatus(int id, String status) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement("UPDATE orders SET status = ? WHERE id = ?")) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addProduct(String name, String category, double price, String image) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement("INSERT INTO products (name, category, price, image) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.setString(4, image);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateProduct(int id, String name, String category, double price, String image) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement("UPDATE products SET name = ?, category = ?, price = ?, image = ? WHERE id = ?")) {
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.setString(4, image);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteProduct(int id) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM products WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteOrder(int id) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM orders WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean saveContact(String json) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement("INSERT INTO contact_messages (name, email, message) VALUES (?, ?, ?)")) {
            ps.setString(1, extract(json, "name"));
            ps.setString(2, extract(json, "email"));
            ps.setString(3, extract(json, "message"));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String extract(String json, String key) {
        try {
            int start = json.indexOf("\"" + key + "\"") + key.length() + 3;
            int end = json.indexOf("\"", start);
            if (end == -1) { // numeric or boolean
                end = json.indexOf(",", start);
                if (end == -1) end = json.indexOf("}", start);
                return json.substring(start, end).trim();
            }
            return json.substring(start, end);
        } catch (Exception e) {
            return "";
        }
    }

    private static String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", " ")
                .replace("\n", " ");
    }
}
