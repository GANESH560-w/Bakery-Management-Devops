package src;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv("PORT") != null ? System.getenv("PORT") : "8080");
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        // Products API
        server.createContext("/api/products", exchange -> {
            addCorsHeaders(exchange);
            if ("GET".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, Database.getProductsJson(), 200);
            }
        });

        // Orders API
        server.createContext("/api/orders/save", new PostHandler(json -> {
            boolean success = Database.saveOrder(json);
            return success ? "{\"status\":\"ok\"}" : "{\"status\":\"error\",\"message\":\"Failed to save order\"}";
        }));

        server.createContext("/api/orders/track", new PostHandler(json -> {
            String lookup = extract(json, "lookup");
            return Database.trackOrdersJson(lookup);
        }));

        server.createContext("/api/contact/save", new PostHandler(json -> {
            boolean success = Database.saveContact(json);
            return success ? "{\"status\":\"ok\"}" : "{\"status\":\"error\",\"message\":\"Failed to save message\"}";
        }));

        // Admin Orders API
        server.createContext("/api/admin/orders", exchange -> {
            addCorsHeaders(exchange);
            if ("GET".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, Database.getOrdersJson(), 200);
            }
        });

        // Admin Order Update API
        server.createContext("/api/admin/orders/update", new PostHandler(json -> {
            int id = Integer.parseInt(extract(json, "id"));
            String status = extract(json, "status");
            Database.updateOrderStatus(id, status);
            return "{\"status\":\"ok\"}";
        }));

        // Admin Login API
        server.createContext("/api/admin/login", new PostHandler(json -> {
            String username = extract(json, "username");
            String password = extract(json, "password");
            String envUser = System.getenv("ADMIN_USERNAME") != null ? System.getenv("ADMIN_USERNAME") : "admin";
            String envPass = System.getenv("ADMIN_PASSWORD") != null ? System.getenv("ADMIN_PASSWORD") : "admin123";
            if (envUser.equals(username) && envPass.equals(password)) {
                return "{\"status\":\"ok\", \"token\":\"admin-token-123\"}";
            }
            return "{\"status\":\"error\", \"message\":\"Invalid credentials\"}";
        }));

        // Employee Login API
        server.createContext("/api/employee/login", new PostHandler(json -> {
            String username = extract(json, "username");
            String password = extract(json, "password");
            // For simplicity, we'll use a hardcoded employee credential
            // In a real application, you would look this up in a database
            if ("employee".equals(username) && "employee123".equals(password)) {
                return "{\"status\":\"ok\", \"token\":\"employee-token-456\"}";
            }
            return "{\"status\":\"error\", \"message\":\"Invalid credentials\"}";
        }));

        // Admin Product Management
        server.createContext("/api/admin/products/add", new PostHandler(json -> {
            String name = extract(json, "name");
            String category = extract(json, "category");
            double price = Double.parseDouble(extract(json, "price"));
            String image = extract(json, "image");
            Database.addProduct(name, category, price, image);
            return "{\"status\":\"ok\"}";
        }));

        server.createContext("/api/admin/products/update", new PostHandler(json -> {
            int id = Integer.parseInt(extract(json, "id"));
            String name = extract(json, "name");
            String category = extract(json, "category");
            double price = Double.parseDouble(extract(json, "price"));
            String image = extract(json, "image");
            Database.updateProduct(id, name, category, price, image);
            return "{\"status\":\"ok\"}";
        }));

        server.createContext("/api/admin/products/delete", new PostHandler(json -> {
            int id = Integer.parseInt(extract(json, "id"));
            Database.deleteProduct(id);
            return "{\"status\":\"ok\"}";
        }));

        server.createContext("/api/admin/orders/delete", new PostHandler(json -> {
            int id = Integer.parseInt(extract(json, "id"));
            Database.deleteOrder(id);
            return "{\"status\":\"ok\"}";
        }));

        server.setExecutor(null);
        System.out.println("Modern Bakery Server started on port " + port + "...");
        server.start();
    }

    private static void sendResponse(HttpExchange exchange, String response, int code) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private static void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

    static class PostHandler implements HttpHandler {
        private final JsonProcessor processor;

        public PostHandler(JsonProcessor processor) {
            this.processor = processor;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                String response = processor.process(body);
                sendResponse(exchange, response, 200);
            }
        }
    }

    interface JsonProcessor {
        String process(String json);
    }

    private static String extract(String json, String key) {
        try {
            int start = json.indexOf("\"" + key + "\"") + key.length() + 3;
            int end = json.indexOf("\"", start);
            if (end == -1) {
                end = json.indexOf(",", start);
                if (end == -1) end = json.indexOf("}", start);
                return json.substring(start, end).trim();
            }
            return json.substring(start, end);
        } catch (Exception e) {
            return "";
        }
    }
}
