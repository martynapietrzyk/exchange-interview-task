package pl.interview.client.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class NBPExchangeDto {
    private String table;
    private String currency;
    private String code;
    private List<Rate> rates;

    public static NBPExchangeDto of(String currency, double mid) {
        Rate rate = new Rate();
        rate.setMid(mid);
        NBPExchangeDto dto = new NBPExchangeDto();
        dto.setCurrency(currency);
        dto.setRates(List.of(rate));
        return dto;
    }

    @Getter
    @Setter
    @ToString
    public static class Rate {
        private String no;
        private String effectiveDate;
        private double mid;
    }
}
