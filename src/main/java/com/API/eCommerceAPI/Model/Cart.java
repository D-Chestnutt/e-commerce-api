package com.API.eCommerceAPI.Model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "CART")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue
    private int cart_id;
    @ElementCollection
    @Valid
    private List<CartProducts> products;
    private boolean checked_out;

}
