package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    public static int[][] matrix = new int[4][4];
    public static int[][] matrixCopy = new int[4][4];
    private GestureDetector gestureScanner;
    final int probDeCuatro = 2; //*10 = %
    final int minMovement = 100;
    private WordListOpenHelper mDB = new WordListOpenHelper(this);
    Boolean once = true;
    private String username;

    TextView[] tvArray = new TextView[16];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = this.getIntent().getExtras();
        username = extras.getString("username");

        inicializar();
        gestureScanner = new GestureDetector(this);

        final Button button = findViewById(R.id.resetButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inicializar();
            }
        });

        final Button button2 = findViewById(R.id.undoButton);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                copy(matrix,matrixCopy);
                updateView(matrix);
            }
        });

        final Button button3 = findViewById(R.id.backtomenuButton);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MenuActivity.class));
                MainActivity.this.finish();
            }
        });

    }

    public boolean onTouchEvent(MotionEvent event){
        return gestureScanner.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }


    @Override
    public boolean onFling (MotionEvent e1, MotionEvent e2,float velocityX,float velocityY){
        float x = e2.getX() - e1.getX();
        float y = e2.getY() - e1.getY();

        float xAux = x;
        float yAux = y;

        Boolean trueMove = false;

        if(x < 0){
            xAux = x*-1;
        }
        if(y<0){
            yAux = y*-1;
        }

        if(xAux > yAux){
            //Derecha o Izquierda
            if(xAux > minMovement){
                copy(matrixCopy,matrix);
                //Tiene que ser mas grande que el minimo
                if(x < 0){
                    //Izquierda
                    trueMove = moverIzquierda(matrix);
                    updateView(matrix);
                }else{
                    //Derecha
                    trueMove = moverDerecha(matrix);
                    updateView(matrix);
                }
            }
        }else{
            //Arriba o abajo
            if(yAux > minMovement){
                //Tiene que ser mas grande que el minimo
                if(y < 0){
                    //Arriba
                    trueMove = moverArriba(matrix);
                    updateView(matrix);
                }else{
                    //Abajo
                    trueMove = moverAbajo(matrix);
                    updateView(matrix);
                }
            }
        }

        if(trueMove){
            spawnRandom(matrix);
            updateView(matrix);
        }

    return true;
    }


    public void inicializar() {
        fillMatrix();

        spawnRandom(matrix);
        spawnRandom(matrix);
        copy(matrixCopy,matrix);

        updateView(matrix);
    }

    public void copy(int[][] matrix1, int[][] matrix2){
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                matrix1[i][j] = matrix2[i][j];
            }
        }
    }


    public Boolean moverArriba(int[][] matriz){

        boolean trueMove = false;
        boolean check = true;

        for (int i = 1; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                if(matriz[i][j] != 0){
                    int node = i;
                    while(node != 0){
                        if(matriz[node-1][j] == 0) {
                            matriz[node-1][j] = matriz[node][j];
                            matriz[node][j] = 0;
                            trueMove = true;
                        }else if(matriz[node-1][j] == matriz[node][j]){
                                if(check){
                                    matriz[node-1][j] = (matriz[node-1][j] + matrix[node][j]) *-1;
                                    check = false;
                                }else{
                                    matriz[node-1][j] = (matriz[node-1][j] + matrix[node][j]);
                                    check = true;
                                }

                                matriz[node][j] = 0;
                                trueMove = true;
                        }

                        node--;
                    }
                }
            }
        }

        return trueMove;
    }


    public Boolean moverAbajo(int[][] matriz){

        Boolean trueMove = false;
        Boolean check = true;

        for (int i = matriz.length-2; i >= 0; i--) {
            for (int j = 0; j < matriz[0].length; j++) {
                if(matriz[i][j] != 0) {
                    int node = i;
                    while (node != 3) {
                        if (matriz[node + 1][j] == 0) {
                            matriz[node + 1][j] = matriz[node][j];
                            matriz[node][j] = 0;
                            trueMove = true;
                        } else if (matriz[node + 1][j] == matriz[node][j]) {
                            if(check) {
                                matriz[node + 1][j] = (matriz[node + 1][j] + matrix[node][j]) * -1;
                                check = false;
                            }else{
                                matriz[node + 1][j] = (matriz[node + 1][j] + matrix[node][j]);
                                check = true;
                            }
                                matriz[node][j] = 0;
                                trueMove = true;
                        }
                        node++;
                    }
                }
            }
        }
        return trueMove;
    }

    public Boolean moverDerecha(int[][] matriz){

        Boolean trueMove = false;
        Boolean check = true;

        for (int i = 0; i < matriz.length; i++) {
            for (int j = matriz[0].length-2; j >= 0; j--) {
                if(matriz[i][j] != 0) {
                    int node = j;
                    while (node != 3) {
                        if (matriz[i][node + 1] == 0) {
                            matriz[i][node + 1] = matriz[i][node];
                            matriz[i][node] = 0;
                            trueMove = true;
                        } else if (matriz[i][node + 1] == matriz[i][node]) {
                            if(check){
                                matriz[i][node + 1] = (matriz[i][node + 1] + matrix[i][node])*-1;
                                check = false;
                            }else{
                                matriz[i][node + 1] = (matriz[i][node + 1] + matrix[i][node]);
                                check = true;
                            }

                                matriz[i][node] = 0;
                                trueMove = true;
                        }
                        node++;
                    }
                }
            }
        }
        return trueMove;
    }

    public Boolean moverIzquierda(int[][] matriz){

        boolean trueMove = false;
        boolean check = true;

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 1; j < matriz[0].length; j++) {
                if(matriz[i][j] != 0) {
                    int node = j;
                    while (node != 0) {
                        if (matriz[i][node - 1] == 0) {
                            matriz[i][node - 1] = matriz[i][node];
                            matriz[i][node] = 0;
                            trueMove = true;
                        } else if (matriz[i][node - 1] == matriz[i][node]) {
                            if(check) {
                                matriz[i][node - 1] = (matriz[i][node - 1] + matrix[i][node]) * -1;
                                check = false;
                            }else{
                                matriz[i][node - 1] = (matriz[i][node - 1] + matrix[i][node]);
                                check = true;
                            }
                                matriz[i][node] = 0;
                                trueMove = true;
                        }
                        node--;
                    }
                }
            }
        }
        return trueMove;
    }

    public void fillMatrix(){

        tvArray[0] = findViewById(R.id.textView00);
        tvArray[1] = findViewById(R.id.textView01);
        tvArray[2] = findViewById(R.id.textView02);
        tvArray[3] = findViewById(R.id.textView03);

        tvArray[4] = findViewById(R.id.textView10);
        tvArray[5] = findViewById(R.id.textView11);
        tvArray[6] = findViewById(R.id.textView12);
        tvArray[7] = findViewById(R.id.textView13);

        tvArray[8] = findViewById(R.id.textView20);
        tvArray[9] = findViewById(R.id.textView21);
        tvArray[10] = findViewById(R.id.textView22);
        tvArray[11] = findViewById(R.id.textView23);

        tvArray[12] = findViewById(R.id.textView30);
        tvArray[13] = findViewById(R.id.textView31);
        tvArray[14] = findViewById(R.id.textView32);
        tvArray[15] = findViewById(R.id.textView33);


        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    public void updateView(int[][] matriz){

        TextView puntuacion = findViewById(R.id.puntuaction);
        int sumaTotal = 0;

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                if(matriz[i][j] < 0){
                    matriz[i][j] = matriz[i][j]*-1;
                }
                sumaTotal = sumaTotal + matriz[i][j];
            }
        }


        puntuacion.setText(String.valueOf(sumaTotal));

        tvArray[0].setText(beautify(matriz[0][0],tvArray[0]));
        tvArray[1].setText(beautify(matriz[0][1],tvArray[1]));
        tvArray[2].setText(beautify(matriz[0][2],tvArray[2]));
        tvArray[3].setText(beautify(matriz[0][3],tvArray[3]));

        tvArray[4].setText(beautify(matriz[1][0],tvArray[4]));
        tvArray[5].setText(beautify(matriz[1][1],tvArray[5]));
        tvArray[6].setText(beautify(matriz[1][2],tvArray[6]));
        tvArray[7].setText(beautify(matriz[1][3],tvArray[7]));

        tvArray[8].setText(beautify(matriz[2][0],tvArray[8]));
        tvArray[9].setText(beautify(matriz[2][1],tvArray[9]));
        tvArray[10].setText(beautify(matriz[2][2],tvArray[10]));
        tvArray[11].setText(beautify(matriz[2][3],tvArray[11]));

        tvArray[12].setText(beautify(matriz[3][0],tvArray[12]));
        tvArray[13].setText(beautify(matriz[3][1],tvArray[13]));
        tvArray[14].setText(beautify(matriz[3][2],tvArray[14]));
        tvArray[15].setText(beautify(matriz[3][3],tvArray[15]));

        if(checkWin(matriz)){
            if(once) {
                Toast.makeText(getApplicationContext(), "Has llegado a 2048, prueba hasta donde puedes llegar!", Toast.LENGTH_LONG).show();
                once = false;
            }
        }

        if(checkFull(matriz)){
            if(!checkLose(matriz)){
                Toast.makeText(getApplicationContext(), "Perdiste!", Toast.LENGTH_LONG).show();
                finishGame();
            }else{
                }
        }
    }

    public void finishGame(){
        String word = ((TextView) findViewById(R.id.puntuaction)).getText().toString();
        Date currentTime = Calendar.getInstance().getTime();
        mDB.insert(word, username, DateFormat.format("dd-MM-yyyy",  currentTime).toString());
        startActivity(new Intent(MainActivity.this, RankingActivity.class));
        MainActivity.this.finish();
    }

    public void spawnRandom(int[][] matriz){
        int randomx = new Random().nextInt(4);
        int randomy = new Random().nextInt(4);

        while(matriz[randomy][randomx] != 0){
            randomx = new Random().nextInt(4);
            randomy = new Random().nextInt(4);
        }

        int randomnumber = new Random().nextInt(10);

        if(randomnumber<probDeCuatro){
            matriz[randomy][randomx] = 4;
        }else{
            matriz[randomy][randomx] = 2;
        }
    }

    public Boolean checkWin(int[][] matriz){

        Boolean win = false;
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                if(matriz[i][j] == 2048) {
                    win = true;
                }
            }
        }
        return win;
    }

    public Boolean checkFull(int[][] matriz){

        Boolean full = true;
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                if(matriz[i][j] == 0) {
                    full = false;
                }
            }
        }
        return full;
    }
    public Boolean checkLose(int[][] matriz){
        Boolean possible = false;
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                if(matriz[i][j] != 0){
                    try {
                        if (matriz[i + 1][j] == matriz[i][j]) {
                            possible = true;
                        }
                        if (matriz[i][j + 1] == matriz[i][j]) {
                            possible = true;
                        }
                        if (matriz[i - 1][j] == matriz[i][j]) {
                            possible = true;
                        }
                        if (matriz[i][j - 1] == matriz[i][j]) {
                            possible = true;
                        }
                    }catch(Exception e){

                    }
                }
            }
        }
        return possible;
    }

    public String beautify(int num, TextView tv){
        if(num < 0){
            num = num * -1;
        }
        if(num == 0){
            tv.setBackgroundColor(Color.GRAY);
            return " ";
        }else if(num == 2){
            tv.setTextSize(50);
            tv.setBackgroundColor(getResources().getColor(R.color.color2));
        }else if(num == 4){
            tv.setTextSize(50);
            tv.setBackgroundColor(getResources().getColor(R.color.color4));
        }else if(num == 8){
            tv.setTextSize(50);
            tv.setBackgroundColor(getResources().getColor(R.color.color8));
        }else if(num == 16){
            tv.setTextSize(50);
            tv.setBackgroundColor(getResources().getColor(R.color.color16));
        }else if(num == 32){
            tv.setTextSize(50);
            tv.setBackgroundColor(getResources().getColor(R.color.color32));
        }else if(num == 64){
            tv.setTextSize(50);
            tv.setBackgroundColor(getResources().getColor(R.color.color64));
        }else if(num == 128){
            tv.setTextSize(40);
            tv.setBackgroundColor(getResources().getColor(R.color.color128));
        }else if(num == 256){
            tv.setTextSize(40);
            tv.setBackgroundColor(getResources().getColor(R.color.color256));
        }else if(num == 512){
            tv.setTextSize(40);
            tv.setBackgroundColor(getResources().getColor(R.color.color512));
        }else if(num == 1024){
            tv.setTextSize(30);
            tv.setBackgroundColor(getResources().getColor(R.color.color1024));
        }else if(num == 2048){
            tv.setTextSize(30);
            tv.setBackgroundColor(getResources().getColor(R.color.color2048));
        }

        return String.valueOf(num);
    }

}