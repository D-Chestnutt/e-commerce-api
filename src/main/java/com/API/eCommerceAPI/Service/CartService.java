package com.API.eCommerceAPI.Service;

import com.API.eCommerceAPI.Model.Cart;
import com.API.eCommerceAPI.Model.CartCheckout;
import com.API.eCommerceAPI.Model.CartProducts;
import com.API.eCommerceAPI.Repository.CartRepository;
import com.API.eCommerceAPI.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductRepository productRepository;

    public Cart save(Cart cart){
        return cartRepository.save(cart);
    }

    public List<Cart> findAll() {return cartRepository.findAll();}

    public CartCheckout checkoutCart(int id) {
        try{
            Optional<Cart> optionalCart = cartRepository.findById(id);
            if(optionalCart.isPresent()){
                Cart checkoutCart = optionalCart.get();
                checkoutCart.setChecked_out(true);
                save(checkoutCart);
                return CartCheckout.builder().cart(checkoutCart).price(getCartPrice(checkoutCart)).build();
            } else {
                throw new RuntimeException("Unable to find cart with id:" + id);
            }
        } catch (RuntimeException runtimeException){
            throw runtimeException;
        }
    }

    private Double getCartPrice (Cart cart) {
        List<CartProducts> cartProducts = cart.getProducts();
        return cartProducts.stream()
                .map(cartProduct -> productRepository.findById(cartProduct.getProduct_id())
                        .map(product -> product.getPrice() * cartProduct.getQuantity())
                        .orElse(0.00))
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}
