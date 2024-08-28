package com.API.eCommerceAPI.Service;

import com.API.eCommerceAPI.Model.Cart;
import com.API.eCommerceAPI.Model.CartCheckout;
import com.API.eCommerceAPI.Model.CartProducts;
import com.API.eCommerceAPI.Repository.CartRepository;
import com.API.eCommerceAPI.Repository.ProductRepository;
import org.hibernate.service.spi.ServiceException;
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

    public Cart saveNewCart(Cart cart){
        if (cart.isChecked_out()){
            throw new ServiceException("Cart cannot be created with a checked_out value of true.");
        }
        if (cart.getCart_id() != 0) {
            throw new ServiceException("cart_id is generated at write and should not be provided.");
        } else {
            return cartRepository.save(cart);
        }
    }

    public Cart saveExistingCart(Cart cart, int cartId) {
        if (cartId == 0 || cart.getCart_id() == 0){
            throw new ServiceException("The field cart_id is required and cannot be 0.");
        } else if (cartId!=cart.getCart_id()){
            throw new ServiceException("The field cart_id does not match the id passed in the request path.");
        } else if (cart.isChecked_out()){
            throw new ServiceException("The field checked_out cannot be set to true by this endpoint. Please use the checkout endpoint.");
        }
        Optional<Cart> optionalCart = cartRepository.findById(cartId);

        if(optionalCart.isPresent()){
            Cart existingCart = optionalCart.get();

            if (existingCart.isChecked_out()){
                throw new ServiceException("Cart has already been checked out");
            } else {
                return cartRepository.save(cart);
            }
        }else {
            throw new ServiceException("Unable to find cart with id: " + cartId);
        }
    }

    public List<Cart> findAll() {return cartRepository.findAll();}

    public CartCheckout checkoutCart(int id) {
        Optional<Cart> optionalCart = cartRepository.findById(id);
        if(optionalCart.isPresent()){
            Cart checkoutCart = optionalCart.get();
            if(checkoutCart.isChecked_out()){
                throw new ServiceException("Cart has already been checked out.");
            }
            checkoutCart.setChecked_out(true);
            cartRepository.save(checkoutCart);
            return CartCheckout.builder().cart(checkoutCart).price(getCartPrice(checkoutCart)).build();
        } else {
            throw new ServiceException("Unable to find cart with id:" + id);
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
