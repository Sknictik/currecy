package sknictik.currency.presentation.stage.currency;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import etr.android.reamp.mvp.ReampAppCompatActivity;
import etr.android.reamp.mvp.ReampPresenter;
import sknictik.currency.CurrencyApplication;
import sknictik.currency.R;

public class CurrencyActivity extends ReampAppCompatActivity<CurrencyPresenter, CurrencyStateModel> {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @Override
    public void onStateChanged(CurrencyStateModel stateModel) {
        //TODO
    }

    @Override
    public CurrencyStateModel onCreateStateModel() {
        return new CurrencyStateModel();
    }

    @Override
    public ReampPresenter<CurrencyStateModel> onCreatePresenter() {
        return new CurrencyPresenter(((CurrencyApplication) getApplication()).getCommandFactory().getExchangeRateCommand());
    }
}
