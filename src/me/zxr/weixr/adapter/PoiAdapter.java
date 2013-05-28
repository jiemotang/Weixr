package me.zxr.weixr.adapter;

import java.util.List;

import me.zxr.weixr.R;
import me.zxr.weixr.bean.PoiBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PoiAdapter extends BaseAdapter{
	private Context context;
	private List<PoiBean> data;
	private LayoutInflater mInflater;
	
	public PoiAdapter(Context context, List<PoiBean> data) {
		super();
		this.context = context;
		this.data = data;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = mInflater.inflate(R.layout.poi_item, null);
		TextView tv_poi_title = (TextView) view.findViewById(R.id.tv_poi_title);
		TextView tv_poi_address = (TextView) view.findViewById(R.id.tv_poi_address);
		
		PoiBean pb = data.get(position);
		
		tv_poi_title.setText(pb.title);
		tv_poi_address.setText(pb.address);
		
		return view;
	}

}
