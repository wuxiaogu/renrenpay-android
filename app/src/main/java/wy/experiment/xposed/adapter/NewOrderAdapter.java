package wy.experiment.xposed.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;
import wy.experiment.xposed.R;
import wy.experiment.xposed.db.model.Order;

public class NewOrderAdapter extends RecyclerAdapter<Order> {

    public NewOrderAdapter(Context context, List<Order> data) {
        super(context, data);
    }

    @Override
    public BaseViewHolder<Order> onCreateBaseViewHolder(ViewGroup parent, int viewType) {

        return new CardRecordHolder(parent);
    }

    class CardRecordHolder extends BaseViewHolder<Order> {

        TextView orderId;
        TextView appId;
        TextView amount;
        TextView from;
        TextView dateData;
        TextView dateTime;
        RelativeLayout itemLayout;

        public CardRecordHolder(ViewGroup parent) {
            super(parent, R.layout.item_statistic);
        }

        @Override
        public void setData(Order order) {
            String[] times = order.getCreatedAt().split("T");
            Log.d("cxy", times[0] + ";" + times[1]);
            super.setData(order);
            orderId.setText(order.getOrderid());
            String strAppId = order.getAppid().substring(0, 10) + "******";
            appId.setText(strAppId);
            amount.setText("￥" + (order.getAmount() / 100f));
            from.setText(order.getPayway() == 2 ? "支付宝" : order.getPayway() == 1 ? "微信" : "sss");
            dateData.setText(times[0]);
            dateTime.setText(times[1].substring(0, 8));
            itemLayout.setBackgroundColor(Color.parseColor(getPosition() % 2 == 0 ? "#31313E" : "#393948"));
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            orderId = itemView.findViewById(R.id.item_order_id);
            appId = itemView.findViewById(R.id.item_app_id);
            amount = itemView.findViewById(R.id.item_amount);
            from = itemView.findViewById(R.id.item_from);
            dateData = itemView.findViewById(R.id.date_data);
            dateTime = itemView.findViewById(R.id.date_time);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }
    }
}


