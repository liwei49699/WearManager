package tohouse.jinmai.com.tohouse.content.firstpage.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.List;

import tohouse.jinmai.com.tohouse.R;
import tohouse.jinmai.com.tohouse.content.firstpage.model.CityChoose;

public class MyEmployeeAdapter extends BaseAdapter implements SectionIndexer{

	private Context context;
	private List<CityChoose.DataBean> dataBeanList;


	public MyEmployeeAdapter(Context context, List<CityChoose.DataBean> dataBeanList) {

		this.context = context;
		this.dataBeanList = dataBeanList;
	}


	@Override
	public int getCount() {

		return dataBeanList == null ? 0 : dataBeanList.size();
	}

	@Override
	public CityChoose.DataBean getItem(int position) {

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

		CityChoose.DataBean employee = getItem(position);
		vh.tvNameEmployee.setText(employee.getAREA_NAME());
		vh.tvIndex.setText(employee.getFirstSpell());

		
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
		for (int i = 0; i < dataBeanList.size(); i++) {
			CityChoose.DataBean employee =  dataBeanList.get(i);

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
