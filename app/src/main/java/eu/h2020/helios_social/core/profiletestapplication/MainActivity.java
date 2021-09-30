package eu.h2020.helios_social.core.profiletestapplication;

import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;

import eu.h2020.helios_social.core.profile.HeliosUserData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = (TextView)findViewById(R.id.txt_area);
                textView.setMovementMethod(new ScrollingMovementMethod());
                textView.append("\n");
                HeliosUserData.getInstance().setValue("test1", "value1");
                HeliosUserData.getInstance().setValue("test2", "value2");
                HeliosUserData.getInstance().setValue("test3", "value3");
                String result;
                boolean found;

                textView.append(VersionUtils.getAndroidVersion() + "\n" + VersionUtils.getDeviceName() + "\n");
                String now = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                textView.append("Profile test sequence run on " + now + "\n");

                result = HeliosUserData.getInstance().getValue("test1");
                if ((result != null) && result.equals("value1")) {
                    textView.append("[OK]    Test string 1 found\n");
                } else {
                    textView.append("[FAIL]  Test string 1 not found\n");
                }

                result = HeliosUserData.getInstance().getValue("test2");
                if ((result != null) && result.equals("value2")) {
                    textView.append("[OK]    Test string 2 found\n");
                } else {
                    textView.append("[FAIL]  Test string 2 not found\n");
                }

                result = HeliosUserData.getInstance().getValue("test3");
                if ((result != null) && result.equals("value3")) {
                    textView.append("[OK]    Test string 3 found\n");
                } else {
                    textView.append("[FAIL]  Test string 3 not found\n");
                }

                found = HeliosUserData.getInstance().hasValue("test3");
                if (found) {
                    textView.append("[OK]    Test key 3 exists\n");
                } else {
                    textView.append("[FAIL]  Test key 3 does not exist\n");
                }

                HeliosUserData.getInstance().setValue("test3", "newvalue");
                result = HeliosUserData.getInstance().getValue("test3");
                if ((result != null) && result.equals("newvalue")) {
                    textView.append("[OK]    Test string 3 new value found\n");
                } else {
                    textView.append("[FAIL]  Test string 3 new value not found\n");
                }

                HeliosUserData.getInstance().removeKey("test3");
                found = HeliosUserData.getInstance().hasValue("test3");
                if (!found) {
                    textView.append("[OK]    Test key 3 removed\n");
                } else {
                    textView.append("[FAIL]  Test key 3 has not been removed\n");
                }
                textView.append("\n\n");
                HeliosUserData.getInstance().removeKey("test2");
                HeliosUserData.getInstance().removeKey("test1");

                Snackbar.make(view, "Profile manager test sequence run", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        HeliosUserData userData = HeliosUserData.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear_view) {
            TextView textView = (TextView)findViewById(R.id.txt_area);
            textView.setText("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
