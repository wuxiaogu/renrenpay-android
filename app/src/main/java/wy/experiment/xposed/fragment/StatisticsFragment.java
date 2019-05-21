package wy.experiment.xposed.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import wy.experiment.xposed.R;
import wy.experiment.xposed.adapter.NewOrderAdapter;
import wy.experiment.xposed.base.BaseFragment;
import wy.experiment.xposed.db.ApiResponse;
import wy.experiment.xposed.db.model.Order;
import wy.experiment.xposed.db.model.OrderResList;
import wy.experiment.xposed.net.HttpResHandler;
import wy.experiment.xposed.net.NetHelper;
import wy.experiment.xposed.view.AppContext;

/**
 * Created by chenxinyou on 2019/2/26.
 */

public class StatisticsFragment extends BaseFragment {

    @BindView(R.id.order_list)
    RefreshRecyclerView recyclerView;
    private static final String TAG = "cxyStat";
    private List<Order> orders = new ArrayList<>();
    private NewOrderAdapter adapter;

    private static int page = 1;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_statistic;
    }

    @Override
    public void initView(View view) {
        initOrder();
    }

    private void initOrder() {
        NetHelper.paycheckList(1, 10, new HttpResHandler() {
            @Override
            public void onFailure(int errorCode, String data) {
                Log.d(TAG, "errorCode -> " + errorCode);
            }

            @Override
            public void onSuccess(ApiResponse res) {

                OrderResList orderResList = JSONObject.parseObject(res.getData().toString(), OrderResList.class);
                Log.d(TAG, orderResList.getRows().toString());
                orders = orderResList.getRows();
                adapter = new NewOrderAdapter(getActivity(), orders);
                recyclerView.setAdapter(adapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(AppContext.getInstance());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                View header = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header, recyclerView, false);
                View footer = LayoutInflater.from(getActivity()).inflate(R.layout.layout_footer, recyclerView, false);
                adapter.setHeader(header);
                adapter.setFooter(footer);
                recyclerView.addRefreshAction(new Action() {
                    @Override
                    public void onAction() {
                        page = 1;
                        refreshAction(1, true);

                    }
                });
                recyclerView.addLoadMoreAction(new Action() {
                    @Override
                    public void onAction() {
                        page += 1;
                        refreshAction(page, false);
                    }
                });
            }
        });
    }

    private void refreshAction(int page, boolean refresh) {
        NetHelper.paycheckList(page, 10, new HttpResHandler() {
            @Override
            public void onFailure(int errorCode, String data) {
                Log.d(TAG, "errorCode -> " + errorCode);
            }

            @Override
            public void onSuccess(ApiResponse res) {
                OrderResList orderResList = JSONObject.parseObject(res.getData().toString(), OrderResList.class);
                Log.d(TAG, orderResList.getRows().toString());
                if(refresh) {
                    orders = orderResList.getRows();
                    adapter.clear();
                    adapter.addAll(orders);
                    recyclerView.dismissSwipeRefresh();
                } else {
                    orders.addAll(orderResList.getRows());
                    adapter.addAll(orderResList.getRows());
                    recyclerView.showNoMore();
                }
                Log.d(TAG, "size -> " + adapter.getData().size());
            }
        });
    }

    @Override
    public void initData() {
    }

}
