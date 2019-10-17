package com.example.admin_adi.feepayment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    EditText User, Password;
    Button LogIn ;
    String PasswordHolder, UserHolder;
    String finalResult ;
    String HttpURL = "https://clgfees.000webhostapp.com/clg/UserLogin.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    public static final String UserMobile = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        User = (EditText)findViewById(R.id.username);
        Password = (EditText)findViewById(R.id.password);
        LogIn = (Button)findViewById(R.id.Login);

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){
                    UserLoginFunction(UserHolder, PasswordHolder);
                }
                else {
                    Toast.makeText(MainActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    public void CheckEditTextIsEmptyOrNot(){

        UserHolder = User.getText().toString();
        PasswordHolder = Password.getText().toString();

        if(TextUtils.isEmpty(UserHolder) || TextUtils.isEmpty(PasswordHolder))
        {
            CheckEditText = false;
        }
        else {

            CheckEditText = true ;
        }
    }

    public void UserLoginFunction(final String user, final String password){

        class UserLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(MainActivity.this,"Checking Credentials",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if(httpResponseMsg.equalsIgnoreCase("Data Matched")){
                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                    intent.putExtra("UserHolder",user);
                    startActivity(intent);
                }
                else if(httpResponseMsg.equalsIgnoreCase("")) {
                    Toast.makeText(MainActivity.this,"Server error pls try later",Toast.LENGTH_LONG);
                }
                else{

                    Toast.makeText(MainActivity.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("username",params[0]);

                hashMap.put("password",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(user,password);
    }


}