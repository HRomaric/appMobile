package fr.ul.demomobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.os.LocaleListCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    protected Intent intent;
    private ActivityResultLauncher<Intent>launcher;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //ajout du menu
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //ajour action pour les boutons
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);

        //Image de la photo
        iv = findViewById(R.id.imageview_main);

        //activité photo
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
                            String msg = getString(R.string.dimension) + hauteur + "x" + largeur;
                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            Log.i("PHOTO", msg);

                            iv.setImageBitmap(image);
                        }
                        else if (resCode == 2) {
                            String dateRecue = result.getData().getStringExtra("date_retour");
                            Toast.makeText(MainActivity.this, "Date : " + dateRecue, Toast.LENGTH_LONG).show();
                        }
                        else {
                            Log.i("PHOTO", "ERROR");
                        }
                    }
                }
        );
    }


    //fct onclick de on Bonjour, bt bonjour
    public void onBonjour(View view){
        String msg = getString(R.string.popUpHello);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.i("Bonjour", msg);
    }



    //fct onclick des autres bt
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button2) { // Encvoie d'un message à la deuxième activité
            String txt = getString(R.string.text_arrive);
            int val = 42;
            /*Snackbar sb = Snackbar.make(this.findViewById(android.R.id.content), txt, 10000);
            sb.show();
            sb.setAction("OK", new View.OnClickListener() {
               @Override
               public void onClick(View view){
                   Log.i("SNACKBAR", "OK");
               }
            });*/
            Intent i = new Intent(this, Page2.class);
            i.putExtra("info", txt);
            i.putExtra("val", val);
            launcher.launch(i);
        }
        if (v.getId() == R.id.button3){// On lance l'appareil photo
            launcher.launch(intent);
        }
    }
}