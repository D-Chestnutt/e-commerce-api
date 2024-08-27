package com.API.eCommerceAPI.Controller;

import com.API.eCommerceAPI.Model.Product;
import com.API.eCommerceAPI.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping("/products")
    ResponseEntity<?> createProduct(@RequestBody Product product){
        try{
            Product savedProduct = productService.save(product);
            return ResponseEntity.ok().body(savedProduct);
        } catch (RuntimeException runtimeException) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database service is currently unavailable");
        }
    }

    @GetMapping("/products")
    ResponseEntity<?> getProducts(){
        try{
            List<Product> products = productService.findAll();
            return ResponseEntity.ok().body(products);
        }catch (RuntimeException runtimeException){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database service is currently unavailable");
        }
    }

    @GetMapping("/products/{id}")
    ResponseEntity<?> getProductsById(@PathVariable int id){
        try{
            Product product = productService.findById(id);
            return ResponseEntity.ok().body(product);
        }catch (RuntimeException runtimeException){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database service is currently unavailable");
        }
    }

    @DeleteMapping("/products/{id}")
    ResponseEntity<?> deleteProduct(@PathVariable int id){
        try{
            productService.deleteById(id);
            return ResponseEntity.ok().body("Successfully deleted product with id " + id);
        } catch (RuntimeException runtimeException) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database service is currently unavailable");
        }
    }
}
