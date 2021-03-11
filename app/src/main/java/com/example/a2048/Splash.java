package com.example.a2048;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Splash extends AppCompatActivity {
    private TextView logo1;
    private TextView logo2;
    private TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        initializeViews();
        startAnimations();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseAnimations();
    }

    private void initializeViews() {
        this.logo1 = (TextView) findViewById(R.id.TextViewTopTitle);
        this.logo2 = (TextView) findViewById(R.id.TextViewBottomTitle);
        this.table = (TableLayout) findViewById(R.id.TableLayout);
    }

    private void startAnimations() {
        Animation fade1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fade2 = AnimationUtils.loadAnimation(this, R.anim.fade_in2);
        Animation spinin = AnimationUtils.loadAnimation(this, R.anim.custom_anim);

        logo1.startAnimation(fade1);
        logo2.startAnimation(fade2);
        LayoutAnimationController controller = new LayoutAnimationController(spinin);
        TableRow row = (TableRow) table.getChildAt(0);
        row.setLayoutAnimation(controller);

        fade2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(Splash.this, MenuActivity.class));
                Splash.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void pauseAnimations() {
        this.logo1.clearAnimation();
        this.logo2.clearAnimation();
        for (int i = 0; i < this.table.getChildCount(); i++) {
            TableRow row = (TableRow) this.table.getChildAt(i);
            row.clearAnimation();
        }
    }
}

