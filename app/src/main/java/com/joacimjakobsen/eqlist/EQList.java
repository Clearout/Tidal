package com.joacimjakobsen.eqlist;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EQList extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private EQ[] eqList;

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

        eqList = new EQ[0];

        JSONEQTask task = new JSONEQTask();
        task.execute();
    }

    // When a list item is touched.
    public void goToEQ(int position) {
        Intent intent = new Intent(this, EQActivity.class);
        intent.putExtra(EQActivity.LAT, eqList[position].getLatitude());
        intent.putExtra(EQActivity.LONG, eqList[position].getLongitude());
        intent.putExtra(EQActivity.MAG, eqList[position].getMag());
        intent.putExtra(EQActivity.PLACE, eqList[position].getPlace());
        startActivity(intent);
    }

    // Update view with fetched EQ data or provide error dialog.
    private void updateListView(EQ[] eqlist) {
        if (eqlist == null || eqlist.length == 0) {
            new AlertDialog.Builder(this)
                    .setTitle("ERROR")
                    .setMessage("Can't find any earthquake data. You may not have an internet connection or the server may be unavailable.")
                    .setNeutralButton("Retry.", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            JSONEQTask task = new JSONEQTask();
                            task.execute();}})
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        if (this.eqList.length <= eqlist.length) {
            this.eqList = eqlist;
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
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            String endTime = dateFormat.format(now);
            Date weekAgo = new Date(now.getTime() - 604800000L); // One week in millis
            String startTime = dateFormat.format(weekAgo);
            String URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=" +
                    startTime + "&endtime=" + endTime + "&limit=20&orderby=magnitude&eventtype=earthquake";
            EQ[] eqlist = null;
            JSONClient client = new JSONClient(URL);
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
            if (eqList.length < 1)
                return false;
            EQ[] tsunamiList = EQ.filterToTsunamis(eqList);
            Log.e("TSUNAMIS " + tsunamiList.length, tsunamiList[0].toString());
            updateListView(tsunamiList);
            return true;
        } else if (id == R.id.action_show_all) {
            if (eqList.length < 1)
                return false;
            updateListView(eqList);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
