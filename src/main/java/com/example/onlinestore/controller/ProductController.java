package com.example.onlinestore.controller;

import com.example.onlinestore.entity.Product;
import com.example.onlinestore.payload.AddStockPayload;
import com.example.onlinestore.payload.ProductPayload;
import com.example.onlinestore.service.FirebaseProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/admin/products")
public class ProductController {

    @Autowired
    private FirebaseProductService firebaseProductService;

    //Get all products
    @GetMapping("getAll")
    public ResponseEntity<Object> getAllProducts() {
        try {
            return ResponseEntity.ok(firebaseProductService.getAllProducts());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching products");
        }
    }

    // Get all products that have price other than 0
    @GetMapping("getAllPriced")
    public ResponseEntity<Object> getPricedProducts() {
        try {
            return ResponseEntity.ok(firebaseProductService.getPricedProducts());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching products");
        }
    }

    // Change product price to given value
    @PutMapping("changePrice/{id}/{price}")
    public ResponseEntity<String> changeProductPrice(@PathVariable String id, @PathVariable double price) {
        try {
            String response = firebaseProductService.changeProductPrice(id, price);
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating product price");
        }
    }

    // Change product stock
    @PutMapping("changeStock/{id}/{quantityInStock}")
    public ResponseEntity<String> changeProductStock(@PathVariable String id, @PathVariable int quantityInStock) {
        try {
            String response = firebaseProductService.changeProductStock(id, quantityInStock);
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating product stock");
        }
    }

    @GetMapping("getMostWishlisted")
    public ResponseEntity<Object> getMostWishlistedProducts() {
        try {
            return ResponseEntity.ok(firebaseProductService.getMostWishlistedProducts());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching products");
        }
    }

    @GetMapping("getNewArrivals")
    public ResponseEntity<Object> getNewArrivals() {
        try {
            return ResponseEntity.ok(firebaseProductService.getNewArrivals());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching products");
        }
    }

    //Get product by ID
    @GetMapping("getById/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable String id) {
        try {
            Product product = firebaseProductService.getProductById(id);
            if (product != null) {
                return ResponseEntity.ok(product);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching product");
        }
    }

    @PostMapping
    public ResponseEntity<String> addProduct(@ModelAttribute ProductPayload request) {
        try {
            String response = firebaseProductService.saveProduct(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }  catch (Exception e) {
            e.printStackTrace();  // Print detailed stack trace in logs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding product: " + e.getMessage());
        }
    }
    // Add multiple
    @PostMapping("/addMultiple")
    public ResponseEntity<String> addMultipleProducts(@ModelAttribute ProductPayload[] requests) {
        try {
            for (ProductPayload request : requests) {
                firebaseProductService.saveProduct(request);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Products added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding products: " + e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        try {
            String response = firebaseProductService.deleteProductById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting product");
        }
    }

    @PostMapping("/addStock")
    public ResponseEntity<String> addStock(@RequestBody AddStockPayload payload) {
        try {
            String response = firebaseProductService.increaseQuantityInStock(payload);
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding stock");
        }
    }


    @PutMapping("applyDiscount/{id}")
    public ResponseEntity<String> applyDiscount(@PathVariable String id, @RequestParam double discount) {
        try {
            if (discount < 0 || discount > 100) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Discount must be between 0 and 100.");
            }
    
            String response = firebaseProductService.applyDiscount(id, discount);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error applying discount");
        }
    }
}
