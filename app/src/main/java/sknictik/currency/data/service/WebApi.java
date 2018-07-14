package sknictik.currency.data.service;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import sknictik.currency.data.model.ExchangeRateResponse;

public interface WebApi {

    @GET("latest")
    Observable<ExchangeRateResponse> getCurrencies(@Query("base") String baseCurrency);

}
