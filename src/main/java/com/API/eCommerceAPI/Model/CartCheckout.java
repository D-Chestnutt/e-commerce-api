package com.API.eCommerceAPI.Model;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
public class CartCheckout {
    Cart cart;
    double price;
}
