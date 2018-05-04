package epi.pfa.medicalcenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ConsultationsActivity extends AppCompatActivity {


    String token;
    String profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultations);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        profileId = intent.getStringExtra("id");

        getData();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void getData(){

        
    }
}
