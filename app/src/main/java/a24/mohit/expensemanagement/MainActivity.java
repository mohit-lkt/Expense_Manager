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

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Button btnlogin;
    EditText etusername, etpassword;
    String Etusername, Etpassword;
    String sever_url;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        text = (TextView) layout.findViewById(R.id.tvtoast);
        text.setText("Hello! This is a custom toast!");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();*/
        btnlogin = (Button) findViewById(R.id.btnlogin);
        etusername = (EditText) findViewById(R.id.etusername);
        etpassword = (EditText) findViewById(R.id.etpassword);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Etusername = etusername.getText().toString();
                Etpassword = etpassword.getText().toString();

               checkConnection();
               String username = etusername.getText().toString();
               String password = etpassword.getText().toString();
               if (username.equals("")||password.equals("")){
                   LayoutInflater inflater = getLayoutInflater();
                   View layout = inflater.inflate(R.layout.toast_layout,
                           (ViewGroup) findViewById(R.id.toast_layout_root));

                   text = (TextView) layout.findViewById(R.id.tvtoast);
                   text.setText("Enter Valid username and password");

                   Toast toast = new Toast(getApplicationContext());
                   toast.setGravity(Gravity.BOTTOM, 0, 50);
                   toast.setDuration(Toast.LENGTH_SHORT);
                   toast.setView(layout);
                   toast.show();
                  // Toast.makeText(MainActivity.this, "Enter Valid username and password", Toast.LENGTH_SHORT).show();
               }

            }
        });

    }

   /* protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public void checkConnection(){
        if(isOnline()){
            Toast.makeText(MainActivity.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
            new FetchData().execute();
        }else{
            Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }*/


    //----------------------------------------------------------------------------

    class GetLoginAsynctask extends AsyncTask<String, String, String> {

        String output = "";
        /*ProgressDialog dialog;*/

        @Override
        protected void onPreExecute() {
           /* dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();*/
            super.onPreExecute();
            /*arComentChat = new ArrayList<>();*/
        }

        @Override
        protected String doInBackground(String... params) {

     /*   String urlParameters = "";
        try {
            urlParameters = "f_id=" + URLEncoder.encode(s_id, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
       sever_url = "http://inquisitor.tech/apis/login?Username="+Etusername+"&Password="+Etpassword;

          //  Log.e("sever_url>>>>>>>>>", sever_url);
            output = HttpHandler.makeServiceCall(sever_url);
         //   Log.e("getcomment_url", output);
        //    System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
                /*dialog.dismiss();*/
            } else {
                try {
                   /* dialog.dismiss();*/
                    JSONObject json = new JSONObject(output);
                    String response = json.getString("response");
                    String compcode = json.getString("compcode");
                    String message = json.getString("message");
                    Log.e("json>>>>>>>>>", json.toString());


                    // Toast.makeText(ActivityForumshow.this, response, Toast.LENGTH_LONG).show();
                    if (response.equalsIgnoreCase("200")) {
                       /* dialog.dismiss();*/
                        //   AppPrefences.setLogout(LoginActivity.this , Outlet);
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_layout,
                                (ViewGroup) findViewById(R.id.toast_layout_root));

                        text = (TextView) layout.findViewById(R.id.tvtoast);
                        text.setText("Login Successfully");

                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();

                       // Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, DashBoard.class);
                        startActivity(intent);

                    } else if (response.equalsIgnoreCase("401")){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_layout,
                                (ViewGroup) findViewById(R.id.toast_layout_root));

                        text = (TextView) layout.findViewById(R.id.tvtoast);
                        text.setText(message);

                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                        //Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                       // startActivity(new Intent(MainActivity.this,MainActivity.class));
                    }
                    Log.e("TAG",message);
                } catch (JSONException e) {
                    e.printStackTrace();
                   /* dialog.dismiss();*/
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

               // Toast.makeText(MainActivity.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
                new GetLoginAsynctask().execute();
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
                //Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            }
        }

        //-------------------------------------------------------------------------

}
