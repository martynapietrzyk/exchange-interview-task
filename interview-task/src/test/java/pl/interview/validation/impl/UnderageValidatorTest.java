package pl.interview.validation.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.interview.util.DateProvider;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnderageValidatorTest {

    @Test
    void shouldValidateUnderAge() {
        //given
        DateProvider mockDateProvider = Mockito.mock(DateProvider.class);
        UnderageValidator underageValidator = new UnderageValidator(mockDateProvider);
        Mockito.when(mockDateProvider.provideToday()).thenReturn(LocalDate.of(2024, 7, 1));
        //when
        boolean valid = underageValidator.isValid("82010112345", null);
        boolean notValid = underageValidator.isValid("12220512344", null);
        boolean notValidNotCorrect = underageValidator.isValid("abcdef", null);
        //then
        assertTrue(valid);
        assertFalse(notValid);
        assertFalse(notValidNotCorrect);
    }

}