/**
 * @FileName LoginFragment.java
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */

package edu.syr.jgopalak.trashtotreasure;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class LoginFragment extends android.support.v4.app.Fragment {

    // google sign in
    private static final String TAG="GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private OnFragmentInteractionListener mListener;

    private EditText inputEmail , inputPassword;
    private FirebaseAuth auth;
   // private ProgressBar progressBar;
    private Button btnSignup , btnLogin , btnReset;
    private ImageButton ib_fingerprintlogin;
    private GoogleSignInClient mGoogleSignInClient;

    public LoginFragment() {
        // Required empty public constructor
        }

        public static LoginFragment newInstance()
        {
            LoginFragment fragment = new LoginFragment();
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
        }
        @Override
        public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState)
        {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_login , container , false);

            //Get Firebase auth instance
            auth = FirebaseAuth.getInstance();

            inputEmail = (EditText) view.findViewById(R.id.edittext_email);
            inputPassword = (EditText) view.findViewById(R.id.edittext_password);
         //   progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            btnSignup = (Button) view.findViewById(R.id.btn_signup);
            btnLogin = (Button) view.findViewById(R.id.btn_login);
            btnReset = (Button) view.findViewById(R.id.btn_reset_password);
           // ib_fingerprintlogin = (ImageButton) view.findViewById(R.id.ib_fingerprintlogin);

            //Get Firebase auth instance
            auth = FirebaseAuth.getInstance();


            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (mListener != null) {
                        mListener.onSignupRoutine();
                    }
                }
            });

//            ib_fingerprintlogin.setOnClickListener(new View.OnClickListener(){
//                public void onClick(View v)
//                {
//
//                    Intent intent = new Intent(getActivity(), FingerprintActivity.class);
//                    startActivity(intent); getActivity();
//                }
//            });

            btnReset.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String emailID = inputEmail.getText().toString();
                    if (TextUtils.isEmpty(emailID))
                    {
                        Toast.makeText(getContext(), "Enter email address!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    auth.sendPasswordResetEmail(emailID)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });

            btnLogin.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final String email = inputEmail.getText().toString();
                    final String password = inputPassword.getText().toString();
                    if (TextUtils.isEmpty(email))
                    {
                        Toast.makeText(getContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                  //  progressBar.setVisibility(View.VISIBLE);
                    //authenticate user
                    auth.signInWithEmailAndPassword(email , password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult >()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            // If sign in fails , display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                           // progressBar.setVisibility(View.GONE);
                            if (!task.isSuccessful())
                            {
                                // there was an error
                                if (password.length() < 6)
                                {
                                    inputPassword.setError(getString(R.string.minimum_password));
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                UserProfile.getInstance().setLocalAccount();
                                UserProfile.getInstance().userId = email.replace(".","-");
                                UserProfile.getInstance().updateUserProfile();
                                Toast.makeText(getActivity(), "Authentication SUCCESS", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(), MainScreen.class);
                                startActivity(intent); getActivity().finish();
                            }
                        }
                    });
                }
            });

            // Enabling Google Sign IN
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
            view.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    googleSignIn();
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

    @Override public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    private void googleSignIn() {
        UserProfile.getInstance().setGoogleAccount();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null) {
                String personName = account.getDisplayName();
                String personGivenName = account.getGivenName();
                String personFamilyName = account.getFamilyName();
                String personEmail = account.getEmail();
                String personId = account.getId();
                Uri personPhoto = account.getPhotoUrl();
                UserProfile.getInstance().createGoogleUserProfile(personEmail,personName,personPhoto.toString(),"","");

            }
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
            Intent intent = new Intent(getActivity(), MainScreen.class);
            startActivity(intent); getActivity().finish();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }


    /** * This interface must be implemented by activities that contain this
     * * fragment to allow an interaction in this fragment to be communicated
     * * to the activity and potentially other fragments contained in that * activity. */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onSignupRoutine();
    }
}

