package com.andry.address.oriontek.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.andry.address.oriontek.R;
import com.andry.address.oriontek.activities.ShowDataActivity;
import com.andry.address.oriontek.adapters.SectionsPageAdapterLogin;
import com.andry.address.oriontek.databinding.ActivityLoginBinding;
import com.andry.address.oriontek.utils.Utils;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

/**
 * The type Login activity.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * ActivityLoginBinding
     */
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //validateTheSession in the device
        validateTheSession();

        //viewPager config
        setUpViewPager();


    }

    /**
     * Set up view pager.
     */
    public void setUpViewPager(){

        Log.i("setUpViewPager", "Method calling");

        binding.viewPagerLogin.setAdapter(new SectionsPageAdapterLogin(getSupportFragmentManager(), getLifecycle()));

        new TabLayoutMediator(binding.tabLayout, binding.viewPagerLogin, true, (tab, position) -> {

            if (position == 0)
                tab.setText(getResources().getString(R.string.existing));

            if (position == 1)
                tab.setText(getResources().getString(R.string.new_user));

        }).attach();

    }

    public void validateTheSession(){

        if (!Utils.getUidInSharedPreferences(this).isEmpty() && !FirebaseAuth.getInstance().getUid().isEmpty()){

            startActivity(new Intent(this, ShowDataActivity.class));
            finish();
        }
    }
}