package com.dev.supermarkets;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SupermarketService service = new SupermarketService();
        Scanner scanner = new Scanner(System.in);

        try {
            // Charger les données
            service.loadProducts();
            service.loadOrders();

            // Afficher le menu
            while (true) {
                System.out.println("\n=== MENU ===");
                System.out.println("1. Afficher tous les produits");
                System.out.println("2. Afficher toutes les commandes");
                System.out.println("3. Afficher les produits de la catégorie Dairy");
                System.out.println("4. Afficher les produits avec stock < 20");
                System.out.println("5. Filtrer les produits par plage de prix");
                System.out.println("6. Afficher les commandes récentes (moins d'1 an)");
                System.out.println("0. Quitter");
                System.out.print("Choisissez une option : ");

                int choix = scanner.nextInt();
                scanner.nextLine(); // Consommer le retour à la ligne

                switch (choix) {
                    case 1:
                        System.out.println("\n=== Tous les produits ===");
                        service.displayProducts();
                        break;
                    case 2:
                        System.out.println("\n=== Toutes les commandes ===");
                        service.displayOrders();
                        break;
                    case 3:
                        System.out.println("\n=== Produits de la catégorie Dairy ===");
                        List<Product> dairyProducts = service.getProductsByCategory("Dairy");
                        for (Product product : dairyProducts) {
                            System.out.println(product.getName());
                        }
                        break;
                    case 4:
                        System.out.println("\n=== Produits avec stock < 20 ===");
                        List<Product> lowStockProducts = service.getProductsWithLowStock();
                        for (Product product : lowStockProducts) {
                            System.out.println(product.getName() + " (Stock: " + product.getStock() + ")");
                        }
                        break;
                    case 5:
                        System.out.print("Entrez le prix minimum : ");
                        double minPrice = scanner.nextDouble();
                        System.out.print("Entrez le prix maximum : ");
                        double maxPrice = scanner.nextDouble();
                        scanner.nextLine(); // Consommer le retour à la ligne

                        System.out.println("\n=== Produits entre " + minPrice + " et " + maxPrice + " ===");
                        List<Product> priceRangeProducts = service.filterProductsByPriceRange(minPrice, maxPrice);
                        for (Product product : priceRangeProducts) {
                            System.out.println(product.getName() + " (Prix: " + product.getPrice() + ")");
                        }
                        break;
                    case 6:
                        System.out.println("\n=== Commandes récentes (moins d'1 an) ===");
                        List<Order> recentOrders = service.filterRecentOrders();
                        for (Order order : recentOrders) {
                            System.out.println("Order ID: " + order.getOrderId() + ", Date: " + order.getOrderDate());
                        }
                        break;
                    case 0:
                        System.out.println("Fermeture du programme...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Option invalide, veuillez réessayer.");
                }
            }

        } catch (SQLException e) {
            LogManager.log("ERREUR: Problème lors de l'exécution - " + e.getMessage());
            e.printStackTrace();
        }
    }
}
