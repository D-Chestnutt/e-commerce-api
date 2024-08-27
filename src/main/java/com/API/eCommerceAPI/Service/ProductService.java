package com.API.eCommerceAPI.Service;

import com.API.eCommerceAPI.Model.Product;
import com.API.eCommerceAPI.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product findById(int productId) {
        Optional <Product> optionalProduct  = productRepository.findById(productId);

        if(optionalProduct.isPresent()){
            return optionalProduct.get();
        } else {
            throw new RuntimeException("Unable to find product with id:" + productId);
        }
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void deleteById(int productId) {
        productRepository.deleteById(productId);
    }
}
