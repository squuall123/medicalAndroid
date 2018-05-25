package epi.pfa.medicalcenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class howTreatmentActivity extends AppCompatActivity {

    private TextView titele,contenu,docName,patientName;
    private String token,patientId,doctorId,titleString,contenuString, finalDoctorName,finalPatientName;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(howTreatmentActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        setContentView(R.layout.activity_how_treatment);
        titele = (TextView)findViewById(R.id.treatment_title);
        contenu = (TextView) findViewById(R.id.treatment_contenu);
        docName = (TextView)findViewById(R.id.treatment_doctor);
        patientName = (TextView) findViewById(R.id.treatment_patient);

        Intent intent = getIntent();
        titleString = intent.getStringExtra("titre");
        contenuString = intent.getStringExtra("contenu");
        patientId = intent.getStringExtra("patientId");
        doctorId = intent.getStringExtra("doctorId");
        token = intent.getStringExtra("token");

        getDoctor();

    }

    private void update() {
        titele.setText(titleString);
        contenu.setText(contenuString);
        docName.setText(finalDoctorName);
        patientName.setText(finalPatientName);
        progressDialog.dismiss();
    }

    private void getDoctor(){
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                finalDoctorName = (String) message.obj;
                getPatient();
                return false;
            }
        });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .header("Authorization","Bearer "+token)
                        .url("http://54.38.34.120:8500/api/medecin/"+doctorId)
                        .build();


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        try {
                            JSONObject json = new JSONObject(response.body().string());
                            Log.i("DEBUG",json.toString());
                            Message msg = new Message();
                            msg.obj = json.getString("name");
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

    private void getPatient(){
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                finalPatientName = (String) message.obj;
                update();
                return false;
            }
        });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .header("Authorization","Bearer "+token)
                        .url("http://54.38.34.120:8500/api/patient/"+patientId)
                        .build();


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        try {
                            JSONObject json = new JSONObject(response.body().string());
                            Message msg = new Message();
                            msg.obj = json.getString("name");
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

    private void generatePDF(){

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
