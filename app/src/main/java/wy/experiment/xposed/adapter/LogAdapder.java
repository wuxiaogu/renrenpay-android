package wy.experiment.xposed.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;
import wy.experiment.xposed.R;
import wy.experiment.xposed.db.model.Logs;
import wy.experiment.xposed.utils.XPConstant;

public class LogAdapder extends RecyclerAdapter<Logs> {

    public LogAdapder(Context context, List<Logs> data) {
        super(context, data);
    }

    @Override
    public BaseViewHolder<Logs> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new CardRecordHolder(parent);
    }

    class CardRecordHolder extends BaseViewHolder<Logs> {

        TextView log;
        TextView time;
        View itemLayout;

        public CardRecordHolder(ViewGroup parent) {
            super(parent, R.layout.item_logs);
        }

        @Override
        public void setData(Logs data) {
            super.setData(data);
            log.setText(data.getContent());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(data.getTime());
            time.setText(simpleDateFormat.format(date));
            itemLayout.setBackgroundColor(Color.parseColor(getPosition() % 2 == 0 ? "#393948" : "#31313E"));
            log.setTextColor(Color.parseColor(data.getLogLv() == XPConstant.DEBUG ? "#FFFFFF" :
                    data.getLogLv() == XPConstant.INFO ? "#FFD700" : data.getLogLv() == XPConstant.ERROR ? "#FF4081" : "#FFFFFF" ));
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            log = itemView.findViewById(R.id.log_content);
            time = itemView.findViewById(R.id.log_time);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }
    }
}
