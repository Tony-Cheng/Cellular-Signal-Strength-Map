package none.theradiowavesdetector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static none.theradiowavesdetector.R.id.button4;

public class Setting extends AppCompatActivity {

    static boolean initialized = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Button button2 = (Button) findViewById(R.id.button4);

//        if(initialized == false){
//            initialized = true;
//            Calculations.init();
//        }
        button2.setOnClickListener(new View.OnClickListener(){


            @Override

            public void onClick(View v) {

                goToMain();

            }

        });
    }
    private void goToMain() {

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

    }

}
