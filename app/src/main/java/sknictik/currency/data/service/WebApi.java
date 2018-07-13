package sknictik.currency.data.service;

import retrofit2.http.GET;
import rx.Observable;
import sknictik.currency.data.model.CurrenciesResponse;

public interface WebApi {

    @GET("latest?base=EUR")
    Observable<CurrenciesResponse> getCurrencies();

}
