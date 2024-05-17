package dev.chijiokeibekwe.librarymanagementsystem.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.hibernate.validator.internal.constraintvalidators.bv.time.futureorpresent.FutureOrPresentValidatorForYear;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PastOrPresentYearValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PastOrPresentYear {

    String message() default "Year is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
