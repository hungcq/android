package com.online.foodplus.adapters;

/**
 * Created by 1918 on 10-Jan-17.
 */

public class CityListAdapter/* extends RecyclerView.Adapter<CityListAdapter.MyViewHolder>*/ {

/*    private Context context;
    private List<CityModel> cityModelList;

    public CityListAdapter(List<CityModel> cityModelList) {
        this.cityModelList = cityModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_tv_item, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityModel cityModel = cityModelList.get(myViewHolder.getAdapterPosition());
                ((SearchAdvancedActivity) context).setCityModel(cityModel);
//                cityModel.getId();
//                cityModel.getTen();
                ((AppCompatActivity) context).getSupportFragmentManager().popBackStack();
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(cityModelList.get(position).getTen());
    }

    @Override
    public int getItemCount() {
        return cityModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }*/
}
