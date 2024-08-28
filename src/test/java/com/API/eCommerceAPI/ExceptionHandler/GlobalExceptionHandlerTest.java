package com.API.eCommerceAPI.ExceptionHandler;

import com.API.eCommerceAPI.Controller.ProductController;
import com.API.eCommerceAPI.Model.Product;
import com.API.eCommerceAPI.Service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.RollbackException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.TransactionSystemException;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
public class GlobalExceptionHandlerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ProductService productService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testRequestHandledCorrectlyWhenConstraintValidationExceptionThrown() throws Exception {
        ConstraintViolation<Object> mockViolation = mock(ConstraintViolation.class);
        Path mockPath = mock(Path.class);
        Product product = getValidProduct();
        String productJson = objectMapper.writeValueAsString(product);
        Set<ConstraintViolation<Object>> violations = Set.of(mockViolation);
        ConstraintViolationException constraintViolationException = new ConstraintViolationException("There have been violations thrown", violations);
        RollbackException rollbackException = new RollbackException(constraintViolationException);
        TransactionSystemException transactionSystemException = new TransactionSystemException("Transaction failed", rollbackException);

        when(mockViolation.getMessage()).thenReturn("name is not valid");
        when(mockViolation.getPropertyPath()).thenReturn(mockPath);

        when(mockPath.toString()).thenReturn("name");

        when(productService.saveNewProduct(any(Product.class))).thenThrow(transactionSystemException);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"name\":\"name is not valid\"}"));
    }

    @Test
    public void testRequestHandledCorrectlyWhenTransactionSystemExceptionThrown() throws Exception {
        Product product = getValidProduct();
        String productJson = objectMapper.writeValueAsString(product);
        TransactionSystemException transactionSystemException = new TransactionSystemException("Transaction failed");

        when(productService.saveNewProduct(any(Product.class))).thenThrow(transactionSystemException);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("{\"error\":\"An unexpected transaction error occurred.\"}"));
    }

    @Test
    public void testRequestHandledCorrectlyWhenRollbackExceptionThrown() throws Exception {
        Product product = getValidProduct();
        String productJson = objectMapper.writeValueAsString(product);
        RollbackException rollbackException = new RollbackException("Rollback exception");
        TransactionSystemException transactionSystemException = new TransactionSystemException("Transaction failed", rollbackException);

        when(productService.saveNewProduct(any(Product.class))).thenThrow(transactionSystemException);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("{\"error\":\"An unexpected transaction error occurred.\"}"));
    }

    @Test
    public void testRequestWhenDataIntegrityViolationExceptionThrown() throws Exception {
        Product product = getValidProduct();
        String productJson = objectMapper.writeValueAsString(product);
        DataIntegrityViolationException dataIntegrityViolationException = new DataIntegrityViolationException("Unable to write to database due to data integrity issue");

        when(productService.saveNewProduct(any(Product.class))).thenThrow(dataIntegrityViolationException);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"Unable to write to database due to data integrity issue\"}"));
    }

    @Test
    public void testRequestHandledCorrectlyWhenServiceExceptionThrown() throws Exception {
        Product product = getValidProduct();
        String productJson = objectMapper.writeValueAsString(product);
        ServiceException serviceException = new ServiceException("Service exception thrown.");

        when(productService.saveNewProduct(any(Product.class))).thenThrow(serviceException);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"Service exception thrown.\"}"));
    }


    private Product getValidProduct() {
        return Product.builder()
                .product_id(1)
                .name("milk")
                .price(3.00)
                .labels(List.of("drink"))
                .build();

    }
}
