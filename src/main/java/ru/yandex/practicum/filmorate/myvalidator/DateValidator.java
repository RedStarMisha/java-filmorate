package ru.yandex.practicum.filmorate.myvalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<Date, LocalDate> {

    @Override
    public void initialize(Date constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate == null) return false;
        if (localDate.isAfter(LocalDate.of(1895,12,28)) && localDate.isBefore(LocalDate.now())) {
            return true;
        }
        return false;
    }

}
