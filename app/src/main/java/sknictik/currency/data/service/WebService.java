package sknictik.currency.data.service;

import rx.Observable;
import sknictik.currency.data.model.ExchangeRateResponse;

public interface WebService {
    Observable<ExchangeRateResponse> getCurrencies(String currency);
}
