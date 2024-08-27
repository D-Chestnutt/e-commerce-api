package com.API.eCommerceAPI.Controller;

import com.API.eCommerceAPI.Model.Cart;
import com.API.eCommerceAPI.Model.CartCheckout;
import com.API.eCommerceAPI.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping("/carts")
    ResponseEntity<?> createCart(@RequestBody Cart cart){
        try{
            Cart savedCart = cartService.save(cart);
            return ResponseEntity.ok().body(savedCart);
        } catch (RuntimeException runtimeException) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(runtimeException.getMessage());
        }
    }

    @GetMapping("/carts")
    ResponseEntity<?> getCarts(){
        try{
            List<Cart> carts = cartService.findAll();
            return ResponseEntity.ok().body(carts);
        }catch (RuntimeException runtimeException){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database service is currently unavailable");
        }
    }

    @PutMapping("/carts/{id}")
    ResponseEntity<?> updateCart(@RequestBody Cart cart){
        try{
            Cart savedCart = cartService.save(cart);
            return ResponseEntity.ok().body(savedCart);
        } catch (RuntimeException runtimeException) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(runtimeException.getMessage());
        }
    }

    @PostMapping("/carts/{id}/checkout")
    ResponseEntity<?> checkoutCart(@PathVariable int id){
        try{
            CartCheckout cartCheckout = cartService.checkoutCart(id);
            return ResponseEntity.ok().body(cartCheckout);
        } catch (RuntimeException runtimeException){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database service is currently unavailable");
        }
    }
}
