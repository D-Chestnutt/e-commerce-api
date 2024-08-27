package com.API.eCommerceAPI.Controller;

import com.API.eCommerceAPI.Model.Cart;
import com.API.eCommerceAPI.Model.CartCheckout;
import com.API.eCommerceAPI.Model.CartProducts;
import com.API.eCommerceAPI.Service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.security.PublicKey;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    CartService cartService;

    CartProducts cartProducts1 = CartProducts.builder()
            .product_id(1)
            .quantity(5)
            .build();
    CartProducts cartProducts2 = CartProducts.builder()
            .product_id(2)
            .quantity(3)
            .build();
    Cart cart1 = Cart.builder()
            .cart_id(1)
            .products(List.of(cartProducts1, cartProducts2))
            .checked_out(false)
            .build();
    Cart cart1CheckedOut = Cart.builder()
            .cart_id(1)
            .products(List.of(cartProducts1, cartProducts2))
            .checked_out(true)
            .build();
    CartCheckout cartCheckout = CartCheckout.builder().cart(cart1CheckedOut).price(30.25).build();
    Cart cartNoId = Cart.builder()
            .products(List.of(cartProducts1, cartProducts2))
            .checked_out(false)
            .build();
    List<Cart> expectedCarts = List.of(cart1);
    String createCartBody = "{\"products\":[{\"product_id\":1,\"quantity\":5},{\"product_id\":2,\"quantity\":3}],\"checked_out\":false}";
    String expectedCartString = "{\"cart_id\":1,\"products\":[{\"product_id\":1,\"quantity\":5},{\"product_id\":2,\"quantity\":3}],\"checked_out\":false}";
    String expectedCartCheckoutString = "{\"cart\":{\"cart_id\":1,\"products\":[{\"product_id\":1,\"quantity\":5},{\"product_id\":2,\"quantity\":3}],\"checked_out\":true},\"price\":30.25}";
    String expectedCartsString = "[{\"cart_id\":1,\"products\":[{\"product_id\":1,\"quantity\":5},{\"product_id\":2,\"quantity\":3}],\"checked_out\":false}]";

    @Test
    public void testCreateCartsSuccess() throws Exception {
        when(cartService.save(any(Cart.class))).thenReturn(cart1);

        mockMvc.perform(post("/carts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(createCartBody)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedCartString)));
    }

    @Test
    public void testGetCartsSuccess() throws Exception {
        when(cartService.findAll()).thenReturn(expectedCarts);

        mockMvc.perform(get("/carts"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCartsString));
    }

    @Test
    public void testUpdateCartSuccess() throws Exception {
        when(cartService.save(any(Cart.class))).thenReturn(cart1);

        mockMvc.perform(put("/carts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(createCartBody)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedCartString)));
    }

    @Test
    public void testCheckoutCartSuccess() throws Exception {
        when(cartService.checkoutCart(1)).thenReturn(cartCheckout);

        mockMvc.perform(post("/carts/1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(createCartBody)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedCartCheckoutString)));
    }

    @Test
    public void testCreateCartThrowsExceptionWhenCartServiceThrowsException() throws Exception {
        RuntimeException runtimeException = new RuntimeException("Database service is currently unavailable");
        when(cartService.save(any(Cart.class))).thenThrow(runtimeException);

        mockMvc.perform(post("/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(createCartBody)))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().string("Database service is currently unavailable"));
    }

}
