package epi.pfa.medicalcenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

public class myDoctorsActivity extends AppCompatActivity {

    String token;
    String uid;
    ArrayList<Doctor> mList;

    ProgressDialog progressDialog;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctors);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(myDoctorsActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        mList = new ArrayList<Doctor>();


        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        uid = intent.getStringExtra("uid");
        if (token != null){
            getData();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void getData(){
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {

                ArrayList<Doctor> lst = new ArrayList<Doctor>();
                lst = (ArrayList<Doctor>) message.obj;

                mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerView.setLayoutManager(mLayoutManager);

                // specify an adapter (see also next example)
                mAdapter = new DoctorsAdapter(lst,getApplicationContext(),token);
                mRecyclerView.setAdapter(mAdapter);

                progressDialog.dismiss();
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
                        .url("http://54.38.34.120:8500/api/mymedecins/"+uid)
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
                                doctor.setSpecialite(obj.getString("specialite"));
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
