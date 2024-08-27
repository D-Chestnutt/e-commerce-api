package com.API.eCommerceAPI.Service;

import com.API.eCommerceAPI.Model.Cart;
import com.API.eCommerceAPI.Model.Product;
import com.API.eCommerceAPI.Repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    ProductService productService;
    @MockBean
    ProductRepository productRepository;

    Product product1 = Product.builder().product_id(1).name("meatballs").price(5.45).added_at("2024/12/14").labels(List.of("food")).build();
    Product product2 = Product.builder().product_id(1).name("water").price(5.45).added_at("2024/12/14").labels(List.of("drink")).build();

    @Test
    public void testSave(){
        when(productRepository.save(product1)).thenReturn(product1);

        Product actualProduct = productService.save(product1);

        assertEquals(actualProduct, product1);
    }

    @Test
    public void testFindById(){
        when(productRepository.findById(1)).thenReturn(Optional.of(product1));

        Product actualProduct = productService.findById(1);

        assertEquals(actualProduct, product1);
    }

    @Test
    public void testFindAll(){
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<Product> actualProducts = productService.findAll();

        assertEquals(List.of(product1, product2), actualProducts);
    }

    @Test
    public void testDeleteById(){
        doNothing().when(productRepository).deleteById(1);

        assertDoesNotThrow(() -> productService.deleteById(1));
    }

    @Test
    public void testFindByIdThrowsRuntimeExceptionWhenEmptyOptionalReturned(){
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.findById(1));
    }
}
