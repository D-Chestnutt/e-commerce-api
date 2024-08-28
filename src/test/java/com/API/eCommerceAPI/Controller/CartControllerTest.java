package com.API.eCommerceAPI.Controller;

import com.API.eCommerceAPI.Model.Cart;
import com.API.eCommerceAPI.Model.CartCheckout;
import com.API.eCommerceAPI.Model.CartProducts;
import com.API.eCommerceAPI.Service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    CartService cartService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testCreateCartsSuccess() throws Exception {
        Cart cart = getValidCart();
        String cartJson = objectMapper.writeValueAsString(cart);

        when(cartService.saveNewCart(any(Cart.class))).thenReturn(cart);

        mockMvc.perform(post("/carts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(cartJson)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(cartJson)));
    }

    @Test
    public void testGetCartsSuccess() throws Exception {
        List<Cart> carts = List.of(getValidCart());
        String cartsJson = objectMapper.writeValueAsString(carts);

        when(cartService.findAll()).thenReturn(carts);

        mockMvc.perform(get("/carts"))
                .andExpect(status().isOk())
                .andExpect(content().string(cartsJson));
    }

    @Test
    public void testUpdateCartSuccess() throws Exception {
        Cart cart = getValidCart();
        String cartJson = objectMapper.writeValueAsString(cart);
        when(cartService.saveExistingCart(any(Cart.class), eq(1))).thenReturn(cart);

        mockMvc.perform(put("/carts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(cartJson)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(cartJson)));
    }

    @Test
    public void testCheckoutCartSuccess() throws Exception {
        Cart cart = getValidCart();
        CartCheckout cartCheckout = CartCheckout.builder().cart(cart).price(10.0).build();
        String cartJson = objectMapper.writeValueAsString(cart);
        String cartCheckoutJson = objectMapper.writeValueAsString(cartCheckout);


        when(cartService.checkoutCart(1)).thenReturn(cartCheckout);

        mockMvc.perform(post("/carts/1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(cartJson)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(cartCheckoutJson)));
    }

    private Cart getValidCart(){
        CartProducts cartProducts1 = CartProducts.builder()
                .product_id(1)
                .quantity(5)
                .build();
        CartProducts cartProducts2 = CartProducts.builder()
                .product_id(2)
                .quantity(3)
                .build();
        return Cart.builder()
                .cart_id(1)
                .products(List.of(cartProducts1, cartProducts2))
                .checked_out(false)
                .build();
    }
}
