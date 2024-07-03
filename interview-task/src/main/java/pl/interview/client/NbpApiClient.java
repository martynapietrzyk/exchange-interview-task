package pl.interview.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.interview.client.response.NBPExchangeDto;

@Service
public class NbpApiClient {

    private final RestTemplate restTemplate;

    public NbpApiClient() {
        this.restTemplate = new RestTemplate();
    }

    public NBPExchangeDto getExchangeRate(String currency) {
        return restTemplate.getForObject("https://api.nbp.pl/api/exchangerates/rates/a/" + currency + "/?format=json", NBPExchangeDto.class);
    }

}
