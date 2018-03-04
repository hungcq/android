package com.online.foodplus.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.online.foodplus.R;
import com.online.foodplus.activities.DetailActivity;
import com.online.foodplus.models.Base;
import com.online.foodplus.utils.Tool;
import com.online.foodplus.widgets.ToastCustom;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by thanhthang on 28/11/2016.
 */

public class ReviewDialog extends DialogFragment implements MaterialRatingBar.OnRatingChangeListener, View.OnClickListener {
    private static String title, description, feature, id, cid, t, uid;
    private EditText etTitle, etContent;
    private MaterialRatingBar ratingBar;
    private ImageView imgSmile;
    private Button bSend, bCancel;
    private CircularImageView imgFeature;
    private TextView tvTitle, tvDescription;

    public ReviewDialog() {
        super();
    }

    public static ReviewDialog newInstance(String inputTitle, String inputDescription, String inputFeature, String inputId, String inputT, String inputCid, String inputUid) {
        id = inputId;
        t = inputT;
        cid = inputCid;
        title = inputTitle;
        description = inputDescription;
        feature = inputFeature;
        if (inputUid != null && inputUid.trim().length() > 0)
            uid = inputUid;
        return new ReviewDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        if (window != null) {
            dialog.setCanceledOnTouchOutside(true);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_review, container);
        init(view);
        listener();
        defaultValue();
        return view;
    }

    private void init(View view) {
        ratingBar = (MaterialRatingBar) view.findViewById(R.id.ratingBar);
        imgSmile = (ImageView) view.findViewById(R.id.imgSmile);
        bSend = (Button) view.findViewById(R.id.bSend);
        bCancel = (Button) view.findViewById(R.id.bCancel);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        imgFeature = (CircularImageView) view.findViewById(R.id.imgFeature);
        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etContent = (EditText) view.findViewById(R.id.etContent);
    }

    private void listener() {
        bSend.setOnClickListener(this);
        bCancel.setOnClickListener(this);
        ratingBar.setOnRatingChangeListener(this);
    }

    private void defaultValue() {
        int valueRatingBar = (int) ratingBar.getRating();
        changeSmile(valueRatingBar);

        tvTitle.setText(title);
        tvDescription.setText((description != null && !description.equals("null")) ? description : "");

        if (feature != null)
            Glide.with(imgFeature.getContext()).load(feature)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgFeature);
        //            Picasso.with(imgFeature.getContext())
        //                    .load(feature)
        //                    .resize(180, 180)
        //                    .centerCrop()
        //                    .into(imgFeature);
    }


    @Override
    public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
        changeSmile((int) rating);
    }

    private void changeSmile(int rating) {
        switch (rating) {
            case 1:
                imgSmile.setImageResource(R.drawable.ic_smile1);
                break;
            case 2:
                imgSmile.setImageResource(R.drawable.ic_smile2);
                break;
            case 3:
                imgSmile.setImageResource(R.drawable.ic_smile3);
                break;
            case 4:
                imgSmile.setImageResource(R.drawable.ic_smile4);
                break;
            case 5:
                imgSmile.setImageResource(R.drawable.ic_smile5);
                break;
            default:
                imgSmile.setImageResource(R.drawable.ic_smile_grey);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bCancel:
                dismiss();
                break;
            case R.id.bSend:
                send();
                break;
        }
    }

    private void send() {
        int star = (int) ratingBar.getRating();
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        if (title.length() == 0 || etContent.length() == 0) {
            ToastCustom.show(etTitle.getContext(), "Vui lòng diền đầy đủ vào các trường", ToastCustom.LENGTH_LONG, ToastCustom.WARNING);
            return;
        }


        RequestParams params = new RequestParams();
        params.put("opt", 2);
        params.put("star", star);
        params.put("cid", cid);
        params.put("id", id);
        params.put("uid", uid);
        params.put("title", title);
        params.put("con", content);
        //System.out.println("--------------------PARAM: " + params.toString());

        Tool.post(getResources().getString(R.string.api_votesrv), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //                        System.out.println("--------------------RESPONSE: " + response);
                try {
                    if (response.getInt("status") != 0) {
                        etTitle.setText("");
                        etContent.setText("");
                        ((DetailActivity) getActivity()).updateResult(new Base());
                        ToastCustom.show(etTitle.getContext(), getResources().getString(R.string.send_review_success), ToastCustom.LENGTH_LONG, ToastCustom.SUCCESS);
                        dismiss();
                    } else
                        ToastCustom.show(etTitle.getContext(), getResources().getString(R.string.please_retry_later), ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (isAdded())
                    ToastCustom.show(etTitle.getContext(), getResources().getString(R.string.please_retry_later) + " (error " + String.valueOf(statusCode) + ")", ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (isAdded())
                    ToastCustom.show(etTitle.getContext(), getResources().getString(R.string.please_retry_later) + " (error " + String.valueOf(statusCode) + ")", ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
            }
        });
    }
}
