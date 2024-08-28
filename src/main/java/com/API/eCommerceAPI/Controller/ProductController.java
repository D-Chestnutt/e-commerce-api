package com.API.eCommerceAPI.Controller;

import com.API.eCommerceAPI.Model.Product;
import com.API.eCommerceAPI.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping("/products")
    ResponseEntity<?> createProduct(@RequestBody Product product){
        Product savedProduct = productService.saveNewProduct(product);
        return ResponseEntity.ok().body(savedProduct);
    }

    @GetMapping("/products")
    ResponseEntity<?> getProducts(){
        List<Product> products = productService.findAll();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/products/{id}")
    ResponseEntity<?> getProductsById(@PathVariable int id){
        Product product = productService.findById(id);
        return ResponseEntity.ok().body(product);
    }

    @DeleteMapping("/products/{id}")
    ResponseEntity<?> deleteProduct(@PathVariable int id){
        productService.deleteById(id);
        return ResponseEntity.ok().body("Successfully deleted product with id " + id);
    }
}
