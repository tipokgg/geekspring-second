package com.geekbrains.geekspring;

import com.geekbrains.geekspring.validation.EmailValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmailValidationUnitTests {

    EmailValidator emailValidator;

    // перед каждым тестом создаем новый объект валидатора
    @BeforeEach
    public void init() {
        this.emailValidator = new EmailValidator();
    }

    // проверяем, что если в мередан null в email, то вернётся false
    @Test
    public void falseIfEmailIsNull() {
        boolean result = emailValidator.isValid(null, null);
        Assertions.assertFalse(result);
    }

    // проверяем, что возвращается false, если email некорректный
    @Test
    public void invalidEmailTest() {
        boolean result = emailValidator.isValid("antiv18@mail", null);
        Assertions.assertFalse(result);
    }

    // проверяем, что возвращается true, если email корректный
    @Test
    public void correctEmailTest() {
        boolean result = emailValidator.isValid("antiv18@mail.ru", null);
        Assertions.assertTrue(result);
    }
}
