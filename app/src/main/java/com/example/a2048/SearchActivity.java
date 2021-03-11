package com.example.a2048;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {
    private TextView mTextView;
    private EditText mEditWordView;
    private WordListOpenHelper mDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        mEditWordView = findViewById(R.id.search_word);
        mTextView = findViewById(R.id.search_result);
        mDB = new WordListOpenHelper(this);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        final RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioPuntuation = (RadioButton) group.getChildAt(0);
                RadioButton radioName = (RadioButton) group.getChildAt(1);

                if(radioPuntuation.isChecked()){
                    radioGroup2.getChildAt(0).setEnabled(false);
                    radioGroup2.getChildAt(1).setEnabled(false);
                    radioGroup2.getChildAt(2).setEnabled(false);
                }else if(radioName.isChecked()){
                    radioGroup2.getChildAt(0).setEnabled(true);
                    radioGroup2.getChildAt(1).setEnabled(true);
                    radioGroup2.getChildAt(2).setEnabled(true);
                }
            }
        });

    }

    public void showResult(View view){
        String editSearch = mEditWordView.getText().toString();
        mTextView.setText("Result for " + editSearch + ":\n\n");

        RadioButton radioPuntuation = findViewById(R.id.puntuationRadioButton);
        RadioButton radioName = findViewById(R.id.usernameRadioButton);
        RadioButton radioEquals = findViewById(R.id.equalsRadio);
        RadioButton radioMore = findViewById(R.id.moreRadio);
        RadioButton radioLess = findViewById(R.id.lessRadio);

        Cursor cursor = null;
        int radioStatus = 0;

        if(radioPuntuation.isChecked()){
            if(radioEquals.isChecked()){
                radioStatus = 1;
            }else if(radioMore.isChecked()){
                radioStatus = 2;
            }else if(radioLess.isChecked()){
                radioStatus = 3;
            }
            cursor = mDB.searchForPuntuation(editSearch,radioStatus);
        }else if(radioName.isChecked()){
            cursor = mDB.searchForUsername(editSearch);
        }


        if (cursor != null & cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                int index = cursor.getColumnIndex(WordListOpenHelper.PUNTUATION);
                int index2 = cursor.getColumnIndex(WordListOpenHelper.USER_NAME);
                int index3 = cursor.getColumnIndex(WordListOpenHelper.DATE);
                String result = cursor.getString(index);
                String result2 = cursor.getString(index2);
                String result3 = cursor.getString(index3);


                mTextView.append("Score: "+result +"\n");
                mTextView.append("Name: " + result2 +"\n");
                mTextView.append("Date: " + result3 +"\n");
                mTextView.append("\n");

            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
