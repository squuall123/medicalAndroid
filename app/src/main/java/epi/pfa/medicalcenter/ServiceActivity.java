package epi.pfa.medicalcenter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServiceActivity extends AppCompatActivity {

    TextView serviceName;
    TextView serviceNumber;
    TextView serviceDescription;
    Button call;
    String token;
    String serviceId;

    ArrayList<Doctor> mList;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        serviceName = (TextView)findViewById(R.id.serviceName);
        serviceNumber = (TextView)findViewById(R.id.serviceNumber);
        serviceDescription = (TextView)findViewById(R.id.serviceDescription);
        call = (Button)findViewById(R.id.btnCall);

        mList = new ArrayList<Doctor>();


        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        serviceId = intent.getStringExtra("id");
        if (token != null){
            getData();
        }
        Log.i("ID",intent.getStringExtra("id"));



    }

    public void getData(){

        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                Service service = (Service) message.obj;

                serviceName.setText(service.getName());
                serviceNumber.setText(service.getPhone());
                serviceDescription.setText(service.getDescription());



                getDoctors();

                return false;
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("THREAD","Starting thread");

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .header("Authorization","Bearer "+token)
                        .url("http://54.38.34.120:8500/api/service/"+serviceId)
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
                            Service service = new Service();
                            service.setName(json.getString("nom"));
                            service.setPhone(json.getString("num_tel"));
                            service.setDescription(json.getString("description"));
                            Message msg = new Message();
                            msg.obj = service;
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    }

                });

            }
        });
        thread.start();
        call.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.EMPTY.parse("'"+serviceNumber+"'"));
                if (ActivityCompat.checkSelfPermission(ServiceActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });
    }

    public void getDoctors(){

        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {

                ArrayList<Doctor> lst = new ArrayList<Doctor>();
                lst = (ArrayList<Doctor>) message.obj;

                mRecyclerView = (RecyclerView) findViewById(R.id.doctrosList);

                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerView.setLayoutManager(mLayoutManager);

                // specify an adapter (see also next example)
                mAdapter = new DoctorsAdapter(lst,getApplicationContext(),token);
                mRecyclerView.setAdapter(mAdapter);
                return false;
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("THREAD","Starting 2nd thread");

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .header("Authorization","Bearer "+token)
                        .url("http://54.38.34.120:8500/api/medecins/"+serviceName.getText().toString())
                        .build();


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        //Log.i("Doctors",response.body().string());


                        try {
                            JSONArray json = new JSONArray(response.body().string());
                            //Log.i("LENGTH", String.valueOf(json.length()));
                            for (int i =0;i<json.length();i++){
                                JSONObject obj = (JSONObject) json.get(i);
                                Log.i("DEBUG",obj.toString());
                                Doctor doctor = new Doctor();
                                doctor.setId(obj.getInt("id"));
                                doctor.setName(obj.getString("name"));
                                doctor.setEmail(obj.getString("email"));
                                doctor.setPhone(obj.getString("phone"));
                                doctor.setSpecialite(serviceName.getText().toString());
                                mList.add(doctor);
                            }
                            Message msg = new Message();
                            msg.obj = mList;
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
