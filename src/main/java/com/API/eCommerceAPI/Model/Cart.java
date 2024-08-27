package com.API.eCommerceAPI.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "CART")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Cart {
    @Id
    @GeneratedValue
    private int cart_id;
    @ElementCollection
    private List<CartProducts> products;
    private boolean checked_out;

}
