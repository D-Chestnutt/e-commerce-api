package com.API.eCommerceAPI.Service;

import com.API.eCommerceAPI.Model.Cart;
import com.API.eCommerceAPI.Model.CartCheckout;
import com.API.eCommerceAPI.Model.CartProducts;
import com.API.eCommerceAPI.Model.Product;
import com.API.eCommerceAPI.Repository.CartRepository;
import com.API.eCommerceAPI.Repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CartServiceTest {

    @Autowired
    CartService cartService;
    @MockBean
    CartRepository cartRepository;
    @MockBean
    ProductRepository productRepository;        
    
    Product product1 = Product.builder().product_id(1).price(10.25).build();
    Product product2 = Product.builder().product_id(2).price(6.80).build();
    CartProducts cartProduct1 = CartProducts.builder().product_id(1).quantity(2).build();
    CartProducts cartProduct2 = CartProducts.builder().product_id(2).quantity(3).build();
    List<CartProducts> products = List.of(cartProduct1, cartProduct2);
    Cart cart = Cart.builder().cart_id(1).checked_out(false).products(products).build();
    Cart checkedOutCart = Cart.builder().cart_id(1).checked_out(true).products(products).build();

    @Test
    public void testSave(){
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart actualCart = cartService.save(cart);

        assertEquals(actualCart, cart);
    }
    
    @Test
    public void testFindAll(){
        when(cartRepository.findAll()).thenReturn(List.of(cart));
        
        List<Cart> actualCarts = cartService.findAll();
        
        assertEquals(List.of(cart), actualCarts);
        
    }

    @Test
    public void testCheckoutCart(){
        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2)).thenReturn(Optional.of(product2));
        
        CartCheckout expectedCartCheckout = CartCheckout.builder().cart(checkedOutCart).price(40.90).build();
        CartCheckout actualCartCheckout = cartService.checkoutCart(1);

        assertEquals(actualCartCheckout.getCart().getCart_id(), expectedCartCheckout.getCart().getCart_id());
        assertEquals(actualCartCheckout.getCart().getProducts(), expectedCartCheckout.getCart().getProducts());
        assertEquals(actualCartCheckout.getCart().isChecked_out(), expectedCartCheckout.getCart().isChecked_out());
        assertEquals(actualCartCheckout.getPrice(), expectedCartCheckout.getPrice());
    }

    @Test
    public void testSaveThrowsException(){
        Exception exception = new RuntimeException("Unable to save cart to database");

        when(cartRepository.save(cart)).thenThrow(exception);

        assertThrows(RuntimeException.class, () -> cartService.save(cart));
    }

    @Test
    public void testFindAllThrowsException(){
        Exception exception = new RuntimeException("Unable to save cart to database");

        when(cartRepository.findAll()).thenThrow(exception);

        assertThrows(RuntimeException.class, () -> cartService.findAll());
    }

    @Test
    public void testCheckoutCartThrowsException(){
        Exception exception = new RuntimeException("Unable to find product id 2");

        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2)).thenThrow(exception);

        assertThrows(RuntimeException.class, () -> cartService.checkoutCart(1));
    }

    @Test
    public void testCheckoutCartThrowsExceptionWhenOptionalCartIsEmpty(){
        when(cartRepository.findById(1)).thenReturn(Optional.empty());
        when(productRepository.findById(1)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2)).thenReturn(Optional.of(product2));

        assertThrows(RuntimeException.class, () -> cartService.checkoutCart(1));
    }
}
