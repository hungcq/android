package raijin.session6_hw;

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

    private List<String> listContact;
    private Context context;
    private LayoutInflater layoutInflater;

    public MyAdapter(List<String> listContact, Context context) {
        this.listContact = listContact;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return listContact.size();
    }

    @Override
    public Object getItem(int position) {
        return listContact.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.item_on_list,null);
        if(view != null) {
            TextView txtContactName = (TextView) view.findViewById(R.id.name_text);
            txtContactName.setText(listContact.get(position));
        }
        return view;
    }
}
