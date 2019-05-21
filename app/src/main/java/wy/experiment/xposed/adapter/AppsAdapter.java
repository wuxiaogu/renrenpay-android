package wy.experiment.xposed.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wy.experiment.xposed.R;
import wy.experiment.xposed.db.model.Apps;


/**
 * Created by chenxinyou on 2019/3/9.
 */

public class AppsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Apps> apps;
    private onRecyclerItemClickerListener mListener;

    public AppsAdapter(List<Apps> apps) {
        this.apps = apps;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_app, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Apps app = apps.get(position);
        String appId = app.getAppid().substring(0,10) + "******";
        ((ViewHolder) holder).appId.setText(appId);
        ((ViewHolder) holder).appName.setText(app.getName());
        String appSecret = app.getAppsecret().substring(0,10) + "******";
        ((ViewHolder) holder).appSecret.setText(appSecret);
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView appName;
        TextView appId;
        TextView appSecret;

        public ViewHolder(View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.text_app_name);
            appId = itemView.findViewById(R.id.value_app_id);
            appSecret = itemView.findViewById(R.id.value_app_Secret);
        }
    }

    private View.OnClickListener getOnClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener && null != v) {
                    mListener.onRecyclerItemClick(v, apps.get(position), position);
                }
            }
        };
    }

    /**
     * 点击监听回调接口
     */
    public interface onRecyclerItemClickerListener {
        void onRecyclerItemClick(View view, Object data, int position);
    }

    /**
     * 增加点击监听
     */
    public void setItemListener(onRecyclerItemClickerListener mListener) {
        this.mListener = mListener;
    }

}
