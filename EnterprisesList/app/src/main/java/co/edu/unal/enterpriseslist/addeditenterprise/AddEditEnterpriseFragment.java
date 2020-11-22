package co.edu.unal.enterpriseslist.addeditenterprise;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import co.edu.unal.enterpriseslist.R;
import co.edu.unal.enterpriseslist.data.Enterprise;
import co.edu.unal.enterpriseslist.data.EnterprisesDBHelper;

public class AddEditEnterpriseFragment extends Fragment {
    private static final String ARG_ENTERPRISE_ID = "enterpriseId";
    private String mEnterpriseId;
    private EnterprisesDBHelper mEnterprisesDBHelper;

    private FloatingActionButton mSaveButton;
    private TextInputEditText mNameField;
    private TextInputEditText mWebURLField;
    private TextInputEditText mPhoneContactField;
    private TextInputEditText mMailContactField;
    private TextInputEditText mProductsField;
    private TextInputEditText mConsultingField;
    private TextInputEditText mSoftDevelopField;
    private TextInputEditText mSoftFabricField;
    private CheckBox mConsultingCB;
    private CheckBox mSoftDevelopCB;
    private CheckBox mSoftFabricCB;

    private TextInputLayout mNameLabel;
    private TextInputLayout mWebURLLabel;
    private TextInputLayout mPhoneContactLabel;
    private TextInputLayout mMailContactLabel;
    private TextInputLayout mProductsLabel;
    private TextInputLayout mConsultingLabel;
    private TextInputLayout mSoftDevelopLabel;
    private TextInputLayout mSoftFabricLabel;

    public AddEditEnterpriseFragment() {
        // Required empty public constructor
    }

    public static AddEditEnterpriseFragment newInstance(String enterpriseId) {
        AddEditEnterpriseFragment fragment = new AddEditEnterpriseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ENTERPRISE_ID, enterpriseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEnterpriseId = getArguments().getString(ARG_ENTERPRISE_ID);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_edit_enterprise, container, false);
        //UI references
        mSaveButton = getActivity().findViewById(R.id.fab);
        mNameField = root.findViewById(R.id.et_name);
        mWebURLField = root.findViewById(R.id.et_web_url);
        mPhoneContactField = root.findViewById(R.id.et_phone_contact);
        mMailContactField = root.findViewById(R.id.et_mail_contact);
        mProductsField = root.findViewById(R.id.et_products);


        mConsultingCB = root.findViewById(R.id.cb_consulting);
        mSoftDevelopCB = root.findViewById(R.id.cb_soft_dev);
        mSoftFabricCB = root.findViewById(R.id.cb_soft_fab);

        mNameLabel = root.findViewById(R.id.til_name);
        mWebURLLabel = root.findViewById(R.id.til_web_url);
        mPhoneContactLabel = root.findViewById(R.id.til_phone_contact);
        mMailContactLabel = root.findViewById(R.id.til_mail_contact);
        mProductsLabel = root.findViewById(R.id.til_products);


        // Events
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditEnterprise();
            }
        });
        mEnterprisesDBHelper = new EnterprisesDBHelper(getActivity());

        // Carga de datos
        if (mEnterpriseId != null) {
            loadEnterprise();
        }

        return root;
    }

    private void loadEnterprise(){
            new GetEnterpriseByIdTask().execute();
    }
    private void showEnterprise(Enterprise enterprise) {
        mNameField.setText(enterprise.getName());
        mWebURLField.setText(enterprise.getWebURL());
        mPhoneContactField.setText(enterprise.getPhoneContact());
        mMailContactField.setText(enterprise.getMailContact());
        mProductsField.setText(enterprise.getProducts());
        if(enterprise.isConsulting()>0) mConsultingCB.setChecked(true);
        if(enterprise.isSoftDevelop()>0) mSoftDevelopCB.setChecked(true);
        if(enterprise.isSoftFabric()>0) mSoftFabricCB.setChecked(true);
    }

    private void showEnterpriseScreen(Boolean requery) {
        if (!requery) {
            showAddEditError();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            getActivity().setResult(Activity.RESULT_OK);
        }

        getActivity().finish();
    }

    private void showAddEditError() {
        Toast.makeText(getActivity(),
                "Error adding new info", Toast.LENGTH_SHORT).show();
    }
    private void showLoadError() {
        Toast.makeText(getActivity(),
                "Error getting enterprise info", Toast.LENGTH_SHORT).show();
    }
    private void addEditEnterprise() {
        boolean error = false;

        String name = mNameField.getText().toString();
        String webURL = mWebURLField.getText().toString();
        String phone = mPhoneContactField.getText().toString();
        String mailC = mMailContactField.getText().toString();
        String products = mProductsField.getText().toString();
        int softDevel = 0;
        int softFabr = 0;
        int consult = 0;

        if (TextUtils.isEmpty(name)) {
            mNameLabel.setError("Enter value");
            error = true;
        }

        if (TextUtils.isEmpty(webURL)) {
            mWebURLLabel.setError("Enter value");
            error = true;
        }
        if (TextUtils.isEmpty(phone)) {
            mPhoneContactLabel.setError("Enter value");
            error = true;
        }
        if (TextUtils.isEmpty(mailC)) {
            mMailContactLabel.setError("Enter value");
            error = true;
        }
        if (TextUtils.isEmpty(products)) {
            mProductsLabel.setError("Enter value");
            error = true;
        }
        if (mConsultingCB.isChecked()) {
            consult = 1;
        }
        if (mSoftDevelopCB.isChecked()) {
            softDevel = 1;
        }
        if (mSoftFabricCB.isChecked()) {
            softFabr = 1;
        }

        if (error) {
            return;
        }

        Enterprise enterprise = new Enterprise(name, webURL, phone, mailC, products, consult, softDevel, softFabr, "");

        new AddEditEnterpriseTask().execute(enterprise);

    }

    private class GetEnterpriseByIdTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return mEnterprisesDBHelper.getEnterpriseById(mEnterpriseId);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.moveToLast()) {
                //showEnterprise(new Enterprise(cursor));
                Enterprise empresa = new Enterprise(cursor);
                showEnterprise(empresa);
            } else {
                showLoadError();
            }
        }

    }

    private class AddEditEnterpriseTask extends AsyncTask<Enterprise, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Enterprise... enterprises) {
            if (mEnterpriseId != null) {
                return mEnterprisesDBHelper.updateEnterprise(enterprises[0], mEnterpriseId) > 0;

            } else {
                return mEnterprisesDBHelper.saveLawyer(enterprises[0]) > 0;
            }

        }

        @Override
        protected void onPostExecute(Boolean result) {
            showEnterpriseScreen(result);
        }

    }
/* not sure
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AddEditEnterpriseFragment.this)
                        .navigate(R.id.action_FirstFragment_to_Second2Fragment);
            }
        });
    }*/
}