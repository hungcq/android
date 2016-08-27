package raijin.session21_hw;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment implements View.OnClickListener {

    private TextView countTextView;
    private TextView rightCountTextView;
    private TextView questionContentTextView;
    private List<Button> answerButtonList;
    private Button backButton;
    private Button nextButton;

    private DBContext dbContext;

    private MathWord mathWord;
    private int result;

    private static int count = 0;
    private static int rightAnswerCount = 0;

    public QuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        countTextView = (TextView) view.findViewById(R.id.text_count);
        rightCountTextView = (TextView) view.findViewById(R.id.text_right_count);
        questionContentTextView = (TextView) view.findViewById(R.id.question_content);
        answerButtonList = new ArrayList<>();
        answerButtonList.add((Button) view.findViewById(R.id.answer_0));
        answerButtonList.add((Button) view.findViewById(R.id.answer_1));
        answerButtonList.add((Button) view.findViewById(R.id.answer_2));
        answerButtonList.add((Button) view.findViewById(R.id.answer_3));
        backButton = (Button) view.findViewById(R.id.button_back);
        nextButton = (Button) view.findViewById(R.id.button_next);

        backButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        dbContext = DBContext.getInst(getActivity());

        getData();
        setData();
        setButtonColor();
    }

    private void getData() {
        dbContext.openConnection();
        mathWord = dbContext.getMathWord(count);
        dbContext.closeConnection();
    }

    private void setData() {
        countTextView.setText("Question: " + mathWord.getId() + "/100");
        rightCountTextView.setText("Right answers: " + rightAnswerCount);
        questionContentTextView.setText(mathWord.getQuestionContent());
        for (int i = 0; i < 4; i++) {
            answerButtonList.get(i).setText(mathWord.getListAnswer().get(i));
        }
        result = mathWord.getResult();
    }

    private void setButtonColor() {
        int chosenAnswer = MainActivity.getChosenAnswer().get(count);
        if (chosenAnswer == -1) {
            for (int i = 0; i < 4; i++) {
                answerButtonList.get(i).setOnClickListener(this);
            }
        } else if (chosenAnswer == result) {
            for (int i = 0; i < 4; i++) {
                answerButtonList.get(i).setBackgroundResource(R.drawable.custom_inactive_button);
            }
            answerButtonList.get(chosenAnswer).setBackgroundResource(R.drawable.custom_active_button);
        } else if (chosenAnswer != result) {
            for (int i = 0; i < 4; i++) {
                answerButtonList.get(i).setBackgroundResource(R.drawable.custom_inactive_button);
            }
            answerButtonList.get(result).setBackgroundResource(R.drawable.custom_active_button);
            answerButtonList.get(chosenAnswer).setBackgroundResource(R.drawable.custom_wrong_answer_button);
        }
    }

    private void openFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new QuestionFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.answer_0:
                MainActivity.getChosenAnswer().set(count, 0);
                questionContentTextView.setText(mathWord.getExplanation());
                setButtonColor();
                for (int i = 0; i < 4; i++) {
                    answerButtonList.get(i).setOnClickListener(null);
                }
                if (result == 0) {
                    rightAnswerCount++;
                    rightCountTextView.setText("Right answers: " + rightAnswerCount);
                }
                break;
            case R.id.answer_1:
                MainActivity.getChosenAnswer().set(count, 1);
                questionContentTextView.setText(mathWord.getExplanation());
                setButtonColor();
                for (int i = 0; i < 4; i++) {
                    answerButtonList.get(i).setOnClickListener(null);
                }
                if (result == 1) {
                    rightAnswerCount++;
                    rightCountTextView.setText("Right answers: " + rightAnswerCount);
                }
                break;
            case R.id.answer_2:
                MainActivity.getChosenAnswer().set(count, 2);
                questionContentTextView.setText(mathWord.getExplanation());
                setButtonColor();
                for (int i = 0; i < 4; i++) {
                    answerButtonList.get(i).setOnClickListener(null);
                }
                if (result == 2) {
                    rightAnswerCount++;
                    rightCountTextView.setText("Right answers: " + rightAnswerCount);
                }
                break;
            case R.id.answer_3:
                MainActivity.getChosenAnswer().set(count, 3);
                questionContentTextView.setText(mathWord.getExplanation());
                setButtonColor();
                for (int i = 0; i < 4; i++) {
                    answerButtonList.get(i).setOnClickListener(null);
                }
                if (result == 3) {
                    rightAnswerCount++;
                    rightCountTextView.setText("Right answers: " + rightAnswerCount);
                }
                break;
            case R.id.button_next:
                if (count < 99) {
                    count++;
                    openFragment();
                }
                break;
            case R.id.button_back:
                if (count > 0) {
                    count--;
                    openFragment();
                }
                break;
            default:
                break;
        }
    }
}
