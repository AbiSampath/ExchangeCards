package com.abinaya.business;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class ListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> message;

	public ListAdapter(Context c, ArrayList<String> msgList) {
		this.context = c;
		this.message = msgList;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return message.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int pos, View child, ViewGroup parent) {
		Holder holder;
		LayoutInflater inflater;
		if (child == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			child = inflater.inflate(R.layout.activity_list_all, null);
			holder = new Holder();
			holder.txt_id = (TextView) child.findViewById(R.id.txt_id);

			child.setTag(holder);
		} else {
			holder = (Holder) child.getTag();
		}
		holder.txt_id.setText(message.get(pos));
		return child;
	}

	public class Holder {
		TextView txt_id;
	}

}
