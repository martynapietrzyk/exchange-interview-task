package pl.interview.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.interview.model.command.CreateAccountCommand;
import pl.interview.model.dto.AccountDto;
import pl.interview.service.AccountService;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(@RequestBody CreateAccountCommand createAccountCommand) {
        log.info("createAccount({})", createAccountCommand);
        return accountService.createAccount(createAccountCommand);
    }

    @GetMapping("/{id}")
    public AccountDto getAccount(@PathVariable Long id,
                                 @RequestParam(required = false, name = "currency", defaultValue = "PLN") String currency) {
        log.info("getAccount({}, {})", id, currency);
        return accountService.getAccountData(id, currency);
    }

}
