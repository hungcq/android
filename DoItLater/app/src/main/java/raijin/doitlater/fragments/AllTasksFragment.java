package raijin.doitlater.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.List;

import raijin.doitlater.R;
import raijin.doitlater.activities.MainActivity;
import raijin.doitlater.adapters.ListNoteAdapter;
import raijin.doitlater.database.RealmHandle;
import raijin.doitlater.database.models.NoteModel;
import raijin.doitlater.managers.ScreenManager;

/**
 * Created by Qk Lahpita on 7/23/2016.
 */
public class AllTasksFragment extends Fragment implements FragmentWithSearch {
    private Animation animation;
    private RecyclerView recyclerView;
    private ListNoteAdapter listNoteAdapter;
    private RealmHandle realmHandle;
    private List<NoteModel> noteModelList;

    public static AllTasksFragment create(ScreenManager screenManager) {
        AllTasksFragment listNoteFragment = new AllTasksFragment();
        return listNoteFragment;
    }

    public AllTasksFragment() {
        realmHandle = RealmHandle.getInst();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_note);
        recyclerView.hasFixedSize();
        setUpListNote();
        listNoteAdapter = new ListNoteAdapter(noteModelList);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        recyclerView.setAdapter(listNoteAdapter);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("All Tasks");
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        return view;
    }

    private void setUpListNote() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = settings.getString(MainActivity.SettingsFragment.PREF_KEY_SORT, "priority");
        if (sortBy.equals("time")) {
            noteModelList = realmHandle.getAllNoteList();
        } else if (sortBy.equals("priority")) {
            noteModelList = realmHandle.getSortedPriorityAllNoteList();
        } else if (sortBy.equals("title")) {
            noteModelList = realmHandle.getSortedTitleAllNoteList();
        }
    }

    @Override
    public void doSearch(String searchString) {
        List<NoteModel> foundNoteModelList = realmHandle.findNoteByTitleOrDetail(searchString);
        if (this.listNoteAdapter != null) {
            this.listNoteAdapter.reloadData(foundNoteModelList);
        }
    }

    @Override
    public void closeSearch() {
        listNoteAdapter.reloadData(noteModelList);
    }
}
