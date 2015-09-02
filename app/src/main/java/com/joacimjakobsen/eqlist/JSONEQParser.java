package com.joacimjakobsen.eqlist;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.joacimjakobsen.eqlist.EQ;
/**
 * Created by joaci_000 on 25-Aug-15.
 */
public class JSONEQParser {

    public static EQ[] getEQ(String data) {
        try {
            JSONObject jFeatures = new JSONObject(data);
            JSONArray jArr = jFeatures.getJSONArray("features");
            EQ[] eqlist = new EQ[jArr.length()];
            JSONObject jObj = null;
            JSONObject jProp = null;
            JSONArray jCoors = null;
            for (int i=0; i<jArr.length(); i++) {
                jObj = jArr.getJSONObject(i);
                jProp = jObj.getJSONObject("properties");
                jCoors = jObj.getJSONObject("geometry").getJSONArray("coordinates");

                eqlist[i] = new EQ(
                        jProp.getString("mag"),
                        jProp.getString("place"),
                        jObj.getString("id"),
                        jProp.getInt("tsunami")==1,
                        jCoors.getString(0),
                        jCoors.getString(1)
                );
               // Log.d("DATA " + i, eqlist[i].toString());
            }
            return eqlist;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
