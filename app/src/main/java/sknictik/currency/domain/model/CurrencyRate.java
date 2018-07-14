package sknictik.currency.domain.model;

public class CurrencyRate {

    private String currency;
    private double exchangeRate;

    public CurrencyRate(String currency, double exchangeRate) {
        this.currency = currency;
        this.exchangeRate = exchangeRate;
    }

    public String getCurrency() {
        return currency;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }
}
