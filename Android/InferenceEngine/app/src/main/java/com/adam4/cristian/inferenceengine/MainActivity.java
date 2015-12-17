package com.adam4.cristian.inferenceengine;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner user, session, distraction, param1, param2, param3;
    ConnectionClass connectionClass;

    private static final String[]userOptions = {"John Doe", "Negative Nancy", "Positive Polly"};
    private static final String[]sessionOptions = {"Session 1", "Session 2", "Session 3"};
    private static final String[]distractionOptions = {"Normal Driving", "Spelling Questions", "Relaxing Music"};
    private static final String[]param1Options = {"Steering Wheel Angle", "Alpha wave", "Right Hand Acceleration X"};
    private static final String[]param2Options = {"Steering Wheel Angle", "Alpha wave", "Right Hand Acceleration X"};
    private static final String[]param3Options = {"Steering Wheel Angle", "Alpha wave", "Right Hand Acceleration X"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectionClass = new ConnectionClass();


        //*************************************************************
        // Setup GUI menu  ********************************************
        //*************************************************************

        user = (Spinner)findViewById(R.id.User);
        ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,userOptions);
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user.setAdapter(userAdapter);
        user.setOnItemSelectedListener(this);

        session = (Spinner)findViewById(R.id.Session);
        ArrayAdapter<String> sessionAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,sessionOptions);
        sessionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        session.setAdapter(sessionAdapter);
        session.setOnItemSelectedListener(this);

        distraction = (Spinner)findViewById(R.id.Distraction);
        ArrayAdapter<String> distractionAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,distractionOptions);
        distractionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distraction.setAdapter(distractionAdapter);
        distraction.setOnItemSelectedListener(this);

        param1 = (Spinner)findViewById(R.id.Param1);
        ArrayAdapter<String> param1Adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,param1Options);
        param1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        param1.setAdapter(param1Adapter);
        param1.setOnItemSelectedListener(this);

        param2 = (Spinner)findViewById(R.id.Param2);
        ArrayAdapter<String> param2Adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,param2Options);
        param2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        param2.setAdapter(param2Adapter);
        param2.setOnItemSelectedListener(this);
        param2.setVisibility(View.GONE);

        param3 = (Spinner)findViewById(R.id.Param3);
        ArrayAdapter<String> param3Adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,param3Options);
        param3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        param3.setAdapter(param3Adapter);
        param3.setOnItemSelectedListener(this);
        param3.setVisibility(View.GONE);

    }

    public void onNothingSelected(AdapterView<?> parent)
    {
        // needed to keep compiler happy
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // 1st item selected
                break;
            case 1:
                // 2nd item selected
                param2.setVisibility(View.VISIBLE);
                break;
            case 2:
                // 3rd item selected
                param3.setVisibility(View.VISIBLE);
                break;


        }
    }

    public class GetResult extends AsyncTask<String, String, String> {
        String z = "";

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "SELECT TOP 1 [ID]\n" +
                            "      ,[Result]\n" +
                            "      ,[Result2]\n" +
                            "  FROM [EnterpriseExpertSystem].[dbo].[MatLabResults]\n" +
                            "  WHERE Test_Type = ? AND Column1_Test like ? AND Column2_Test like ?;";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    ArrayList data1 = new ArrayList();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("A", rs.getString("Id"));
                        datanum.put("B", rs.getString("ProName"));
                        datanum.put("C", rs.getString("ProDesc"));
                    }
                    z = "Success";
                }
            } catch (Exception ex) {
                z = "Error retrieving data from table";
            }
            return z;
        }
    }
}

