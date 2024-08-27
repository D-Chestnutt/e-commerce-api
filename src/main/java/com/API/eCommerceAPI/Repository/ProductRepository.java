package com.API.eCommerceAPI.Repository;

import com.API.eCommerceAPI.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository <Product, Integer> {
}
