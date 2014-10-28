package no.clap.higster;
import no.clap.higster.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, "LLbpoz4GE3wrTZJPsQwRp9C3AgxRsW8uki0QqubJ", "WQOR89pmKgjZF3m0fH0t3rQbASrPmbNuvTvNZs0b");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public void startCafeteriaActivity(View v) {
        Intent intent = new Intent(this, CafeteriaActivity.class);
        startActivity(intent);
    }

    public void startMapsActivity(View v){
        Intent map = new Intent(this, MapsActivity.class);
        startActivity(map);
    }

    public void startHelpdeskActivity(View v){
        Intent hd = new Intent(this, HelpdeskActivity.class);
        startActivity(hd);
    }

}
