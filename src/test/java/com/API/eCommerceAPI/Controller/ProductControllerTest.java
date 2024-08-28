package com.API.eCommerceAPI.Controller;

import com.API.eCommerceAPI.Model.Product;
import com.API.eCommerceAPI.Service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    ProductService productService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testCreateProductsSuccess() throws Exception {
        Product product = getValidProduct();
        String productJson = objectMapper.writeValueAsString(product);

        when(productService.saveNewProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(productJson)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(productJson)));
    }

    @Test
    public void testGetProductsSuccess() throws Exception {
        List<Product> products = List.of(getValidProduct());
        String productsJson = objectMapper.writeValueAsString(products);

        when(productService.findAll()).thenReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(productsJson)));
    }

    @Test
    public void testGetProductByIdSuccess() throws Exception {
        Product product = getValidProduct();
        String productJson = objectMapper.writeValueAsString(product);

        when(productService.findById(1)).thenReturn(product);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(productJson)));
    }

    @Test
    public void testDeleteProductByIdSuccess() throws Exception {
        doNothing().when(productService).deleteById(1);

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk());
    }

    private Product getValidProduct() {
        return Product.builder().product_id(1).name("Wine").price(1.00).labels(List.of("drink")).build();
    }
}
