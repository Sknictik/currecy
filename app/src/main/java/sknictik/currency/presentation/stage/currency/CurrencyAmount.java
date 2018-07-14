package sknictik.currency.presentation.stage.currency;

import java.io.Serializable;

public class CurrencyAmount implements Serializable {

    private String currency;
    private double amount;

    public CurrencyAmount(String currency, double amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyAmount that = (CurrencyAmount) o;

        if (Double.compare(that.amount, amount) != 0) return false;
        return currency != null ? currency.equals(that.currency) : that.currency == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = currency != null ? currency.hashCode() : 0;
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
