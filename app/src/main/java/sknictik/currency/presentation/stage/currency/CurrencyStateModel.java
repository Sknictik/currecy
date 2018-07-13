package sknictik.currency.presentation.stage.currency;

import java.util.List;

import etr.android.reamp.mvp.ReampStateModel;
import sknictik.currency.domain.model.CurrencyRate;

public class CurrencyStateModel extends ReampStateModel {

    String minorError;
    List<CurrencyRate> currencyRates;

}
