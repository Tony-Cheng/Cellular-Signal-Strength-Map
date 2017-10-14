package none.theradiowavesdetector;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.List;

/**
 * Created by T28 on 2017-02-16.
 */

public class GameView extends SurfaceView {

    private Bitmap bmp;
    private Bitmap bmp2;
    private Bitmap bmp3;
    private Bitmap bmp4;
    private Bitmap bmp5;
    private Bitmap bmp6;
    private Bitmap bmp7;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private TelephonyManager telephonyManager;
    private PhoneStateListener phoneStateListener;
    int mSignalStrength;
    static int[][] toDraw;
    static int[][] size;
    static int longitude;
    static int latitude;
    private int curPosX;
    private int curPosY;
    private int oriPosX;
    private int oriPosY;
    Context context;

    public void requestLocationChange() {
        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                locationListener.onLocationChanged(getLastKnownLocation());
                updateIntensity();
            }
        });
    }
    private void updateIntensity(){
        if(oriPosX == 0 && oriPosY == 0) {
            oriPosX = GameView.longitude;
            oriPosY = GameView.latitude;
        }
        curPosX = GameView.longitude - oriPosX + 500;
        curPosY = GameView.latitude - oriPosY + 500;
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
           for(int i =  curPosX - 10; i <= Math.min(999,curPosX + 10); i++){
                for(int j = curPosY - 10; j <= Math.min(999,curPosY + 10);j++) {
                    if (Math.abs(i - curPosX)*Math.abs(i - curPosX) + Math.abs(j-curPosY)*Math.abs(j-curPosY) <= 100){
                        toDraw[i][j] += mSignalStrength;
                        size[i][j]++;
                    }
                }
            }
        }
    }
    private Location getLastKnownLocation() {

        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }


    public GameView(Context context){
        super(context);
        phoneStateListener = new PhoneStateListener(){
          @Override
          public void onSignalStrengthsChanged(SignalStrength signalStrength){
              super.onSignalStrengthsChanged(signalStrength);
              mSignalStrength = signalStrength.getLevel();
          }
        };
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        locationManager = (LocationManager)context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        this.context = context;
        if(toDraw == null)
            toDraw = new int[1000][1000];
        if(size == null)
            size = new int[1000][10000];
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        //locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                GameView.latitude = (int) (location.getLatitude()*100000);
                GameView.longitude = (int) (location.getLongitude()*100000);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }

        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,(long) (10),(float)(0.1),locationListener);
        holder.addCallback(new SurfaceHolder.Callback(){

            @Override
            public void surfaceDestroyed(SurfaceHolder holder){

            }

            @Override
            public void surfaceCreated(SurfaceHolder holder){
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

            }
        });


        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.c8b4031a146e58f81625af9276cea3);
        bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.squarexxl);
        bmp3 = BitmapFactory.decodeResource(getResources(), R.drawable.greysquare);
        bmp4 = BitmapFactory.decodeResource(getResources(), R.drawable.square512);
        bmp5 = BitmapFactory.decodeResource(getResources(), R.drawable.redsvg);
        bmp6 = BitmapFactory.decodeResource(getResources(), R.drawable.whitesquare);
        bmp7 = BitmapFactory.decodeResource(getResources(), R.drawable.logoorange1234mediathequel);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp3,0,0,null);
        canvas.drawBitmap(bmp3,100,0,null);
        canvas.drawBitmap(bmp3,100,100,null);
        canvas.drawBitmap(bmp3,0,100,null);
        for(int i = 0; i < toDraw.length; i++){
            for(int j = 0; j < toDraw.length; j++){
                if (Math.abs(i - curPosX) * Math.abs(i - curPosX) + Math.abs(j - curPosY) * Math.abs(j - curPosY) <= 500) {
                    canvas.drawBitmap(bmp2, i + 40, j + 40, null);
                }
                else if (size[i][j] == 0) {
                    canvas.drawBitmap(bmp6, i + 40, j + 40, null);
                } else if (1.0*toDraw[i][j]/size[i][j] + 0.5 >= 4) {
                    canvas.drawBitmap(bmp, i + 40, j + 40, null);
                } else if (1.0*toDraw[i][j]/size[i][j] + 0.5 >= 3) {
                    canvas.drawBitmap(bmp4, i + 40, j + 40, null);
                } else if (1.0*toDraw[i][j]/size[i][j] + 0.5 >= 2) {
                    canvas.drawBitmap(bmp7, i + 40, j + 40, null);
                } else {
                    canvas.drawBitmap(bmp5, i + 40, j + 40, null);
                }
            }
        }
        if(curPosX < 1000 && curPosY < 1000 && curPosX >= 0 && curPosY >= 0)
        for(int i = 0; i < 1000; i++){
            for(int j = 0; j < 1000; j++){
                if(Math.abs(i-500)*Math.abs(i -500) + Math.abs(j - 500)*Math.abs(j - 500) <= 30000){
                    if (mSignalStrength == 0) {
                        canvas.drawBitmap(bmp6, i + 40, j + 900, null);
                    } else if (mSignalStrength >= 4) {
                        canvas.drawBitmap(bmp, i + 40, j + 900, null);
                    } else if (mSignalStrength>= 3) {
                        canvas.drawBitmap(bmp4, i + 40, j + 900, null);
                    } else if (mSignalStrength>= 2) {
                        canvas.drawBitmap(bmp7, i + 40, j + 900, null);
                    } else {
                        canvas.drawBitmap(bmp5, i + 40, j + 900, null);
                    }
                }
            }

        }
    }
}
