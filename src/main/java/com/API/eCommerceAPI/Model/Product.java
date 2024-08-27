package com.API.eCommerceAPI.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name= "PRODUCT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue
    private int product_id;
    @Column(unique = true)
    @NotEmpty
    @Size(max=200)
    private String name;
    @Min(0)
    private double price;
    private String added_at;
    private List<String> labels;
}
