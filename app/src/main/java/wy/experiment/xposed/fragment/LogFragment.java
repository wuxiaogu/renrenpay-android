package wy.experiment.xposed.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import java.util.List;

import butterknife.BindView;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import wy.experiment.xposed.R;
import wy.experiment.xposed.adapter.LogAdapder;
import wy.experiment.xposed.base.BaseFragment;
import wy.experiment.xposed.db.model.Logs;
import wy.experiment.xposed.db.model.User;
import wy.experiment.xposed.db.util.LogDao;
import wy.experiment.xposed.view.AppContext;

/**
 * Created by chenxinyou on 2019/2/26.
 */

public class LogFragment extends BaseFragment {

    @BindView(R.id.local_logs)
    RefreshRecyclerView recyclerView;
    private LogAdapder adapter;
    private User user;
    private List<Logs> logs;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_log;
    }

    @Override
    public void initData() {
        user = AppContext.getInstance().getUser();
        logs = LogDao.getLogs(user.getUserid());
        adapter = new LogAdapder(getActivity(), logs);
        recyclerView.setAdapter(adapter);
        recyclerView.addRefreshAction(new Action() {
            @Override
            public void onAction() {
                logs.clear();
                logs = LogDao.getLogs(user.getUserid());
                adapter.clear();
                adapter.addAll(logs);
                recyclerView.dismissSwipeRefresh();
            }
        });
        recyclerView.showNoMore();
        LinearLayoutManager layoutManager = new LinearLayoutManager(AppContext.getInstance());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

}
