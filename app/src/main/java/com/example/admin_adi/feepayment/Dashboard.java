package com.example.admin_adi.feepayment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Random;

public class Dashboard extends AppCompatActivity {
    TextView textView,textView3;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String finalResult,user,msg ;
    String HttpURL = "https://clgfees.000webhostapp.com/clg/status.php";
    ProgressDialog progressDialog;
    Random random=new Random();
    TextView textView2;
    int no=random.nextInt(90) + 10;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        textView=(TextView)findViewById(R.id.userdisplay);
        textView2=(TextView)findViewById(R.id.userdisplay3);
        textView3=(TextView)findViewById(R.id.userdisplay4);
        Intent intent=getIntent();
        button=(Button)findViewById(R.id.pay);
        user=intent.getStringExtra("UserHolder");
        textView.setText("Welcome "+user);
        textView2.setText("Roll No : 16CS0"+no);
    }
    public void UserLoginFunction(final String user){

        class UserLoginClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(Dashboard.this,"Checking Fees",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                if(httpResponseMsg.equals("Paid"))
                {
                    Toast.makeText(Dashboard.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                    textView3.setText("Paid");
                }
                else {
                    msg=httpResponseMsg;
                    Toast.makeText(Dashboard.this, "Pending Fees is Rs"+httpResponseMsg, Toast.LENGTH_LONG).show();
                    textView3.setText("Pending Fees is Rs "+httpResponseMsg);
                    button.setVisibility(View.VISIBLE);
                    }
                }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("username",params[0]);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }
        }
        UserLoginClass userLoginClass = new UserLoginClass();
        userLoginClass.execute(user);
    }

    public void logout(View view) {
        finish();
       /* Intent intent=new Intent();
        startActivity(intent);*/
    }

    public void paynow(View view) {
        Intent intent = new Intent(Dashboard.this, Main2Activity.class);
        intent.putExtra("amt", msg);
        intent.putExtra("UserHolder", user);
        startActivity(intent);
    }

    public void check(View view) {
        UserLoginFunction(user);
    }
}
