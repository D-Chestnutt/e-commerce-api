package com.API.eCommerceAPI.Model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartProducts {
    int product_id;
    int quantity;
}
