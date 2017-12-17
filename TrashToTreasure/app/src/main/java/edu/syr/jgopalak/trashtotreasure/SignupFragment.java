/**
 * @FileName SignupFragment.java
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */



package edu.syr.jgopalak.trashtotreasure;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignupFragment extends android.support.v4.app.Fragment {

    private EditText inputFullName, inputEmail , inputPassword;
    private Button btnSignIn , btnSignUp , btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private OnFragmentInteractionListener mListener;
    public SignupFragment() {
        // Required empty public constructor
        }

 public static SignupFragment newInstance()
 {
     SignupFragment fragment = new SignupFragment();
     return fragment;
 }
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
}
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState) {
    // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup , container , false);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        btnSignIn = (Button) view.findViewById(R.id.btn_login);
        btnSignUp = (Button) view.findViewById(R.id.btn_signup);
        inputEmail = (EditText) view.findViewById(R.id.signup_email);
        inputPassword = (EditText) view.findViewById(R.id.signup_password);
        inputFullName = (EditText) view.findViewById(R.id.edittext_name);
      //  progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        btnSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mListener != null)
                    mListener.onSigninRoutine();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                final String fullName = inputFullName.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(fullName)) {
                    Toast.makeText(getContext(), "Enter your full name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getContext(), "Password too short , enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
              //  progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(getActivity(),
                        new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult > task) {
                                Log.d("createUserWithEmail: ", "onComplete:" + task.isSuccessful());
                                Toast.makeText(getContext(), "Account Created Successfully", Toast.LENGTH_LONG).show();
//                                progressBar.setVisibility(View.GONE);
                                // If sign in fails , display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful())
                                {
                                    Toast.makeText(getContext(), "Authentication failed." + task.getException(), Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    UserProfile.getInstance().createUserProfile(email,fullName,"UserProfilePictures/default_image.png","Not Provided","Not Provided");
                                    mListener.onSigninRoutine();
                                }
                            }
                        });
                }
                });
        return view;
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
//        progressBar.setVisibility(View.GONE);
    }
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        // void onFragmentInteraction(Uri uri);
        void onSigninRoutine();
    }
}