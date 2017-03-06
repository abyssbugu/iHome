package com.tianchuang.ihome_b.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.tianchuang.ihome_b.R;
import com.tianchuang.ihome_b.activity.DataSearchActivity;
import com.tianchuang.ihome_b.adapter.EquipmentTypeAdapter;
import com.tianchuang.ihome_b.base.BaseFragment;
import com.tianchuang.ihome_b.bean.EquipmentTypeSearchBean;
import com.tianchuang.ihome_b.bean.recyclerview.EquipmentTypeItemDecoration;
import com.tianchuang.ihome_b.http.retrofit.RxHelper;
import com.tianchuang.ihome_b.http.retrofit.RxSubscribe;
import com.tianchuang.ihome_b.http.retrofit.model.DataSearchModel;
import com.tianchuang.ihome_b.utils.DensityUtil;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Abyss on 2017/3/1.
 * description:设备类型查询
 */

public class EquipmentTypeFragment extends BaseFragment {

	@BindView(R.id.rv_list)
	RecyclerView rvList;
	private DataSearchActivity holdingActivity;
	private EquipmentTypeAdapter typeAdapter;
	private ArrayList<EquipmentTypeSearchBean> mData;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_equipment_type_search;
	}

	public static EquipmentTypeFragment newInstance() {
		return new EquipmentTypeFragment();
	}

	@Override
	public void onStart() {
		super.onStart();
		holdingActivity.setToolbarTitle("设备查询");
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		holdingActivity = ((DataSearchActivity) getHoldingActivity());
		rvList.setLayoutManager(new GridLayoutManager(getContext(), 3));
		rvList.addItemDecoration(new EquipmentTypeItemDecoration(DensityUtil.dip2px(getContext(), 10)));

		requestNet();
	}

	/**
	 * 请求网络
	 */
	private void requestNet() {
		DataSearchModel.requestEquipmentTypeSearch()
				.compose(RxHelper.<ArrayList<EquipmentTypeSearchBean>>handleResult())
				.compose(this.<ArrayList<EquipmentTypeSearchBean>>bindToLifecycle())
				.subscribe(new RxSubscribe<ArrayList<EquipmentTypeSearchBean>>() {


					@Override
					protected void _onNext(ArrayList<EquipmentTypeSearchBean> list) {
						mData = list;
						typeAdapter = new EquipmentTypeAdapter(R.layout.equipment_type_item_holder, mData);
						rvList.setAdapter(typeAdapter);
						rvList.addOnItemTouchListener(new OnItemClickListener() {
							@Override
							public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
								for (EquipmentTypeSearchBean data : mData) {
									data.setSelected(false);
								}
								EquipmentTypeSearchBean equipmentTypeSearchBean = mData.get(position);
								equipmentTypeSearchBean.setSelected(true);
								adapter.notifyDataSetChanged();
								addFragment(EquipmentTypeFormFragment.newInstance(equipmentTypeSearchBean));
							}
						});
					}

					@Override
					protected void _onError(String message) {

					}

					@Override
					public void onCompleted() {

					}
				});

	}


}