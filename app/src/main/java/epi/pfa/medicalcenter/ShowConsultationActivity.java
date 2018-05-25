package epi.pfa.medicalcenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ShowConsultationActivity extends AppCompatActivity {

    private String patient,date,time,description;
    private TextView patientName,consDate,consTime,consDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_consultation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        patientName = (TextView) findViewById(R.id.consultation_patient);
        consDate = (TextView) findViewById(R.id.consultation_date);
        consTime = (TextView) findViewById(R.id.consultation_time);
        consDesc = (TextView)findViewById(R.id.consultation_description);

        Intent intent = getIntent();
        patient = intent.getStringExtra("patient");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        description = intent.getStringExtra("description");

        update();
    }

    private void update(){
        patientName.setText(patient);
        consDate.setText(date);
        consTime.setText(time);
        consDesc.setText(description);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
