package com.API.eCommerceAPI.Model;

import com.API.eCommerceAPI.Validator.ValidListOfProductLabels;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
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
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private Date added_at;
    @ValidListOfProductLabels
    private List<String> labels;

    @PrePersist
    protected void onCreate(){
        if (this.added_at == null){
            this.setAdded_at(new Date());
        }
    }
}
