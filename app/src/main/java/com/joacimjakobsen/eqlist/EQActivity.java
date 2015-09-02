package com.joacimjakobsen.eqlist;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.internal.GuestCallback;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.core.services.params.Geocode;
import com.twitter.sdk.android.core.services.params.Geocode.Distance;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;
import com.twitter.sdk.android.tweetui.internal.GuestSessionProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.fabric.sdk.android.Fabric;


public class EQActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TWITTER_KEY = "f3m0c5NL3aswPud465U65uV5L";
    private static final String TWITTER_SECRET = "a9uVjfvrFL3fsslHFaw917lM4AZqgvxlNhCfcxT5Ykq9mzA9g5";
    public static final String EQ_ID = "com.joacimjakobsen.eqlist.EQ_ID";
    public static final String LAT = "com.joacimjakobsen.eqlist.LAT";
    public static final String LONG = "com.joacimjakobsen.eqlist.LONG";
    public static final String PLACE = "com.joacimjakobsen.eqlist.PLACE";
    public static final String MAG = "com.joacimjakobsen.eqlist.MAG";
    private String eqID, latitude, longitude, magnitude, place;

    private ListView tweetView;
    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private Tweet[] tweetList;
    final ArrayList<Tweet> tweets = new ArrayList<Tweet>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        eqID = intent.getStringExtra(EQ_ID);
        latitude = intent.getStringExtra(LAT);
        longitude = intent.getStringExtra(LONG);
        magnitude = intent.getStringExtra(MAG);
        place = intent.getStringExtra(PLACE);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));


        tweetView
                = (ListView) findViewById(R.id.tweetList);


        final String query = "https://api.twitter.com/1.1/search/tweets.json?q=&100&result_type=mixed&count=20";


        TwitterCore.getInstance().logIn(this,new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                getTweets(result.data, query);
            }

            @Override
            public void failure(TwitterException e) {
                // DISPLAY ERROR MESSAGE
            }
        });



        Log.e("TWEETS", tweets.toString());



        setContentView(R.layout.activity_eq);
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void getTweets(TwitterSession guestAppSession, String query) {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(guestAppSession);
        SearchService searchService = twitterApiClient.getSearchService();
        Callback<Search> callback = new Callback<Search>() {
            @Override
            public void success(Result<Search> result) {
                tweets.addAll(result.data.tweets);
            }

            @Override
            public void failure(TwitterException e) {

            }
        };
        searchService.tweets(query, new Geocode(Double.parseDouble(latitude),Double.parseDouble(longitude), 100, Geocode.Distance.KILOMETERS),
                null,null,null,null,null,null,null,false,callback);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        googleMap.addMarker(new MarkerOptions()
                .title(magnitude)
                .snippet(place)
                .position(location));
    }
}
