package com.API.eCommerceAPI.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductLabels {
    DRINK("drink"),
    FOOD("food"),
    CLOTHES("clothes"),
    LIMITED("limited");

    private final String value;
}
