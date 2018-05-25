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

public class ConsultationsActivity extends AppCompatActivity {


    String token;
    String profileId;
    ArrayList<Consultation> mList;

    ProgressDialog progressDialog;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultations);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(ConsultationsActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        profileId = intent.getStringExtra("id");
        Log.i("ID",profileId);

        getData();
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

                ArrayList<Consultation> lst = (ArrayList<Consultation>) message.obj;

                mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerView.setLayoutManager(mLayoutManager);

                // specify an adapter (see also next example)
                mAdapter = new ConsultationsAdapter(lst,getApplicationContext(),token);
                mRecyclerView.setAdapter(mAdapter);

                //Log.i("DEBUG",String.valueOf(lst.size()));
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
                        .url("http://54.38.34.120:8500/api/consultations/"+profileId)
                        .build();


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        //Log.i("Doctors",response.body().string());
                        mList = new ArrayList<Consultation>();

                        try {
                            JSONArray json = new JSONArray(response.body().string());
                            Log.i("LENGTH", String.valueOf(json.length()));
                            for (int i =0;i<json.length();i++){
                                JSONObject obj = (JSONObject) json.get(i);
                                Log.i("DEBUG",obj.toString());
                                Consultation consultation = new Consultation();
                                consultation.setTime(obj.getString("time_r_d_v"));
                                consultation.setDate(obj.getString("date_r_d_v"));
                                consultation.setPatient(obj.getString("nom_patient"));
                                consultation.setDescription(obj.getString("description"));
                                mList.add(consultation);
                            }
                            Message msg = new Message();
                            msg.obj = mList;
                            handler.sendMessage(msg);
                            Log.i("Mlist length",String.valueOf(mList.size()));

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
