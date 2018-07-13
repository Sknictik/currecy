package sknictik.currency;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import sknictik.currency.data.service.WebServiceImpl;
import sknictik.currency.domain.CommandFactory;

public class CurrencyApplication extends Application {

    private CommandFactory commandFactory;

    private static final int CONNECTION_TIMEOUT = 120;

    @Override
    public void onCreate() {
        super.onCreate();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(interceptor).build();

        commandFactory = new CommandFactory(new WebServiceImpl(okHttpClient, BuildConfig.BASE_URL));
    }

    public CommandFactory getCommandFactory() {
        return commandFactory;
    }
}
