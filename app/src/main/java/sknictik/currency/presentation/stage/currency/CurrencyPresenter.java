package sknictik.currency.presentation.stage.currency;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import etr.android.reamp.mvp.ReampPresenter;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import sknictik.currency.domain.command.ExchangeRateCommand;
import sknictik.currency.domain.model.CurrencyRate;

public class CurrencyPresenter extends ReampPresenter<CurrencyStateModel> {

    private ExchangeRateCommand exchangeRateCommand;

    private Subscription exchangeRateRequestSubscription;

    private List<CurrencyRate> latestCurrencyRates;

    CurrencyPresenter(ExchangeRateCommand exchangeRateCommand) {
        this.exchangeRateCommand = exchangeRateCommand;
    }

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();

        getStateModel().baseCurrency = new CurrencyAmount("EUR", 1);
    }

    @Override
    public void onConnect() {
        super.onConnect();

        subscribeForExchangeRate();
    }

    @Override
    public void onDisconnect() {
        super.onDisconnect();

        if (exchangeRateRequestSubscription != null) {
            exchangeRateRequestSubscription.unsubscribe();
        }
    }

    private void subscribeForExchangeRate() {
        if (exchangeRateRequestSubscription != null) {
            exchangeRateRequestSubscription.unsubscribe();
        }

        exchangeRateRequestSubscription = getExchangeRateObs(getStateModel().baseCurrency.getCurrency())
                .subscribe(new Subscriber<List<CurrencyRate>>() {
                    @Override
                    public void onCompleted() {
                        //Do nothing. Won't be called normally.
                    }

                    @Override
                    public void onError(Throwable e) {
                        CurrencyPresenter.this.onError(e);
                    }

                    @Override
                    public void onNext(List<CurrencyRate> currencyRates) {
                        latestCurrencyRates = currencyRates;
                        if (getStateModel().currencyAmountList == null) {
                            getStateModel().currencyAmountList = calculateInitialCurrencyAmount(currencyRates, getStateModel().baseCurrency);
                        } else {
                            getStateModel().currencyAmountList = calculateCurrencyAmountWithEstablishedOrder(currencyRates, getStateModel().baseCurrency);
                        }
                        sendStateModel();
                    }
                });
    }

    Observable<List<CurrencyRate>> getExchangeRateObs(String currency) {
        return Observable.interval(1, TimeUnit.SECONDS)
                .flatMap(v -> exchangeRateCommand.getListOfCurrencyRatesObs(currency)
                        .onErrorResumeNext(throwable -> {
                            onError(throwable);
                            return Observable.empty();
                        }))
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void onError(Throwable e) {
        Log.e(CurrencyPresenter.class.getSimpleName(), "Failed to retrieve exchange rates", e);
        getStateModel().error.set(e.getLocalizedMessage());
        sendStateModel();
    }

    private List<CurrencyAmount> calculateInitialCurrencyAmount(List<CurrencyRate> currencyRates, CurrencyAmount baseCurrencyAmount) {
        List<CurrencyAmount> currencyAmountList = new ArrayList<>();
        currencyAmountList.add(baseCurrencyAmount);

        for (CurrencyRate currencyRate : currencyRates) {
            currencyAmountList.add(new CurrencyAmount(currencyRate.getCurrency(), new BigDecimal(currencyRate.getExchangeRate()).multiply(new BigDecimal(baseCurrencyAmount.getAmount())).doubleValue()));
        }
        return currencyAmountList;
    }

    private List<CurrencyAmount> calculateCurrencyAmountWithEstablishedOrder(List<CurrencyRate> currencyRates, CurrencyAmount baseCurrencyAmount) {
        List<CurrencyAmount> currencyAmountList = new ArrayList<>();
        currencyAmountList.add(baseCurrencyAmount);

        for (CurrencyAmount currencyAmount : getStateModel().currencyAmountList) {
            CurrencyRate currencyRate = getCurrencyRateByName(currencyAmount.getCurrency(), currencyRates);
            if (currencyRate != null && !currencyRate.getCurrency().equals(baseCurrencyAmount.getCurrency())) {
                currencyAmountList.add(new CurrencyAmount(currencyRate.getCurrency(), new BigDecimal(currencyRate.getExchangeRate()).multiply(new BigDecimal(baseCurrencyAmount.getAmount()))
                        .setScale(2, RoundingMode.FLOOR).doubleValue()));
            } else {
                //If no exchange rate found for currency - remove currency from displayed list
            }
        }

        return currencyAmountList;
    }

    private CurrencyRate getCurrencyRateByName(String currencyName, List<CurrencyRate> currencyRateList) {
        for (CurrencyRate currencyRate : currencyRateList) {
            if (currencyRate.getCurrency().equals(currencyName)) {
                return currencyRate;
            }
        }

        return null;
    }

    private void onBaseCurrencyUpdated(CurrencyAmount newBaseCurrency) {
        getStateModel().baseCurrency = newBaseCurrency;
        subscribeForExchangeRate();
    }

    void onCurrencyOrderChanged(CurrencyAmount newBaseCurrency) {
        onBaseCurrencyUpdated(newBaseCurrency);
    }

    void onCurrencyAmountChanged(CurrencyAmount updatedCurrency) {
        onBaseCurrencyUpdated(updatedCurrency);
        getStateModel().currencyAmountList = calculateCurrencyAmountWithEstablishedOrder(latestCurrencyRates, getStateModel().baseCurrency);
        sendStateModel();
    }

}
