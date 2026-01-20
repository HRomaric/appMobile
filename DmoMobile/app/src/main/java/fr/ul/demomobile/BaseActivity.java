package fr.ul.demomobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_langue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.lang_switch) {
            toggleLanguage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleLanguage() {
        LocaleListCompat currentLocales = AppCompatDelegate.getApplicationLocales();
        String newLang = (currentLocales.isEmpty() || currentLocales.toLanguageTags().startsWith("fr")) ? "en" : "fr";
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(newLang));
    }
}