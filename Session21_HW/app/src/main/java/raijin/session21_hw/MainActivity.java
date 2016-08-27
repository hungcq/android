package raijin.session21_hw;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static List<Integer> chosenAnswer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for(int i = 0; i < 100; i++) {
            chosenAnswer.add(-1);
        }

        openFragment();
    }

    private void openFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new QuestionFragment());
        fragmentTransaction.commit();
    }

    public static List<Integer> getChosenAnswer() {
        return chosenAnswer;
    }
}
