package wy.experiment.xposed.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import wy.experiment.xposed.R;
import wy.experiment.xposed.db.model.Order;

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Order> orders;

    private View mHeaderView;
    private View mFooterView;
    private View mEmptyView;

    private int ITEM_TYPE_NORMAL = 0;
    private int ITEM_TYPE_HEADER = 1;
    private int ITEM_TYPE_FOOTER = 2;
    private int ITEM_TYPE_EMPTY = 3;

    public OrdersAdapter(List<Order> orders) {
        this.orders = orders;
    }

    public void setDatas(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            return new ViewHolder(mHeaderView);
        } else if (viewType == ITEM_TYPE_EMPTY) {
            return new ViewHolder(mEmptyView);
        } else if (viewType == ITEM_TYPE_FOOTER) {
            return new ViewHolder(mFooterView);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_statistic, parent, false);
            OrdersAdapter.ViewHolder viewHolder = new OrdersAdapter.ViewHolder(v);
            return viewHolder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (null != mHeaderView && position == 0) {
            return ITEM_TYPE_HEADER;
        }
        if (null != mFooterView
                && position == getItemCount() - 1) {
            return ITEM_TYPE_FOOTER;
        }
        if (null != mEmptyView && orders.size() == 0) {
            return ITEM_TYPE_EMPTY;
        }
        return ITEM_TYPE_NORMAL;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ITEM_TYPE_HEADER
                || type == ITEM_TYPE_FOOTER
                || type == ITEM_TYPE_EMPTY) {
            return;
        }

        Order order = orders.get(position);
        ((OrdersAdapter.ViewHolder) holder).orderId.setText(order.getOrderid());
        ((OrdersAdapter.ViewHolder) holder).appId.setText(order.getAppid());
        ((OrdersAdapter.ViewHolder) holder).amount.setText(order.getAmount() + "");
        ((OrdersAdapter.ViewHolder) holder).from.setText(order.getPayway() == 2 ? "支付宝" : order.getPayway() == 1 ? "微信" : "sss");
//        ((OrdersAdapter.ViewHolder) holder).itemLayout.setBackgroundColor( position % 2 == 0 ? R.color.card_view_color : R.color.list1);
        ((OrdersAdapter.ViewHolder) holder).itemLayout.setBackgroundColor(Color.parseColor(position % 2 == 0 ? "#31313E" : "#393948"));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId;
        TextView appId;
        TextView amount;
        TextView from;
        RelativeLayout itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.item_order_id);
            appId = itemView.findViewById(R.id.item_app_id);
            amount = itemView.findViewById(R.id.item_amount);
            from = itemView.findViewById(R.id.item_from);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }
    }

    public void addHeaderView(View view) {
        mHeaderView = view;
        notifyItemInserted(0);
    }

    public void addFooterView(View view) {
        mFooterView = view;
        notifyItemInserted(getItemCount() - 1);
    }

    public void setEmptyView(View view) {
        mEmptyView = view;
        notifyDataSetChanged();
    }

    private int getRealItemPosition(int position) {
        if (null != mHeaderView) {
            return position - 1;
        }
        return position;
    }
}
