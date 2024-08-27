package com.API.eCommerceAPI.Repository;

import com.API.eCommerceAPI.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository <Cart, Integer> {
}
