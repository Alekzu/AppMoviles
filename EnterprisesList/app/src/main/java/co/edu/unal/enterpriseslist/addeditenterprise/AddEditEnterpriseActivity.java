package co.edu.unal.enterpriseslist.addeditenterprise;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import co.edu.unal.enterpriseslist.R;
import co.edu.unal.enterpriseslist.enterpriseView;

public class AddEditEnterpriseActivity extends AppCompatActivity {
    public static final int REQUEST_ADD_ENTERPRISE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_enterprise);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String enterpriseId = getIntent().getStringExtra(enterpriseView.EXTRA_ENTERPRISE_ID);

        setTitle(enterpriseId == null ? "Add Enterprise" : "Edit Enterprise");

        AddEditEnterpriseFragment addEditEnterpriseFragment = (AddEditEnterpriseFragment)
                getSupportFragmentManager().findFragmentById(R.id.add_edit_enterprise_container);
        if (addEditEnterpriseFragment == null) {
            addEditEnterpriseFragment = AddEditEnterpriseFragment.newInstance(enterpriseId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.add_edit_enterprise_container, addEditEnterpriseFragment)
                    .commit();
        }
/*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditEnterprise();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}