package sknictik.currency.presentation.stage.currency;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sknictik.currency.R;
import sknictik.currency.databinding.ItemCurrencyBinding;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

    private List<CurrencyAmount> currencyAmountList;

    private OnCurrencyListChangedListener onCurrencyListChangedListener;

    CurrencyAdapter(OnCurrencyListChangedListener onCurrencyListChangedListener) {
        this.onCurrencyListChangedListener = onCurrencyListChangedListener;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return currencyAmountList.get(position).getCurrency().hashCode();
    }

    void setCurrencyAmountList(List<CurrencyAmount> currencyAmountList) {
        if (currencyAmountList != null) {
            if (this.currencyAmountList == null
                    || !this.currencyAmountList.containsAll(currencyAmountList)
                    || this.currencyAmountList.size() != currencyAmountList.size()) {
                //Prevent changing array from outside of adapter
                boolean isNeedToUpdateFirstPosition = this.currencyAmountList == null || this.currencyAmountList.isEmpty();
                this.currencyAmountList = new ArrayList<>(currencyAmountList);
                if (isNeedToUpdateFirstPosition) {
                    notifyDataSetChanged();
                } else {
                    //Never update first position if adapter already filled
                    notifyDatasetChangedExceptFirstPosition();
                }
            }
        } else if (this.currencyAmountList != null && !this.currencyAmountList.isEmpty()) {
            this.currencyAmountList = new ArrayList<>();
            notifyDataSetChanged();
        }
    }

    private void notifyDatasetChangedExceptFirstPosition() {
        notifyItemRangeChanged(1, currencyAmountList.size() - 1);
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CurrencyViewHolder(DataBindingUtil.inflate(inflater, R.layout.item_currency, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        holder.bind(currencyAmountList.get(position));
    }

    @Override
    public int getItemCount() {
        return currencyAmountList == null ? 0 : currencyAmountList.size();
    }

    class CurrencyViewHolder extends RecyclerView.ViewHolder {

        private ItemCurrencyBinding binding;

        CurrencyViewHolder(ItemCurrencyBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
            binding.currencyAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //Do nothing
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //Do nothing
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    CurrencyAmount currencyAmount = binding.getData();
                    String amountStr = editable.toString();
                    if (!TextUtils.isEmpty(amountStr)) {
                        //Just in case locale uses comma instead of dots...
                        amountStr = amountStr.replaceAll(",", ".");
                        currencyAmount.setAmount(Double.parseDouble(amountStr));
                    } else {
                        currencyAmount.setAmount(0);
                    }


                    if (binding.currencyAmount.getTag() == null) {
                        if (onCurrencyListChangedListener != null) {
                            onCurrencyListChangedListener.onCurrencyAmountChanged(currencyAmount);
                        }
                    } else {
                        //Mark programmatic change as complete
                        binding.currencyAmount.setTag(null);
                    }
                }
            });

            binding.currencyAmount.setOnFocusChangeListener((view, isFocused) -> {
                if (isFocused) {
                    CurrencyAmount currencyAmount = binding.getData();
                    int oldIndex = currencyAmountList.indexOf(currencyAmount);
                    if (oldIndex != 0) {
                        currencyAmountList.remove(currencyAmount);
                        currencyAmountList.add(0, currencyAmount);
                        notifyItemMoved(oldIndex, 0);
                        if (onCurrencyListChangedListener != null) {
                            onCurrencyListChangedListener.onCurrencyListOrderChanged(currencyAmount);
                        }
                    }
                }
            });
        }

        void bind(CurrencyAmount currencyAmount) {
            //Set arbitrary tag to mark edit text as being changed programmatically. We can prevent recursive updates with this.
            binding.currencyAmount.setTag(new Object());
            binding.setData(currencyAmount);
        }
    }

    public interface OnCurrencyListChangedListener {
        void onCurrencyListOrderChanged(CurrencyAmount updatedCurrency);

        void onCurrencyAmountChanged(CurrencyAmount updatedCurrencyEntry);
    }
}
