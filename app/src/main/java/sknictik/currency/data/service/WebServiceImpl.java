package sknictik.currency.data.service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import sknictik.currency.data.model.ExchangeRateResponse;

public class WebServiceImpl implements WebService {

    private WebApi webApi;

    public WebServiceImpl(OkHttpClient client, String baseUrl) {
        webApi = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build().create(WebApi.class);
    }


    @Override
    public Observable<ExchangeRateResponse> getCurrencies(String currency) {
        return webApi.getCurrencies(currency);
    }
}
