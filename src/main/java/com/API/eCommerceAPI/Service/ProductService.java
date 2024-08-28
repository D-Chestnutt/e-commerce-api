package com.API.eCommerceAPI.Service;

import com.API.eCommerceAPI.Model.Product;
import com.API.eCommerceAPI.Repository.ProductRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public Product saveNewProduct(Product product) {
        if(product.getProduct_id() == 0){
            return productRepository.save(product);
        } else{
            throw new ServiceException("product_id is generated at write and should not be provided.");
        }
    }

    public Product findById(int productId) {
        Optional <Product> optionalProduct  = productRepository.findById(productId);

        if(optionalProduct.isPresent()){
            return optionalProduct.get();
        } else {
            throw new ServiceException("Unable to find product with id:" + productId);
        }
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void deleteById(int productId) {
        productRepository.deleteById(productId);
    }
}
