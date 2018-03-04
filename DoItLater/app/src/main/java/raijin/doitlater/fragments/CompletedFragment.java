package raijin.doitlater.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import raijin.doitlater.R;
import raijin.doitlater.activities.MainActivity;
import raijin.doitlater.adapters.ListNoteAdapter;
import raijin.doitlater.database.RealmHandle;
import raijin.doitlater.database.models.NoteModel;
import raijin.doitlater.managers.ScreenManager;

/**
 * A simple {@link Fragment} subclass.
 */

/**
 * Created by Qk Lahpita on 7/23/2016.
 */
public class CompletedFragment extends Fragment implements FragmentWithSearch {
    private RecyclerView recyclerView;
    private ListNoteAdapter listNoteAdapter;
    private RealmHandle realmHandle;
    private List<NoteModel> noteModelList;

    public static CompletedFragment create(ScreenManager screenManager) {
        CompletedFragment listNoteFragment = new CompletedFragment();
        return listNoteFragment;
    }

    public CompletedFragment() {
        realmHandle = RealmHandle.getInst();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_note);
        setUpListNote();
        setUpRecyclerView();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("Completed Tasks");


        ItemTouchHelper.Callback callback = new ListNoteTouchHelper();
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void setUpListNote() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = settings.getString(MainActivity.SettingsFragment.PREF_KEY_SORT, "priority");
        if (sortBy.equals("time")) {
            noteModelList = realmHandle.getCompletedList();
        } else if (sortBy.equals("priority")) {
            noteModelList = realmHandle.getSortedPriorityCompletedList();
        } else if (sortBy.equals("title")) {
            noteModelList = realmHandle.getSortedTitleCompletedList();
        }
    }

    private void setUpRecyclerView() {
        recyclerView.hasFixedSize();
        listNoteAdapter = new ListNoteAdapter(noteModelList);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        recyclerView.setAdapter(listNoteAdapter);
    }

    @Override
    public void doSearch(String searchString) {
        List<NoteModel> foundNoteModelList = realmHandle.findNoteByTitleOrDetail(searchString, true);
        if (this.listNoteAdapter != null) {
            this.listNoteAdapter.reloadData(foundNoteModelList);
        }
    }

    @Override
    public void closeSearch() {
        listNoteAdapter.reloadData(noteModelList);
    }

    public class ListNoteTouchHelper extends ItemTouchHelper.SimpleCallback {

        public ListNoteTouchHelper() {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //Not implemented here
            return false;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            NoteModel deletedNote = noteModelList.get(position);
            int ID = noteModelList.get(position).getID();
            String pathOfAudio = deletedNote.getPathOfAudio();
            String title = deletedNote.getTitle();
            String detail = deletedNote.getDetail();
            String location = deletedNote.getLocation();
            String timeReminder = deletedNote.getTimeReminder();
            String imagePath = deletedNote.getImagePath();
            boolean hasAudio = deletedNote.isHasAudio();
            String dateReminder = deletedNote.getDateReminder();
            boolean isCompleted = deletedNote.isCompleted();
            int requestCodeAlarm = deletedNote.getRequestCodeAlarm();
            int priority = deletedNote.getPriority();
            final NoteModel noteModel = new NoteModel(ID, pathOfAudio, title, detail, location, timeReminder, imagePath,
                    hasAudio, dateReminder, requestCodeAlarm, isCompleted, priority);
            realmHandle.deleteNoteFromRealm(deletedNote);
            listNoteAdapter.notifyItemRemoved(position);
            Snackbar snackbar = Snackbar.make(getView(), "Deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            realmHandle.addNoteToRealm(noteModel);
                            recyclerView.removeAllViews();
                            setUpRecyclerView();
                        }
                    })
                    .setActionTextColor(Color.WHITE);
            snackbar.show();
        }
    }
}
