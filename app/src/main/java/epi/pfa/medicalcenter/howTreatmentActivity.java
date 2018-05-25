package epi.pfa.medicalcenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
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

    private static final int REQUEST = 112;
    private Context mContext=howTreatmentActivity.this;


    private Button getPDF;
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
        getPDF = (Button) findViewById(R.id.getPDF);
        Intent intent = getIntent();
        titleString = intent.getStringExtra("titre");
        contenuString = intent.getStringExtra("contenu");
        patientId = intent.getStringExtra("patientId");
        doctorId = intent.getStringExtra("doctorId");
        token = intent.getStringExtra("token");

        getDoctor();

        getPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 23) {
                    String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
                    if (!hasPermissions(mContext, PERMISSIONS)) {
                        ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
                    } else {
                        createandDisplayPdf(finalPatientName,finalDoctorName,titleString,contenuString);
                    }
                }

                }
        });
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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
/*
    private void generatePDF(){

        String extstoragedir = Environment.getExternalStorageDirectory().toString();
        File fol = new File(extstoragedir, "pdf");
        File folder=new File(fol,"pdf");
        if(!folder.exists()) {
            boolean bool = folder.mkdir();
        }
        try {
            final File file = new File(folder, "treatment.pdf");
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);


            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new
                    PdfDocument.PageInfo.Builder(100, 100, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();

            canvas.drawText("Hellow treatement", 10, 10, paint);



            document.finishPage(page);
            document.writeTo(fOut);
            document.close();

        }catch (IOException e){
            Log.i("error",e.getLocalizedMessage());
        }


    }

    */

    public void createandDisplayPdf(String finalDoctorName,String finalPatientName, String titleString, String contenuString) {

        Document doc = new Document();
        String path = null;

        try {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, "treatment.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            Paragraph p1 = new Paragraph("Doctor : "+finalDoctorName);
            Paragraph p2 = new Paragraph("Patient : "+finalPatientName);
            Paragraph p3 = new Paragraph("Title : "+titleString);
            Paragraph p4 = new Paragraph("Treatment : "+contenuString);
            //Font paraFont= new Font(Font.);
            //p1.setAlignment(Paragraph.ALIGN_CENTER);
            //p1.setFont(paraFont);

            //add paragraph to document
            doc.add(p1);
            doc.add(p2);
            doc.add(p3);
            doc.add(p4);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally {
            doc.close();
        }

        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
                viewPdf("treatment.pdf", "Dir");

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    // Method for opening a pdf file
    private void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Uri path = Uri.fromFile(pdfFile);

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(howTreatmentActivity.this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
