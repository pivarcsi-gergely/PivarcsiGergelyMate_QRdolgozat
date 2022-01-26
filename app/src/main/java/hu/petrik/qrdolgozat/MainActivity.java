package hu.petrik.qrdolgozat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {
    private TextView TVMegjelenit;
    private Button ButtonKiir, ButtonScan;


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

                //Elindítás
                intentIntegrator.initiateScan();
            }
        });
    }

    public void init() {
        TVMegjelenit = findViewById(R.id.TVMegjelenit);
        ButtonScan = findViewById(R.id.ButtonScan);
        ButtonKiir = findViewById(R.id.ButtonKiir);
    }
}