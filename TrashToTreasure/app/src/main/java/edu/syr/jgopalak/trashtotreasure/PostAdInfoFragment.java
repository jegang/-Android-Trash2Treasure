/**
 * @FileName PostAdInfoFragment.java
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */

package edu.syr.jgopalak.trashtotreasure;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostAdInfoFragment.OnFragmentInteractionListenerPostAdInfo} interface
 * to handle interaction events.
 * Use the {@link PostAdInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostAdInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int REQ_CODE_SPEECH_INPUT = 100;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Parameters for Items in the fragment
    private EditText itemName;
    private Spinner categorySpinner;
    private EditText itemColor;
    private EditText itemAge;
    private EditText itemDescription;
    private ImageButton voiceToTextButton;
    private Button continueItem;

    private OnFragmentPostAdInfoInteractionListener mListener;

    public PostAdInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostAdInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostAdInfoFragment newInstance() {
        PostAdInfoFragment fragment = new PostAdInfoFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post_ad_info , container , false);



        //Parameters for Items in the fragment
        itemName = (EditText) view.findViewById(R.id.itemName);
        categorySpinner = (Spinner) view.findViewById(R.id.categorySpinner);
        itemColor = (EditText) view.findViewById(R.id.itemColor);
        itemAge = (EditText) view.findViewById(R.id.itemAge);
        itemDescription = (EditText) view.findViewById(R.id.itemDescription);
        voiceToTextButton = (ImageButton) view.findViewById(R.id.voiceToTextButton);
        continueItem = (Button) view.findViewById(R.id.continueItem);

        // Code to convert text to speech
        voiceToTextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                promptSpeechInput();
            }
        });

        continueItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                initiateLocationFragment();
            }
        });

        return view;
    }

    // Method to store information
    private void initiateLocationFragment()
    {
        HashMap<String,String> adInfo = new HashMap<String,String>();

        String name = itemName.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String color = itemColor.getText().toString();
        String age = itemAge.getText().toString();
        String desc = itemDescription.getText().toString();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(category) || TextUtils.isEmpty(color) || TextUtils.isEmpty(age) || TextUtils.isEmpty(desc)) {
            Toast.makeText(getContext(),
                    "All Fields must be entered to post your ad",
                    Toast.LENGTH_LONG).show();
        }else{
            adInfo.put("title", itemName.getText().toString());
            adInfo.put("category", categorySpinner.getSelectedItem().toString());
            adInfo.put("color", itemColor.getText().toString());
            adInfo.put("Age", itemAge.getText().toString());
            adInfo.put("description", itemDescription.getText().toString());

            if (mListener != null) {
                mListener.onFragmentInteractionPostAdInfo(adInfo);
            }
            else
            {
                Log.e("initiateLocationFrag", "mListener null");
            }
        }

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentPostAdInfoInteractionListener)
        {
            mListener = (OnFragmentPostAdInfoInteractionListener) context;
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

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {

            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a)
        {
            Toast.makeText(getContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data)
                {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    itemDescription.setText(result.get(0));
                }
                break;
            }

        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentPostAdInfoInteractionListener {
        // TODO: Update argument type and name
        void
        onFragmentInteractionPostAdInfo(HashMap<String, String> info);
    }
}
