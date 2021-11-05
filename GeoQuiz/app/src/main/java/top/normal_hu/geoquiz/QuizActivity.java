package top.normal_hu.geoquiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "key_index";
    private static final int REQUEST_CODE_CHEAT = 0;
    //该字符串为extra的键值，为了与其他应用的activity传入的extra区分，需加上包名
    private static final String EXTRA_ANSWER_IS_TRUE =
            "top.normal_hu.geoquiz.answer_is_true";
    private static final String KEY_ISCHEATED = "is_cheated";
    private static final String KEY_ISCHEATS = "is_cheats";

    private Button mTrueBtn;
    private Button mFalseBtn;
    private Button mCheatBtn;
    private TextView mQuestionText;
    private ImageButton mNextBtn;
    private ImageButton mPreBtn;
    private boolean mIsCheater;
    private Question[] mQuestions= new Question[]{
            new Question(R.string.q_highestmount,true),
            new Question(R.string.q_biggestlake,false),
            new Question(R.string.q_biggestisland,true)
    };
    private boolean[] mIsCheats = new boolean[]{false,false,false};
    private int mCurrentIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            Log.d(TAG,"index:" + mCurrentIndex);
            //mIsCheater = savedInstanceState.getBoolean(KEY_ISCHEATED);
            mIsCheats = savedInstanceState.getBooleanArray(KEY_ISCHEATS);
            if(mIsCheats[mCurrentIndex] == true)
                mIsCheater = true;
        }

        mTrueBtn = findViewById(R.id.true_btn);

        //设置初始视图
        mQuestionText = findViewById(R.id.question_text_view);
        mQuestionText.setOnClickListener(new NextQuestionListener());
        mQuestionText.setText(mQuestions[mCurrentIndex].getTextResId());

        mTrueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mFalseBtn = findViewById(R.id.false_btn);
        mFalseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
        mNextBtn = findViewById(R.id.next_btn);
        mNextBtn.setOnClickListener(new NextQuestionListener());

        mPreBtn = findViewById(R.id.pre_btn);
        mPreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //要实现index递减顺序:0,2,1,0
                //mCurrentIndex = mCurrentIndex == 0 ? mQuestions.length -1 : mCurrentIndex-1;
                mCurrentIndex = (mCurrentIndex-1+mQuestions.length) % mQuestions.length;
                Log.d(TAG,"index:" + mCurrentIndex);
                int textId = mQuestions[mCurrentIndex].getTextResId();
                mQuestionText.setText(textId);
                //mIsCheater = false;
                if(mIsCheats[mCurrentIndex] == true)
                    mIsCheater = true;
            }
        });

        mCheatBtn = findViewById(R.id.cheat_btn);
        mCheatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CheatActivity.newIntent(QuizActivity.this,
                        mQuestions[mCurrentIndex].isAnswerTrue());
                //intent.putExtra(EXTRA_ANSWER_IS_TRUE,mQuestions[mCurrentIndex].isAnswerTrue());
                startActivityForResult(intent,REQUEST_CODE_CHEAT);
            }
        });
    }

    private void checkAnswer(boolean userPressed){
        boolean answer = mQuestions[mCurrentIndex].isAnswerTrue();
        int stringId;
        if(userPressed == answer)
            stringId = R.string.correct_tst;
        else
            stringId =R.string.incorrect_tst;
        if(mIsCheater)
            stringId = R.string.judgment_toast;
        Toast.makeText(QuizActivity.this,stringId,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX,mCurrentIndex);
        outState.putBooleanArray(KEY_ISCHEATS,mIsCheats);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null)
                return;
            mIsCheater = CheatActivity.wasAnswerShown(data);
            mIsCheats[mCurrentIndex] = mIsCheater;
        }
    }

    class NextQuestionListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //让index递增，然后获取对应Question实例的问题
            mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
            Log.d(TAG,"index:" + mCurrentIndex);
            int textId = mQuestions[mCurrentIndex].getTextResId();
            mQuestionText.setText(textId);
            //mIsCheater = false;
            //mIsCheater由当前问题是否已作过弊决定
            if(mIsCheats[mCurrentIndex] == true)
                mIsCheater = true;
        }
    }
}