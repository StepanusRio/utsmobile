package com.example.utsaplikasimobiledatabuku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utsaplikasimobiledatabuku.API.LoginAPI;
import com.example.utsaplikasimobiledatabuku.API.ServerAPI;
import com.example.utsaplikasimobiledatabuku.ui.profile.ProfileFragment;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginScreen extends AppCompatActivity {
    TextInputEditText EtEmail,EtPassword;
    Button BtnLogin;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        getSupportActionBar().hide();

        BtnLogin = (Button) findViewById(R.id.btnLogin);
        EtEmail = (TextInputEditText) findViewById(R.id.EtEmail);
        EtPassword = (TextInputEditText) findViewById(R.id.EtPassword);

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(view.getContext());
                pd.setTitle("Proses Login. . .");
                pd.setMessage("Tunggu sebentar");
                pd.setCancelable(true);
                pd.setIndeterminate(true);
                pd.show();
                progressLogin(EtEmail.getText().toString(),EtPassword.getText().toString());
            }
        });
    }
    void progressLogin (String temail,String tpassword){
        ServerAPI urlAPI = new ServerAPI();
        String URL = urlAPI.BASE_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
        LoginAPI api = retrofit.create(LoginAPI.class);
        if (!isEmailValid(EtEmail.getText().toString())) {
            AlertDialog.Builder msg = new AlertDialog.Builder(LoginScreen.this);

            msg.setMessage("Email tidak Valid").setNegativeButton("Retry",null)
                    .create().show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    msg.create().dismiss();
                }
            }, 3000);
            return;
        }
        api.login(temail,tpassword).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    if (json.getString("status").equals("1")) {
                        Toast.makeText(LoginScreen.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder msg = new AlertDialog.Builder(LoginScreen.this);
                        Intent intent = new Intent(LoginScreen.this,MainActivity.class);
                        intent.putExtra("username",json.getJSONObject("data").getString("username"));
                        intent.putExtra("email",json.getJSONObject("data").getString("email"));
                        startActivity(intent);
                        finish();
                        pd.dismiss();
                    }else {
                        AlertDialog.Builder msg = new AlertDialog.Builder(LoginScreen.this);
                        msg.setMessage("Login gagal")
                                .setNegativeButton("Retry",null).create().show();
                        EtEmail.setText("");
                        EtPassword.setText("");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                msg.create().dismiss();
                            }
                        }, 3000);
                    }
                    pd.dismiss();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
                @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("Info Login", "onFailure: Login Gagal"+t.toString());
                    pd.dismiss();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder msg = new AlertDialog.Builder(LoginScreen.this);
                            msg.setMessage("Login gagal").setNegativeButton("Retry", null).create().show();
                        }
                    }, 3000);
            }
        });
    }
    public boolean isEmailValid(String email){
        boolean isValid = false;

        String expression ="^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            isValid=true;
        }
        return  isValid;
    }
}