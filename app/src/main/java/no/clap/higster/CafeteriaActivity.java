package no.clap.higster;
import no.clap.higster.R;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class CafeteriaActivity extends FragmentActivity {
    // Dinner list
    ListView list;
    TextView day;
    TextView food;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    private static String url = "http://www.stud.hig.no/~120217/higger/middag.json";
    // JSON Node names
    private static final String TAG_DINNER = "dinner";
    private static final String TAG_DAY = "day";
    private static final String TAG_FOOD = "food";
    JSONArray android = null;

    // Tabs
    public static Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafeteria);
        setUpTabs();
        appContext = getApplicationContext();

        if (haveNetworkConnection()) {                              // Check for network connection
            new JSONParse().execute();                              // Get the JSON with dinner list
        }
        else {
            Toast.makeText(appContext, "Cannot fetch dinner list", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cafeteria, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.cafeteria_contact) {
            SimpleDialogFragment.createBuilder(this, getSupportFragmentManager())
                    .setTitle(R.string.cafeteria_contact_title).setMessage(R.string.cafeteria_contact_info)
                    .show();
            return true;
        }
        if (id == R.id.cafeteria_facebook) {
            String uri = "fb://page/153122051413387";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void updateDinnerList(MenuItem item) {
        list.invalidateViews();                 // Clear List items and
        oslist.clear();                         // the ArrayList
        new JSONParse().execute();
        Toast.makeText(getApplicationContext(), "Dinner list updated!", Toast.LENGTH_SHORT).show();
    }

    public void setUpTabs() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab DinnerTab = actionBar.newTab().setText("Middagsliste");
        ActionBar.Tab PriceTab = actionBar.newTab().setText("Prisliste");

        Fragment DinnerFragment= new DinnerFragment();
        Fragment PriceFragment = new PriceFragment();

        DinnerTab.setTabListener(new MyTabsListener(DinnerFragment));
        PriceTab.setTabListener(new MyTabsListener(PriceFragment));

        actionBar.addTab(DinnerTab);
        actionBar.addTab(PriceTab);
    }

    // Tabs

    class MyTabsListener implements ActionBar.TabListener {
        public Fragment fragment;

        public MyTabsListener(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            Toast.makeText(CafeteriaActivity.appContext, "Reselected!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.replace(R.id.fragment_container, fragment);
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.remove(fragment);
        }

    }

    // Have network?
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    // Below: JSONparsing and ListView initialization for the external dinnerlist JSON-file



    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            day = (TextView)findViewById(R.id.day);
            food = (TextView)findViewById(R.id.food);
            pDialog = new ProgressDialog(CafeteriaActivity.this);
            pDialog.setMessage(CafeteriaActivity.this.getString(R.string.getting_data));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                // Getting JSON Array from URL
                android = json.getJSONArray(TAG_DINNER);
                for(int i = 0; i < android.length(); i++){
                    JSONObject c = android.getJSONObject(i);
                    // Storing JSON item in a Variable
                    String day = c.getString(TAG_DAY);
                    String food = c.getString(TAG_FOOD);
                    // Adding value HashMap key => value
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_DAY, day);
                    map.put(TAG_FOOD, food);
                    oslist.add(map);
                    list=(ListView)findViewById(R.id.dinnerList);
                    ListAdapter adapter = new SimpleAdapter(CafeteriaActivity.this, oslist,
                            R.layout.list_v,
                            new String[] { TAG_DAY, TAG_FOOD }, new int[] {
                    R.id.day, R.id.food});
                    list.setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
              }
        }
    }
}
