package dev.chijiokeibekwe.librarymanagementsystem.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class PastOrPresentYearValidator implements ConstraintValidator<PastOrPresentYear, Integer> {

    @Override
    public void initialize(PastOrPresentYear year) {
    }

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext cxt) {
        LocalDate startOfYear = LocalDate.ofYearDay(year, 1);
        return startOfYear.isBefore(LocalDate.now());
    }
}
