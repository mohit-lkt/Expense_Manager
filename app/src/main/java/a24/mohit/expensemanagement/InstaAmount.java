package a24.mohit.expensemanagement;

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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InstaAmount extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;
    ArrayList<DataProvider_Entry> list = new ArrayList<>();
    String[] mainExpense,subExpense,date_entry,receiptNo,amount,remark,fromHead;
    android.support.v7.widget.Toolbar toolbar;
    TextView text;

    String DocNo ;
    String Amount1 ;
    String Remark1 ;
    String DocDate ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta_amount);
        checkConnectionGET_insta();
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBaar);
toolbar.setTitle("Undeclared List");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InstaAmount.this,DashBoard.class));
            }
        });
       /* mainExpense = getResources().getStringArray(R.array.MainExpenseCode);
        subExpense = getResources().getStringArray(R.array.SubExpenseCode);
        receiptNo = getResources().getStringArray(R.array.ReceiptNo);
        date_entry = getResources().getStringArray(R.array.DateEntry);*/
      /*  for(int i = 0;i<arrayList.size();i++){
            mainExpense[i] = arrayList.get(i).toString();
        }
        fromHead = getResources().getStringArray(R.array.FromHead);
        amount = getResources().getStringArray(R.array.Amount);
        remark = getResources().getStringArray(R.array.Remark);
        */
       /* Intent i = getIntent();
        String receipt = i.getStringExtra("receipt");
        String date = i.getStringExtra("date");
        String amount_add = i.getStringExtra("amount");
        String remark_add = i.getStringExtra("remark");*/

       /* DataProvider_Entry dataProvider_entry = new DataProvider_Entry(DocNo,DocDate,"-","-","-",Amount1,Remark1);
        list.add(dataProvider_entry);*/

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_insta);
       /* manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        adapter = new DataAdapter_Entry(list);
        recyclerView.setAdapter(adapter);*/
    }

    class GetLoginAsynctask extends AsyncTask<String, String, String> {

        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(InstaAmount.this);
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
                        DocNo = jsonObject.getString("DocNo");
                        Amount1 = jsonObject.getString("Amount");
                        Remark1 = jsonObject.getString("Remark");
                        DocDate = jsonObject.getString("DocDate");

                        DataProvider_Entry dataProvider_entry = new DataProvider_Entry(DocNo,DocDate,"-","-","-",Amount1,Remark1);
                        list.add(dataProvider_entry);

                    }
                    manager = new LinearLayoutManager(InstaAmount.this);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setHasFixedSize(true);
                    adapter = new DataAdapter_Entry(list);
                    recyclerView.setAdapter(adapter);
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
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public void checkConnectionGET_insta() {
        if (isOnline()) {

            new InstaAmount.GetLoginAsynctask().execute();
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
          //  Toast.makeText(InstaAmount.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }
}
