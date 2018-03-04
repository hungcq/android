package com.online.foodplus.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.online.foodplus.Constants;
import com.online.foodplus.R;
import com.online.foodplus.adapters.CommentAdapter;
import com.online.foodplus.libraries.SpacesItemDecoration;
import com.online.foodplus.models.Comment;
import com.online.foodplus.utils.Tool;
import com.online.foodplus.widgets.ToastCustom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;

/**
 * Created by thanhthang on 07/02/2017.
 */

public class CommentDialog extends DialogFragment implements View.OnClickListener {
    private RecyclerView rvComments;
    private CommentAdapter adapter;
    private ArrayList<Comment> datas;
    private EditText etComment;
    private Button bSend, bPhotoAttach, bPhotoRemove;
    private ImageView imgPhotoAttach, imgClose;
    private RelativeLayout layoutPhotoAttach;
    private Uri photoAttachUri;
    private String username;
    private int p = 0;
    private LinearLayoutManager layoutManager;
    private static String title, description, feature, id, t, cid, uid;
    private boolean isSending = false;
    private boolean isMoreDataAvailable = true;

    public static CommentDialog newInstance(String inputTitle, String inputDescription, String inputFeature, String inputId, String inputT, String inputCid, String inputUid) {
        title = inputTitle;
        description = inputDescription;
        feature = inputFeature;
        t = inputT;
        cid = inputCid;
        id = inputId;
        uid = inputUid;
        return new CommentDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_comment, container);
        init(view);
        listener();
        initData(view);
        loadComment();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        if (getDialog().getWindow() != null)
            getDialog().getWindow()
                    .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        if (window != null) {
            dialog.setCanceledOnTouchOutside(true);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    private void init(View view) {
        rvComments = (RecyclerView) view.findViewById(R.id.rvComments);
        etComment = (EditText) view.findViewById(R.id.etComment);
        bSend = (Button) view.findViewById(R.id.bSend);
        imgClose = (ImageView) view.findViewById(R.id.imgClose);
        layoutPhotoAttach = (RelativeLayout) view.findViewById(R.id.layoutPhotoAttach);
        bPhotoAttach = (Button) view.findViewById(R.id.bPhotoAttach);
        bPhotoRemove = (Button) view.findViewById(R.id.bPhotoRemove);
        imgPhotoAttach = (ImageView) view.findViewById(R.id.imgPhotoAttach);
    }

    private void listener() {
        bSend.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        bPhotoAttach.setOnClickListener(this);
        bPhotoRemove.setOnClickListener(this);
    }

    private void initData(View view) {
        ImageView imgFeature = (ImageView) view.findViewById(R.id.imgFeature);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        tvTitle.setText(title);
        if (description != null && !description.equals("null"))
            tvDescription.setText(description);
        if (feature != null && feature.length() > 0)
            Glide.with(getActivity()).load(feature)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgFeature);
        //            Picasso.with(getActivity())
        //                    .load(feature)
        //                    .resize(180, 180)
        //                    .centerCrop()
        //                    .into(imgFeature);

        username = Tool.getUsername(getActivity());
        datas = new ArrayList<>();
        adapter = new CommentAdapter(datas);
        layoutManager = new LinearLayoutManager(getActivity());
        rvComments.setLayoutManager(layoutManager);
        rvComments.setAdapter(adapter);
        rvComments.addItemDecoration(new SpacesItemDecoration((int) getResources().getDimension(R.dimen._5sdp)));

        rvComments.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //check for scroll down
                if (isMoreDataAvailable)
                    if (!isSending)
                        if (dy > 0)
                            if ((layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition()) >= layoutManager.getItemCount())
                                loadComment();                                //Do pagination.. i.e. fetch new data
            }
        });
    }

    private void loadComment() {
        if (!isSending && isMoreDataAvailable) {
            isSending = true;
            p++;
            RequestParams params = new RequestParams();
            params.put("opt", 0);
            params.put("cid", cid);
            params.put("p", p);
            params.put("id", id);
            // System.out.println("--------------------COMMENT LOAD PARAM: " + getResources().getString(R.string.api_comsrv) + "?" + params.toString());

            Tool.get(getResources().getString(R.string.api_comsrv), params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject resp) {
                    //System.out.println("--------------------COMMENT LOAD RESULT: " + resp);
                    if (isAdded() && resp.has("comment")) {
                        try {
                            JSONArray response = resp.getJSONArray("comment");

                            int length = response.length();
                            if (length > 0) {
                                Comment comment;

                                for (int i = 0; i < length; i++) {
                                    comment = new Comment();
                                    comment.setAvatar(Tool.getStringJson("avatar", response.getJSONObject(i)));
                                    comment.setUsername(Tool.getStringJson("username", response.getJSONObject(i)));
                                    comment.setComment_id(Tool.getStringJson("userid", response.getJSONObject(i)));
                                    comment.setContent(Tool.getStringJson("content", response.getJSONObject(i)));
                                    String photo = Tool.getStringJson("comment_photo", response.getJSONObject(i));
                                    if (photo != null && !photo.equals("null"))
                                        comment.setComment_photo(photo);
                                    datas.add(comment);
                                }
                                adapter.notifyDataSetChanged();

                            } else
                                isMoreDataAvailable = false;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    if (isAdded())
                        ToastCustom.show(getActivity(), "Lỗi...", ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
                }

                @Override
                public void onFinish() {
                    isSending = false;
                }
            });
        }
    }


    private void sendComment() {
        final String content = etComment.getText().toString();

        if (content.trim().length() == 0 && photoAttachUri == null) {
            ToastCustom.show(getActivity(), "Không để trống nội dung", ToastCustom.LENGTH_SHORT, ToastCustom.WARNING);
            return;
        }


        if (!isSending) {
            isSending = true;
            RequestParams params = new RequestParams();
            params.put("t", t);
            params.put("cid", cid);
            params.put("id", id);
            //params.put("content", content);
            params.put("uid", uid);
            params.put("c", content);

            if (photoAttachUri != null) {
                try {
                    File myFile = new File(Tool.getRealPathFromURI(getActivity(), photoAttachUri));
                    params.put("photo", myFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            //System.out.println("--------------------COMMENT SEND PARAMS: " + getResources().getString(R.string.api_votesrv) + "?" + params.toString());

            Tool.post(getResources().getString(R.string.api_votesrv), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //  System.out.println("--------------------COMMENT SEND RESULT: " + response);

                    try {
                        if (response.has("status") && response.getString("status").equals("1")) {

                            //add new comment
                            Comment comment = new Comment();
                            comment.setContent(content);
                            comment.setUsername(username);
                            if (photoAttachUri != null)
                                comment.setComment_photo(photoAttachUri.toString());
                            datas.add(0, comment);      // insert at first data list
                            hiddenKeyboard();           //if Keyboard Shown, hidden id
                            adapter.notifyDataSetChanged();
                            layoutManager.scrollToPositionWithOffset(0, 0);
                            etComment.setText("");
                            photoAttachUri = null;
                            imgPhotoAttach.setImageBitmap(null);
                            bPhotoAttach.setBackgroundResource(R.drawable.ic_picture_grey);
                            layoutPhotoAttach.setVisibility(View.GONE);

                        } else
                            ToastCustom.show(getActivity(), "Lỗi gửi bình luận", ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinish() {
                    isSending = false;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    ToastCustom.show(getActivity(), "Lỗi gửi bình luận", ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgClose:
                dismiss();
                break;
            case R.id.bSend:
                sendComment();
                break;
            case R.id.bPhotoAttach:
                if (photoAttachUri == null) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    getActivity().startActivityForResult(photoPickerIntent, Constants.STATUS_CODE_DIALOG_COMMENT);
                }
                break;
            case R.id.bPhotoRemove:
                layoutPhotoAttach.setVisibility(View.GONE);
                bPhotoAttach.setBackgroundResource(R.drawable.ic_picture_grey);
                photoAttachUri = null;
                break;
        }
    }

    private void hiddenKeyboard() {
        // Check if no view has focus:
        if (getView() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.STATUS_CODE_DIALOG_COMMENT) {
            if (resultCode == RESULT_OK) {
                try {
                    photoAttachUri = data.getData();
                    InputStream imageStream = getActivity().getContentResolver().openInputStream(photoAttachUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imgPhotoAttach.setImageBitmap(selectedImage);
                    bPhotoAttach.setBackgroundResource(R.drawable.ic_picture_grey_light);
                    layoutPhotoAttach.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
