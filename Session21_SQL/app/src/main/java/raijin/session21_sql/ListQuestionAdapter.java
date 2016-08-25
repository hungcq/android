package raijin.session21_sql;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1918 on 23-Aug-16.
 */
public class ListQuestionAdapter extends RecyclerView.Adapter<ListQuestionAdapter.ItemHolder> {
    private List<MathWord> mathWordList;
    private Context context;
    private LayoutInflater inflater;

    public ListQuestionAdapter(List<MathWord> mathWordList, Context context) {
        this.mathWordList = mathWordList;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_on_list, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        MathWord model = mathWordList.get(position);
        if (model != null) {
            holder.contentText.setText(model.getQuestionContent());
            for (int i = 0; i < holder.textViewList.size(); i++) {
                holder.textViewList.get(i).setText(model.getListAnswer().get(i));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mathWordList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView contentText;
        private List<TextView> textViewList;

        public ItemHolder(View itemView) {
            super(itemView);
            contentText = (TextView) itemView.findViewById(R.id.text_content);
            textViewList=new ArrayList<>();
            textViewList.add((TextView) itemView.findViewById(R.id.text_01));
            textViewList.add((TextView) itemView.findViewById(R.id.text_02));
            textViewList.add((TextView) itemView.findViewById(R.id.text_03));
            textViewList.add((TextView) itemView.findViewById(R.id.text_04));
        }
    }
}
