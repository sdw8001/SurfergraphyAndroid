package com.surfergraphy.surf.wavepurchase;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.surfergraphy.surf.R;
import com.surfergraphy.surf.wavepurchase.data.WavePurchase;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

public class Adapter_WavePurchase extends RealmBasedRecyclerViewAdapter<WavePurchase, Adapter_WavePurchase.ViewHolder> {

    private Context context;

    public interface OnBuyEventListener {
        void OnBuyClickEvent(WavePurchase wavePurchase);
    }

    private OnBuyEventListener onBuyEventListener;

    public void setOnBuyEventListener(OnBuyEventListener onBuyEventListener) {
        this.onBuyEventListener = onBuyEventListener;
    }

    public Adapter_WavePurchase(Context context, RealmResults<WavePurchase> realmResults, boolean automaticUpdate, boolean animateIdType) {
        super(context, realmResults, automaticUpdate, animateIdType);
        this.context = context;
    }

    public Adapter_WavePurchase(Context context, RealmResults<WavePurchase> realmResults, boolean automaticUpdate, boolean animateIdType, OnBuyEventListener onBuyEventListener) {
        this(context, realmResults, automaticUpdate, animateIdType);
        this.onBuyEventListener = onBuyEventListener;
    }

    public class ViewHolder extends RealmViewHolder {

        @BindView(R.id.text_view_wave_count)
        TextView textView_waveCount;
        @BindView(R.id.text_view_wave_price)
        TextView textView_wavePrice;
        @BindView(R.id.button_buy)
        Button button_buy;

        public ViewHolder(ViewGroup container) {
            super(container);
            ButterKnife.bind(this, container);
        }
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.item_wave_purchase_recyclerview, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder((ViewGroup) view);
        return viewHolder;
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
        final WavePurchase wavePurchase = realmResults.get(position);
        viewHolder.textView_waveCount.setText(String.valueOf(wavePurchase.waveCount));
        viewHolder.textView_wavePrice.setText(context.getString(R.string.sign_money_ko) + String.format("%,d KRW", wavePurchase.wavePrice));
        viewHolder.button_buy.setOnClickListener(v -> onBuyEventListener.OnBuyClickEvent(wavePurchase));
    }
}
