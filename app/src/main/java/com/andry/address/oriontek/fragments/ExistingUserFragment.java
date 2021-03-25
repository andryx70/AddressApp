package com.andry.address.oriontek.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.andry.address.oriontek.R;
import com.andry.address.oriontek.activities.ShowDataActivity;
import com.andry.address.oriontek.databinding.FragmentExistingUserBinding;
import com.andry.address.oriontek.models.User;
import com.andry.address.oriontek.utils.Constants;
import com.andry.address.oriontek.utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExistingUserFragment} factory method to
 * create an instance of this fragment.
 */
public class ExistingUserFragment extends Fragment implements Validator.ValidationListener{

    public static final float FLOAT_16 = 16.0f;

    public static final float FLOAT_ZERO = 0.0f;
    /**
     * email
     */
    @NotEmpty(messageResId = R.string.empty_field)
    @Email(messageResId = R.string.bad_email_structure)
    private TextInputEditText email;

    /**
     * password
     */
    @NotEmpty(messageResId = R.string.empty_field)
    @Password
    private TextInputEditText password;

    /**
     * Buttons
     */
    private MaterialButton loginBtn, forgotPassword;

    /**
     * FragmentExistingUser Binding
     */
    private FragmentExistingUserBinding binding;

    /**
     * validator
     */
    private Validator validator;

    /**
     * FirebaseAuth
     */
    private FirebaseAuth mAuth;

    private FirebaseFirestore firebaseFirestore;

    private SharedPreferences references;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExistingUserBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        references = getActivity().
                getSharedPreferences(Constants.nameFileTheSharedPreferences, Context.MODE_PRIVATE);

        email = binding.emailAddress;
        password = binding.password;
        loginBtn = binding.loginBtn;
        forgotPassword = binding.forgotPassword;

        validator = new Validator(this);
        validator.setValidationListener(this);

        loginBtn.setOnClickListener(v -> {

            if (binding.loginBtn.getText().toString().equals(getResources().getString(R.string.text_button_login))){

                validator.validate();

            }else {

                sendEmailForForgotPassword();
                forgotPasswordDeactivate();
            }



        });

        forgotPassword.setOnClickListener(v -> {

            if (forgotPassword.getText().toString().equals(getResources().getString(R.string.cancel))){

                forgotPasswordDeactivate();
            } else {
                forgotPasswordActivate();}

        });

        return binding.getRoot();
    }


    /**
     * onValidationSucceeded
     */
    @Override
    public void onValidationSucceeded() {

        Log.i("Method calling", "onValidationSucceeded");

        if (Utils.verifyNetwork(getActivity())) {

            login();

        } else {

            enableAllField();

            new MaterialAlertDialogBuilder(getActivity())
                    .setTitle(getResources().getText(R.string.oops))
                    .setMessage(getResources().getText(R.string.no_connect))
                    .setPositiveButton("Ok", (dialog, which) -> enableAllField())
                    .show();

        }

    }

    /**
     * onValidationFailed
     * @param errors
     */
    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        Log.i("Method calling", "onValidationFailed");


        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());

            // Display error messages ;)
            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }

    }

    /**
     * login
     */
    private void login(){

        Log.i("login", "Method calling");

        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(),
                password.getText().toString().trim()).addOnCompleteListener(task -> {

                    if (task.isSuccessful()){

                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user!=null){

                            if (!user.isEmailVerified()){

                                Toast.makeText(getContext(), getResources().getString(R.string.check_email),
                                        Toast.LENGTH_SHORT).show();

                                cleanAllField();
                                enableAllField();

                            } else {

                                setUidInSharedPreferences(user.getUid());
                                setRolInSharedPreferences(user.getUid());

                            }

                        }
                    } else {
                        cleanAllField();
                        Toast.makeText(getContext(), getResources().getString(R.string.invalid_pass_user), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> enableAllField());
    }

    public void setUidInSharedPreferences(String uid){

        references.edit().putString(Constants.uid, uid).apply();
    }

    public void setRolInSharedPreferences(String uid){

        firebaseFirestore.collection(Constants.routeUsers)
                .whereEqualTo(Constants.uid, uid).addSnapshotListener((value, error) -> {

                    if (value!=null){

                        for (QueryDocumentSnapshot doc : value) {

                            User user = new ModelMapper().map(doc.getData(), User.class);

                            references.edit().putString(Constants.ROL, user.getRol().toString()).apply();

                            startActivity(new Intent(getActivity(), ShowDataActivity.class));
                            getActivity().finish();

                        }
                    }
                });

    }

    /**
     * cleanAllField
     */
    private void cleanAllField(){

        Log.i("cleanAllField", "Method calling");

        email.setText("");
        password.setText("");

    }

    /**
     * enableAllField
     */
    private void enableAllField(){

        Log.i("enableAllField", "Method calling");

        email.setEnabled(true);
        password.setEnabled(true);
        loginBtn.setEnabled(true);
        forgotPassword.setEnabled(true);
    }

    /**
     * forgotPasswordActivate
     */
    private void forgotPasswordActivate(){

        Log.i("forgotPasswordActivate", "Method calling");


        binding.passwordContent.setVisibility(View.GONE);
        binding.loginBtn.setText(getResources().getString(R.string.send));
        binding.forgotPassword.setText(getResources().getString(R.string.cancel));

        binding.emailAddressContainer.setBoxCornerRadii(FLOAT_16,FLOAT_16,FLOAT_16,FLOAT_16);

    }

    /**
     * forgotPasswordDeactivate
     */
    private void forgotPasswordDeactivate(){

        Log.i("Method calling", "forgotPasswordDeactivate");


        binding.passwordContent.setVisibility(View.VISIBLE);
        binding.loginBtn.setText(getResources().getString(R.string.text_button_login));
        binding.forgotPassword.setText(getResources().getString(R.string.forgot_password));

        binding.emailAddressContainer.setBoxCornerRadii(FLOAT_16,FLOAT_16,FLOAT_ZERO,FLOAT_ZERO);
    }

    /**
     * sendEmailForForgotPassword
     */
    private void sendEmailForForgotPassword(){

        Log.i("Method calling", "sendEmailForForgotPassword");


        //email format validation
        Pattern pattern = Pattern.compile(Constants.emailRegex);
        Matcher matcher = pattern.matcher(binding.emailAddress.getText().toString().trim());

        if (!matcher.find()){
            binding.emailAddress.setError(getResources().getString(R.string.bad_email_structure));
        }

        if (binding.emailAddress.getText().toString().isEmpty()){
            binding.emailAddress.setError(getResources().getString(R.string.empty_field));
        }

        if (!binding.emailAddress.getText().toString().isEmpty()){

            mAuth.sendPasswordResetEmail(binding.emailAddress.getText().toString());

            Toast.makeText(getActivity(), getResources().getString(R.string.email_send), Toast.LENGTH_SHORT).show();


        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}