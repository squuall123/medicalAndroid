package epi.pfa.medicalcenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText name;
    EditText mail;
    EditText password;
    EditText ssn;
    EditText phone;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText)findViewById(R.id.input_name);
        mail = (EditText)findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        ssn = (EditText)findViewById(R.id.input_ssn);
        phone = (EditText)findViewById(R.id.input_phone);
        register = (Button)findViewById(R.id.btn_signup);
        final Intent intent = new Intent(this,LoginActivity.class);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OkHttpClient client = new OkHttpClient();
                String post = null;

                try {
                    post = generateRequest();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, post.toString());



                Request request = new Request.Builder()

                        .url("http://54.38.34.120:8500/api/register ")
                        .post(body)
                        .build();


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        Log.i("RESPONSE",response.body().string());
                        //Toast.makeText(getApplicationContext(), "Registrated Successfully !!!", Toast.LENGTH_SHORT).show();
                        intent.putExtra("registerSuccess","1");
                        startActivity(intent);
                        finish();

                    }

                });

            }
        });
    }

    private String generateRequest() throws JSONException {

        JSONObject json = new JSONObject();
        JSONObject patientJson = new JSONObject();
        JSONObject passwordJson = new JSONObject();
        passwordJson.put("first",password.getText().toString());
        passwordJson.put("second",password.getText().toString());
        patientJson.put("name",name.getText().toString());
        patientJson.put("email",mail.getText().toString());
        patientJson.put("plainPassword",passwordJson);
        patientJson.put("ssn",ssn.getText().toString());
        patientJson.put("phone",phone.getText().toString());
        json.put("patient",patientJson);

        return json.toString();
    }
}
