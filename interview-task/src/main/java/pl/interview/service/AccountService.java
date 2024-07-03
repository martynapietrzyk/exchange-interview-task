package pl.interview.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.interview.client.NbpApiClient;
import pl.interview.client.response.NBPExchangeDto;
import pl.interview.exception.AccountAlreadyExistsException;
import pl.interview.exception.AccountNotFoundException;
import pl.interview.model.Account;
import pl.interview.model.command.CreateAccountCommand;
import pl.interview.model.dto.AccountDto;
import pl.interview.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final NbpApiClient nbpApiClient;

    @Transactional
    public AccountDto createAccount(CreateAccountCommand createAccountCommand) {
        if (accountRepository.existsByCustomerPesel(createAccountCommand.customerPesel())) {
            throw new AccountAlreadyExistsException();
        }
        Account account = accountRepository.saveAndFlush(Account.from(createAccountCommand));
        return AccountDto.from(account);
    }

    @Transactional(readOnly = true)
    public AccountDto getAccountData(Long id, String currency) {
        Account account = accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
        if ("PLN".equalsIgnoreCase(currency)) return AccountDto.from(account);
        NBPExchangeDto exchangeRate = nbpApiClient.getExchangeRate(currency);
        return AccountDto.from(account, exchangeRate.getRates().get(0).getMid(), currency);
    }
}
