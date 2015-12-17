package com.adam4.cristian.inferenceengine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    private Spinner Test_Type, Column1_Test, C1_Data_Type, Column2_Test, C2_Data_Type;
    Button button;

    private String test, col1, dist1, col2, dist2;

    private static final String[]Test_TypeOptions = {"MEAN", "STDDEV", "VARIANCE", "CORR", "TTEST"};
    private static final String[]TestOptions = {"GSR_kOhm", "THETA", "BETA"};
    private static final String[]Data_TypeOptions = {"Normal", "Phone"};

    ConnectionClass connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectionClass = new ConnectionClass();



        //*************************************************************
        // Setup GUI menu  ********************************************
        //*************************************************************

        Test_Type = (Spinner)findViewById(R.id.Test_Type);
        ArrayAdapter<String> Test_TypeAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,Test_TypeOptions);
        Test_TypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Test_Type.setAdapter(Test_TypeAdapter);
        Test_Type.setOnItemSelectedListener(this);

        Column1_Test = (Spinner)findViewById(R.id.Column1_Test);
        ArrayAdapter<String> Column1_TestAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,TestOptions);
        Column1_TestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Column1_Test.setAdapter(Column1_TestAdapter);
        Column1_Test.setOnItemSelectedListener(this);

        C1_Data_Type = (Spinner)findViewById(R.id.C1_Data_Type);
        ArrayAdapter<String> C1_Data_TypeAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,Data_TypeOptions);
        C1_Data_TypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        C1_Data_Type.setAdapter(C1_Data_TypeAdapter);
        C1_Data_Type.setOnItemSelectedListener(this);

        Column2_Test = (Spinner)findViewById(R.id.Column2_Test);
        ArrayAdapter<String> Column2_TestAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,TestOptions);
        Column2_TestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Column2_Test.setAdapter(Column2_TestAdapter);
        Column2_Test.setOnItemSelectedListener(this);
        Column2_Test.setVisibility(View.GONE);

        C2_Data_Type = (Spinner)findViewById(R.id.C2_Data_Type);
        ArrayAdapter<String> C2_Data_TypeAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,Data_TypeOptions);
        C2_Data_TypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        C2_Data_Type.setAdapter(C2_Data_TypeAdapter);
        C2_Data_Type.setOnItemSelectedListener(this);
        C2_Data_Type.setVisibility(View.GONE);

        button = (Button) findViewById(R.id.SubButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new GetResult().execute("");
            }
        });



    }

    public void onNothingSelected(AdapterView<?> parent)
    {
        // needed to keep compiler happy
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        Spinner spinner = (Spinner) parent;
        if ((R.id.Test_Type) == spinner.getId())
        {
            switch (position) {
                case 0:
                    test = "MEAN";
                    C2_Data_Type.setVisibility(View.GONE);
                    Column2_Test.setVisibility(View.GONE);
                    break;
                case 1:
                    test = "STDDEV";
                    C2_Data_Type.setVisibility(View.GONE);
                    Column2_Test.setVisibility(View.GONE);
                    break;
                case 2:
                    test = "VARIANCE";
                    C2_Data_Type.setVisibility(View.GONE);
                    Column2_Test.setVisibility(View.GONE);
                case 3:
                    test = "CORR";
                    C2_Data_Type.setVisibility(View.VISIBLE);
                    Column2_Test.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    test = "TTEST";
                    C2_Data_Type.setVisibility(View.VISIBLE);
                    Column2_Test.setVisibility(View.VISIBLE);
                    break;
            }// end switch
        }// end if
        else if ((R.id.Column1_Test) == spinner.getId())
        {
            switch (position) {
                case 0:
                    col1 = "GSR_kOhm";
                    break;
                case 1:
                    col1 = "THETA";
                    break;
                case 2:
                    col1 = "BETA";
                    break;
            }// end switch
        }// end if
        else if ((R.id.Column2_Test) == spinner.getId())
        {
            switch (position) {
                case 0:
                    col2 = "GSR_kOhm";
                    break;
                case 1:
                    col2 = "THETA";
                    break;
                case 2:
                    col2 = "BETA";
                    break;
            }// end switch
        }// end if
        else if ((R.id.C2_Data_Type) == spinner.getId())
        {
            switch (position) {
                case 0:
                    dist2 = "Normal";
                    break;
                case 1:
                    dist2 = "Driving";
                    break;
            }// end switch
        }// end if
        else if ((R.id.C1_Data_Type) == spinner.getId())
        {
            switch (position) {
                case 0:
                    dist1 = "Normal";
                    break;
                case 1:
                    dist1 = "Driving";
                    break;
            }// end switch
        }// end if
    }


    private class GetResult extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... urls) {
            String z = "unset";
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "";
                    PreparedStatement ps;
                    if (test.equals("MEAN") || test.equals("STDDEV") || test.equals("VARIANCE")) {
                        query = "SELECT TOP 1 [ID]\n" +
                                "      ,[Result]\n" +
                                "      ,[Comments]\n" +
                                "  FROM [EnterpriseExpertSystem].[dbo].[MatLabResults]\n" +
                                "  WHERE Test_Type = ? AND Column1_Test  LIKE ? AND C1_Data_Type  LIKE ?;";
                        ps = con.prepareStatement(query);
                        ps.setString(1, test);
                        ps.setString(2, col1);
                        ps.setString(3, dist1);
                    }
                    else
                    {
                        query = "SELECT TOP 1 [ID]\n" +
                                "      ,[Result]\n" +
                                "      ,[Comments]\n" +
                                "  FROM [EnterpriseExpertSystem].[dbo].[MatLabResults]\n" +
                                "  WHERE Test_Type = ? AND Column1_Test  LIKE ? AND C1_Data_Type  LIKE ? AND Column2_Test  LIKE ? AND C2_Data_Type  LIKE ?;";
                        ps = con.prepareStatement(query);
                        ps.setString(1, test);
                        ps.setString(2, col1);
                        ps.setString(3, dist1);
                        ps.setString(4, col2);
                        ps.setString(5, dist2);
                    }
                    ResultSet rs = ps.executeQuery();
                    z = "";
                    while (rs.next()) {
                        z += rs.getString("Result");
                        z += "\n"; // add newline separator
                        z += rs.getString("Comments");
                    }
                    if (z.endsWith("null")) {
                        z = z.substring(0, z.length() - 5);
                    }
                    if (z.equals(""))
                    {
                        z = "Results not found";
                    }
                }
            } catch (Exception ex) {
                z = "Error retrieving data from table";
            }
            //Log.e("ERRO", "Toaster: " + z);
            return z;
        }

        @Override
        protected void onPostExecute(String result) {
       //     create toast with results
       //     Log.e("ERRO", "toast: " + result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();


        }
    } // end async task class

}

