package none.theradiowavesdetector;

import android.graphics.Canvas;

/**
 * Created by T28 on 2017-02-16.
 */

public class GameLoopThread extends Thread {
    private GameView view;
    private boolean running = false;
    static long startTime;
    static long count;
    public GameLoopThread(GameView view){
        this.view = view;
    }
    public void setRunning(boolean run){
        running = run;
    }

    @Override
    public void run(){
        long tickPS = 1000/20;
        long sleepTime;
        count = 1;
        while(running){
            Canvas c = null;
            startTime = System.currentTimeMillis();
            view.requestLocationChange();
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    view.onDraw(c);
                view.getHolder().unlockCanvasAndPost(c);
            }
            sleepTime = tickPS - (System.currentTimeMillis() - startTime);
            try{
                if(sleepTime > 0)
                    sleep(sleepTime);
            }
            catch (Exception e){
            }
            count %= 6;
            count++;
        }

    }
}
