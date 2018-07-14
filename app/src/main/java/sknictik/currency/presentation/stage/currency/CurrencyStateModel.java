package sknictik.currency.presentation.stage.currency;

import java.util.List;

import etr.android.reamp.mvp.Action;
import etr.android.reamp.mvp.ReampStateModel;

public class CurrencyStateModel extends ReampStateModel {

    Action<String> error = new Action<>();
    List<CurrencyAmount> currencyAmountList;
    CurrencyAmount baseCurrency;

}
