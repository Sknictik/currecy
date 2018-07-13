package sknictik.currency.domain.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import sknictik.currency.data.service.WebService;
import sknictik.currency.domain.model.CurrencyRate;

public class ExchangeRateCommand {

    private WebService webService;

    public ExchangeRateCommand(WebService webService) {
        this.webService = webService;
    }

    public Observable<List<CurrencyRate>> getListOfCurrencyRatesObs() {
        return webService.getCurrencies().map(currenciesResponse -> {
            List<CurrencyRate> currencyRateList = new ArrayList<>();
            if (currenciesResponse.getCurrencies() != null) {
                for (Map.Entry<String, Double> entry : currenciesResponse.getCurrencies().entrySet()) {
                    currencyRateList.add(new CurrencyRate(entry.getKey(), entry.getValue()));
                }
            }
            return currencyRateList;
        });
    }

}
