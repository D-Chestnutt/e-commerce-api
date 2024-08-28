package com.API.eCommerceAPI.Validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Set;

public class ProductLabelsValidator implements ConstraintValidator<ValidListOfProductLabels, List<String>> {

    Set<String> validLabels = Set.of("food", "drink", "clothing", "limited");

    @Override
    public boolean isValid(List<String> labels, ConstraintValidatorContext constraintValidatorContext) {
        if (labels == null) {
            return false;
        }
        return labels.stream().allMatch(label -> validLabels.contains(label));
    }
}
