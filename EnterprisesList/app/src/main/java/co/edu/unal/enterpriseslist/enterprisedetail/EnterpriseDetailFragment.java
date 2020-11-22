package co.edu.unal.enterpriseslist.enterprisedetail;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import co.edu.unal.enterpriseslist.EnterpriseFragment;
import co.edu.unal.enterpriseslist.R;
import co.edu.unal.enterpriseslist.addeditenterprise.AddEditEnterpriseActivity;
import co.edu.unal.enterpriseslist.data.Enterprise;
import co.edu.unal.enterpriseslist.data.EnterprisesDBHelper;
import co.edu.unal.enterpriseslist.enterpriseView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnterpriseDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnterpriseDetailFragment extends Fragment {
    private static final String ARG_ENTERPRISE_ID = "enterpriseId";
    private String mEnterpriseId;

    private CollapsingToolbarLayout mCollapsingView;
    private ImageView mAvatar;
    //private String name;
    private TextView mWebUrl;
    private TextView mPhoneContact;
    private TextView mMailContact;
    private TextView mProducts;
    private TextView mConsulting;
    private TextView mSoftDevelop;
    private TextView mSoftFabric;
    //private CheckedTextView mSoftFabric;

    private EnterprisesDBHelper mEnterprisesDBHelper;

    public EnterpriseDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param enterpriseId Parameter 1.
     * @return A new instance of fragment EnterpriseDetailFragment.
     */

    public static EnterpriseDetailFragment newInstance(String enterpriseId) {
        EnterpriseDetailFragment fragment = new EnterpriseDetailFragment();
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

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_enterprise_detail, container, false);
        mCollapsingView = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
        mAvatar = (ImageView) getActivity().findViewById(R.id.iv_avatar);
        mWebUrl = (TextView) root.findViewById(R.id.tv_web_url);
        mPhoneContact = (TextView) root.findViewById(R.id.tv_phone_contact);
        mMailContact = (TextView) root.findViewById(R.id.tv_mail_contact);
        mProducts = (TextView) root.findViewById(R.id.tv_products);
        mConsulting = (TextView) root.findViewById(R.id.tv_consulting);
        mSoftDevelop = (TextView) root.findViewById(R.id.tv_soft_develop);
        //mSoftFabric = root.findViewById(R.id.ctv_soft_fabric);
        mSoftFabric = root.findViewById(R.id.tv_soft_fabric);

        mEnterprisesDBHelper = new EnterprisesDBHelper(getActivity());
        loadEnterprise();


        return root;
    }
    private void loadEnterprise() {
        new GetEnterpriseByIdTask().execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                showEditScreen();
                break;
            case R.id.action_delete:
                new DeleteEnterpriseTask().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showEnterprise(Enterprise enterprise) {
        //mCollapsingView.setTitle(mEnterpriseId);
        mCollapsingView.setTitle(enterprise.getName());
        /*Glide.with(this)
                .load(Uri.parse("file:///android_asset/" + lawyer.getAvatarUri()))
                .centerCrop()
                .into(mAvatar);*/
        //Uri imagePath = Uri.parse(enterprise.getAvatarUri());
        //mAvatar.setImageURI(imagePath@);
        mWebUrl.setText(enterprise.getWebURL());
        mPhoneContact.setText(enterprise.getPhoneContact());
        mMailContact.setText(enterprise.getMailContact());
        mProducts.setText(enterprise.getProducts());
        if(enterprise.isConsulting()>0) mConsulting.setVisibility(View.VISIBLE);
        if(enterprise.isSoftDevelop()>0) mSoftDevelop.setVisibility(View.VISIBLE);
        if(enterprise.isSoftFabric()>0) mSoftFabric.setVisibility(View.VISIBLE);
        //mConsulting.setText("enterprise.isConsulting()");
        //mSoftDevelop.setText("enterprise.isSoftDevelop()");
        //mSoftFabric.setText("enterprise.isSoftFabric()");
        //mSoftFabric.setVisibility(View.VISIBLE);
    }

    private void showEnterpriseScreen(boolean requery) {
        if (!requery) {
            showDeleteError();
        }
        getActivity().setResult(requery ? Activity.RESULT_OK : Activity.RESULT_CANCELED);
        getActivity().finish();
    }
    private void showDeleteError() {
        Toast.makeText(getActivity(),
                "Error deleting enterprise", Toast.LENGTH_SHORT).show();
    }

    private void showLoadError() {
        Toast.makeText(getActivity(),
                "Error al cargar información", Toast.LENGTH_SHORT).show();
    }
    private void showEditScreen() {
        Intent intent = new Intent(getActivity(), AddEditEnterpriseActivity.class);
        intent.putExtra(enterpriseView.EXTRA_ENTERPRISE_ID, mEnterpriseId);
        startActivityForResult(intent, EnterpriseFragment.REQUEST_UPDATE_DELETE_ENTERPRISE);
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

    private class DeleteEnterpriseTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            return mEnterprisesDBHelper.deleteEnterprise(mEnterpriseId);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            showEnterpriseScreen(integer > 0);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EnterpriseFragment.REQUEST_UPDATE_DELETE_ENTERPRISE) {
            if (resultCode == Activity.RESULT_OK) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        }
    }
}