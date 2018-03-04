package com.online.foodplus.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.online.foodplus.Constants;
import com.online.foodplus.R;
import com.online.foodplus.activities.CategoryActivity;
import com.online.foodplus.activities.DetailActivity;
import com.online.foodplus.libraries.GridSpacingItemDecoration;
import com.online.foodplus.libraries.RecyclerItemClickListener;
import com.online.foodplus.models.Base;
import com.online.foodplus.models.Display;
import com.online.foodplus.utils.Tool;

import java.util.ArrayList;

/**
 * Created by thanhthang on 14/02/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Display> displays;
    private Context context;
    private static final int LOAD = 0;
    private static final int GALLERY = 1;
    private static final int BIG = 2;
    private static final int GRID_SIMPLE = 3;
    private static final int LIST = 4;
    private static final int GRID = 5;
    private static final int LIST_SQUARE = 6;
    private static final int LIST_SQUARE_SINGLE = 7;
    private static final int LIST_ROUND = 8;
    private static final int TITLE_BOX = 9;
    private static final int FOOTER = 10;

    public CategoryAdapter(Context context, ArrayList<Display> displays) {
        this.displays = displays;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (displays.get(position) == null)
            return LOAD;

        switch (displays.get(position).getType()) {
            case "gallery":
                return GALLERY;
            case "grid":
                return GRID;
            case "list":
                return LIST;
            case "grid_simple":
                return GRID_SIMPLE;
            case "big":
                return BIG;
            case "list_square_single":
                return LIST_SQUARE_SINGLE;
            case "list_square":
                return LIST_SQUARE;
            case "list_round":
                return LIST_ROUND;
            case "titlebox":
                return TITLE_BOX;
            case "footer":
                return FOOTER;
        }
        return LOAD;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case GALLERY:
                return new GalleryViewHolder(context, inflater.inflate(R.layout.custom_gallery, parent, false));
            case GRID:
                return new GridViewHolder(context, inflater.inflate(R.layout.holder_content, parent, false));
            case LIST:
                return new ListViewHolder(context, inflater.inflate(R.layout.holder_content_edge, parent, false));
            case GRID_SIMPLE:
                return new GridSimpleViewHolder(context, inflater.inflate(R.layout.holder_content_edge, parent, false));
            case BIG:
                return new BigViewHolder(context, inflater.inflate(R.layout.holder_content, parent, false));
            case LIST_SQUARE_SINGLE:
                return new ListSquareViewHolder(context, inflater.inflate(R.layout.holder_content_edge, parent, false), true);
            case LIST_SQUARE:
                return new ListSquareViewHolder(context, inflater.inflate(R.layout.holder_content, parent, false), false);
            case LIST_ROUND:
                return new ListRoundViewHolder(context, inflater.inflate(R.layout.holder_content_edge, parent, false));
            case TITLE_BOX:
                return new TitleBoxViewHolder(context, inflater.inflate(R.layout.custom_titlebox, parent, false));
            case FOOTER:
                return new FooterViewHolder(context, inflater.inflate(R.layout.row_footer, parent, false));
        }
        return new LoadingViewHolder(context, inflater.inflate(R.layout.custom_loadingview, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case GALLERY:
                if (displays.get(position) != null)
                    ((GalleryViewHolder) holder).bindData(displays.get(position));
                break;
            case GRID:
                if (displays.get(position) != null)
                    ((GridViewHolder) holder).bindData(displays.get(position));
                break;
            case LIST:
                if (displays.get(position) != null)
                    ((ListViewHolder) holder).bindData(displays.get(position));
                break;
            case GRID_SIMPLE:
                if (displays.get(position) != null)
                    ((GridSimpleViewHolder) holder).bindData(displays.get(position));
                break;
            case BIG:
                if (displays.get(position) != null)
                    ((BigViewHolder) holder).bindData(displays.get(position));
                break;
            case LIST_SQUARE_SINGLE:
                if (displays.get(position) != null)
                    ((ListSquareViewHolder) holder).bindData(displays.get(position));
                break;
            case LIST_SQUARE:
                if (displays.get(position) != null)
                    ((ListSquareViewHolder) holder).bindData(displays.get(position));
                break;
            case LIST_ROUND:
                if (displays.get(position) != null)
                    ((ListRoundViewHolder) holder).bindData(displays.get(position));
                break;
            case TITLE_BOX:
                ((TitleBoxViewHolder) holder).bindData(displays.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return displays.size();
    }

    /*----------------------------------------------------------------------------------------------
    *
    * VIEWHOLDER
    *
    * ---------------------------------------------------------------------------------------------*/
    private static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        LoadingViewHolder(Context context, View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    private static class GridViewHolder extends RecyclerView.ViewHolder {
        private GridAdapter adapter;
        private RecyclerView rvContent;
        private ArrayList<Base> datas;

        private GridViewHolder(final Context context, View itemView) {
            super(itemView);
            rvContent = (RecyclerView) itemView.findViewById(R.id.rvContent);
            rvContent.setLayoutManager(new GridLayoutManager(context, 2));
            rvContent.setNestedScrollingEnabled(false);
            datas = new ArrayList<>();
            adapter = new GridAdapter(datas);
            rvContent.setAdapter(adapter);
            rvContent.addItemDecoration(new GridSpacingItemDecoration(2, (int) context.getResources().getDimension(R.dimen._2sdp), false));
            rvContent.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("id", datas.get(position).getId());
                    intent.putExtra("t", datas.get(position).getTid());
                    intent.putExtra("cid", datas.get(position).getCid());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //prevent Exception crash when startActivity from custom view
                    context.startActivity(intent);

                    //Animation if Activity
                    if (context instanceof Activity)
                        ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                }
            }));
        }

        void bindData(final Display display) {
            if (display.getData() != null) {
                datas.clear();
                datas.addAll(display.getData());
                adapter.notifyDataSetChanged();
            }
        }
    }

    private static class ListViewHolder extends RecyclerView.ViewHolder {
        private ListNormalAdapter adapter;
        private RecyclerView rvContent;
        private ArrayList<Base> datas;

        private ListViewHolder(final Context context, View itemView) {
            super(itemView);
            rvContent = (RecyclerView) itemView.findViewById(R.id.rvContent);
            datas = new ArrayList<>();
            adapter = new ListNormalAdapter(datas);
            rvContent.setNestedScrollingEnabled(false);
            rvContent.setAdapter(adapter);
            rvContent.setLayoutManager(new LinearLayoutManager(context));
            rvContent.addItemDecoration(new GridSpacingItemDecoration(1, (int) context.getResources().getDimension(R.dimen._3sdp), false));
            rvContent.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("id", datas.get(position).getId());
                    intent.putExtra("t", datas.get(position).getTid());
                    intent.putExtra("cid", datas.get(position).getCid());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //prevent Exception crash when startActivity from custom view
                    context.startActivity(intent);

                    //Animation if Activity
                    if (context instanceof Activity)
                        ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }));
        }

        void bindData(Display display) {
            if (display.getData() != null) {
                datas.clear();
                datas.addAll(display.getData());
                adapter.notifyDataSetChanged();
            }
        }
    }

    private static class GridSimpleViewHolder extends RecyclerView.ViewHolder {
        private GridSimpleAdapter adapter;
        private RecyclerView rvContent;
        private ArrayList<Base> datas;

        private GridSimpleViewHolder(final Context context, View itemView) {
            super(itemView);
            rvContent = (RecyclerView) itemView.findViewById(R.id.rvContent);
            datas = new ArrayList<>();
            adapter = new GridSimpleAdapter(datas);
            rvContent.setNestedScrollingEnabled(false);
            rvContent.setAdapter(adapter);
            rvContent.setLayoutManager(new GridLayoutManager(context, 3));
            rvContent.addItemDecoration(new GridSpacingItemDecoration(3, (int) context.getResources().getDimension(R.dimen._3sdp), false));
            rvContent.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("id", datas.get(position).getId());
                    intent.putExtra("t", datas.get(position).getTid());
                    intent.putExtra("cid", datas.get(position).getCid());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //prevent Exception crash when startActivity from custom view
                    context.startActivity(intent);

                    //Animation if Activity
                    if (context instanceof Activity)
                        ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }));
        }

        void bindData(final Display display) {
            if (display.getData() != null) {
                datas.clear();
                datas.addAll(display.getData());
                adapter.notifyDataSetChanged();
            }
        }
    }

    private static class BigViewHolder extends RecyclerView.ViewHolder {
        private BigViewAdapter adapter;
        private RecyclerView rvContent;
        private ArrayList<Base> datas;

        private BigViewHolder(final Context context, View itemView) {
            super(itemView);
            rvContent = (RecyclerView) itemView.findViewById(R.id.rvContent);
            rvContent.setNestedScrollingEnabled(false);
            rvContent.setLayoutManager(new LinearLayoutManager(context));
            datas = new ArrayList<>();
            adapter = new BigViewAdapter(datas);
            rvContent.setAdapter(adapter);
            //            rvContent.addItemDecoration(new GridSpacingItemDecoration(1, (int) context.getResources().getDimension(R.dimen._3sdp), false));
            rvContent.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("id", datas.get(position).getId());
                    intent.putExtra("t", datas.get(position).getTid());
                    intent.putExtra("cid", datas.get(position).getCid());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //prevent Exception crash when startActivity from custom view
                    context.startActivity(intent);

                    //Animation if Activity
                    if (context instanceof Activity)
                        ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }));
        }

        void bindData(final Display display) {
            if (display.getData() != null) {
                datas.clear();
                datas.addAll(display.getData());
                adapter.notifyDataSetChanged();
            }
        }
    }

    private static class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgFeature, img1, img2, img3, img4;
        private TextView tvTitle, tvRating, tvDescription, tvStar, tvFavourite, tvComment, tvPin;
        private TextView tvDesc1, tvDesc2, tvDesc3, tvDesc4;
        private Context context;
        private ArrayList<Base> datas;

        private GalleryViewHolder(Context context, View itemView) {
            super(itemView);
            imgFeature = (ImageView) itemView.findViewById(R.id.imgFeature);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvRating = (TextView) itemView.findViewById(R.id.tvRating);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvStar = (TextView) itemView.findViewById(R.id.tvStar);
            tvComment = (TextView) itemView.findViewById(R.id.tvComment);
            tvPin = (TextView) itemView.findViewById(R.id.tvPin);
            tvFavourite = (TextView) itemView.findViewById(R.id.tvFavourite);
            img1 = (ImageView) itemView.findViewById(R.id.img1);
            img2 = (ImageView) itemView.findViewById(R.id.img2);
            img3 = (ImageView) itemView.findViewById(R.id.img3);
            img4 = (ImageView) itemView.findViewById(R.id.img4);
            tvDesc1 = (TextView) itemView.findViewById(R.id.tvDesc1);
            tvDesc2 = (TextView) itemView.findViewById(R.id.tvDesc2);
            tvDesc3 = (TextView) itemView.findViewById(R.id.tvDesc3);
            tvDesc4 = (TextView) itemView.findViewById(R.id.tvDesc4);

            img1.setOnClickListener(this);
            img2.setOnClickListener(this);
            img3.setOnClickListener(this);
            img4.setOnClickListener(this);
            imgFeature.setOnClickListener(this);

            this.context = context;

        }

        void bindData(final Display display) {
            if (display.getData() == null) return;
            this.datas = display.getData();
            int size = datas.size();
            if (size == 0)
                return;

            if (datas.get(0).getTitle() != null)
                tvTitle.setText(datas.get(0).getTitle());
            if (datas.get(0).getRating() != null)
                tvRating.setText(datas.get(0).getRating());
            String description = datas.get(0).getDescription();
            if (description != null && !description.equals("null"))
                tvDescription.setText(description);
            else
                tvDescription.setVisibility(View.GONE);
            if (datas.get(0).getStar() != null)
                tvStar.setText(datas.get(0).getStar());
            if (datas.get(0).getFavourite() != null)
                tvFavourite.setText(datas.get(0).getFavourite());
            if (datas.get(0).getComment() != null)
                tvComment.setText(datas.get(0).getComment());
            if (datas.get(0).getPin() != null)
                tvPin.setText(datas.get(0).getPin());


            if (size >= 1 && datas.get(0) != null)
                if (datas.get(0).getImages().get(0) != null)
                    Glide.with(imgFeature.getContext()).load(datas.get(0).getImages().get(0))
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imgFeature);
            //                    with(context)
            //                            .load(datas.get(0).getImages().get(0))
            //                            .into(imgFeature);


            if (size >= 2 && datas.get(1) != null) {
                if (datas.get(1).getImages().get(0) != null)
                    Glide.with(imgFeature.getContext()).load(Tool.findImageUrl(datas.get(1).getImages().get(0), Constants.IMAGE_SMALL))
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(img1);
                //                    with(context)
                //                            .load(datas.get(1).getImages().get(0))
                //                            .resize(200, 200)
                //                            .centerCrop()
                //                            .into(img1);
                if (datas.get(1).getTitle() != null && !datas.get(1).getTitle().equals("null")) {
                    tvDesc1.setText(datas.get(1).getTitle());
                    tvDesc1.setVisibility(View.VISIBLE);
                }
            }


            if (size >= 3 && datas.get(2) != null) {
                if (datas.get(2).getImages().get(0) != null)
                    Glide.with(imgFeature.getContext()).load(Tool.findImageUrl(datas.get(2).getImages().get(0), Constants.IMAGE_SMALL))
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(img2);
                //                    with(context)
                //                            .load(datas.get(2).getImages().get(0))
                //                            .resize(200, 200)
                //                            .centerCrop()
                //                            .into(img2);
                if (datas.get(2).getTitle() != null && !datas.get(2).getTitle().equals("null")) {
                    tvDesc2.setText(datas.get(2).getTitle());
                    tvDesc2.setVisibility(View.VISIBLE);
                }
            }
            if (size >= 4 && datas.get(3) != null) {
                if (datas.get(3).getImages().get(0) != null)
                    Glide.with(imgFeature.getContext()).load(Tool.findImageUrl(datas.get(3).getImages().get(0), Constants.IMAGE_SMALL))
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(img3);
                //                    with(context)
                //                            .load(datas.get(3).getImages().get(0))
                //                            .resize(200, 200)
                //                            .centerCrop()
                //                            .into(img3);
                if (datas.get(3).getTitle() != null && !datas.get(3).getTitle().equals("null")) {
                    tvDesc3.setText(datas.get(3).getTitle());
                    tvDesc3.setVisibility(View.VISIBLE);
                }
            }

            if (size >= 5 && datas.get(4) != null) {
                if (datas.get(4).getImages().get(0) != null)
                    Glide.with(imgFeature.getContext()).load(Tool.findImageUrl(datas.get(4).getImages().get(0), Constants.IMAGE_SMALL))
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(img4);
                //                    with(context)
                //                            .load(datas.get(4).getImages().get(0))
                //                            .resize(200, 200)
                //                            .centerCrop()
                //                            .into(img4);
                if (datas.get(4).getTitle() != null && !datas.get(4).getTitle().equals("null")) {
                    tvDesc4.setText(datas.get(4).getTitle());
                    tvDesc4.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onClick(View view) {
            String id = null;
            String cid = null;
            String t = null;
            int size = datas.size();
            switch (view.getId()) {
                case R.id.imgFeature:
                    if (size >= 1 && datas.get(0) != null) {
                        id = datas.get(0) != null ? datas.get(0).getId() : null;
                        cid = datas.get(0) != null ? datas.get(0).getCid() : null;
                        t = datas.get(0) != null ? datas.get(0).getTid() : null;
                    }
                    break;
                case R.id.img1:
                    if (size >= 2 && datas.get(1) != null) {
                        id = datas.get(1) != null ? datas.get(1).getId() : null;
                        cid = datas.get(1) != null ? datas.get(1).getCid() : null;
                        t = datas.get(1) != null ? datas.get(1).getTid() : null;
                    }
                    break;
                case R.id.img2:
                    if (size >= 3 && datas.get(2) != null) {
                        id = datas.get(2) != null ? datas.get(2).getId() : null;
                        cid = datas.get(2) != null ? datas.get(2).getCid() : null;
                        t = datas.get(2) != null ? datas.get(2).getTid() : null;
                    }
                    break;
                case R.id.img3:
                    if (size >= 4 && datas.get(3) != null) {
                        id = datas.get(3) != null ? datas.get(3).getId() : null;
                        cid = datas.get(3) != null ? datas.get(3).getCid() : null;
                        t = datas.get(3) != null ? datas.get(3).getTid() : null;
                    }
                    break;
                case R.id.img4:
                    if (size >= 5 && datas.get(4) != null) {
                        id = datas.get(4) != null ? datas.get(4).getId() : null;
                        cid = datas.get(4) != null ? datas.get(4).getCid() : null;
                        t = datas.get(4) != null ? datas.get(4).getTid() : null;
                    }

                    break;
            }
            if (id != null) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("t", t);
                intent.putExtra("cid", cid);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //prevent Exception crash when startActivity from custom view
                context.startActivity(intent);

                //Animation if Activity
                if (context instanceof Activity)
                    ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }
    }

    private static class ListSquareViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView rvContent;
        private ArrayList<Base> datas;
        private ListSquareAdapter adapter;

        private ListSquareViewHolder(final Context context, View itemView, boolean isCardView) {
            super(itemView);
            rvContent = (RecyclerView) itemView.findViewById(R.id.rvContent);
            rvContent.setLayoutManager(new LinearLayoutManager(context));
            rvContent.setNestedScrollingEnabled(false);
            datas = new ArrayList<>();
            adapter = isCardView ? new ListSquareAdapter(datas, true) : new ListSquareAdapter(datas);
            rvContent.setAdapter(adapter);
            rvContent.addItemDecoration(new GridSpacingItemDecoration(1, (int) context.getResources().getDimension(R.dimen._2sdp), false));
            rvContent.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("id", datas.get(position).getId());
                    intent.putExtra("t", datas.get(position).getTid());
                    intent.putExtra("cid", datas.get(position).getCid());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //prevent Exception crash when startActivity from custom view
                    context.startActivity(intent);

                    //Animation if Activity
                    if (context instanceof Activity)
                        ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }));
        }

        void bindData(final Display display) {
            if (display.getData() != null) {
                datas.clear();
                datas.addAll(display.getData());
                adapter.notifyDataSetChanged();
            }
        }
    }

    private static class ListRoundViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView rvContent;
        private ArrayList<Base> datas;
        private ListRoundAdapter adapter;

        private ListRoundViewHolder(final Context context, View itemView) {
            super(itemView);
            rvContent = (RecyclerView) itemView.findViewById(R.id.rvContent);
            rvContent.setLayoutManager(new LinearLayoutManager(context));
            rvContent.setNestedScrollingEnabled(false);
            datas = new ArrayList<>();
            adapter = new ListRoundAdapter(this.datas);
            rvContent.setAdapter(adapter);
            rvContent.addItemDecoration(new GridSpacingItemDecoration(1, (int) context.getResources().getDimension(R.dimen._3sdp), false));
            rvContent.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("id", datas.get(position).getId());
                    intent.putExtra("t", datas.get(position).getTid());
                    intent.putExtra("cid", datas.get(position).getCid());
                    //                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //prevent Exception crash when startActivity from custom view
                    context.startActivity(intent);

                    //Animation if Activity
                    if (context instanceof Activity)
                        ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }));
        }

        void bindData(final Display display) {
            if (display.getData() != null) {
                datas.clear();
                datas.addAll(display.getData());
                adapter.notifyDataSetChanged();
            }
        }
    }

    private static class TitleBoxViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout layoutParent;
        private ImageView imgIcon;
        private TextView tvTitle;
        private Display display;


        private TitleBoxViewHolder(final Context context, View itemView) {
            super(itemView);
            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            layoutParent = (RelativeLayout) itemView.findViewById(R.id.layoutParent);
            layoutParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (display.getTid() != null && display.getCid() != null) {
                        Intent intent = new Intent(context, CategoryActivity.class);
                        intent.putExtra("title", display.getTitle());
                        intent.putExtra("t", display.getTid());
                        intent.putExtra("cid", display.getCid());
                        //                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //prevent Exception crash when startActivity from custom view
                        context.startActivity(intent);

                        //Animation if Activity
                        if (context instanceof Activity)
                            ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
            });
        }

        void bindData(Display display) {
            this.display = display;
            if (display.getTitle() != null)
                tvTitle.setText(display.getTitle());
            if (display.getIcon() != null)
                Glide.with(tvTitle.getContext()).load(display.getIcon())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgIcon);
            //                Picasso.with(tvTitle.getContext())
            //                        .load(display.getIcon())
            //                        .resize(90, 90)
            //                        .centerCrop().into(imgIcon);
        }
    }

    private static class FooterViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDescription;

        private FooterViewHolder(Context context, View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);

            String versionName;
            try {
                versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (Exception e) {
                versionName = "";
                e.printStackTrace();
            }
            tvTitle.setText(context.getResources().getString(R.string.app_name) + " " + versionName);
        }

        void bindData(Display display) {
            if (display.getTitle() != null)
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            if (display.getDescription() != null)
                tvDescription.setText(display.getDescription());

        }
    }
}
