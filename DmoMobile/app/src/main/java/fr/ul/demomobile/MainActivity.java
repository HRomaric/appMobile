package fr.ul.demomobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    protected Intent intent;
    private ActivityResultLauncher<Intent>launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resCode = result.getResultCode();
                        if (resCode == RESULT_OK){
                            Bundle extras = result.getData().getExtras();
                            assert extras != null;
                            Bitmap image = (Bitmap) extras.get("data");
                            int largeur = image.getWidth();
                            int hauteur = image.getHeight();
                            String msg = "Dim img : " + largeur + " " + hauteur;
                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            Log.i("PHOTO", msg);
                        }
                        else {
                            Log.i("PHOTO", "ERROR");
                        }
                    }
                }
        );
    }

    public void onBonjour(View view){
        String msg = "HELLO THERE !";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.i("Bonjour", msg);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button2) {
            String txt = "Hello world !";
            Snackbar sb = Snackbar.make(this.findViewById(android.R.id.content), txt, 10000);
            sb.show();
            sb.setAction("OK", new View.OnClickListener() {
               @Override
               public void onClick(View view){
                   Log.i("SNACKBAR", "OK");
               }
            });
        }
        if (v.getId() == R.id.button3){
            launcher.launch(intent);
        }
    }
}