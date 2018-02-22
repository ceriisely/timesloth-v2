package com.example.obberertest.timesloth_sm10;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleConfigWiFiFragment extends Fragment {

    static String TAG = "ModuleConfigWiFiFragment";

    public ModuleConfigWiFiFragment() {
        // Required empty public constructor
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleConfigWiFiFragment"));
        //SiteData.writeFile(NetworkSetting_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "initConfig"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        return inflater.inflate(R.layout.fragment_module_config_wi_fi, container, false);
    }

}
