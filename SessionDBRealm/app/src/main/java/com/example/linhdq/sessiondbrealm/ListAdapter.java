package com.example.linhdq.sessiondbrealm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by linhdq on 8/14/16.
 */
public class ListAdapter extends BaseAdapter {
    private List<Student> list;
    private Context context;
    private LayoutInflater inflater;

    public ListAdapter(List<Student> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View layoutItem = inflater.inflate(R.layout.item_on_list, null);
        Student student = list.get(i);
        if (layoutItem != null && student != null) {

            TextView txtName = (TextView) layoutItem.findViewById(R.id.txt_name);
            TextView txtAge = (TextView) layoutItem.findViewById(R.id.txt_age);

            txtName.setText(student.getName());
            txtAge.setText(String.valueOf(student.getAge()));
        }
        return layoutItem;
    }
}
