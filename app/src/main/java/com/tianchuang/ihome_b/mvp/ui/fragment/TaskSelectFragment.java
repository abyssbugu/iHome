package com.tianchuang.ihome_b.mvp.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.tianchuang.ihome_b.R;
import com.tianchuang.ihome_b.adapter.TaskSelectAdapter;
import com.tianchuang.ihome_b.base.BaseFragment;
import com.tianchuang.ihome_b.bean.ListBean;
import com.tianchuang.ihome_b.bean.QrCodeBean;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by abyss on 2017/3/23.
 */

public class TaskSelectFragment extends BaseFragment {
    @BindView(R.id.rv_list)
    RecyclerView rvList;


    public static TaskSelectFragment newInstance(ListBean list) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("listBean", list);
        TaskSelectFragment taskSelectFragment = new TaskSelectFragment();
        taskSelectFragment.setArguments(bundle);
        return taskSelectFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        setToolbarTitle("选择任务");
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        ListBean listBean = (ListBean) getArguments().getSerializable("listBean");
        ArrayList<QrCodeBean> list = listBean.getQrCodeBeanArrayList();
        TaskSelectAdapter adapter = new TaskSelectAdapter(list);
        rvList.setAdapter(adapter);
        rvList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                //跳转到控制点的任务详情
                QrCodeBean qrCodeBean = (QrCodeBean) adapter.getData().get(position);
                addFragment(MyTaskControlPointDetailFragment.newInstance(qrCodeBean.getTaskRecordId()));
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_task_select;
    }

}
