package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import android.os.Handler;
import java.util.Random;

public class easyMode extends View {
    private Bitmap bmGrass1, bmGrass2, bmSnake, bmPrey;
    public static int sizeOfMap = 75*Constants.SCREEN_WIDTH/1080;
    private int h=21, w=12;
    private ArrayList<Grass> arrGrass = new ArrayList<>();
    private Snake snake;
    private boolean move=false;
    private float mx,my;
    public static boolean isPlaying = false;
    public static int score2 = 0, bestScore2 = 0;
    private Context context;
    private Handler handler;
    private Runnable r;
    private Prey prey;

    public easyMode(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        SharedPreferences sp = context.getSharedPreferences("gamesetting", Context.MODE_PRIVATE);
        if(sp!=null){
            bestScore2 = sp.getInt("bestscore2",0);
        }
        bmGrass1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.textile);
        bmGrass1 = Bitmap.createScaledBitmap(bmGrass1, sizeOfMap, sizeOfMap, true);
        bmGrass2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.textile1);
        bmGrass2 = Bitmap.createScaledBitmap(bmGrass2, sizeOfMap, sizeOfMap, true);
        bmSnake = BitmapFactory.decodeResource(this.getResources(), R.drawable.snake1);
        bmSnake = Bitmap.createScaledBitmap(bmSnake, 14 * sizeOfMap, sizeOfMap, true);
        bmPrey = BitmapFactory.decodeResource(this.getResources(), R.drawable.prey);
        bmPrey = Bitmap.createScaledBitmap(bmPrey, sizeOfMap, sizeOfMap, true);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if ((i + j) % 2 == 0) {
                    arrGrass.add(new Grass(bmGrass1, j * sizeOfMap + Constants.SCREEN_WIDTH / 2 - (w / 2) * sizeOfMap,
                            i * sizeOfMap + 75 * Constants.SCREEN_HEIGHT / 1920, sizeOfMap, sizeOfMap));
                } else {
                    arrGrass.add(new Grass(bmGrass2, j * sizeOfMap + Constants.SCREEN_WIDTH / 2 - (w / 2) * sizeOfMap,
                            i * sizeOfMap + 75 * Constants.SCREEN_HEIGHT / 1920, sizeOfMap, sizeOfMap));
                }
            }
        }
        snake = new Snake(bmSnake, arrGrass.get(126).getX(), arrGrass.get(126).getY(), 4);
        prey = new Prey(bmPrey, arrGrass.get(randomPrey()[0]).getX(), arrGrass.get(randomPrey()[1]).getY());
        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int a = event.getActionMasked();
        switch (a){
            case MotionEvent.ACTION_MOVE:{
                if(move == false){
                    mx=event.getX();
                    my=event.getY();
                    move = true;
                }else{
                    if(mx - event.getX()>75*Constants.SCREEN_WIDTH/1080 && !snake.isMove_right()){
                        mx=event.getX();
                        my=event.getY();
                        this.snake.setMove_left(true);
                        isPlaying = true;
                        MainActivity.img_swipe.setVisibility(INVISIBLE);
                    }else if(event.getX()-mx>75*Constants.SCREEN_WIDTH/1080 && !snake.isMove_left()){
                        mx=event.getX();
                        my=event.getY();
                        this.snake.setMove_right(true);
                        isPlaying = true;
                        MainActivity.img_swipe.setVisibility(INVISIBLE);
                    }else if(my - event.getY()>75*Constants.SCREEN_WIDTH/1080 && !snake.isMove_bottom()){
                        mx=event.getX();
                        my=event.getY();
                        this.snake.setMove_top(true);
                        isPlaying = true;
                        MainActivity.img_swipe.setVisibility(INVISIBLE);
                    }else if(event.getY()-my>75*Constants.SCREEN_WIDTH/1080 && !snake.isMove_top()){
                        mx=event.getX();
                        my=event.getY();
                        this.snake.setMove_bottom(true);
                        isPlaying = true;
                        MainActivity.img_swipe.setVisibility(INVISIBLE);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:{
                mx=0;
                my=0;
                move = false;
                break;
            }
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        canvas.drawColor(0xFF018786);
        for(int i = 0; i < arrGrass.size(); i++){
            canvas.drawBitmap(arrGrass.get(i).getBm(), arrGrass.get(i).getX(), arrGrass.get(i).getY(), null);
        }
        if(isPlaying){
            snake.update();
            if(snake.getArrPartSnake().get(0).getX() < this.arrGrass.get(0).getX()
                    ||snake.getArrPartSnake().get(0).getY() < this.arrGrass.get(0).getY()
                    ||snake.getArrPartSnake().get(0).getY()+sizeOfMap>this.arrGrass.get(this.arrGrass.size()-1).getY() + sizeOfMap
                    ||snake.getArrPartSnake().get(0).getX()+sizeOfMap>this.arrGrass.get(this.arrGrass.size()-1).getX() + sizeOfMap){
                gameOver();
            }
            for (int i = 1; i < snake.getArrPartSnake().size(); i++){
                if (snake.getArrPartSnake().get(0).getrBody().intersect(snake.getArrPartSnake().get(i).getrBody())){
                    gameOver();
                }
            }
        }
        snake.drawSnake(canvas);
        prey.draw(canvas);
        if(snake.getArrPartSnake().get(0).getrBody().intersect(prey.getR())){
            prey.reset(arrGrass.get(randomPrey()[0]).getX(), arrGrass.get(randomPrey()[1]).getY());
            snake.addPart();
            score2++;
            MainActivity2.txt_score2.setText(score2+"");
            if(score2 > bestScore2){
                bestScore2 = score2;
                SharedPreferences sp = context.getSharedPreferences("gamesetting2", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("bestscore2", bestScore2);
                editor.apply();
                MainActivity2.txt_best_score2.setText(bestScore2+"");
            }
        }
        handler.postDelayed(r, 100);
    }

    private void gameOver() {
        isPlaying = false;
        MainActivity2.dialogScore2.show();
        MainActivity2.txt_dialog_best_score2.setText(bestScore2+"");
        MainActivity2.txt_dialog_score2.setText(score2+"");
    }

    public void reset(){
        for(int i = 0; i < h; i++){
            for (int j = 0; j < w; j++){
                if((j+i)%2==0){
                    arrGrass.add(new Grass(bmGrass1, j*bmGrass1.getWidth() + Constants.SCREEN_WIDTH/2 - (w/2)*bmGrass1.getWidth(), i*bmGrass1.getHeight()+75*Constants.SCREEN_HEIGHT/1920, bmGrass1.getWidth(), bmGrass1.getHeight()));
                }else{
                    arrGrass.add(new Grass(bmGrass2, j*bmGrass2.getWidth() + Constants.SCREEN_WIDTH/2 - (w/2)*bmGrass2.getWidth(), i*bmGrass2.getHeight()+75*Constants.SCREEN_HEIGHT/1920, bmGrass2.getWidth(), bmGrass2.getHeight()));
                }
            }
        }
        snake = new Snake(bmSnake, arrGrass.get(126).getX(),arrGrass.get(126).getY(), 4);
        prey = new Prey(bmPrey, arrGrass.get(randomPrey()[0]).getX(), arrGrass.get(randomPrey()[1]).getY());
        easyMode.score2 = 0;
    }

    public int[] randomPrey(){
        int []xy = new int[2];
        Random r = new Random();
        xy[0] = r.nextInt(arrGrass.size() - 1);
        xy[1] = r.nextInt(arrGrass.size() - 1);
        Rect rect = new Rect(arrGrass.get(xy[0]).getX(), arrGrass.get(xy[1]).getY(), arrGrass.get(xy[0]).getX()+sizeOfMap, arrGrass.get(xy[1]).getY()+sizeOfMap);
        boolean check = true;
        while(check){
            check = false;
            for (int i=0; i<snake.getArrPartSnake().size(); i++){
                if(rect.intersect(snake.getArrPartSnake().get(i).getrBody())){
                    check = true;
                    xy[0] = r.nextInt(arrGrass.size()-1);
                    xy[1] = r.nextInt(arrGrass.size()-1);
                    rect = new Rect(arrGrass.get(xy[0]).getX(), arrGrass.get(xy[1]).getY(), arrGrass.get(xy[0]).getX()+sizeOfMap, arrGrass.get(xy[1]).getY()+sizeOfMap);
                }
            }
        }
        return xy;
    }
}
