package fr.ul.demomobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import android.view.Menu;
import android.view.MenuItem;

// On hérite de AppCompatActivity pour garder les fonctionnalités de base
public class BaseActivity extends AppCompatActivity {

    // Cette méthode affiche le menu sur tous les écrans qui héritent de BaseActivity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_langue, menu);
        return true;
    }

    // Cette méthode gère le clic sur le bouton de langue pour tout le monde
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.lang_switch) {
            toggleLanguage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // La logique de changement de langue (Français <-> Anglais)
    private void toggleLanguage() {
        LocaleListCompat currentLocales = AppCompatDelegate.getApplicationLocales();
        String newLang = (currentLocales.isEmpty() || currentLocales.toLanguageTags().startsWith("fr")) ? "en" : "fr";
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(newLang));
    }
}