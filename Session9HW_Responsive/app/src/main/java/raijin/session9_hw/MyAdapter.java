package raijin.session9_hw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 1918 on 10-Jul-16.
 */
public class MyAdapter extends BaseAdapter {

    private List<PersonInfo> listEmployee;
    private Context context;
    private LayoutInflater layoutInflater;

    public MyAdapter(List<PersonInfo> listEmployee, Context context) {
        this.listEmployee = listEmployee;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return listEmployee.size();
    }

    @Override
    public Object getItem(int position) {
        return listEmployee.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.item_on_list,null);
        if(view != null) {
            TextView nameText = (TextView) view.findViewById(R.id.name_text);
            TextView ageText = (TextView) view.findViewById(R.id.age_text);
            TextView phoneText = (TextView) view.findViewById(R.id.phone_text);
            nameText.setText(listEmployee.get(position).getName());
            ageText.setText(listEmployee.get(position).getAge());
            phoneText.setText(listEmployee.get(position).getPhone());
        }
        return view;
    }
}
