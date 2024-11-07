package cz.muni.fi.xtrelak.controller;

import cz.muni.fi.xtrelak.model.Product;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = { "/products" })
public class ProductController {

    @GetMapping
    public String getAllProducts() {
        return "Retrieving all products";
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable int id) {
        return "Product details for product ID: " + id;
    }

    @PostMapping
    public String addProduct(@RequestBody Product product) {
        return "Product added: " + product.getName() + ", Price: " + product.getPrice();
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable int id, @RequestBody Product product) {
        return "Product with ID " + id + " updated with new name: " + product.getName() + " and price: " + product.getPrice();
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable int id) {
        return "Product with ID " + id + " deleted";
    }

    @PatchMapping("/{id}")
    public String updateProductPartially(@PathVariable int id, @RequestBody Product product) {
        return "Product with ID " + id + " updated with new name: " + product.getName() + " and price: " + product.getPrice();
    }
}
