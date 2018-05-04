package epi.pfa.medicalcenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    String token;
    String profileJSON;

    private EditText name,email,phone,password,ssn;
    private TextView nameL;
    private Button update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = (EditText)findViewById(R.id.profileName);
        email = (EditText)findViewById(R.id.profileEmail);
        phone = (EditText)findViewById(R.id.profilePhone);
        //password = (EditText)findViewById(R.id.profilePassword);
        ssn = (EditText) findViewById(R.id.profileSSN);
        nameL = (TextView) findViewById(R.id.PatientName);
        update = (Button)findViewById(R.id.profileUpdate);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        profileJSON = intent.getStringExtra("profile");

        try {
            JSONObject profile = new JSONObject(profileJSON);

            //PROFILE COMPONENTS
            nameL.setText(profile.getString("name"));
            name.setText(profile.getString("name"));
            email.setText(profile.getString("email"));
            phone.setText(profile.getString("phone"));
            ssn.setText(profile.getString("ssn"));
            //password.setText(profile.getString("password"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_consultations) {
            Log.i("ACTION","Consultations");
            try {
                startActivity(new Intent(this,ConsultationsActivity.class).putExtra("token",token).putExtra("id",new JSONObject(profileJSON).getString("id")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        if (id == R.id.action_doctors) {
            Log.i("ACTION","Doctors");

            try {
                startActivity(new Intent(this,myDoctorsActivity.class).putExtra("token",token).putExtra("uid",new JSONObject(profileJSON).getInt("id")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
