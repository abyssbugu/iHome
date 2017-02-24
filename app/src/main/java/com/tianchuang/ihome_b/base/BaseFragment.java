package com.tianchuang.ihome_b.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tianchuang.ihome_b.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Abyss on 2017/2/8.
 * description:fragment的基类
 */

public abstract class BaseFragment extends RxFragment implements DialogProgress {
	protected BaseActivity mActivity;
	private Unbinder bind;
	private View rootView;

	protected abstract void initView(View view, Bundle savedInstanceState);

	//获取布局文件ID
	protected abstract int getLayoutId();

	//获取宿主Activity
	protected BaseActivity getHoldingActivity() {
		return mActivity;
	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.mActivity = (BaseActivity) context;
	}

	//添加fragment
	protected void addFragment(BaseFragment fragment) {
		if (null != fragment) {
			getHoldingActivity().addFragment(fragment);
		}
	}

	//移除fragment
	protected void removeFragment() {
		getHoldingActivity().removeFragment();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (null != rootView) {//处理fragment的view重复加载
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (null != parent) {
				parent.removeView(rootView);
			}
		} else {
			rootView = inflater.inflate(getLayoutId(), container, false);
			bind = ButterKnife.bind(this, rootView);
			initView(rootView, savedInstanceState);
			initData();
			initListener();

		}
		return rootView;
	}

	//初始化监听

	protected void initListener() {

	}

	//初始化数据
	protected void initData() {

	}

	@Override
	public void showProgress() {
		getHoldingActivity().showProgress();
	}

	@Override
	public void dismissProgress() {
		getHoldingActivity().dismissProgress();
	}

	public void startActivityWithAnim(Intent intent) {
		startActivity(intent);
		mActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		bind.unbind();
	}
}
