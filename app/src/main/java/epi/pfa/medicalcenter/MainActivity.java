package epi.pfa.medicalcenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String token;
    private JSONObject profile;
    TextView nameL;
    TextView mailL;
    ImageButton boutondoctos;
    ImageButton boutontreatments;
    ImageButton boutonappointements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        nameL = (TextView) findViewById(R.id.nameLabel);
//        mailL = (TextView) findViewById(R.id.emailLabel);
        Intent intent = getIntent();
        //boutonappointements = (ImageButton)findViewById(R.id.btnagenda);
        //boutondoctos = (ImageButton)findViewById(R.id.btndoctors);
        //boutontreatments = (ImageButton)findViewById(R.id.btntreatments);
        token = intent.getStringExtra("token");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, "Welcome to Medical Center Android App!", Snackbar.LENGTH_LONG)
                .setAction("Welcome", null).show();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        nameL = headerView.findViewById(R.id.nameLabel);
        mailL = headerView.findViewById(R.id.emailLabel);
        navigationView.setNavigationItemSelectedListener(this);



        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {

                JSONObject json = (JSONObject) message.obj;
                profile = (JSONObject) message.obj;
                try {

                    //init(json.getString("name"),json.getString("email"));
                    nameL.setText(json.getString("name"));
                    mailL.setText(json.getString("email"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .header("Authorization","Bearer "+token)
                        .url("http://54.38.34.120:8500/api/getUser")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.w("Failure",e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //Log.i("User",response.body().string());
                        Message msg = new Message();
                        try {
                            JSONObject json = new JSONObject(response.body().string());
                            msg.obj = json;
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //handler.handleMessage(msg);

                       /* boutonappointements.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(this,ConsultationsActivity.class).putExtra("token",token));
                            }
                        });
                        boutondoctos.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(this,DoctorsAdapter.class).putExtra("token",token));
                            }
                        });
                    */
                    }
                });
            }
        });
        thread.start();
        Log.i("Token",token);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Log.i("Profile",profile.toString());
            startActivity(new Intent(this,ProfileActivity.class).putExtra("token",token).putExtra("profile",profile.toString()));

        } else if (id == R.id.nav_gallery) {
            Log.i("Services","Services");
            startActivity(new Intent(this,ServicesActivity.class).putExtra("token",token));
        } else if (id == R.id.nav_share) {
            Log.i("About","About");
            startActivity(new Intent(this,AboutActivity.class));

        } else if (id == R.id.nav_send) {
            Log.i("Contact","Contact");
            startActivity(new Intent(this,ContactActivity.class).putExtra("token",token));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void init(String name, String email){


        nameL.setText(name);
        mailL.setText(email);

    }
}
