package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    public static int[][] matrix = new int[4][4];
    private GestureDetector gestureScanner;
    final int probDeCuatro = 2;
    final int minMovement = 100;

    TextView[] tvArray = new TextView[16];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillMatrix();

        spawnRandom(matrix);
        spawnRandom(matrix);

        updateView(matrix);
        gestureScanner = new GestureDetector(this);
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

        if(x < 0){
            xAux = x*-1;
        }
        if(y<0){
            yAux = y*-1;
        }

        if(xAux > yAux){
            //Derecha o Izquierda
            if(xAux > minMovement){
                //Tiene que ser mas grande que el minimo
                if(x < 0){
                    //Izquierda
                    Toast.makeText(getApplicationContext(), "Izquierda", Toast.LENGTH_LONG).show();
                    moverIzquierda(matrix);
                    spawnRandom(matrix);
                    updateView(matrix);
                }else{
                    //Derecha
                    Toast.makeText(getApplicationContext(), "Derecha", Toast.LENGTH_LONG).show();
                    moverDerecha(matrix);
                    spawnRandom(matrix);
                    updateView(matrix);
                }
            }
        }else{
            //Arriba o abajo
            if(yAux > minMovement){
                //Tiene que ser mas grande que el minimo
                if(y < 0){
                    //Arriba
                    Toast.makeText(getApplicationContext(), "Arriba", Toast.LENGTH_LONG).show();
                    moverArriba(matrix);
                    spawnRandom(matrix);
                    updateView(matrix);
                }else{
                    //Abajo
                    Toast.makeText(getApplicationContext(), "Abajo", Toast.LENGTH_LONG).show();
                    moverAbajo(matrix);
                    spawnRandom(matrix);
                    updateView(matrix);
                }
            }
        }

    return true;
    }


    public void moverArriba(int[][] matriz){

        for (int i = 1; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                int node = i;
                while(node != 0){
                    if(matriz[node-1][j] == 0) {
                        matriz[node-1][j] = matriz[node][j];
                        matriz[node][j] = 0;
                    }else if(matriz[node-1][j] == matriz[node][j]){
                        matriz[node-1][j] = matriz[node-1][j] + matrix[node][j];
                        matriz[node][j] = 0;
                    }
                    node--;
                }

            }
        }
    }


    public void moverAbajo(int[][] matriz){

        for (int i = matriz.length-2; i >= 0; i--) {
            for (int j = 0; j < matriz[0].length; j++) {
                int node = i;
                while(node != 3){
                    if(matriz[node+1][j] == 0) {
                        matriz[node+1][j] = matriz[node][j];
                        matriz[node][j] = 0;
                    }else if(matriz[node+1][j] == matriz[node][j]){
                        matriz[node+1][j] = matriz[node+1][j] + matrix[node][j];
                        matriz[node][j] = 0;
                    }
                    node++;
                }

            }
        }
    }

    public void moverDerecha(int[][] matriz){

        for (int i = 0; i < matriz.length; i++) {
            for (int j = matriz[0].length-2; j >= 0; j--) {
                int node = j;
                    while(node != 3){
                        if(matriz[i][node+1] == 0) {
                            matriz[i][node + 1] = matriz[i][node];
                            matriz[i][node] = 0;
                        }else if(matriz[i][node+1] == matriz[i][node]){
                            matriz[i][node+1] = matriz[i][node+1] + matrix[i][node];
                            matriz[i][node] = 0;
                        }
                        node++;
                    }

            }
        }
    }

    public void moverIzquierda(int[][] matriz){

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 1; j < matriz[0].length; j++) {
                int node = j;
                while(node != 0){
                    if(matriz[i][node-1] == 0) {
                        matriz[i][node - 1] = matriz[i][node];
                        matriz[i][node] = 0;
                    }else if(matriz[i][node-1] == matriz[i][node]){
                        matriz[i][node-1] = matriz[i][node-1] + matrix[i][node];
                        matriz[i][node] = 0;
                    }
                    node--;
                }

            }
        }
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

    public String beautify(int num, TextView tv){
        if(num == 0){
            tv.setBackgroundColor(Color.GRAY);
            return " ";
        }else if(num == 2){
            tv.setBackgroundColor(Color.WHITE);

        }else if(num == 4){
            tv.setBackgroundColor(Color.YELLOW);
        }else if(num == 8){
            tv.setBackgroundColor(Color.CYAN);
        }else if(num == 16){
            tv.setBackgroundColor(Color.BLUE);
        }

        return String.valueOf(num);
    }

}