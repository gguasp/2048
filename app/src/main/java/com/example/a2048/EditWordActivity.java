/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.a2048;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


/**
 * Activity to edit an existing or create a new word.
 */
public class EditWordActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
    }

    public void returnReply(View view) {
        String username = ((EditText) findViewById(R.id.edit_word)).getText().toString();
        Bundle extras = getIntent().getExtras();

        Intent replyIntent = new Intent();
        replyIntent.putExtra("username", username);
        replyIntent.putExtra("id", extras.getInt("id", -99));
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}

