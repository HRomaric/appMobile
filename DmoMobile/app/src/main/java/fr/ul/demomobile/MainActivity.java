package fr.ul.demomobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
    }

    public void onBonjour(View view){
        String msg = "HELLO THERE !";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.i("Bonjour", msg);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button2) {
            String txt = "bb(resdtfcygvbhb";
            Snackbar sb = Snackbar.make(this.findViewById(android.R.id.content), txt, 10000);
            sb.show();
            sb.setAction("OK", new View.OnClickListener() {
               @Override
               public void onClick(View view){
                   Log.i("SNACKBAR", "OK");
               }
            });
        }
    }
}