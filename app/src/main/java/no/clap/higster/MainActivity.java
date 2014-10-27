package no.clap.higster;
import no.clap.higster.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
