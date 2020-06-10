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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class ExpenseReceipt extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner spnfromledger,spntoledger;
    TextView tvFromBalance,tvToBalance,text;
    Button btnsave;
    ArrayList<String> alf = new ArrayList<String>();
    ArrayList<String> alfBal = new ArrayList<String>();
    ArrayList<String> altBal = new ArrayList<String>();
    ArrayList<String> alt = new ArrayList<String>();

    EditText etreceiptNo,etreceiptDate,etamount,etremark;
    String[] bankNames={"BOI","SBI","HDFC","PNB","OBC"};
    android.support.v7.widget.Toolbar toolbar;


   ArrayList<DataProviderFromLedger> alFrom = new ArrayList<>();
    ArrayList<DataProviderToLedger> alTo = new ArrayList<>();


    ArrayList<String> al = new ArrayList<>();



    String ReceiptNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_receipt);
        spnfromledger = (Spinner)findViewById(R.id.spnfromLedger);
        spntoledger = (Spinner)findViewById(R.id.spntoLedger);
        btnsave = (Button)findViewById(R.id.btnsavereceipt);
        etreceiptNo = (EditText)findViewById(R.id.etreceiptNo);
        etreceiptDate = (EditText)findViewById(R.id.etreceiptDate);
        etamount = (EditText)findViewById(R.id.etamountreceipt);
        etremark = (EditText)findViewById(R.id.etremarkreceipt);
        tvFromBalance = (TextView)findViewById(R.id.tvFromBalance);
        tvToBalance = (TextView)findViewById(R.id.tvToBalance);
        al.add("1");
        al.add("2");
        al.add("3");
        al.add("4");
        al.add("5");
        al.add("1");
        al.add("7");
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBaar);
        toolbar.setTitle("Expense Receipt");
        checkConnection_receiptNo();

        checkConnection_GET_Info_2();
        checkConnection_GET_Info_3();
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checkConnection_GET_Info_2();
                final String receiptNumber = etreceiptNo.getText().toString();
                final String receiptDate = etreceiptDate.getText().toString();
                final String receipAmount = etamount.getText().toString();

                final String fromledger = spnfromledger.getSelectedItem().toString();
                final String toledger = spntoledger.getSelectedItem().toString();
                final String receipRemark = etremark.getText().toString();
                if(!receiptNumber.equals("")&&!receiptDate.equals("")&&!receipAmount.equals("")&&!fromledger.equals("")&&!toledger.equals("")&&!receipRemark.equals("")){
                    checkConnection_POST_INFO_2();
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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExpenseReceipt.this,DashBoard.class));
            }
        });
        spnfromledger.setOnItemSelectedListener(this);
        spntoledger.setOnItemSelectedListener(this);


//Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter<String> aaFrom = new ArrayAdapter<>(ExpenseReceipt.this,android.R.layout.simple_spinner_item,bankNames);
        aaFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        spnfromledger.setAdapter(aaFrom);

        ArrayAdapter<String> aaTo = new ArrayAdapter<>(ExpenseReceipt.this,android.R.layout.simple_spinner_item,alt);
        aaTo.notifyDataSetChanged();
        aaTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        spntoledger.setAdapter(aaTo);


        final Calendar myCalendar = Calendar.getInstance();
        final Calendar myCalendar1 = Calendar.getInstance();
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etreceiptDate.setText(sdf.format(myCalendar.getTime()));

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                etreceiptDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        etreceiptDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(ExpenseReceipt.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

if(myCalendar.getTime().after((myCalendar1.getTime()))){

}
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             // str = spnfromledger.getSelectedItem().toString();
        //tvFromBalance.setText(alfBal.get(alf.indexOf(spnfromledger.getSelectedItem())));

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

//**************************************************************************************************************
    class GetLoginAsynctaskInfo2 extends AsyncTask<String, String, String> {

        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ExpenseReceipt.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String sever_url = "http://inquisitor.tech/apis/expense_receipt?compcode=INV178191";

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
                dialog.dismiss();
                try {
                   JSONArray jsonArray = new JSONArray(output);
                   for(int i = 0;i<jsonArray.length();i++){
                       JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                       ReceiptNo = jsonObject.getString("ReceiptNo");
                       String ReceiptDate = jsonObject.getString("ReceiptDate");
                       String FromLedgerName = jsonObject.getString("FromLedgerName");
                       String ToLedgerName = jsonObject.getString("ToLedgerName");
                       String FromLedgerBal = jsonObject.getString("FromLedgerBal");
                       String ToLedgerBal = jsonObject.getString("ToLedgerBal");
                       String ReceipAmount = jsonObject.getString("ReceiptAmount");
                       String  Remark = jsonObject.getString("Remark");
                             DataProviderFromLedger from = new DataProviderFromLedger(FromLedgerName,FromLedgerBal);
                             DataProviderToLedger to = new DataProviderToLedger(ToLedgerName,ToLedgerBal);
                             alFrom.add(from);
                           //  alTo.add(to);
                             alf.add(from.getFromledger().toString());
                             alfBal.add(from.getBalfrom().toString());
                            // alf.add(from.getBalfrom());
                             alt.add(to.getToledger().toString());
                             altBal.add(to.getBalto().toString());
                             }
                    spnfromledger.setAdapter(new ArrayAdapter<String>(ExpenseReceipt.this,android.R.layout.simple_spinner_dropdown_item,alf));
                    tvFromBalance.setText(alfBal.get(alf.indexOf(spnfromledger.getSelectedItem())));

                    spntoledger.setAdapter(new ArrayAdapter<String>(ExpenseReceipt.this, android.R.layout.simple_spinner_dropdown_item, alt));

                    tvToBalance.setText(altBal.get(alt.indexOf(spntoledger.getSelectedItem())));


                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
                super.onPostExecute(output);
            }
        }
    }

    public void checkConnection_GET_Info_2() {
        if (isOnline()) {
            new ExpenseReceipt.GetLoginAsynctaskInfo2().execute();
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
           // Toast.makeText(ExpenseReceipt.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }
    //************************************************************************************************************************
   class SendJsonDataToServerUpdate extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(ExpenseReceipt.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://inquisitor.tech/apis/insert_expense_receipt");

                final String receiptNumber = etreceiptNo.getText().toString();
                final String receiptDate = etreceiptDate.getText().toString();
                final String receipAmount = etamount.getText().toString();

                final String fromledger = spnfromledger.getSelectedItem().toString();
                final String toledger = spntoledger.getSelectedItem().toString();
                final String receipRemark = etremark.getText().toString();

                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("compcode","INV178191");
                jsonObject2.put("ReceiptDate",receiptDate);
                jsonObject2.put("ReceiptAmount",receipAmount);
                jsonObject2.put("FromLedgerName",fromledger);
                jsonObject2.put("ToLedgerName",toledger);
                jsonObject2.put("Remark",receipRemark);




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
                       // Toast.makeText(ExpenseReceipt.this, "hello" , Toast.LENGTH_SHORT).show();
                       // Intent i = new Intent(ExpenseReceipt.this,Dashboard.class);
                      //  startActivity(i);
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



    public void checkConnection_POST_INFO_2() {
        if (isOnline()) {
            new ExpenseReceipt.SendJsonDataToServerUpdate().execute();
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
          //  Toast.makeText(ExpenseReceipt.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }

    //***************************************************************************************************************
    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public void checkConnection_receiptNo() {
        if (isOnline()) {
            new ExpenseReceipt.GetLoginAsynctask_receiptNo().execute();
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
            //Toast.makeText(ExpenseReceipt.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }
    class GetLoginAsynctask_receiptNo extends AsyncTask<String, String, String> {

        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ExpenseReceipt.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String sever_url = "http://inquisitor.tech/apis/receipt_number?compcode=INV178191";

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
                    JSONObject jsonObject = new JSONObject(output);
                    String receiptNo = jsonObject.getString("ReceiptNo");
                    String response = jsonObject.getString("response");
                    if(response.equalsIgnoreCase("200")){
                        etreceiptNo.setText(receiptNo);
                    }

                    //startActivity((new Intent(ExpenseHead.this,ExpenseEntry.class)).putExtra("HeadName",alHeadName));
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
                super.onPostExecute(output);
            }
        }
    }
    class GetLoginAsynctaskInfo3 extends AsyncTask<String, String, String> {

        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ExpenseReceipt.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String sever_url = "http://inquisitor.tech/apis/receipt_from_to_ledger?compcode=INV178191";

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
                dialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(output);
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String LedgerId = jsonObject.getString("LedgerId");
                        String LedgerName = jsonObject.getString("LedgerName");

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
                super.onPostExecute(output);
            }
        }
    }

    public void checkConnection_GET_Info_3() {
        if (isOnline()) {
            new ExpenseReceipt.GetLoginAsynctaskInfo3().execute();
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
           // Toast.makeText(ExpenseReceipt.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }
}
