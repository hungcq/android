package raijin.session21_hw;

import java.util.List;

/**
 * Created by 1918 on 23-Aug-16.
 */
public class MathWord {
    private int id;
    private String questionContent;
    private List<String> listAnswer;
    private int result;
    private String explanation;

    public MathWord(int id, String questionContent, List<String> listAnswer, int result, String explanation) {
        this.questionContent = questionContent;
        this.listAnswer = listAnswer;
        this.result = result;
        this.explanation = explanation;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
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
