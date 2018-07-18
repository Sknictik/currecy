package sknictik.currency.presentation.stage.currency;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.observers.TestSubscriber;
import sknictik.currency.domain.command.ExchangeRateCommand;
import sknictik.currency.domain.model.CurrencyRate;

import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyPresenterTests {

    @Rule
    public final RxPluginsRule mRxPluginsRule = new RxPluginsRule();

    @Test
    public void testThread_getExchangeRate() {
        ExchangeRateCommand exchangeRateCommand = mock(ExchangeRateCommand.class);
        doReturn(Observable.just(Collections.emptyList())).when(exchangeRateCommand).getListOfCurrencyRatesObs(any());

        CurrencyPresenter currencyPresenter = new CurrencyPresenter(exchangeRateCommand);

        TestSubscriber<List<CurrencyRate>> testSubscriber = new TestSubscriber<>(new Subscriber<List<CurrencyRate>>() {
            @Override
            public void onCompleted() {
                //Never completes
            }

            @Override
            public void onError(Throwable e) {
                //Do nothing
            }

            @Override
            public void onNext(List<CurrencyRate> irrelevant) {
                //Do nothing
            }
        });

        currencyPresenter.getExchangeRateObs("EUR").subscribe(testSubscriber);

        testSubscriber.awaitValueCount(5, 2, TimeUnit.SECONDS);

        testSubscriber.assertNoErrors();
        testSubscriber.assertNotCompleted();
        //Added a rule to replace AndroidSchedulers.mainThread with io scheduler
        assertTrue(testSubscriber.getLastSeenThread().getName().startsWith("RxIoScheduler"));
    }

}
