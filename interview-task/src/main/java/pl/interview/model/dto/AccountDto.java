package pl.interview.model.dto;

import pl.interview.model.Account;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public record AccountDto(Long id, String customerData, BigDecimal balance, String balanceCurrency) {
    public static AccountDto from(Account account) {
        return new AccountDto(account.getId(),
                account.getCustomerData(),
                account.getBalanceInPln(),
                "PLN");
    }

    public static AccountDto from(Account account, double exchangeRate, String currency) {
        return new AccountDto(account.getId(),
                account.getCustomerData(),
                account.getBalanceInPln().divide(BigDecimal.valueOf(exchangeRate), 2, RoundingMode.CEILING),
                currency);
    }
}
