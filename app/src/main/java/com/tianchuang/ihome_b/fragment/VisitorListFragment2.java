package com.tianchuang.ihome_b.fragment;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tianchuang.ihome_b.R;
import com.tianchuang.ihome_b.activity.VisitorListActivity;
import com.tianchuang.ihome_b.adapter.VistorListAdapter;
import com.tianchuang.ihome_b.bean.VisitorBean;
import com.tianchuang.ihome_b.bean.model.VisitorListModel;
import com.tianchuang.ihome_b.http.retrofit.RxHelper;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by Abyss on 2017/3/12.
 * description:我的访客
 */

public class VisitorListFragment2 extends BaseRefreshAndLoadMoreFragment<VisitorBean.VisitorItemBean, VisitorBean> implements VisitorListActivity.RequestSearchListener {

    private VisitorListActivity holdingActivity;
    private String phone = "";

    public static VisitorListFragment2 newInstance() {
        return new VisitorListFragment2();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        holdingActivity = (VisitorListActivity) getHoldingActivity();
        holdingActivity.setRequestSearchListener(this);
    }

    @Override
    protected BaseQuickAdapter initAdapter(ArrayList<VisitorBean.VisitorItemBean> mData, VisitorBean listBean) {
        return new VistorListAdapter(R.layout.item_visitor, mData);
    }

    @Override
    protected void onListitemClick(VisitorBean.VisitorItemBean itemBean) {

    }

    @Override
    protected Observable<VisitorBean> getNetObservable(int maxId) {
        return VisitorListModel.visitorList(maxId, phone)
                .compose(RxHelper.<VisitorBean>handleResult());
    }

    @Override
    protected String getEmptyString() {
        return "最近访客为空";
    }

    @Override
    public void searchByPhoneNum(String phone) {//访问网络进行搜索
        this.phone = phone;
        onRefresh();
    }
}
