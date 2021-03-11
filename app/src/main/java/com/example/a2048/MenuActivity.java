package com.example.a2048;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        final Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText usernameBox = (EditText) findViewById(R.id.usernameBox);
                String username = usernameBox.getText().toString();
                Intent mainGame = new Intent(MenuActivity.this, MainActivity.class);
                mainGame.putExtra("username", username);
                startActivity(mainGame);
                MenuActivity.this.finish();
            }
        });

        final Button rankingButton = findViewById(R.id.rankingButton);
        rankingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, RankingActivity.class));
                MenuActivity.this.finish();
            }
        });
    }
}
