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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ExpenseMain extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView text;
    RecyclerView.LayoutManager manager;
    ArrayList<DataProvider> list = new ArrayList<>();
    android.support.v7.widget.Toolbar toolbar;

    String[] main = { "Salary" };
    ArrayList<String> almain = new ArrayList<>();
    ArrayList<String> alsub = new ArrayList<>();

    EditText etsubexpense_main,etlimit_main;
    Button btnsave_main;
    AutoCompleteTextView actmain;

    String[] mainexpense,subexpense,limit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_main);

        actmain = (AutoCompleteTextView)findViewById(R.id.actmain);
        etlimit_main = (EditText)findViewById(R.id.etlimit_main);
        etsubexpense_main = (EditText)findViewById(R.id.etsubexpense_main);
        btnsave_main = (Button)findViewById(R.id.btnsave_main);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBaar);
        toolbar.setTitle("Expense Main/Sub");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExpenseMain.this,DashBoard.class));
            }
        });
        checkConnection();
       /* mainexpense = getResources().getStringArray(R.array.MainExpenseCode1);
        subexpense = getResources().getStringArray(R.array.SubExpenseCode1);
        limit = getResources().getStringArray(R.array.Limit);
*/
        int count = 0;
      /*  for(String data : mainexpense){
            DataProvider dataProvider = new DataProvider(data,
                    subexpense[count],limit[count]);
            count++;
            list.add(dataProvider);
        }*/

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, main);
        actmain.setThreshold(1);

        actmain.setAdapter(adapter1);
        btnsave_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mainExpense = actmain.getText().toString();
                String subExpense = etsubexpense_main.getText().toString();
                String limit_str = etlimit_main.getText().toString();
                if(!mainExpense.equals("")&&!subExpense.equals("")&&!limit_str.equals("")) {

                        DataProvider dataProvider = new DataProvider(mainExpense,
                                subExpense, limit_str);

                        list.add(dataProvider);


                    manager = new LinearLayoutManager(ExpenseMain.this);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.Adapter adapter = new DataAdapter(list);
                    recyclerView.setAdapter(adapter);

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
                    //Toast.makeText(ExpenseMain.this, "Please fill The details first", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }




    class GetLoginAsynctask extends AsyncTask<String, String, String> {

        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ExpenseMain.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
            /*arComentChat = new ArrayList<>();*/
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

                            list.add(dataProvider);
                    }


                        manager = new LinearLayoutManager(ExpenseMain.this);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.Adapter adapter1 = new DataAdapter(list);
                        recyclerView.setAdapter(adapter1);

                }

                catch (JSONException e) {
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

            new ExpenseMain.GetLoginAsynctask().execute();
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
           // Toast.makeText(ExpenseMain.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }
}