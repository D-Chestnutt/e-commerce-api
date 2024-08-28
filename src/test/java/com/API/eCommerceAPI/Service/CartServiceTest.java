package com.API.eCommerceAPI.Service;

import com.API.eCommerceAPI.Model.Cart;
import com.API.eCommerceAPI.Model.CartCheckout;
import com.API.eCommerceAPI.Model.CartProducts;
import com.API.eCommerceAPI.Model.Product;
import com.API.eCommerceAPI.Repository.CartRepository;
import com.API.eCommerceAPI.Repository.ProductRepository;
import org.hibernate.service.spi.ServiceException;
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

    @Test
    public void testSaveNewCart(){
        Cart cartId = getValidCart();
        Cart cartNoId = getValidCart();
        cartNoId.setCart_id(0);

        when(cartRepository.save(cartNoId)).thenReturn(cartId);

        Cart actualCart = cartService.saveNewCart(cartNoId);

        assertEquals(actualCart, cartId);
    }

    @Test
    public void testSaveNewCartSetCheckedOutToTrueThrowsException(){
        Cart cart = getValidCart();
        cart.setChecked_out(true);
        cart.setCart_id(1);

        assertThrows(ServiceException.class, () -> cartService.saveNewCart(cart));
    }

    @Test
    public void testSaveExistingCart(){
        Cart cart = getValidCart();

        when(cartRepository.save(cart)).thenReturn(cart);
        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));

        Cart actualCart = cartService.saveExistingCart(cart, 1);

        assertEquals(actualCart, cart);
    }

    @Test
    public void testSaveExistingCartCartIdNotFoundThrowsException(){
        Cart cart = getValidCart();

        when(cartRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> cartService.saveExistingCart(cart, 1));
    }

    @Test
    public void testSaveExistingCartSetCheckedOutToTrueThrowsException(){
        Cart cart = getValidCart();
        cart.setChecked_out(true);

        assertThrows(ServiceException.class, () -> cartService.saveExistingCart(cart, 1));
    }

    @Test
    public void testSaveExistingCartCartIdIs0(){
        Cart cart = getValidCart();
        cart.setCart_id(0);

        assertThrows(ServiceException.class, () -> cartService.saveExistingCart(cart, 0));
    }

    @Test
    public void testSaveExistingCartCartIdInBodyDoesNotMatchPath(){
        Cart cart = getValidCart();
        cart.setCart_id(2);

        assertThrows(ServiceException.class, () -> cartService.saveExistingCart(cart, 3));
    }
    
    @Test
    public void testFindAll(){
        Cart cart = getValidCart();

        when(cartRepository.findAll()).thenReturn(List.of(cart));
        
        List<Cart> actualCarts = cartService.findAll();
        
        assertEquals(List.of(cart), actualCarts);
        
    }

    @Test
    public void testCheckoutCart(){
        Cart cart = getValidCart();
        Cart checkedOutCart = getValidCart();
        checkedOutCart.setChecked_out(true);

        Product product1 = Product.builder().product_id(1).price(10.25).build();
        Product product2 = Product.builder().product_id(2).price(6.80).build();

        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2)).thenReturn(Optional.of(product2));
        
        CartCheckout expectedCartCheckout = CartCheckout.builder().cart(checkedOutCart).price(40.90).build();
        CartCheckout actualCartCheckout = cartService.checkoutCart(1);

        assertEquals(actualCartCheckout.getCart().getCart_id(), expectedCartCheckout.getCart().getCart_id());
        assertEquals(actualCartCheckout.getCart().isChecked_out(), expectedCartCheckout.getCart().isChecked_out());
        assertEquals(actualCartCheckout.getPrice(), expectedCartCheckout.getPrice());
    }

    @Test
    public void testCheckoutCartThrowsExceptionWhenCartAlreadyCheckedOut(){
        Cart checkedOutCart = getValidCart();
        checkedOutCart.setChecked_out(true);

        when(cartRepository.findById(1)).thenReturn(Optional.of(checkedOutCart));

        assertThrows(ServiceException.class, () -> cartService.checkoutCart(1));
    }

    @Test
    public void testSaveThrowsException(){
        Cart cart = getValidCart();

        Exception exception = new RuntimeException("Unable to save cart to database");

        when(cartRepository.save(cart)).thenThrow(exception);

        assertThrows(RuntimeException.class, () -> cartService.saveNewCart(cart));
    }

    @Test
    public void testSaveCartThrowsExceptionWhenCartAlreadyCheckedOut(){
        Cart cart = getValidCart();
        Cart checkedOutCart = getValidCart();
        checkedOutCart.setChecked_out(true);

        when(cartRepository.findById(1)).thenReturn(Optional.of(checkedOutCart));

        assertThrows(ServiceException.class, () -> cartService.saveExistingCart(cart,1));
    }

    @Test
    public void testFindAllThrowsException(){
        Exception exception = new RuntimeException("Unable to save cart to database");

        when(cartRepository.findAll()).thenThrow(exception);

        assertThrows(RuntimeException.class, () -> cartService.findAll());
    }

    @Test
    public void testCheckoutCartThrowsException(){
        Cart cart = getValidCart();
        Product product1 = Product.builder().product_id(1).price(10.25).build();
        Exception exception = new RuntimeException("Unable to find product id 2");

        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2)).thenThrow(exception);

        assertThrows(RuntimeException.class, () -> cartService.checkoutCart(1));
    }

    @Test
    public void testCheckoutCartThrowsExceptionWhenOptionalCartIsEmpty(){
        Product product1 = Product.builder().product_id(1).price(10.25).build();
        Product product2 = Product.builder().product_id(2).price(6.80).build();

        when(cartRepository.findById(1)).thenReturn(Optional.empty());
        when(productRepository.findById(1)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2)).thenReturn(Optional.of(product2));

        assertThrows(RuntimeException.class, () -> cartService.checkoutCart(1));
    }

    private Cart getValidCart() {
        CartProducts cartProduct1 = CartProducts.builder().product_id(1).quantity(2).build();
        CartProducts cartProduct2 = CartProducts.builder().product_id(2).quantity(3).build();
        List<CartProducts> products = List.of(cartProduct1, cartProduct2);
        return Cart.builder().cart_id(1).checked_out(false).products(products).build();
    }
}
