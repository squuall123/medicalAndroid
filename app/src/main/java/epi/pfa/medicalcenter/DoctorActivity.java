package epi.pfa.medicalcenter;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DoctorActivity extends AppCompatActivity {


    String token;
    String id,mailS,phoneS;
    TextView name,phone,email,spec,nameL,mailL;
    Button call,reserve;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new ProgressDialog(DoctorActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        nameL = (TextView)findViewById(R.id.user_profile_name);
        mailL = (TextView) findViewById(R.id.user_profile_email);
        name = (TextView) findViewById(R.id.doctor_profile_name);
        phone = (TextView) findViewById(R.id.doctor_profile_phone);
        email = (TextView) findViewById(R.id.doctor_profile_email);
        spec = (TextView) findViewById(R.id.doctor_profile_specialite);
        call= (Button) findViewById(R.id.doctor_call);
        reserve= (Button) findViewById(R.id.reserve);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        id = intent.getStringExtra("id");
        if(token != null)
        getData();
        //name.setText("HELLOW");
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callService();
            }
        });
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Coming Soon...",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void callService() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.EMPTY.parse("tel:" + phoneS));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);

    }

    public void getData(){

        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                Doctor doc = (Doctor)msg.obj;
                Log.i("DOCTOR",doc.getName());
                nameL.setText(doc.getName());
                name.setText(doc.getName());
                mailL.setText(doc.getEmail());
                phone.setText(doc.getPhone());
                spec.setText(doc.getSpecialite());
                email.setText(doc.getEmail());
                phoneS=doc.getPhone();
                progressDialog.dismiss();
                return false;
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .header("Authorization","Bearer "+token)
                        .url("http://54.38.34.120:8500/api/medecin/"+id)
                        .build();


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        //Log.i("Service",response.body().string());
                        try {
                            JSONObject json = new JSONObject(response.body().string());
                            Doctor doctor = new Doctor();
                            doctor.setId(json.getInt("id"));
                            doctor.setName(json.getString("name"));
                            doctor.setEmail(json.getString("email"));
                            doctor.setPhone(json.getString("phone"));
                            doctor.setSpecialite(json.getJSONObject("specialite").getString("nom"));
                            Message msg = new Message();
                            msg.obj = doctor;
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    }

                });
            }
        });
        thread.start();
    }
}
