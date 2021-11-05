package top.normal_hu.geoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private boolean setResultFlag = false;
    private TextView mAnswerTextView;
    private Button mShowAnswer;
    private static final String EXTRA_ANSWER_IS_TRUE =
            "top.normal_hu.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN =
            "top.normal_hu.geoquiz.answer_shown";
    private static final String KEY_IS_CHEATED = "isCheated";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if(savedInstanceState != null){
            setAnswerShownResult(true);
            setResultFlag = true;
        }
        //直接在这里获取extra
        boolean answer = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);

        mAnswerTextView = findViewById(R.id.answerTextView);

        mShowAnswer = findViewById(R.id.showAnswerBtn);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置一个标志位，表示作弊过
                setResultFlag = true;
                if (answer == true){
                    mAnswerTextView.setText(R.string.true_btn);
                }else{
                    mAnswerTextView.setText(R.string.false_btn);
                }
                setAnswerShownResult(true);
            }
        });
    }

    public static Intent newIntent(Context context, boolean answerIsTrue){
        Intent intent = new Intent(context,CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }

    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        setResult(RESULT_OK,data);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(setResultFlag){
            //将标志位放入intent
            outState.putBoolean(KEY_IS_CHEATED,setResultFlag);
        }
    }
}