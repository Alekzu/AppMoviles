package co.edu.unal.enterpriseslist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import co.edu.unal.enterpriseslist.addeditenterprise.AddEditEnterpriseActivity;
import co.edu.unal.enterpriseslist.data.EnterprisesDBHelper;
import co.edu.unal.enterpriseslist.data.EnterprisesContract.EnterpriseEntry;
import co.edu.unal.enterpriseslist.enterprisedetail.EnterpriseDetailActivity;
import co.edu.unal.enterpriseslist.enterpriseView;

public class EnterpriseFragment extends Fragment {
    public static final int REQUEST_UPDATE_DELETE_ENTERPRISE = 2;

    private EnterprisesDBHelper mEnterprisesDbHelper;

    private ListView mEnterprisesList;
    private EnterpriseCursorAdapter mEnterprisesAdapter;
    private FloatingActionButton mAddButton;
    private FloatingActionButton mSearchButton;
    private TextInputEditText mSearchNameField;
    public EnterpriseFragment() {
        // Required empty public constructor
    }
    public static EnterpriseFragment newInstance() {
        return new EnterpriseFragment();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View root = inflater.inflate(R.layout.fragment_enterprise, container, false);

        // References UI
        mEnterprisesList = (ListView) root.findViewById(R.id.enterprises_list);
        mEnterprisesAdapter = new EnterpriseCursorAdapter(getActivity(), null);
        mAddButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mSearchButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_search);
        mSearchNameField = root.findViewById(R.id.et_search); //null

        // Setup
        mEnterprisesList.setAdapter(mEnterprisesAdapter);

        // Events
        mEnterprisesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor currentItem = (Cursor) mEnterprisesAdapter.getItem(i);
                String currentEnterpriseId = currentItem.getString(
                        currentItem.getColumnIndex(EnterpriseEntry.ID));

                showDetailScreen(currentEnterpriseId);
            }
        });
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddScreen();
            }
        });
        //reset db
        //getActivity().deleteDatabase(EnterprisesDBHelper.DATABASE_NAME);
        //search by name
        mSearchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //String nameSearch = mSearchNameField.getText().toString(); //null pointer
                String nameSearch = enterpriseView.getSearchText();
                searchByName(nameSearch);
            }
        });

        // helper instance
        mEnterprisesDbHelper = new EnterprisesDBHelper(getActivity());

        // Load data
        loadEnterprises();

        // Inflate the layout for this fragment
       return root;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case AddEditEnterpriseActivity.REQUEST_ADD_ENTERPRISE:
                    showSuccessfullSavedMessage();
                    loadEnterprises();
                    break;
                case REQUEST_UPDATE_DELETE_ENTERPRISE:
                    loadEnterprises();
                    break;
            }
        }
    }
    private void searchByName(String nameLike) {
        Cursor cursor = mEnterprisesDbHelper.getEnterpriseByName(nameLike);
        mEnterprisesAdapter.swapCursor(cursor);
        mEnterprisesList.setAdapter(mEnterprisesAdapter);

    }


    private void loadEnterprises() {
        // load data
        new EnterpriseLoadTask().execute();
    }
    private void showSuccessfullSavedMessage() {
        Toast.makeText(getActivity(),
                "Enterprise saved successfully", Toast.LENGTH_SHORT).show();
    }

    private void showAddScreen() {
        Intent intent = new Intent(getActivity(), AddEditEnterpriseActivity.class);
        startActivityForResult(intent, AddEditEnterpriseActivity.REQUEST_ADD_ENTERPRISE);
    }
    private void showDetailScreen(String enterpriseId) {
        Intent intent = new Intent(getActivity(), EnterpriseDetailActivity.class);
        intent.putExtra(enterpriseView.EXTRA_ENTERPRISE_ID, enterpriseId);
        //startActivity(intent);
        startActivityForResult(intent, REQUEST_UPDATE_DELETE_ENTERPRISE);
    }

    //async task
    private class EnterpriseLoadTask extends AsyncTask<Void, Void, Cursor> {


        @Override
        protected Cursor doInBackground(Void... voids) {

            return mEnterprisesDbHelper.getAllEnterprises();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {

            if (cursor != null && cursor.getCount() > 0) {
                mEnterprisesAdapter.swapCursor(cursor);
            } else {
                // Show empty state
            }
        }
    }
}
