package com.API.eCommerceAPI.Service;

import com.API.eCommerceAPI.Model.Product;
import com.API.eCommerceAPI.Repository.ProductRepository;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.SpringVersion;

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

    @Test
    public void testSaveNewProductSuccess(){
        Product product = getValidProduct();
        Product productNoId = getValidProduct();
        productNoId.setProduct_id(0);

        when(productRepository.save(productNoId)).thenReturn(product);

        Product actualProduct = productService.saveNewProduct(productNoId);

        assertEquals(actualProduct, product);
    }

    @Test
    public void testSaveNewProductThrowsServiceExceptionWhenProductIdNotProvided(){
        Product product = getValidProduct();

        assertThrows(ServiceException.class, () -> productService.saveNewProduct(product));
    }

    @Test
    public void testFindById(){
        Product product = getValidProduct();

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Product actualProduct = productService.findById(1);

        assertEquals(actualProduct, product);
    }

    @Test
    public void testFindAll(){
        Product product = getValidProduct();
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> actualProducts = productService.findAll();

        assertEquals(List.of(product), actualProducts);
    }

    @Test
    public void testDeleteById(){
        doNothing().when(productRepository).deleteById(1);

        assertDoesNotThrow(() -> productService.deleteById(1));
    }

    @Test
    public void testFindByIdThrowsRuntimeExceptionWhenEmptyOptionalReturned(){
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> productService.findById(1));
    }

    private Product getValidProduct () {
        return Product.builder().product_id(1).name("meatballs").price(5.45).labels(List.of("food")).build();
    }
}
