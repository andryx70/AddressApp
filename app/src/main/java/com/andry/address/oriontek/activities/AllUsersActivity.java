package com.andry.address.oriontek.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andry.address.oriontek.R;
import com.andry.address.oriontek.adapters.AllUserAdapter;
import com.andry.address.oriontek.databinding.ActivityAllUsersBinding;
import com.andry.address.oriontek.models.UserDto;
import com.andry.address.oriontek.utils.Constants;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class AllUsersActivity extends AppCompatActivity {

    /**
     * ActivityAllUsersBinding
     */
    private ActivityAllUsersBinding binding;

    /**
     * AllUserAdapter
     */
    private AllUserAdapter adapter;

    /**
     * AtomicReference type List of Users
     */
    List<UserDto> listUser = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init ToolBar
        initToolbar();

        //init RecyclerView
        initRecyclerView();

        //get All Data In Firestore
        getAllDataInFirestore();

        adapter.setOnItemClickListener(new AllUserAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                UserDto userDto = adapter.getItem(position);

                Intent i = new Intent(AllUsersActivity.this, ViewAddressActivity.class);
                i.putExtra(Constants.routeUsers, userDto);
                startActivity(i);
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

    }

    /**
     * initToolbar
     */
    public void initToolbar(){

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setTitle(getResources().getString(R.string.all_user));
    }

    /**
     *initRecyclerView
     */
    public void initRecyclerView(){

        binding.rvAllUsers.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAllUsers.setHasFixedSize(true);
        adapter = new AllUserAdapter(this, listUser);
        binding.rvAllUsers.setAdapter(adapter);
    }

    /**
     * getAllDataInFirestore
     */
    public void getAllDataInFirestore(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.routeUsers).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()){

                List<UserDto> userList = new ArrayList<>();

                for (QueryDocumentSnapshot doc : task.getResult()) {

                    UserDto user = new ModelMapper().map(doc.getData(), UserDto.class);
                    user.setKeyFirestore(doc.getId());

                    userList.add(user);

                }

               adapter.notifyChangeData(userList);
            }

        });

    }


}