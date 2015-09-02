package com.joacimjakobsen.eqlist;

public class EQ {
    private String mag;
    private String place;
    private String eventID;
    private boolean tsunami;
    private String longitude;
    private String latitude;

    public EQ(String mag, String place, String eventID, boolean tsunami, String longitude, String latitude) {
        this.mag = mag;
        this.place = place;
        this.eventID = eventID;
        this.tsunami = tsunami;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public String getMag() {
        return mag;
    }

    public void setMag(String mag) {
        this.mag = mag;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public boolean isTsunami() {
        return tsunami;
    }

    public void setTsunami(boolean tsunami) {
        this.tsunami = tsunami;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "EQ{" +
                "mag='" + mag + '\'' +
                ", place='" + place + '\'' +
                ", eventID='" + eventID + '\'' +
                ", tsunami=" + tsunami +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
