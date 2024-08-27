package com.API.eCommerceAPI.Model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CartCheckout {
    Cart cart;
    double price;
}
