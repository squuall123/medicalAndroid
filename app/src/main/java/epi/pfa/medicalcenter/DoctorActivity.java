package epi.pfa.medicalcenter;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    TextView name,phone,email,spec;
    Button call,mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);


        name = (TextView) findViewById(R.id.doctorName);
        phone = (TextView) findViewById(R.id.profilePhone);
        email = (TextView) findViewById(R.id.profileEmail);
        spec = (TextView) findViewById(R.id.profileSSN);
        call= (Button) findViewById(R.id.doctorCall);
        mail= (Button) findViewById(R.id.doctorMail);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        id = intent.getStringExtra("id");
        if(token != null)
        getData();
        //name.setText("HELLOW");
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void getData(){

        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                Doctor doc = (Doctor)msg.obj;
                Log.i("DOCTOR",doc.getName());
                name.setText(doc.getName());
                //phone.setText(doc.getPhone());
               // spec.setText(doc.getSpecialite());
               // email.setText(doc.getEmail());
               // phoneS=doc.getPhone();
              //  mailS=doc.getEmail();

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
                            doctor.setSpecialite(json.getString("specialite"));
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
