package pl.interview.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.interview.model.command.CreateAccountCommand;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerData;
    @Column(unique = true)
    private String customerPesel;
    private BigDecimal balanceInPln;

    private Account(String customerData, String customerPesel, BigDecimal balanceInPln) {
        this.customerData = customerData;
        this.customerPesel = customerPesel;
        this.balanceInPln = balanceInPln;
    }

    public static Account from(CreateAccountCommand createAccountCommand) {
        return new Account(createAccountCommand.customerData(),
                createAccountCommand.customerPesel(),
                createAccountCommand.balanceInPln());
    }
}
