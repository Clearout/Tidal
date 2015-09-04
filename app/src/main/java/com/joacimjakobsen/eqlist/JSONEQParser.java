package com.joacimjakobsen.eqlist;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONEQParser {

    public static EQ[] getEQ(String data) {
        try {
            JSONObject jFeatures = new JSONObject(data);
            JSONArray jArr = jFeatures.getJSONArray("features");
            EQ[] eqList = new EQ[jArr.length()];
            JSONObject jObj = null;
            JSONObject jProp = null;
            JSONArray jCoors = null;
            for (int i=0; i<jArr.length(); i++) {
                jObj = jArr.getJSONObject(i);
                jProp = jObj.getJSONObject("properties");
                jCoors = jObj.getJSONObject("geometry").getJSONArray("coordinates");

                eqList[i] = new EQ(
                        jProp.getString("mag"),
                        jProp.getString("place"),
                        jObj.getString("id"),
                        jProp.getInt("tsunami")==1,
                        jCoors.getString(0),
                        jCoors.getString(1),
                        jProp.getString("mmi")
                );
            }
            return eqList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
