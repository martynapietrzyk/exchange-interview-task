package pl.interview.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.interview.util.DateProvider;
import pl.interview.validation.annotation.Underage;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class UnderageValidator implements ConstraintValidator<Underage, String> {

    private final DateProvider dateProvider;

    @Override
    public boolean isValid(String pesel, ConstraintValidatorContext context) {
        try {
            return Optional.ofNullable(pesel)
                    .map(this::validateAgeFromPesel)
                    .orElse(true);
        } catch (Exception e) {
            log.error("error with pesel parsing: ", e);
            return false;
        }

    }

    private boolean validateAgeFromPesel(String pesel) {
        LocalDate birthDate = extractBirthDateFromPesel(pesel);
        LocalDate currentDate = dateProvider.provideToday();
        return Period.between(birthDate, currentDate).getYears() >= 18;
    }

    private LocalDate extractBirthDateFromPesel(String pesel) {
        int year = Integer.parseInt(pesel.substring(0, 2));
        int month = Integer.parseInt(pesel.substring(2, 4));
        int day = Integer.parseInt(pesel.substring(4, 6));

        if (month > 80) {
            year += 1800;
            month -= 80;
        } else if (month > 60) {
            year += 2200;
            month -= 60;
        } else if (month > 40) {
            year += 2100;
            month -= 40;
        } else if (month > 20) {
            year += 2000;
            month -= 20;
        } else {
            year += 1900;
        }

        return LocalDate.of(year, month, day);
    }
}
