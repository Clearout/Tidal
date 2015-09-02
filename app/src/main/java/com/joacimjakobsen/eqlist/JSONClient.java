package com.joacimjakobsen.eqlist;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONClient {
    private String URL;

    public JSONClient(String URL) {
        this.URL = URL;
    }

    public String getList() {
        HttpURLConnection con = null;
        InputStream in = null;

        try {
            // Set up connection.
            con = (HttpURLConnection) (new URL(URL)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Read response.
            StringBuffer buffer = new StringBuffer();
            in = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            // Close and return response.
            in.close();
            con.disconnect();
            return buffer.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try { in.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
         }
        return null;
    }
}

