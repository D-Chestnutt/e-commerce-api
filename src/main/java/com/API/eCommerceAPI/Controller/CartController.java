package com.API.eCommerceAPI.Controller;

import com.API.eCommerceAPI.Model.Cart;
import com.API.eCommerceAPI.Model.CartCheckout;
import com.API.eCommerceAPI.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping("/carts")
    ResponseEntity<?> createCart(@RequestBody Cart cart){
        Cart savedCart = cartService.saveNewCart(cart);
        return ResponseEntity.ok().body(savedCart);
    }

    @GetMapping("/carts")
    ResponseEntity<?> getCarts(){
        List<Cart> carts = cartService.findAll();
        return ResponseEntity.ok().body(carts);
    }

    @PutMapping("/carts/{id}")
    ResponseEntity<?> updateCart(@RequestBody Cart cart, @PathVariable int id){
        Cart savedCart = cartService.saveExistingCart(cart, id);
        return ResponseEntity.ok().body(savedCart);
    }

    @PostMapping("/carts/{id}/checkout")
    ResponseEntity<?> checkoutCart(@PathVariable int id){
        CartCheckout cartCheckout = cartService.checkoutCart(id);
        return ResponseEntity.ok().body(cartCheckout);
    }
}
