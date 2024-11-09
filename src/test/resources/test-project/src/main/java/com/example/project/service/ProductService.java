package com.example.project.service;


import com.example.project.data.ProductDataLayer;
import com.example.project.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductDataLayer productDataLayer;

    public ProductService(@Autowired ProductDataLayer productDataLayer) {
        this.productDataLayer = productDataLayer;
    }

    public List<Product> getAllProducts() {
       return productDataLayer.getAllProducts();
    }

    public Product getProductById(int id) {
        return productDataLayer.getProductById(id);
    }

    public String addProduct(Product product) {
        if (getProductById(product.id()) != null) {
            return "WARNING: Product with ID " + product.id() + " already exists";
        }
        return productDataLayer.addProduct(product);
    }
}
