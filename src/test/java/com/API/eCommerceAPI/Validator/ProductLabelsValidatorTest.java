package com.API.eCommerceAPI.Validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ProductLabelsValidatorTest {

    private ProductLabelsValidator productLabelsValidator = new ProductLabelsValidator();
    @MockBean
    ConstraintValidatorContext constraintValidatorContext;

    @Test
    public void whenProductLabelsIsNullThenInvalid(){
        assertFalse(productLabelsValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    public void whenProductLabelsContainsInvalidValueThenInvalid(){
        assertFalse(productLabelsValidator.isValid(List.of("invalid"), constraintValidatorContext));
    }

    @Test
    public void whenProductLabelsContainsOnlyValidValuesThenValid(){
        assertTrue(productLabelsValidator.isValid(List.of("drink", "food"), constraintValidatorContext));
    }
}
