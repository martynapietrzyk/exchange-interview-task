package pl.interview.model.command;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.pl.PESEL;
import pl.interview.validation.annotation.Underage;

import java.math.BigDecimal;

public record CreateAccountCommand(
        @NotNull(message = "CUSTOMER_DATA_NOT_NULL") String customerData,
        @NotNull(message = "CUSTOMER_PESEL_NOT_NULL") @Underage @PESEL(message = "INVALID_PESEL") String customerPesel,
        @Positive(message = "BALANCE_NOT_NEGATIVE") BigDecimal balanceInPln) {
}
