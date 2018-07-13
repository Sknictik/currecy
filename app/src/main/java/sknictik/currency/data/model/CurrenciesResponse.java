package sknictik.currency.data.model;

import java.util.Map;

public class CurrenciesResponse {

    private String base;
    private String date; //yyyy-MM-dd
    private Map<String, Double> currencies;

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    public Map<String, Double> getCurrencies() {
        return currencies;
    }
}
