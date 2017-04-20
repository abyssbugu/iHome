package com.tianchuang.ihome_b.mvp.presenter;

import com.tianchuang.ihome_b.bean.LoginBean;
import com.tianchuang.ihome_b.bean.PropertyListItemBean;
import com.tianchuang.ihome_b.bean.model.PropertyModel;
import com.tianchuang.ihome_b.database.UserInfoDbHelper;
import com.tianchuang.ihome_b.http.retrofit.HttpModle;
import com.tianchuang.ihome_b.http.retrofit.RxHelper;
import com.tianchuang.ihome_b.http.retrofit.RxSubscribe;
import com.tianchuang.ihome_b.mvp.BasePresenterImpl;
import com.tianchuang.ihome_b.mvp.contract.PropertyListContract;
import com.tianchuang.ihome_b.utils.UserUtil;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Abyss on 2017/4/17.
 * description:物业列表
 */
public class PropertyListPresenter extends BasePresenterImpl<PropertyListContract.View> implements PropertyListContract.Presenter {
    private ArrayList<PropertyListItemBean> data;

    @Override
    public void requestPropertyListData() {
        PropertyModel.requestPropertyList()
                .compose(RxHelper.handleResult())
                .compose(mView.bindToLifecycle())
                .subscribe(new RxSubscribe<ArrayList<PropertyListItemBean>>() {
                    @Override
                    protected void _onNext(ArrayList<PropertyListItemBean> propertyList) {
                        data = propertyList;
                        mView.showSucceedPage();
                        mView.initAdapter(propertyList);

                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showToast(message);
                        mView.showErrorPage();
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }

    @Override
    public void requestSetOften(PropertyListItemBean propertyListItemBean, int position) {
        PropertyModel.requestSetOften(propertyListItemBean.getId())
                .map(HttpModle::success)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> mView.showProgress())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dismissProgress();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {//设为常用成功
                            for (PropertyListItemBean propertyListItemBean : data) {
                                propertyListItemBean.setOftenUse(false);
                            }
                            PropertyListItemBean bean = data.get(position);
                            LoginBean loginBean = UserUtil.propertyListItemBeanToLoginBean(bean);
                            //储存到数据库中
                            UserInfoDbHelper.saveUserInfo(loginBean, UserUtil.getUserid());
                            bean.setOftenUse(!bean.getOftenUse());
                            mView.notifyUISetOften(position);//通知ui设置常用

                        } else {
                            mView.showToast("设为常用失败");
                        }
                        mView.dismissProgress();
                    }
                });
    }
}