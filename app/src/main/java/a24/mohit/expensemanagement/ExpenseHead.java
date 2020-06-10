package a24.mohit.expensemanagement;

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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class ExpenseHead extends AppCompatActivity {
    EditText etcode,etname,etmobile,etemail;
    String ETcode,ETname,ETmobile,ETemail;
    Button btnsave_head;
    android.support.v7.widget.Toolbar toolbar;
    ArrayList<String> alHeadName = new ArrayList<>();
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_head);
        etcode = (EditText)findViewById(R.id.etcode);
        etname = (EditText)findViewById(R.id.etname);
        etmobile = (EditText)findViewById(R.id.etmobile);

        etemail = (EditText)findViewById(R.id.etemail);
        btnsave_head = (Button)findViewById(R.id.btnsave_head);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBaar);
        toolbar.setTitle("Expense Head");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExpenseHead.this,DashBoard.class));
            }
        });
       // checkConnection();

        btnsave_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ETcode = etcode.getText().toString();
                ETname = etname.getText().toString();
                ETmobile = etmobile.getText().toString();
                ETemail = etemail.getText().toString();

                if(!ETcode.equals("")&&!ETname.equals("")&&!ETmobile.equals("")&&!ETemail.equals("")){
                    checkConnection_POST_head();
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

            }
        });

    }

    class GetLoginAsynctask extends AsyncTask<String, String, String> {

        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ExpenseHead.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String sever_url = "http://inquisitor.tech/apis/expensehead?compcode=INV178191";

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

for(int i = 0;i<jsonArray.length();i++){
    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
    String expenseCode = jsonObject.getString("ExpenseCode");
    String headName = jsonObject.getString("HeadName");
    String mobile = jsonObject.getString("Mobile");
    String email = jsonObject.getString("Email");
//
    alHeadName.add(headName);
}/*Intent intent = new Intent(ExpenseHead.this,ExpenseEntry.class);
                    Bundle args = new Bundle();
                    args.putSerializable("ARRAYLIST",(Serializable)alHeadName);
                    intent.putExtra("BUNDLE",args);
                    startActivity(intent);*/

                //startActivity((new Intent(ExpenseHead.this,ExpenseEntry.class)).putExtra("HeadName",alHeadName));
 } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
                super.onPostExecute(output);
            }
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

    public void checkConnection() {
        if (isOnline()) {
            new ExpenseHead.GetLoginAsynctask().execute();
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
           // Toast.makeText(ExpenseHead.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }
    class SendJsonDataToServerHead extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(ExpenseHead.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://inquisitor.tech/apis/insert_expense_head");

                final String expenseCode = etcode.getText().toString();
                final String headName = etname.getText().toString();
                final String mobile = etmobile.getText().toString();

                final String email = etemail.getText().toString();


                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("compcode",expenseCode);
                jsonObject2.put("HeadName",headName);
                jsonObject2.put("ContPerson","");
                jsonObject2.put("Mobile",mobile);
                jsonObject2.put("Email",email);
                jsonObject2.put("OpeningBal","");
                jsonObject2.put("ClosingBal","");




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
                        text.setText("Data saved successfully");

                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                      //  Toast.makeText(ExpenseHead.this, "Data saved successfully" , Toast.LENGTH_SHORT).show();
                        /* Intent i = new Intent(ExpenseHead.this,DashBoard.class);
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



    public void checkConnection_POST_head() {
        if (isOnline()) {
            new ExpenseHead.SendJsonDataToServerHead().execute();
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
           // Toast.makeText(ExpenseHead.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }

}
