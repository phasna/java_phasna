package com.dev.supermarkets;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SupermarketServiceTest {
    private SupermarketService service;

    @Before
    public void setUp() throws SQLException {
        service = new SupermarketService();
        // Charger les données pour les tests
        service.loadProducts();
        service.loadOrders();
    }

    @Test
    public void testFilterProductsByPriceRange() throws SQLException {
        List<Product> products = service.filterProductsByPriceRange(1.00, 2.00);
        assertEquals(7, products.size()); // On attend 7 produits entre 1.00 et 2.00
        for (Product product : products) {
            assertTrue(product.getPrice() >= 1.00 && product.getPrice() <= 2.00);
        }
    }

    @Test
    public void testFilterRecentOrders() throws SQLException {
        List<Order> recentOrders = service.filterRecentOrders();
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        for (Order order : recentOrders) {
            assertTrue(order.getOrderDate().isAfter(oneYearAgo) || order.getOrderDate().isEqual(oneYearAgo));
        }
        // Vérifier le nombre de commandes récentes (au 27/03/2025, commandes après 27/03/2024)
        assertEquals(5, recentOrders.size()); // Commandes 6 à 10
    }
}