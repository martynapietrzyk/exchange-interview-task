package pl.interview.util;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DateProvider {
    public LocalDate provideToday() {
        return LocalDate.now();
    }
}
