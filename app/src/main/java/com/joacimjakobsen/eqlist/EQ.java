package com.joacimjakobsen.eqlist;

import android.graphics.Color;

public class EQ {
    private String mag;
    private String place;
    private String eventID;
    private boolean tsunami;
    private String longitude;
    private String latitude;
    private int color;

    public EQ(String mag, String place, String eventID, boolean tsunami, String longitude, String latitude, String mmi) {
        this.mag = mag;
        this.place = place;
        this.eventID = eventID;
        this.tsunami = tsunami;
        this.longitude = longitude;
        this.latitude = latitude;
        int numMmi;
        if (mmi.compareTo("null") == 0) {
            numMmi = 0;
        } else {
            try {
                numMmi = Integer.parseInt(mmi);
            } catch (Exception e) {
                numMmi = 0;
            }
        }
        color = getMmiColor(numMmi);
    }

    public static EQ[] filterToTsunamis(EQ[] eqlist) {
        int tsunamiCount = 0;
        for (int i=0; i<eqlist.length; i++) {
            if (eqlist[i].tsunami == true)
                tsunamiCount++;
        }
        EQ[] tsunamiList = new EQ[tsunamiCount];
        int j = 0;
        for (int i=0; i<eqlist.length; i++) {
            if (eqlist[i].tsunami == true) {
                tsunamiList[j] = eqlist[i];
                j++;
            }
        }
        return tsunamiList;
    }

    public int getColor() { return color; }

    public String getMag() {
        return mag;
    }

    public String getPlace() { return place; }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() { return latitude; }

    @Override
    public String toString() {
        return getMag() + "  -  " + getPlace();
    }

    public int getMmiColor(int mmi) {
        switch (mmi) {
            default:
                return Color.WHITE;
            case 2:
                return Color.rgb(135, 206, 250);
            case 3:
                return Color.rgb(135, 206, 250);
            case 4:
                return Color.rgb(64, 224, 208);
            case 5:
                return Color.rgb(0, 255, 127);
            case 6:
                return Color.YELLOW;
            case 7:
                return Color.rgb(255, 165, 0);
            case 8:
                return Color.rgb(255, 140, 0);
            case 9:
                return Color.RED;
            case 10:
                return Color.rgb(178, 34, 34);
        }
    }
}
