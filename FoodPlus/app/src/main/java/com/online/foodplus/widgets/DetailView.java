package com.online.foodplus.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liulishuo.magicprogresswidget.MagicProgressBar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.online.foodplus.R;
import com.online.foodplus.activities.DetailActivity;
import com.online.foodplus.adapters.HorizontalThumbAdapter;
import com.online.foodplus.dialogs.MapDialog;
import com.online.foodplus.dialogs.ThumbDialog;
import com.online.foodplus.libraries.RecyclerItemClickListener;
import com.online.foodplus.models.Base;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Tutorial
 * - http://stackoverflow.com/questions/3312643/android-webview-utf-8-not-showing
 */

public class DetailView extends LinearLayout implements View.OnClickListener {
    private RoundedImageView imgFeature;
    private TextView tvTitle, tvDescription, tvRating;
    private Button bMoreContent;
    private WebView webContent;
    private MagicProgressBar pbStar1, pbStar2, pbStar3, pbStar4, pbStar5;
    private ImageView imgMap;
    private RecyclerView rvItemThumb;
    private MaterialRatingBar ratingBar;
    private Base base;
    private LinearLayout layoutDescription;
    private HorizontalThumbAdapter adapter;
    private ArrayList<String> thumbs;
    private int LIMIT_CONTENT_HEIGHT = 300;
    private boolean isExpaned = true;
    private Context context;

    public DetailView(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    public void setRate(int star1, int star2, int star3, int star4, int star5) {
        //System.out.println("--------" + star1 + " - " + star2 + " - " + star3 + " - " + star4 + " - " + star5);
        int total = star1 + star2 + star3 + star4 + star5;
        Float val1 = (float) star1 / total;
        Float val2 = (float) star2 / total;
        Float val3 = (float) star3 / total;
        Float val4 = (float) star4 / total;
        Float val5 = (float) star5 / total;
        Float defaultValue = 1 / 100f;
        if (total > 0) {
            Float avg = (float) star1 + (star2 * 2) + (star3 * 3) + (star4 * 4) + (star5 * 5) / total;
            tvRating.setText(String.format(Locale.getDefault(), "%.2f", avg));
        } else
            tvRating.setText("0");

        //Bar
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(pbStar1, "percent", 0, star1 < 1 ? defaultValue : val1),
                ObjectAnimator.ofFloat(pbStar2, "percent", 0, star2 < 1 ? defaultValue : val2),
                ObjectAnimator.ofFloat(pbStar3, "percent", 0, star3 < 1 ? defaultValue : val3),
                ObjectAnimator.ofFloat(pbStar4, "percent", 0, star4 < 1 ? defaultValue : val4),
                ObjectAnimator.ofFloat(pbStar5, "percent", 0, star5 < 1 ? defaultValue : val5)
        );
        set.setDuration(600);
        set.setInterpolator(new AccelerateInterpolator());
        set.start();
    }

    public void setData(final Base base) {
        if (base == null) return;
        this.base = base;

        if (base.getRating() != null)
            ratingBar.setRating(Float.valueOf(base.getRating()));

        if (base.getTitle() != null)
            tvTitle.setText(base.getTitle());
        tvTitle.setSelected(true);

        String description = base.getDescription();
        if (description != null && !description.equals("null")) {
            layoutDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(description);
        }

        if (base.getContent() != null && !base.getContent().equals("null"))
            //            webContent.loadData("<style>img{max-width:100%;}</style>" + base.getContent().replace("\r\n", "<br/>"), "text/html; charset=utf-8", "UTF-8");
            webContent.loadData("<style>img{max-width:100%;}</style>" + (base.getContent().replaceAll("(style=\".*?\")", "")).replace("\r\n", "<br/>"), "text/html; charset=utf-8", "UTF-8");

        if (base.getImages() != null && base.getImages().size() > 1)
            rvItemThumb.setVisibility(View.VISIBLE);

        if (base.getImages() != null && base.getImages().size() > 0)
            Picasso.with(imgFeature.getContext())
                    .load(base.getImages().get(0))
                    .into(imgFeature);

        if (base.getImages() != null) {
            thumbs.clear();
            thumbs.addAll(base.getImages());
            rvItemThumb.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //Truyền mảng và vị trí click vào Dialog
                    FragmentActivity fragmentActivity = (FragmentActivity) getContext();
                    FragmentManager fm = fragmentActivity.getSupportFragmentManager();
                    DialogFragment newFragment = ThumbDialog.newInstance(thumbs, position);
                    newFragment.show(fm, "dialogDetailItem");
                }
            }));
            adapter.notifyDataSetChanged();
        }


    }

    public DetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.custom_detail, this);
            //đọc tập tin xml
            imgFeature = (RoundedImageView) findViewById(R.id.imgFeature);
            webContent = (WebView) findViewById(R.id.webContent);


            bMoreContent = (Button) findViewById(R.id.bMoreContent);
            tvTitle = (TextView) findViewById(R.id.tvTitle);
            tvRating = (TextView) findViewById(R.id.tvRating);
            layoutDescription = (LinearLayout) findViewById(R.id.layoutDescription);
            tvDescription = (TextView) findViewById(R.id.tvDescription);
            imgMap = (ImageView) findViewById(R.id.imgMap);
            pbStar1 = (MagicProgressBar) findViewById(R.id.pbStar1);
            pbStar2 = (MagicProgressBar) findViewById(R.id.pbStar2);
            pbStar3 = (MagicProgressBar) findViewById(R.id.pbStar3);
            pbStar4 = (MagicProgressBar) findViewById(R.id.pbStar4);
            pbStar5 = (MagicProgressBar) findViewById(R.id.pbStar5);
            rvItemThumb = (RecyclerView) findViewById(R.id.rvItemThumb);
            ratingBar = (MaterialRatingBar) findViewById(R.id.ratingBar);

            imgFeature.setOnClickListener(this);

            initWebView();
            rvItemThumb.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            thumbs = new ArrayList<>();
            adapter = new HorizontalThumbAdapter(thumbs);
            rvItemThumb.setAdapter(adapter);

            if (attrs != null) {
                TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomDetail, 0, 0);
                try {
                    //Lấy trên XML
                    String textTitle = ta.getString(R.styleable.CustomDetail_title);
                    String textRating = ta.getString(R.styleable.CustomDetail_rating);
                    String textContent = ta.getString(R.styleable.CustomDetail_content);
                    String textDescription = ta.getString(R.styleable.CustomDetail_description);

                    //Thiết lập cho TextView trên CustomView hiện tại
                    tvTitle.setText(textTitle);
                    tvTitle.setSelected(true);
                    tvRating.setText(textRating);
                    //                    tvContent.setText(textContent);
                    if (textDescription != null && !textDescription.equals(""))
                        tvDescription.setText(textDescription);
                    tvDescription.setOnClickListener(this);
                    imgMap.setOnClickListener(this);
                    bMoreContent.setOnClickListener(this);
                    //lấy id của drawable từ xml
                    int resId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
                    //thiết lập cho ImageView trên CustomButton hiện tại
                    imgFeature.setImageResource(resId);
                } finally {
                    ta.recycle();
                }
            }

        }
    }

    private void initWebView() {
        LIMIT_CONTENT_HEIGHT = (int) getResources().getDimension(R.dimen._200sdp);
        webContent.getSettings().setDefaultTextEncodingName("utf-8");
        //        webContent.getSettings().setDefaultFontSize((int) getResources().getDimension(R.dimen._10sdp));
        webContent.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                ViewTreeObserver viewTreeObserver = webContent.getViewTreeObserver();

                viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        int height = webContent.getMeasuredHeight();
                        if (height != 0) {
                            //                            System.out.println("------------------------------REAL:" + height + " - LIMIT: " + LIMIT_CONTENT_HEIGHT);
                            if (height > LIMIT_CONTENT_HEIGHT) {
                                webContent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LIMIT_CONTENT_HEIGHT));
                                bMoreContent.setVisibility(View.VISIBLE);
                                isExpaned = false;
                            }

                            webContent.getViewTreeObserver().removeOnPreDrawListener(this);
                        }
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = ((DetailActivity) getContext()).getSupportFragmentManager();
        switch (v.getId()) {
            case R.id.imgFeature:
                if (base != null) {
                    //Truyền mảng và vị trí click vào Dialog
                    DialogFragment newFragment = ThumbDialog.newInstance(base.getImages(), 0);
                    newFragment.show(fm, "dialogDetailItem");
                }
                break;
            case R.id.imgMap:
            case R.id.tvDescription:
                if (base != null && (base.getLatitude() != 0 || base.getLongitude() != 0)) {
                    DialogFragment newFragment = MapDialog.newInstance(base.getTitle(), base.getDescription(), base.getLatitude(), base.getLongitude());
                    newFragment.show(fm, "dialogDetailMap");
                }
                break;
            case R.id.bMoreContent:
                if (isExpaned) {
                    webContent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LIMIT_CONTENT_HEIGHT));
                    bMoreContent.setText("Xem thêm");
                    isExpaned = false;
                } else {
                    webContent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    bMoreContent.setText("Thu gọn");
                    isExpaned = true;
                }
                break;
        }
    }
}
