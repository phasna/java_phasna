package com.dev.supermarkets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SupermarketService {
    private static final String PRODUCTS_FILE = "src/test/java/com/dev/supermarkets/products.txt";
    private static final String ORDERS_FILE = "src/test/java/com/dev/supermarkets/orders.txt";

    // Charger les produits depuis products.txt et les insérer dans la BD
    public void loadProducts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                Product product = new Product(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        Double.parseDouble(parts[3]),
                        Integer.parseInt(parts[4])
                );
                insertProduct(product);
            }
        } catch (Exception e) {
            LogManager.log("ERREUR: Échec du chargement des produits - " + e.getMessage());
        }
    }

    // Charger les commandes depuis orders.txt et les insérer dans la BD
    public void loadOrders() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                Order order = new Order(
                        Integer.parseInt(parts[0]),
                        LocalDate.parse(parts[1], DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])
                );
                insertOrder(order);
            }
        } catch (Exception e) {
            LogManager.log("ERREUR: Échec du chargement des commandes - " + e.getMessage());
        }
    }

    // Insérer un produit dans la BD
    private void insertProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (product_id, name, category, price, stock) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, product.getProductId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getCategory());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getStock());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LogManager.log("ERREUR: Échec de l'insertion du produit (name: " + product.getName() + ")");
            throw e;
        }
    }

    // Insérer une commande dans la BD
    private void insertOrder(Order order) throws SQLException {
        String sql = "INSERT INTO orders (order_id, order_date, product_id, quantity) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, order.getOrderId());
            stmt.setDate(2, java.sql.Date.valueOf(order.getOrderDate()));
            stmt.setInt(3, order.getProductId());
            stmt.setInt(4, order.getQuantity());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LogManager.log("ERREUR: Échec de l'insertion de la commande (order_id: " + order.getOrderId() + ")");
            throw e;
        }
    }

    // Récupérer tous les produits
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                ));
            }
        }
        return products;
    }

    // Récupérer toutes les commandes
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("order_id"),
                        rs.getDate("order_date").toLocalDate(),
                        rs.getInt("product_id"),
                        rs.getInt("quantity")
                ));
            }
        }
        return orders;
    }

    // Afficher les produits
    public void displayProducts() throws SQLException {
        List<Product> products = getAllProducts();
        for (Product product : products) {
            System.out.println("ID: " + product.getProductId() + ", Name: " + product.getName() +
                    ", Category: " + product.getCategory() + ", Price: " + product.getPrice() +
                    ", Stock: " + product.getStock());
        }
    }

    // Afficher les commandes
    public void displayOrders() throws SQLException {
        List<Order> orders = getAllOrders();
        for (Order order : orders) {
            System.out.println("Order ID: " + order.getOrderId() + ", Date: " + order.getOrderDate() +
                    ", Product ID: " + order.getProductId() + ", Quantity: " + order.getQuantity());
        }
    }

    // Afficher les produits d'une catégorie
    public List<Product> getProductsByCategory(String category) throws SQLException {
        List<Product> products = getAllProducts();
        List<Product> filtered = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                filtered.add(product);
            }
        }
        return filtered;
    }

    // Afficher les produits avec stock < 20
    public List<Product> getProductsWithLowStock() throws SQLException {
        List<Product> products = getAllProducts();
        List<Product> lowStock = new ArrayList<>();
        for (Product product : products) {
            if (product.getStock() < 20) {
                lowStock.add(product);
            }
        }
        return lowStock;
    }

    // Filtrer les produits par plage de prix
    public List<Product> filterProductsByPriceRange(double minPrice, double maxPrice) throws SQLException {
        List<Product> products = getAllProducts();
        List<Product> filtered = new ArrayList<>();
        for (Product product : products) {
            if (product.getPrice() >= minPrice && product.getPrice() <= maxPrice) {
                filtered.add(product);
            }
        }
        return filtered;
    }

    // Supprimer une commande par ID
public void deleteOrderById(int orderId) throws SQLException {
    String sql = "DELETE FROM orders WHERE order_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, orderId);
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Commande supprimée avec succès (ID: " + orderId + ")");
        } else {
            System.out.println("Aucune commande trouvée avec l'ID: " + orderId);
        }
    } catch (SQLException e) {
        LogManager.log("ERREUR: Impossible de supprimer la commande (ID: " + orderId + ")");
        throw e;
    }
}

    // Filtrer les commandes récentes (moins de 1 an)
    public List<Order> filterRecentOrders() throws SQLException {
        List<Order> orders = getAllOrders();
        List<Order> recentOrders = new ArrayList<>();
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        for (Order order : orders) {
            if (order.getOrderDate().isAfter(oneYearAgo) || order.getOrderDate().isEqual(oneYearAgo)) {
                recentOrders.add(order);
            }
        }
        return recentOrders;
    }
}