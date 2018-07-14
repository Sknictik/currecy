package sknictik.currency.presentation.stage.utils;

import android.databinding.BindingAdapter;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class DataBindingAdapters {

    @BindingAdapter("currencyAmount")
    public static void setCurrencyAmount(EditText editText, double amount) {
        //Show hint insted
        if (amount != 0) {
            editText.setText(new DecimalFormat("#.##").format(amount));
        } else {
            editText.setText("");
        }
    }

}
