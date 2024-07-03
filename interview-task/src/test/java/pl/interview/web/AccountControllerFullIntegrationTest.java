package pl.interview.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.interview.Main;
import pl.interview.client.NbpApiClient;
import pl.interview.client.response.NBPExchangeDto;
import pl.interview.model.Account;
import pl.interview.model.command.CreateAccountCommand;
import pl.interview.model.dto.AccountDto;
import pl.interview.repository.AccountRepository;

import java.math.BigDecimal;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
//TODO: active profiles test with different DB if needed.
class AccountControllerFullIntegrationTest {

    @MockBean
    private NbpApiClient nbpApiClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;


    @BeforeEach
    void initNBPClient() {
        Mockito.reset(nbpApiClient);
        Mockito.when(nbpApiClient.getExchangeRate("USD")).thenReturn(NBPExchangeDto.of("USD", 4.0));
    }

    @Test
    void shouldGetAccountInPln() throws Exception {
        //given:
        CreateAccountCommand createAccountCommand = new CreateAccountCommand("John Doe", "85062236369", new BigDecimal("1000.00"));
        String jsonRequest = objectMapper.writeValueAsString(createAccountCommand);
        Account account = accountRepository.saveAndFlush(Account.from(createAccountCommand));
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/" + account.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balanceCurrency").value("PLN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value("1000.0"));
        //then:
        Mockito.verifyNoInteractions(nbpApiClient);
    }

    @Test
    void shouldGetAccountInUsd() throws Exception {
        //given:
        CreateAccountCommand createAccountCommand = new CreateAccountCommand("John Doe", "63080992425", new BigDecimal("1000.00"));
        String jsonRequest = objectMapper.writeValueAsString(createAccountCommand);
        Account account = accountRepository.saveAndFlush(Account.from(createAccountCommand));

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/" + account.getId() + "?currency=USD"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balanceCurrency").value("USD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value("250.0"));
        //then:
        Mockito.verify(nbpApiClient, Mockito.times(1)).getExchangeRate("USD");

    }

    @Test
    void shouldNotCreateAccount_accountAlreadyExists() throws Exception {
        //given:
        CreateAccountCommand createAccountCommand = new CreateAccountCommand("John Doe", "71110298683", new BigDecimal("1000.00"));
        String jsonRequest = objectMapper.writeValueAsString(createAccountCommand);
        accountRepository.saveAndFlush(Account.from(createAccountCommand));
        //when:
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isConflict());
        //then:
        Assertions.assertTrue(accountRepository.existsByCustomerPesel("71110298683"));
        long count = accountRepository.findAll().stream().filter(a -> a.getCustomerPesel().equals("71110298683")).count();
        Assertions.assertEquals(1, count);
    }

    @Test
    void shouldCreateAccount() throws Exception {
        //given:
        CreateAccountCommand createAccountCommand = new CreateAccountCommand("John Doe", "51062937449", new BigDecimal("1000.00"));
        String jsonRequest = objectMapper.writeValueAsString(createAccountCommand);
        //when:
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerData").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value("1000.0"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        //then:
        AccountDto accountDto = objectMapper.readValue(response, AccountDto.class);
        Assertions.assertNotNull(accountDto.id());
        Assertions.assertEquals("John Doe", accountDto.customerData());
        Assertions.assertEquals(new BigDecimal("1000.00"), accountDto.balance());
        Assertions.assertTrue(accountRepository.existsByCustomerPesel("51062937449"));
    }
}