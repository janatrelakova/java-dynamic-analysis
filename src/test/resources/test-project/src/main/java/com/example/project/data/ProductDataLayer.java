package com.example.project.data;

import com.example.project.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDataLayer {

    private final List<Product> products = List.of(new Product(1, "product-1", 100),
            new Product(2, "product-2", 200),
            new Product(3, "product-3", 300));


    public List<Product> getAllProducts() {
        return products;
    }

    public Product getProductById(int id) {
        return products.stream()
                .filter(product -> product.id() == id)
                .findFirst()
                .orElse(null);
    }

    public Product getProductByName(String name) {
        return products.stream()
                .filter(product -> product.name().equals(name))
                .findFirst()
                .orElse(null);
    }

    public String addProduct(Product product) {
        products.add(product);
        return product.name();
    }
}
