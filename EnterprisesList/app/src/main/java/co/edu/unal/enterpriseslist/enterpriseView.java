package co.edu.unal.enterpriseslist;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import co.edu.unal.enterpriseslist.data.Enterprise;

public class enterpriseView extends AppCompatActivity {

    public static final String EXTRA_ENTERPRISE_ID = "extra_enterprise_id";
    private static TextInputEditText mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mSearch = findViewById(R.id.et_search);
        setSupportActionBar(toolbar);

        EnterpriseFragment fragment = (EnterpriseFragment)
                getSupportFragmentManager().findFragmentById(R.id.enterprise_container);

        if (fragment == null) {
            fragment = EnterpriseFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.enterprise_container, fragment)
                    .commit();
        }


       FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public static String getSearchText(){
        return mSearch.getText().toString();
    }
}