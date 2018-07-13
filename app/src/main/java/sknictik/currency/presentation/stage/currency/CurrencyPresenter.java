package sknictik.currency.presentation.stage.currency;

import java.util.List;
import java.util.concurrent.TimeUnit;

import etr.android.reamp.mvp.ReampPresenter;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import sknictik.currency.domain.command.ExchangeRateCommand;
import sknictik.currency.domain.model.CurrencyRate;

public class CurrencyPresenter extends ReampPresenter<CurrencyStateModel> {

    private ExchangeRateCommand exchangeRateCommand;

    private Subscription exchangeRateRequestSubscription;

    public CurrencyPresenter(ExchangeRateCommand exchangeRateCommand) {
        this.exchangeRateCommand = exchangeRateCommand;
    }

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();

        exchangeRateRequestSubscription = Observable.interval(1, TimeUnit.SECONDS)
                .flatMap(v -> exchangeRateCommand.getListOfCurrencyRatesObs()
                        .onErrorResumeNext(throwable -> Observable.empty()))
                .subscribe(new Subscriber<List<CurrencyRate>>() {
                    @Override
                    public void onCompleted() {
                        //Do nothing. Won't be called normally.
                    }

                    @Override
                    public void onError(Throwable e) {
                        getStateModel().minorError = e.getLocalizedMessage();
                        sendStateModel();
                    }

                    @Override
                    public void onNext(List<CurrencyRate> currencyRates) {
                        if (currencyRates != null) {
                            getStateModel().currencyRates = currencyRates;
                            sendStateModel();
                        }
                    }
                });
    }

    @Override
    public void onDestroyPresenter() {
        super.onDestroyPresenter();

        if ()
        exchangeRateRequestSubscription.unsubscribe();
    }
}
