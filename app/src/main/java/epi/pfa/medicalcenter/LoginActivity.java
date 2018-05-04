package epi.pfa.medicalcenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http2.Header;

public class LoginActivity extends AppCompatActivity {


    Button loginBtn;
    TextView registerBtn;
    EditText username;
    EditText password;
    String res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View parentLayout = findViewById(android.R.id.content);

        res = getIntent().getStringExtra("registerSuccess");
        if (res != null && res.equals("1")){
            Snackbar.make(parentLayout, "Successfully registred account! You can now login with your username", Snackbar.LENGTH_LONG)
                    .setAction("Success", null).show();
        }
        loginBtn = (Button)findViewById(R.id.btn_login);
        registerBtn = (TextView) findViewById(R.id.link_signup);
        username = (EditText)findViewById(R.id.input_email);
        password = (EditText)findViewById(R.id.input_password);
        final Intent intent = new Intent(this,MainActivity.class);
        final Intent registerIntent = new Intent(this,RegisterActivity.class);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(registerIntent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {

                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();

                Log.i("name = ",username.getText().toString());
                Log.i("password = ",password.getText().toString());

                OkHttpClient client = new OkHttpClient();


                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("null","null")
                        .build();


                Request request = new Request.Builder()
                        .header("PHP_AUTH_USER",username.getText().toString())
                        .header("PHP_AUTH_PW",password.getText().toString())
                        .url("http://54.38.34.120:8500/api/login")
                        .post(requestBody)
                        .build();


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        //Log.i("Response", String.valueOf(response.code()));
                        String res = String.valueOf(response.code());

                        if (res.equals("404")){
                            progressDialog.dismiss();
                            //User not found
                            //Log.i("Not Found",response.body().string());
                            Snackbar.make(view, "User Name not found, please Register", Snackbar.LENGTH_LONG)
                                    .setAction("Not Found", null).show();
                        }
                        else if (res.equals("401")){
                            //bad credentials
                            progressDialog.dismiss();

                            Log.i("Bad Credentials",response.body().string());
                            Snackbar.make(view, "Bad Credentials, Please try again!", Snackbar.LENGTH_LONG)
                                    .setAction("Bad Credentials", null).show();
                            //System.out.print("bad");
                        }
                        else{
                            //Get login token and proceed
                            try {
                                JSONObject resp = new JSONObject(response.body().string());
                                //Log.i("token",resp.getString("token"));
                                intent.putExtra("token",resp.getString("token"));
                                progressDialog.dismiss();
                                startActivity(intent);
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                });
            }
        });
    }
}
