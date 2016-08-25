package raijin.session21_sql;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1918 on 23-Aug-16.
 */
public class MathWord {
    private String questionContent;
    private List<String> listAnswer;

    public MathWord(String questionContent, List<String> listAnswer) {
        this.questionContent = questionContent;
        this.listAnswer = listAnswer;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public List<String> getListAnswer() {
        return listAnswer;
    }

    public void setListAnswer(List<String> listAnswer) {
        this.listAnswer = listAnswer;
    }
}
