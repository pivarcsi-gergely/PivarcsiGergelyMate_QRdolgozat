package hu.petrik.qrdolgozat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView TVMegjelenit;
    private Button ButtonKiir, ButtonScan;
    private boolean writePermission;
    private static final String fajlNev = "scannedCodes.csv";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        ButtonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.setPrompt("QR Code Scanner made for an exam.");
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.initiateScan();
            }
        });

        ButtonKiir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if (writePermission) {
                     Date date = Calendar.getInstance().getTime();
                     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.GERMANY);
                     String fileSzoveg = TVMegjelenit.getText().toString() + ", " + format.format(date);

                     try {
                         FileOutputStream fileOutputStream = openFileOutput(fajlNev, MODE_PRIVATE);
                         fileOutputStream.write(fileSzoveg.getBytes());

                         Toast.makeText(MainActivity.this, "Sikeresen elmentve: " + getFilesDir() + "/scannedCodes.csv", Toast.LENGTH_SHORT).show();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
                 else {
                     Toast.makeText(MainActivity.this, "Fájlokhoz való hozzáférés nélkül nem lehet fájlba írni!", Toast.LENGTH_SHORT).show();
                 }
            }
        });
    }

    public void init() {
        TVMegjelenit = findViewById(R.id.TVMegjelenit);
        ButtonScan = findViewById(R.id.ButtonScan);
        ButtonKiir = findViewById(R.id.ButtonKiir);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            writePermission = false;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        else {
            writePermission = true;
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            TVMegjelenit.setText(intentResult.getContents());
            String s = TVMegjelenit.getText().toString();

            try {
                Uri uri = Uri.parse(s);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
            catch (Exception e) {
                Log.d("URI ERROR", e.toString());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}