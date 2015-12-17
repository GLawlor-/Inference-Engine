package com.adam4.cristian.inferenceengine;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.sql.DriverManager.*;

/**
 * Created by Cristian on 12/17/2015.
 */
public class ConnectionClass {

    String ip = "52.3.139.221";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db = "ExpertSystems";
    String un = "ES";
    String password = "Dehzangi2015";

    ConnectionClass()
    {
        try
        {
            ip = InetAddress.getByName(new URL("expertsystems.clvmhhfuon3y.us-east-1.rds.amazonaws.com/").getHost()).getHostAddress();
        }
        catch(MalformedURLException e)
        {
            Log.e("ERRO", e.getMessage());
        }
        catch(UnknownHostException e)
        {
            Log.e("ERRO", e.getMessage());
        }
    }

    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }
}