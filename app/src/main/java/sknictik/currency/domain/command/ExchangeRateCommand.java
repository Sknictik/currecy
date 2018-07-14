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

    public Observable<List<CurrencyRate>> getListOfCurrencyRatesObs(String baseCurrency) {
        return webService.getCurrencies(baseCurrency)
                .map(currenciesResponse -> {
                    List<CurrencyRate> currencyRateList = new ArrayList<>();
                    if (currenciesResponse.getRates() != null) {
                        for (Map.Entry<String, Double> entry : currenciesResponse.getRates().entrySet()) {
                            currencyRateList.add(new CurrencyRate(entry.getKey(), entry.getValue()));
                        }
                    }
                    return currencyRateList;
                });
    }

}
