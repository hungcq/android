package raijin.session21_sql;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<MathWord> list;
    private DBContext dbContext;
    private ListQuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        fillDataToRecyclerView();
    }

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        dbContext = DBContext.getInst(this);
    }

    private void getData() {
        dbContext.openConnection();
        list = dbContext.getAllMathWord();
        dbContext.closeConnection();
        Log.d("DMM", list.size() + "");
        if (list != null) {
            adapter = new ListQuestionAdapter(list, this);
        }
    }

    private void fillDataToRecyclerView() {
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
