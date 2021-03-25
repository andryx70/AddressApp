package com.andry.address.oriontek.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.andry.address.oriontek.R;
import com.andry.address.oriontek.databinding.ActivityShowDataBinding;
import com.andry.address.oriontek.models.Rol;
import com.andry.address.oriontek.models.UserDto;
import com.andry.address.oriontek.utils.Constants;
import com.andry.address.oriontek.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.modelmapper.ModelMapper;

public class ShowDataActivity extends AppCompatActivity {

    /**
     * ActivityShowDataBinding
     */
    private ActivityShowDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVisibilityTheItemInActivity();
    }

    /**
     * OnClick
     * @param view
     */
    public void OnClick(View view){

        if (view.getId() == R.id.cardViewAllUser) {
            startActivity(new Intent(ShowDataActivity.this, AllUsersActivity.class));
        }

        if (view.getId() == R.id.cardViewAllAddresses){

            intentAndObjectBundle(FirebaseAuth.getInstance().getUid());
        }

    }

    /**
     * setVisibilityTheItemInActivity
     */
    public void setVisibilityTheItemInActivity(){

        if (Utils.getRolInSharedPreferences(this).equals(Rol.ADMIN.toString())){

            binding.cardViewAllAddresses.setVisibility(View.GONE);

        } else {

            binding.cardViewAllUser.setVisibility(View.GONE);

        }
    }

    /**
     * intentAndObjectBundle
     */
    public void intentAndObjectBundle(String uid) {

        FirebaseFirestore.getInstance().collection(Constants.routeUsers)
                .whereEqualTo(Constants.UID, uid).get().addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        UserDto user = null;

                        for (QueryDocumentSnapshot doc : task.getResult()) {

                            user = new ModelMapper().map(doc.getData(), UserDto.class);
                            user.setKeyFirestore(doc.getId());

                        }

                        if (user != null) {

                            Intent i = new Intent(ShowDataActivity.this, ViewAddressActivity.class);
                            i.putExtra(Constants.routeUsers, user);
                            startActivity(i);
                        }
                    }

                });
    }
}