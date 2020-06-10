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
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EntryList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    android.support.v7.widget.Toolbar toolbar;
    RecyclerView.LayoutManager manager;
    ArrayList<DataProvider_Entry> list = new ArrayList<>();
    String[] mainExpense,subExpense,date_entry,receiptNo,amount,remark,fromHead;
    ArrayList arrayList = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_list);
        checkConnectionEntry_GET();
        arrayList.add("Mohit");

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBaar);
        toolbar.setTitle("Expense Entry");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EntryList.this,DashBoard.class));
            }
        });

        mainExpense = getResources().getStringArray(R.array.MainExpenseCode);
        subExpense = getResources().getStringArray(R.array.SubExpenseCode);
        receiptNo = getResources().getStringArray(R.array.ReceiptNo);
        date_entry = getResources().getStringArray(R.array.DateEntry);
      /*  for(int i = 0;i<arrayList.size();i++){
            mainExpense[i] = arrayList.get(i).toString();
        }*/
        fromHead = getResources().getStringArray(R.array.FromHead);
        amount = getResources().getStringArray(R.array.Amount);
        remark = getResources().getStringArray(R.array.Remark);

       /* int count = 0;
        for (String data : receiptNo) {
            DataProvider_Entry dataProvider = new DataProvider_Entry(data,
                    date_entry[count], fromHead[count], mainExpense[count], subExpense[count], amount[count], remark[count]);
            count++;
            list.add(dataProvider);
        }*/
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_entry);
       /* manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        adapter = new DataAdapter_Entry(list);
        recyclerView.setAdapter(adapter);*/



    }
    class GetLoginAsynctaskEntry_GET extends AsyncTask<String, String, String> {

        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(EntryList.this);
            dialog.setMessage("Loading");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String sever_url = "http://inquisitor.tech/apis/expense_entry_detail?compcode=INV178191&ReceiptNo=00006";

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
                        DataProvider_Entry dataProvider = new DataProvider_Entry(ReceiptNo,
                                ReceiptDate, FromHead, MainExpense, SubExpense, Amount, Remark);
                        list.add(dataProvider);
//

                    }
                    manager = new LinearLayoutManager(EntryList.this);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setHasFixedSize(true);
                    adapter = new DataAdapter_Entry(list);
                    recyclerView.setAdapter(adapter);
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
            new EntryList.GetLoginAsynctaskEntry_GET().execute();
        } else {
            Toast.makeText(EntryList.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
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
}
