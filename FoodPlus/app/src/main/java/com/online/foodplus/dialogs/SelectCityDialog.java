package com.online.foodplus.dialogs;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.online.foodplus.R;
import com.online.foodplus.activities.FindActivity;
import com.online.foodplus.activities.SearchAdvancedActivity;
import com.online.foodplus.libraries.RecyclerItemClickListener;
import com.online.foodplus.libraries.SpacesItemDecoration;
import com.online.foodplus.models.Base;
import com.online.foodplus.utils.StringUtil;
import com.online.foodplus.utils.Tool;
import com.online.foodplus.widgets.ToastCustom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by thanhthang on 10/02/2017.
 */

public class SelectCityDialog extends DialogFragment {
    private ArrayList<Base> cities;
    private ArrayList<Base> filterCities;
    private RecyclerView rvCity;
    private SelectCityAdapter adapter;
    private EditText etSearch;
    private static String id;
    private Button bOK;

    public SelectCityDialog() {

    }

    public static SelectCityDialog newInstance(String inputId) {
        id = inputId;
        SelectCityDialog selectCityDialog = new SelectCityDialog();
        selectCityDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return selectCityDialog;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog().getWindow() != null) {
            getDialog().setCanceledOnTouchOutside(true);
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            Double d = (screenWidth * 1.3);
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, d.intValue());//dialog height = screen width
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_city, container);
        init(view);
        listener();
        load();
        return view;
    }

    private void init(View view) {
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        rvCity = (RecyclerView) view.findViewById(R.id.rvCity);
        bOK = (Button) view.findViewById(R.id.bOK);
        cities = new ArrayList<>();
        filterCities = new ArrayList<>();

        rvCity.setHasFixedSize(true);
        rvCity.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCity.addItemDecoration(new SpacesItemDecoration((int) getResources().getDimension(R.dimen._13sdp)));
        adapter = new SelectCityAdapter(filterCities);
        rvCity.setAdapter(adapter);
    }

    private void listener() {
        bOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rvCity.addOnItemTouchListener(new RecyclerItemClickListener(rvCity.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String selectId = filterCities.get(position).getId();
                String selectTitle = filterCities.get(position).getTitle();
                if (getActivity() instanceof FindActivity) {
                    FindActivity activity = (FindActivity) getActivity();
                    Base base = new Base();
                    base.setTitle(selectTitle);
                    base.setId(selectId);
                    activity.updateResult(base);
                    ToastCustom.show(getActivity(), "Bạn đã chọn: " + selectTitle, ToastCustom.LENGTH_SHORT, ToastCustom.SUCCESS);
                } else if (getActivity() instanceof SearchAdvancedActivity) {
                    SearchAdvancedActivity activity = (SearchAdvancedActivity) getActivity();
                    Base base = new Base();
                    base.setId(selectId);
                    base.setTitle(selectTitle);
                    activity.updateResult(base);
                    ToastCustom.show(getActivity(), "Bạn đã chọn: " + selectTitle, ToastCustom.LENGTH_SHORT, ToastCustom.SUCCESS);
                }
                dismiss();
            }
        }));
    }

    private void load() {
        Tool.get(getResources().getString(R.string.api_list_city), null, new JsonHttpResponseHandler() {
            private ProgressDialog progress;

            @Override
            public void onStart() {
                progress = new ProgressDialog(getActivity());
                progress.setMessage(getResources().getString(R.string.please_wait));
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String title = Tool.getStringJson("ten", object);
                        String id = Tool.getStringJson("id", object);
                        String description = Tool.getStringJson("zipcode", object);
                        String status = Tool.getStringJson("trangthai", object);
                        Base base = new Base();
                        base.setTitle(title);
                        base.setDescription(description);
                        base.setId(id);
                        base.setStatus(status);
                        cities.add(base);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                filterCities.addAll(cities);
                adapter.notifyDataChanged();    //Cập nhật Recycleview

                //Scrool tới vị trí Thành phố hiện tại
                if (id != null && !id.equals("")) {
                    int size = filterCities.size();
                    for (int i = 0; i < size; i++) {
                        if (filterCities.get(i).getId().trim().equals(id)) {
                            rvCity.getLayoutManager().scrollToPosition((i - 1 >= 0) ? (i - 1) : i);
                            break;
                        }
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (isAdded())
                    ToastCustom.show(getActivity(), getResources().getString(R.string.please_retry_later), ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
            }

            @Override
            public void onFinish() {
                if (progress.isShowing())
                    progress.dismiss();
            }
        });
    }

    public class SelectCityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
        private ArrayList<Base> mValues;
        private CustomFilter mFilter;
        String searchString = "";

        SelectCityAdapter(ArrayList<Base> items) {
            this.mValues = items;
            this.mFilter = new SelectCityAdapter.CustomFilter(SelectCityAdapter.this);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_city, parent, false);
            return new SelectCityAdapter.CityViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((SelectCityAdapter.CityViewHolder) holder).bindData(mValues.get(position));
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }


        @Override
        public Filter getFilter() {
            return mFilter;
        }

        private class CityViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTitle, tvDescription;

            CityViewHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
                tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            }

            void bindData(Base base) {
                if (base.getTitle() != null)
                    setHightLightResult(tvTitle, base.getTitle(), searchString);
                if (base.getId() != null)
                    tvDescription.setText(base.getId());
                if (id != null && base.getId().equals(id)) {
                    tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
                    tvTitle.setTextColor(ContextCompat.getColor(tvTitle.getContext(), R.color.restaurantIconColor));
                } else {
                    tvTitle.setTypeface(Typeface.DEFAULT);
                    tvTitle.setTextColor(ContextCompat.getColor(tvTitle.getContext(), R.color.divider));
                }

            }
        }

        private void setHightLightResult(TextView textView, String valueString, String searchString) {
            String value = StringUtil.bodau(valueString.toLowerCase());
            if (value.contains(searchString.toLowerCase())) {
                int startPos = value.indexOf(searchString);
                int endPos = startPos + searchString.length();
                Spannable spanText = Spannable.Factory.getInstance().newSpannable(valueString);
                spanText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(textView.getContext(), R.color.colorMain)), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(spanText, TextView.BufferType.SPANNABLE);
            } else
                textView.setText(valueString);

        }

        void notifyDataChanged() {
            notifyDataSetChanged();
        }

        private class CustomFilter extends Filter {
            private SelectCityAdapter mAdapter;

            private CustomFilter(SelectCityAdapter mAdapter) {
                super();
                this.mAdapter = mAdapter;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                searchString = StringUtil.bodau(constraint.toString().toLowerCase().trim());

                filterCities.clear();
                final FilterResults results = new FilterResults();
                if (constraint.length() == 0) {
                    filterCities.addAll(cities);
                } else {
                    String filterPattern = searchString;
                    for (Base city : cities) {
                        if (StringUtil.bodau(city.getTitle().toLowerCase()).contains(filterPattern)) {
                            filterCities.add(city);
                        }
                    }
                }
                results.values = filterCities;
                results.count = filterCities.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }
}
