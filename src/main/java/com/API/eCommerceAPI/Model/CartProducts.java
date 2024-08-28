package com.API.eCommerceAPI.Model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartProducts {
    int product_id;
    @Min(0)
    int quantity;
}
