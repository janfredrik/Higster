package no.clap.higster;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
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

import no.clap.higster.R;
import android.R;


public class CafeteriaActivity extends FragmentActivity {
    ListView list;
    TextView day;
    TextView food;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    //URL to get JSON Array
    private static String url = "http://www.stud.hig.no/~120217/higger/middag.json";
    //JSON Node names
    private static final String TAG_DINNER = "dinner";
    private static final String TAG_DAY = "day";
    private static final String TAG_FOOD = "food";
    JSONArray android = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpTabs();
        setContentView(R.layout.activity_cafeteria);

        new JSONParse().execute();                              // Get the JSON with dinner list
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

        ActionBar.Tab mainTab = actionBar.newTab().setText("Middagsliste").setTabListener(new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            //    Toast.makeText(getApplicationContext(), "Hei", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        });

        actionBar.addTab(mainTab);

        ActionBar.Tab priceTab = actionBar.newTab().setText("Priser").setTabListener(new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        });

        actionBar.addTab(priceTab);
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
