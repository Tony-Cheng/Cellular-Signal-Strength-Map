package none.theradiowavesdetector;

import android.app.Activity;
import android.location.*;
import android.os.Bundle;

import android.util.Log;


public class MainActivity extends Activity {

    static boolean initialized = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));
    }


}
