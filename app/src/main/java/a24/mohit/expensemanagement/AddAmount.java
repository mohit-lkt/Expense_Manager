package a24.mohit.expensemanagement;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class AddAmount extends AppCompatActivity {
    EditText etreceiptNo_add,etamount_add,etremark_add;
    EditText etDate_add;
    Button btnsave_add;
    android.support.v7.widget.Toolbar toolbar;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_amount);
       // checkConnectionGET_insta();
        etDate_add = (EditText)findViewById(R.id.etDate_add);
        etreceiptNo_add = (EditText)findViewById(R.id.etReceiptNo_add);
        etremark_add = (EditText)findViewById(R.id.etremark_add);
        etamount_add = (EditText)findViewById(R.id.etamount_add);
        btnsave_add = (Button)findViewById(R.id.btnsave_add);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBaar);
        toolbar.setTitle("Insta Amount");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddAmount.this,DashBoard.class));
            }
        });
        btnsave_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String receipt = etreceiptNo_add.getText().toString();
                String date = etDate_add.getText().toString();
                String  amount= etamount_add.getText().toString();
                String remark = etremark_add.getText().toString();
                if(!receipt.equals("")&&!date.equals("")&&!amount.equals("")&&!remark.equals("")){
                    checkConnection_POST_insta();
                }else{
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast_layout,
                            (ViewGroup) findViewById(R.id.toast_layout_root));

                    text = (TextView) layout.findViewById(R.id.tvtoast);
                    text.setText("Please fill The details first");

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM, 0, 50);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }

                 /* Intent intent = new Intent(AddAmount.this,InstaAmount.class);
                  intent.putExtra("receipt",receipt);
                intent.putExtra("date",date);
                intent.putExtra("amount",amount);
                intent.putExtra("remark",remark);
                startActivity(intent);*/
               // Toast.makeText(AddAmount.this, "Successfully Saved", Toast.LENGTH_SHORT).show();

            }
        });

        final Calendar myCalendar = Calendar.getInstance();
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etDate_add.setText(sdf.format(myCalendar.getTime()));

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                etDate_add.setText(sdf.format(myCalendar.getTime()));
            }};

        etDate_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(AddAmount.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    class SendJsonDataToServerInsta extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(AddAmount.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://inquisitor.tech/apis/insta_payment_transaction");

                String receipt = etreceiptNo_add.getText().toString();
                String date = etDate_add.getText().toString();
                String  amount= etamount_add.getText().toString();
                String remark = etremark_add.getText().toString();


                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("compcode","INV178191");
                jsonObject2.put("Amount",amount);
                jsonObject2.put("Remark",remark);
                jsonObject2.put("DocDate",date);





                Log.e("jsonObject", jsonObject2.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(jsonObject2));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {



                    BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        result.append(line);
                    }
                    r.close();
                    return result.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                JSONObject jsonObject = null;
                Log.e("SendJsonDataToServer>>>", result.toString());
                try {


                    // result=getJSONUrl(URL);  //<< get json string from server
                    jsonObject = new JSONObject(result);
                    String response = jsonObject.getString("response");
                    String Message = jsonObject.getString("message");





                    if(response.equalsIgnoreCase("200")){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_layout,
                                (ViewGroup) findViewById(R.id.toast_layout_root));

                        text = (TextView) layout.findViewById(R.id.tvtoast);
                        text.setText(Message);

                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                      //  Toast.makeText(AddAmount.this, Message , Toast.LENGTH_SHORT).show();
                        etamount_add.setText("");
                        etremark_add.setText("");
                       /* Intent i = new Intent(AddAmount.this,DashBoard.class);
                        startActivity(i);*/
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }


    }



    public void checkConnection_POST_insta() {
        if (isOnline()) {
            new AddAmount.SendJsonDataToServerInsta().execute();
        } else {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_layout,
                    (ViewGroup) findViewById(R.id.toast_layout_root));

            text = (TextView) layout.findViewById(R.id.tvtoast);
            text.setText("You are not connected to Internet");

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM, 0, 50);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
           // Toast.makeText(AddAmount.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
   /* class GetLoginAsynctask extends AsyncTask<String, String, String> {

        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(AddAmount.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String sever_url = "http://inquisitor.tech/apis/insta_pay_details?compcode=INV178191";

            Log.e("sever_url>>>>>>>>>", sever_url);
            output = HttpHandler.makeServiceCall(sever_url);
            Log.e("getcomment_url", output);
            System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
                dialog.dismiss();
            } else {
                try {
                    dialog.dismiss();
                    JSONArray jsonArray = new JSONArray(output);
                    for(int i = 0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String DocNo = jsonObject.getString("DocNo");
                        String Amount = jsonObject.getString("Amount");
                        String Remark = jsonObject.getString("Remark");
                        String DocDate = jsonObject.getString("DocDate");
                    }
                }

                catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
               *//* Intent intent = new Intent(ExpenseEntry.this,ExpenseEntry.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)almain);
                intent.putExtra("BUNDLE",args);
                Bundle args1 = new Bundle();
                args1.putSerializable("ARRAYLIST",(Serializable)alsub);
                intent.putExtra("BUNDLE",args);
                startActivity(intent);*//*

                super.onPostExecute(output);
            }
        }
    }
    public void checkConnectionGET_insta() {
        if (isOnline()) {

            new AddAmount.GetLoginAsynctask().execute();
        } else {
            Toast.makeText(AddAmount.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }*/
}
