package com.API.eCommerceAPI.Validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ProductLabelsValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ValidListOfProductLabels {
    String message() default "List contains invalid values.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
