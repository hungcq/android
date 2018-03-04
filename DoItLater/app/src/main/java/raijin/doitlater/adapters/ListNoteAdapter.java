package raijin.doitlater.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import raijin.doitlater.R;
import raijin.doitlater.activities.ActivityType;
import raijin.doitlater.activities.MainActivity;
import raijin.doitlater.activities.NoteActivity;
import raijin.doitlater.database.RealmHandle;
import raijin.doitlater.database.models.NoteModel;
import raijin.doitlater.managers.FragmentType;
import raijin.doitlater.managers.ScreenManager;
import raijin.doitlater.utils.Utils;

/**
 * Created by Qk Lahpita on 7/23/2016.
 */
public class ListNoteAdapter extends RecyclerView.Adapter<ListNoteAdapter.RecyclerViewHolder> {
    private Animation animation;
    private Context context;
    Intent intent;
    List<NoteModel> listNote;
    private ActionMode actionMode;
    private RealmHandle realmHandle;

    public ListNoteAdapter(List<NoteModel> listNote) {
        this.listNote = listNote;
    }

    @Override
    public ListNoteAdapter.RecyclerViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        realmHandle = RealmHandle.getInst();
        context = parent.getContext();

        animation = AnimationUtils.loadAnimation(parent.getContext(), R.anim.zoom_in);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationUtils.loadAnimation(parent.getContext(), R.anim.zoom_out);
                context.startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        LayoutInflater inflater = LayoutInflater.from(context);
        //
        final View view = inflater.inflate(R.layout.item_list_note, parent, false);
        //
        final RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                intent = new Intent(context, NoteActivity.class);
                Bundle bundle = new Bundle();
                int position = recyclerViewHolder.getAdapterPosition();
                bundle.putInt("ID",listNote.get(position).getID());
                intent.putExtras(bundle);
                NoteActivity.activityType = ActivityType.EDITNOTE;
                NoteActivity.checkCaller = true;
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                final int position = recyclerViewHolder.getAdapterPosition();
                if (actionMode != null) return false;
                actionMode = ((MainActivity) context).startSupportActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        mode.getMenuInflater().inflate(R.menu.context_menu, menu);
                        mode.setTitle(listNote.get(position).getTitle());
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.context_menu_delete:
                                FragmentType currentFragment = ScreenManager.getCurrentFragment();
                                if (currentFragment == FragmentType.ALLTASK) {
                                    realmHandle.deleteNoteFromRealm(realmHandle.getAllNoteList().get(position));
                                } else if (currentFragment == FragmentType.COMPLETED) {
                                    realmHandle.deleteNoteFromRealm(realmHandle.getCompletedList().get(position));
                                } else if (currentFragment == FragmentType.TODO) {
                                    realmHandle.deleteNoteFromRealm(realmHandle.getToDoList().get(position));
                                }
                                notifyItemRemoved(position);
                                mode.finish();
                                return true;
                            case R.id.context_menu_edit:
                                view.startAnimation(animation);
                                intent = new Intent(context, NoteActivity.class);
                                Bundle bundle = new Bundle();
                                int position = recyclerViewHolder.getAdapterPosition();
                                bundle.putInt("ID",listNote.get(position).getID());
                                intent.putExtras(bundle);
                                NoteActivity.activityType = ActivityType.EDITNOTE;
                                NoteActivity.checkCaller = true;
                                mode.finish();
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        actionMode = null;
                    }
                });
                return true;
            }
        });
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(ListNoteAdapter.RecyclerViewHolder holder, int position) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean showImage = settings.getBoolean(MainActivity.SettingsFragment.PREF_KEY_SHOW_IMAGE, true);
        Boolean showReminder = settings.getBoolean(MainActivity.SettingsFragment.PREF_KEY_SHOW_REMINDER, true);
        Boolean showLocation = settings.getBoolean(MainActivity.SettingsFragment.PREF_KEY_SHOW_LOCATION, true);
        NoteModel noteModel = listNote.get(position);
        holder.itemTitle.setText(noteModel.getTitle());
        if (noteModel.getDetail() != null && !noteModel.getDetail().equals("")) {
            holder.itemDetail.setText(noteModel.getDetail());
        } else {
            holder.itemDetail.setText("");
            holder.itemDetail.setVisibility(View.GONE);
        }
        if (noteModel.getLocation() != null && !noteModel.getLocation().equals("") && showLocation) {
            holder.itemLocation.setText(noteModel.getLocation());
            holder.itemLocation.setVisibility(View.VISIBLE);
        }
        if (noteModel.getImagePath() != null && !noteModel.getImagePath().equals("") && showImage) {
            holder.itemImage.setImageBitmap(Utils.decodeImageFile(noteModel.getImagePath()));
        } else {
            holder.itemImage.setImageBitmap(null);
            holder.itemImage.setVisibility(View.GONE);
        }
        if (noteModel.getDateReminder() != null && !noteModel.getDateReminder().equals("") && showReminder) {
            holder.itemReminder.setText(noteModel.getDateReminder() + ", " + noteModel.getTimeReminder());
            holder.layoutReminder.setVisibility(View.VISIBLE);
        } else {
            holder.itemReminder.setText(null);
            holder.itemReminder.setVisibility(View.GONE);
        }
        if (noteModel.getPriority() == 2) {
            holder.view.setBackgroundColor(context.getResources().getColor(R.color.colorGrey800));
        } else if (noteModel.getPriority() == 1) {
            holder.view.setBackgroundColor(context.getResources().getColor(R.color.colorGrey600));
        } else if (noteModel.getPriority() == 0) {
            holder.view.setBackgroundColor(context.getResources().getColor(R.color.colorGrey400));
        }

    }

    @Override
    public int getItemCount() {
        return listNote.size();
    }

    public void reloadData(List<NoteModel> noteModelList) {
        this.listNote = noteModelList;
        this.notifyDataSetChanged();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle, itemDetail, itemReminder, itemLocation;
        ImageView itemImage;
        LinearLayout layoutReminder;
        ImageView view;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
            itemDetail = (TextView) itemView.findViewById(R.id.item_detail);
            itemLocation = (TextView) itemView.findViewById(R.id.item_location);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemReminder = (TextView) itemView.findViewById(R.id.tv_reminder_item_list_note);
            layoutReminder = (LinearLayout) itemView.findViewById(R.id.layout_reminder);
            view = (ImageView) itemView.findViewById(R.id.view_priority);
        }
    }
}
