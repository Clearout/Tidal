package com.joacimjakobsen.eqlist;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;

public class EQList extends AppCompatActivity {


    private ListView listView;
    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private EQ[] eqlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_eqlist);
        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                goToEQ(position);
            }
        });

        eqlist = new EQ[0]; // Change to prev saved eqlist?
        JSONEQTask task = new JSONEQTask();
        task.execute();
    }

    // When a list item is touched.
    public void goToEQ(int position) {
        Intent intent = new Intent(this, EQActivity.class);
        intent.putExtra(EQActivity.EQ_ID, eqlist[position].getEventID());
        intent.putExtra(EQActivity.LAT, eqlist[position].getLatitude());
        intent.putExtra(EQActivity.LONG, eqlist[position].getLongitude());
        intent.putExtra(EQActivity.MAG, eqlist[position].getMag());
        intent.putExtra(EQActivity.PLACE, eqlist[position].getPlace());
        startActivity(intent);
    }

    private void updateListView(EQ[] eqlist) {
        if (this.eqlist.length <= eqlist.length) {
            this.eqlist = eqlist;
        }
        listItems.clear();
        for (int i=0; i<eqlist.length; i++) {
            listItems.add(eqlist[i].getMag() + "  -  " + eqlist[i].getPlace());
        }

        adapter = new ArrayAdapter<String>(this, R.layout.listview_item_row, listItems);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class JSONEQTask extends AsyncTask<String, Void, EQ[]>{

        @Override
        protected EQ[] doInBackground(String... params) {

            String URL = "";
            EQ[] eqlist = null;
            JSONClient client = new JSONClient("http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2015-08-18&endtime=2015-08-25&limit=20&orderby=magnitude&eventtype=earthquake");
            String data = client.getList();

            try {
                eqlist = JSONEQParser.getEQ(data);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return eqlist;
        }

        @Override
        protected void onPostExecute(EQ[] eqlist) {
            super.onPostExecute(eqlist);
            // Add fields to all buttons
            if (eqlist == null) {
                Log.e("JSON ERR", "Can't load eqlist");
                return;
            }

            updateListView(eqlist);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_eqlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_show_tsunami) {
            EQ[] tsunamiList = EQ.filterToTsunamis(eqlist);
            Log.e("TSUNAMIS " + tsunamiList.length, tsunamiList[0].toString());
            updateListView(tsunamiList);
            return true;
        } else if (id == R.id.action_show_all) {
            updateListView(eqlist);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
