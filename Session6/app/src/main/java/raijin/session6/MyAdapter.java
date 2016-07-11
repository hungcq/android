package raijin.session6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
            TextView txtContactName = (TextView) view.findViewById(R.id.txt_name);
//            Button btnShow = (Button) view.findViewById(R.id.btn_show);
            txtContactName.setText(listContact.get(position));

//            btnShow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(view.getContext(),listContact.get(position),Toast.LENGTH_SHORT).show();
//                }
//            });
        }
        return view;
    }
}
