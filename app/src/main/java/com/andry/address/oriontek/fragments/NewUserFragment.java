package com.andry.address.oriontek.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.andry.address.oriontek.R;
import com.andry.address.oriontek.databinding.FragmentNewUserBinding;
import com.andry.address.oriontek.models.Rol;
import com.andry.address.oriontek.models.User;
import com.andry.address.oriontek.models.UserStatus;
import com.andry.address.oriontek.utils.Constants;
import com.andry.address.oriontek.utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewUserFragment} factory method to
 * create an instance of this fragment.
 */
public class NewUserFragment extends Fragment implements Validator.ValidationListener {

    /**
     * FirebaseAuth
     */
    private FirebaseAuth mAuth;

    /**
     * DATA_BASE
     */
    private FirebaseFirestore firestore;

    /**
     * name
     */
    @NotEmpty(messageResId = R.string.empty_field)
    private TextInputEditText name;

    /**
     * email
     */
    @NotEmpty(messageResId = R.string.empty_field)
    @Email(messageResId = R.string.bad_email_structure)
    private TextInputEditText email;

    /**
     * phoneNumber
     */
    @NotEmpty(messageResId = R.string.empty_field)
    @Pattern(regex = "\\d*")
    private TextInputEditText phoneNumber;

    /**
     * password
     */
    @NotEmpty(messageResId = R.string.empty_field)
    @Password
    private TextInputEditText password;

    /**
     * confirmPassword
     */
    @NotEmpty(messageResId = R.string.empty_field)
    @ConfirmPassword(messageResId = R.string.invalid_password)
    private TextInputEditText confirmPassword;

    /**
     * Button
     */
    private MaterialButton registerBtn;

    /**
     * validator
     */
    private Validator validator;

    /**
     * FragmentNewUserBinding
     */
    private FragmentNewUserBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentNewUserBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        name = binding.editTextName;
        email = binding.editTextEmail;
        phoneNumber = binding.editTextPhoneNumber;
        password = binding.editTextPassword;
        confirmPassword = binding.editTextPasswordConfirm;
        registerBtn = binding.registerBtn;

        validator = new Validator(this);
        validator.setValidationListener(this);

        registerBtn.setOnClickListener(view -> {

            validator.validate();

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

            name.setEnabled(false);
            email.setEnabled(false);
            phoneNumber.setEnabled(false);
            password.setEnabled(false);
            confirmPassword.setEnabled(false);
            registerBtn.setEnabled(false);

            firestore.collection(Constants.routeUsers).whereEqualTo(Constants.EMAIL, email.getText().toString().trim())
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                if (queryDocumentSnapshots.size() > 0){

                    enableAllField();

                    email.setError(getResources().getString(R.string.already_exist));

                } else {

                    registerUserInFirebaseAuth();
                }
            });

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
     * registerUserInFirebaseAuth
     */
    private void registerUserInFirebaseAuth() {

        Log.i("Method calling", "registerUserInFirebaseAuth");


        mAuth.createUserWithEmailAndPassword(String.valueOf(email.getText()), String.valueOf(password.getText()))
                .addOnCompleteListener(getActivity(), task -> {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    if (firebaseUser != null){
                        firebaseUser.sendEmailVerification();
                        saveDataTheUserInDataBase(firebaseUser.getUid());
                    }

                }).addOnFailureListener(e -> enableAllField());
    }

    /**
     * saveDataTheUserInDataBase
     * @param value
     */
    private void saveDataTheUserInDataBase(String value) {

        Log.i("Method calling", "saveDataTheUserInDataBase");

        User user = new User(value, email.getText().toString().trim(), email.getText().toString().trim(),
                phoneNumber.getText().toString().trim(),
                UserStatus.ENABLED, Rol.USER, Collections.emptyList());

        firestore.collection(Constants.routeUsers)
                .add(user).addOnSuccessListener(documentReference -> {

            Toast.makeText(getContext(), getResources().getString(R.string.email_send),
                    Toast.LENGTH_SHORT).show();
            cleanAllField();

        }).addOnFailureListener(e -> enableAllField());

    }

    /**
     * cleanAllField
     */
    private void cleanAllField() {

        Log.i("Method calling", "cleanAllField");


        name.setText("");
        email.setText("");
        phoneNumber.setText("");
        password.setText("");
        confirmPassword.setText("");

    }

    /**
     * enableAllField
     */
    private void enableAllField() {

        Log.i("Method calling", "enableAllField");


        name.setEnabled(true);
        email.setEnabled(true);
        phoneNumber.setEnabled(true);
        password.setEnabled(true);
        confirmPassword.setEnabled(true);
        registerBtn.setEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}