package ru.yandex.practicum.filmorate.myvalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
public @interface Date {

    String message() default "Недопустимая дата";

    Class<?>[] groups() default  { };

    Class<? extends Payload>[] payload() default  { };
}
