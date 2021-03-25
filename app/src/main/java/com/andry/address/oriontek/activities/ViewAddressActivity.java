package com.andry.address.oriontek.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andry.address.oriontek.R;
import com.andry.address.oriontek.adapters.AllAddressAdapter;
import com.andry.address.oriontek.databinding.ActivityViewAddressBinding;
import com.andry.address.oriontek.models.Rol;
import com.andry.address.oriontek.models.User;
import com.andry.address.oriontek.models.UserDto;
import com.andry.address.oriontek.utils.Constants;
import com.andry.address.oriontek.utils.Utils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewAddressActivity extends AppCompatActivity {

    /**
     * ActivityViewAddressBinding
     */
    private ActivityViewAddressBinding binding;

    /**
     * user
     */
    private UserDto user;

    /**
     * AllAddressAdapter
     */
    private AllAddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //validateTheRol
        validateTheRol();

        //Get Data in intent
        user = (UserDto) getIntent().getSerializableExtra(Constants.routeUsers);

        //init ToolBar
        initToolbar();

        //init RecyclerView
        initRecyclerView();
    }

    /**
     *initRecyclerView
     */
    public void initRecyclerView(){

        binding.rvAllAddress.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAllAddress.setHasFixedSize(true);
        adapter = new AllAddressAdapter(this, user.getAddress());
        binding.rvAllAddress.setAdapter(adapter);
    }

    /**
     * initToolbar
     */
    public void initToolbar(){

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setTitle(user.getName());
    }

    /**
     * Onclick
     * @param view
     */
    public void Onclick(View view){

        if (view.getId() == R.id.fab_button){

            EditText input = new EditText(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);

            new MaterialAlertDialogBuilder(this)
                    .setTitle(getResources().getString(R.string.new_address))
                    .setView(input)
                    .setPositiveButton(getResources().getString(R.string.save), (dialog, which) -> {
                        saveDataInFirestore(input.getText().toString().trim());
                    })
                    .setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> {

                    })
                    .show();

        }

    }

    /**
     * saveDataInFirestore
     *
     */
    public void saveDataInFirestore(String address) {

        User u =  new User(user.getUid(),user.getName(),user.getEmail(),user.getPhoneNumber()
                ,user.getStatus(), user.getRol(),user.getAddress());
        u.getAddress().add(address);

        FirebaseFirestore.getInstance().collection(Constants.routeUsers).document(user.getKeyFirestore()).set(u)
                .addOnSuccessListener(unused -> {

                    adapter.notifyChangeData(u.getAddress());

                    Toast.makeText(ViewAddressActivity.this,
                            getResources().getString(R.string.success_address), Toast.LENGTH_SHORT).show();

        });
    }

    /**
     * validateTheRol
     */
    public void validateTheRol(){
        if (Utils.getRolInSharedPreferences(this).equals(Rol.ADMIN.toString())){

            binding.fabButton.setVisibility(View.GONE);
        }
    }
}