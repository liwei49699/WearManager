package com.chengzhen.wearmanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.bean.PeopleListResponse;

import java.util.List;


public class MyEmployeeAdapter extends BaseAdapter implements SectionIndexer {

	private List<PeopleListResponse.DataBean> dataBeanList;

	public void setPeopleList(List<PeopleListResponse.DataBean> dataBeanList){
		this.dataBeanList = dataBeanList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {

		return dataBeanList == null ? 0 : dataBeanList.size();
	}

	@Override
	public PeopleListResponse.DataBean getItem(int position) {

		return dataBeanList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder vh;

		if(convertView == null){

			vh = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appoint_employee,parent,false);
			vh.tvNameEmployee = (TextView) convertView.findViewById(R.id.tv_name_employee);
			vh.tvIndex = (TextView) convertView.findViewById(R.id.tv_index);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}

		PeopleListResponse.DataBean employee = getItem(position);
		vh.tvNameEmployee.setText(employee.getDeviceName());
		vh.tvIndex.setText(employee.getFirstSpell());
//		if(position != ) {
//
//		}

		if( position !=0  && getItem(position-1).getFirstSpell().equals(employee.getFirstSpell())){

			vh.tvIndex.setVisibility(View.GONE);
			/*vh.tvNameEmployee.setVisibility(View.VISIBLE);*/
		}else {

			vh.tvIndex.setVisibility(View.VISIBLE);
			/*vh.tvNameEmployee.setVisibility(View.GONE);*/
		}

		return convertView;
	}

	class ViewHolder{

		TextView tvNameEmployee,tvIndex;
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		if(sectionIndex == '↑'){
			return -1;  // +1 = 0   正好所有头视图的位置
		}
		for (int i = 0; i < (dataBeanList == null ? 0 : dataBeanList.size()); i++) {
			PeopleListResponse.DataBean employee =  dataBeanList.get(i);

			if(employee.getFirstSpell().equals("" +  (char)sectionIndex)){
				return i;
			}
		}
		return -2;
	}
	
	
	
	
	@Override
	public Object[] getSections() {

		return null;
	}


	@Override
	public int getSectionForPosition(int position) {

		return 0;
	}
	
}
