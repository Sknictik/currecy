package sknictik.currency.data.service;

import rx.Observable;
import sknictik.currency.data.model.CurrenciesResponse;

public interface WebService {
    Observable<CurrenciesResponse> getCurrencies();
}
