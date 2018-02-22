package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.os.SystemClock.sleep;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleAlertPopupFragment extends Fragment {

    String TAG = "ModuleAlertPopupFragment";

    private MainActivity Main_activity;
    private View View_main;
    RelativeLayout popup_box;

    public ModuleAlertPopupFragment() {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleAlertPopupFragment"));
        SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleAlertPopupFragment"));
    }

    @SuppressLint("ValidFragment")
    public ModuleAlertPopupFragment(MainActivity main_activity) {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleAlertPopupFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "main_activity", String.valueOf(main_activity)));

        Main_activity = main_activity;
        SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleAlertPopupFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "main_activity", String.valueOf(main_activity)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_alert_popup, container, false);
        popup_box = View_main.findViewById(R.id.popup_box);
        popup_box.setVisibility(View.INVISIBLE);
        return View_main;
    }

    void Alert(String type){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "Alert") + " - " + TAG_MODIFIED.tagArgument("String", "type", type));
        SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "Alert") + " - " + TAG_MODIFIED.tagArgument("String", "type", type));
        popup_box.setVisibility(View.VISIBLE);
        TextView text = View_main.findViewById(R.id.popup_text);
        switch (type){
            case "RFID_Error": {
                text.setText("This card has not been registered to the Time Sloths system. Please contact IT department");
                break;
            }
            case "usenow_text_subject": {
                text.setText("Please enter subject");
                break;
            }
            default: {
                text.setText(type);
                break;
            }
        }
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                //Log.d("countdown", "seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                popup_box.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

}
