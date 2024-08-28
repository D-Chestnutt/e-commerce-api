package com.API.eCommerceAPI.Model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class ProductTest {

    @Test
    public void testProductWithoutAddedAtIsPopulatedBeforeTheEntityIsPersisted(){
        Product product = Product.builder().build();

        product.onCreate();

        Date actualAddedAt = product.getAdded_at();
        Date expectedAddedAt = new Date();
        assert(actualAddedAt.equals(expectedAddedAt));
    }
}
