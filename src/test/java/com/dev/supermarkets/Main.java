package com.dev.supermarkets;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SupermarketService service = new SupermarketService();

        try {
            // Charger les données
            service.loadProducts();
            service.loadOrders();

            // Afficher tous les produits
            System.out.println("=== Tous les produits ===");
            service.displayProducts();

            // Afficher toutes les commandes
            System.out.println("\n=== Toutes les commandes ===");
            service.displayOrders();

            // Afficher les produits de la catégorie "Dairy"
            System.out.println("\n=== Produits de la catégorie Dairy ===");
            List<Product> dairyProducts = service.getProductsByCategory("Dairy");
            for (Product product : dairyProducts) {
                System.out.println(product.getName());
            }

            // Afficher les produits avec stock < 20
            System.out.println("\n=== Produits avec stock < 20 ===");
            List<Product> lowStockProducts = service.getProductsWithLowStock();
            for (Product product : lowStockProducts) {
                System.out.println(product.getName() + " (Stock: " + product.getStock() + ")");
            }

            // Filtrer les produits par plage de prix
            System.out.println("\n=== Produits entre 1.00 et 2.00 ===");
            List<Product> priceRangeProducts = service.filterProductsByPriceRange(1.00, 2.00);
            for (Product product : priceRangeProducts) {
                System.out.println(product.getName() + " (Price: " + product.getPrice() + ")");
            }

            // Filtrer les commandes récentes
            System.out.println("\n=== Commandes récentes (moins d'1 an) ===");
            List<Order> recentOrders = service.filterRecentOrders();
            for (Order order : recentOrders) {
                System.out.println("Order ID: " + order.getOrderId() + ", Date: " + order.getOrderDate());
            }

        } catch (SQLException e) {
            LogManager.log("ERREUR: Problème lors de l'exécution - " + e.getMessage());
            e.printStackTrace();
        }
    }
}