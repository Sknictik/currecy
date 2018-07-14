package sknictik.currency.presentation.stage.currency;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.List;

import etr.android.reamp.mvp.ReampAppCompatActivity;
import etr.android.reamp.mvp.ReampPresenter;
import sknictik.currency.CurrencyApplication;
import sknictik.currency.R;
import sknictik.currency.databinding.ActivityMainBinding;

public class CurrencyActivity extends ReampAppCompatActivity<CurrencyPresenter, CurrencyStateModel> {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.currencyList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.currencyList.setAdapter(new CurrencyAdapter(new CurrencyAdapter.OnCurrencyListChangedListener() {
            @Override
            public void onCurrencyListOrderChanged(CurrencyAmount updatedCurrency) {
                getPresenter().onCurrencyOrderChanged(updatedCurrency);
            }

            @Override
            public void onCurrencyAmountChanged(CurrencyAmount updatedCurrencyEntry) {
                getPresenter().onCurrencyAmountChanged(updatedCurrencyEntry);
            }
        }));

        //Hide animation for item changes. It does not look pretty.
        ((SimpleItemAnimator) binding.currencyList.getItemAnimator()).setSupportsChangeAnimations(false);
        setSupportActionBar(binding.toolbar);
    }

    @Override
    public void onStateChanged(CurrencyStateModel stateModel) {
        ((CurrencyAdapter) binding.currencyList.getAdapter()).setCurrencyAmountList(stateModel.currencyAmountList);
        if (stateModel.error.hasAction()) {
            showError(stateModel.error.get());
        }
    }

    @Override
    public CurrencyStateModel onCreateStateModel() {
        return new CurrencyStateModel();
    }

    @Override
    public ReampPresenter<CurrencyStateModel> onCreatePresenter() {
        return new CurrencyPresenter(((CurrencyApplication) getApplication()).getCommandFactory().getExchangeRateCommand());
    }

    private void showError(String error) {
        if (!TextUtils.isEmpty(error)) {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }
    }
}
