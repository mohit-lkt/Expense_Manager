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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

public class ExpenseEntry extends AppCompatActivity {
    EditText etamount_entry,etremark_entry,etreceipt;
     EditText etDate;
     TextView text;
    ArrayList<String> alHeadName = new ArrayList<>();
    ArrayList<String> almain = new ArrayList<>();
    ArrayList<String> alsub = new ArrayList<>();
    AutoCompleteTextView actfromHead,actmainExpense,actsubExpense;
    Button btnsave_entry,btnviewList_entry;
    android.support.v7.widget.Toolbar toolbar;

    ArrayList<String> object11 = new ArrayList<>();
    ArrayList<String> object1 = new ArrayList<>();
    ArrayList<String> object2 = new ArrayList<>();

    ArrayList<DataProvider> list = new ArrayList<>();


ArrayList main = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_entry);
        checkConnectionGETMAIN_SUB();
       // checkConnectionEntry_GET();
        etreceipt = (EditText)findViewById(R.id.etReceiptNo_entry);
        etDate = (EditText)findViewById(R.id.etDate) ;
        actfromHead = (AutoCompleteTextView)findViewById(R.id.actfromHead);
        actmainExpense = (AutoCompleteTextView)findViewById(R.id.actmainExpense);
        actsubExpense = (AutoCompleteTextView)findViewById(R.id.actsubExpense);
        etamount_entry = (EditText)findViewById(R.id.etamount_entry);
        etremark_entry = (EditText)findViewById(R.id.etremark_entry);
        btnsave_entry = (Button)findViewById(R.id.btnsave_entry);
        btnviewList_entry = (Button)findViewById(R.id.btnviewList_entry);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBaar);
        toolbar.setTitle("Expense Entry");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExpenseEntry.this,DashBoard.class));
            }
        });
        checkConnection();

        Intent intent = getIntent();
        String amount = intent.getStringExtra("amount");
        String remark = intent.getStringExtra("remark");


        etremark_entry.setText(remark);
        etamount_entry.setText(amount);

        /*Intent intent2 = getIntent();
        String[] str  = intent2.getExtras("HeadName");
        main.add(fromHead);*/
       /* Intent intent11 = getIntent();
        Bundle args11 = intent11.getBundleExtra("BUNDLE");
       object11 = (ArrayList<String>) args11.getSerializable("ARRAYLIST");
*/
       /* Intent intent1 = getIntent();
        Bundle args1 = intent1.getBundleExtra("BUNDLE");
       object1 = (ArrayList<String>) args1.getSerializable("ARRAYLIST");

        Intent intent2 = getIntent();
        Bundle args2 = intent2.getBundleExtra("BUNDLE");
       object2 = (ArrayList<String>) args2.getSerializable("ARRAYLIST");*/



        ArrayAdapter<String> adapter_fromHead = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,alHeadName);
        actfromHead.setThreshold(1);

        actfromHead.setAdapter(adapter_fromHead);


        ArrayAdapter<String> adapter_mainExpense = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, almain);
        actmainExpense.setThreshold(1);
        actmainExpense.setAdapter(adapter_mainExpense);

        ArrayAdapter<String> adapter_subExpense = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, alsub);
        actsubExpense.setThreshold(1);
        actsubExpense.setAdapter(adapter_subExpense);

       final  Calendar myCalendar = Calendar.getInstance();
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etDate.setText(sdf.format(myCalendar.getTime()));

       final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                etDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        etDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(ExpenseEntry.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnviewList_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExpenseEntry.this,EntryList.class));
            }
        });
        btnsave_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String receiptNumber = etreceipt.getText().toString();
                final String receiptDate = etDate.getText().toString();
                final String fromHead = actfromHead.getText().toString();
                final String mainExpense = actmainExpense.getText().toString();
                final String subExpense = actsubExpense.getText().toString();

                final String receipAmount = etamount_entry.getText().toString();


                final String receipRemark = etremark_entry.getText().toString();
                if(!receiptNumber.equals("")&&!receiptDate.equals("")&&!receipAmount.equals("")&&!fromHead.equals("")&&!mainExpense.equals("")&&!subExpense.equals("")&&!receipRemark.equals("")){
                    checkConnection_POST_ENTRY();
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





    class GetLoginAsynctaskheadName extends AsyncTask<String, String, String> {

        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ExpenseEntry.this);
            dialog.setMessage("Loading");
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
                    }/*Intent intent = new Intent(ExpenseReceipt.this,ExpenseEntry.class);
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


    public void checkConnection() {
        if (isOnline()) {
            new ExpenseEntry.GetLoginAsynctaskheadName().execute();
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
           // Toast.makeText(ExpenseEntry.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
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

   /* class GetLoginAsynctaskEntry_GET extends AsyncTask<String, String, String> {

        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ExpenseEntry.this);
            dialog.setMessage("Loading");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String sever_url = "http://inquisitor.tech/apis/expense_entry_detail?compcode=INV178191&ReceiptNo=00007";

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
                        String ReceiptNo = jsonObject.getString("ReceiptNo");
                        String ReceiptDate = jsonObject.getString("ReceiptDate");
                        String FromHead = jsonObject.getString("FromHead");
                        String MainExpense = jsonObject.getString("MainExpense");
                        String SubExpense = jsonObject.getString("SubExpense");
                        String MainExpCode = jsonObject.getString("MainExpCode");
                        String SubExpCode = jsonObject.getString("SubExpCode");
                        String Remark = jsonObject.getString("Remark");
                        String Amount = jsonObject.getString("Amount");
//

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
                super.onPostExecute(output);
            }
        }
    }
    public void checkConnectionEntry_GET() {
        if (isOnline()) {
            new ExpenseEntry.GetLoginAsynctaskEntry_GET().execute();
        } else {
            Toast.makeText(ExpenseEntry.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }*/

    class GetLoginAsynctask extends AsyncTask<String, String, String> {

        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ExpenseEntry.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String sever_url = "http://inquisitor.tech/apis/main_sub_expense?compcode=INV178191";

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
                        String mainExpense1 = jsonObject.getString("MainExpense");
                        almain.add(mainExpense1);
                        String subExpense1 = jsonObject.getString("SubExpense");
                        alsub.add(subExpense1);
                        String limit1 = jsonObject.getString("ExpenseLimit");
                        DataProvider dataProvider = new DataProvider(mainExpense1,
                                subExpense1,limit1);

                        list.add(dataProvider);}

                }

                catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
               /* Intent intent = new Intent(ExpenseEntry.this,ExpenseEntry.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)almain);
                intent.putExtra("BUNDLE",args);
                Bundle args1 = new Bundle();
                args1.putSerializable("ARRAYLIST",(Serializable)alsub);
                intent.putExtra("BUNDLE",args);
                startActivity(intent);*/

                super.onPostExecute(output);
            }
        }
    }
    public void checkConnectionGETMAIN_SUB() {
        if (isOnline()) {

            new ExpenseEntry.GetLoginAsynctask().execute();
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
           // Toast.makeText(ExpenseEntry.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }
    class SendJsonDataToServerUpdate extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(ExpenseEntry.this);
            dialog.setMessage("Saving");
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://inquisitor.tech/apis/expense_entry");

                final String receiptNumber = etreceipt.getText().toString();
                final String receiptDate = etDate.getText().toString();
                final String fromHead = actfromHead.getText().toString();
                final String mainExpense = actmainExpense.getText().toString();
                final String subExpense = actsubExpense.getText().toString();

                final String receipAmount = etamount_entry.getText().toString();


                final String receipRemark = etremark_entry.getText().toString();

                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("compcode", "INV178191");
                jsonObject2.put("FromHead", fromHead);
                jsonObject2.put("ReceiptNo", receiptNumber);
                jsonObject2.put("ReceiptDate", receiptDate);
                jsonObject2.put("MainExpense", mainExpense);
                jsonObject2.put("SubExpense", subExpense);
                jsonObject2.put("MainExpCode", "-");
                jsonObject2.put("SubExpCode", "-");
                jsonObject2.put("Amount", receipAmount);
                jsonObject2.put("Remark", receipRemark);


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


                    if (response.equalsIgnoreCase("200")) {
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_layout,
                                (ViewGroup) findViewById(R.id.toast_layout_root));

                        text = (TextView) layout.findViewById(R.id.tvtoast);
                        text.setText("Data Saved Successfully");

                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                     //   Toast.makeText(ExpenseEntry.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                        /* Intent i = new Intent(ExpenseEntry.this,DashBoard.class);
                          startActivity(i);*/
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

    public void checkConnection_POST_ENTRY() {
        if (isOnline()) {
            new ExpenseEntry.SendJsonDataToServerUpdate().execute();
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
            Toast.makeText(ExpenseEntry.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }

}
