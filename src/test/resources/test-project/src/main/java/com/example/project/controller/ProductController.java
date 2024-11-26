package com.example.project.controller;

import com.example.project.model.Product;
import com.example.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = { "/products" })
public class ProductController {

    private final ProductService productService;

    public ProductController(@Autowired ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable("id") int id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public String addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable("id") int id, @RequestBody Product product) {
        return "Product with ID " + id + " updated with new name: " + product.name() + " and price: " + product.price();
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        return "Product with ID " + id + " deleted";
    }

    @PatchMapping("/{id}")
    public String updateProductPartially(@PathVariable("id") int id, @RequestBody Product product) {
        return "Product with ID " + id + " updated with new name: " + product.name() + " and price: " + product.price();
    }

    @GetMapping("/valid/{id}")
    public boolean isProductValid(@Valid Product product) {
        return true;
    }
}
