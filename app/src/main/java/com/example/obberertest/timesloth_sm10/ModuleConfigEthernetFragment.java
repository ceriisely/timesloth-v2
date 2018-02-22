package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleConfigEthernetFragment extends Fragment {

    static String TAG = "ModuleConfigEthernetFragment";

    private NetworkSettingActivity NetworkSetting_activity;
    private View View_main;
    private RadioButton radio_dhcp;
    private RadioButton radio_manually;
    private LinearLayout form_config_dhcp;
    private LinearLayout form_config_manually;

    public ModuleConfigEthernetFragment() {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleConfigEthernetFragment"));
        SiteData.writeFile(NetworkSetting_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleConfigEthernetFragment"));
    }

    @SuppressLint("ValidFragment")
    public ModuleConfigEthernetFragment(NetworkSettingActivity context) {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleConfigEthernetFragment") + " - " + TAG_MODIFIED.tagArgument("NetworkSettingActivity", "context", String.valueOf(context)));

        NetworkSetting_activity = context;
        SiteData.writeFile(NetworkSetting_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleConfigEthernetFragment") + " - " + TAG_MODIFIED.tagArgument("NetworkSettingActivity", "context", String.valueOf(context)));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        SiteData.writeFile(NetworkSetting_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_config_ethernet, container, false);
        radio_dhcp = View_main.findViewById(R.id.radio_dhcp);
        radio_manually = View_main.findViewById(R.id.radio_manually);
        form_config_dhcp = View_main.findViewById(R.id.form_config_dhcp);
        form_config_manually = View_main.findViewById(R.id.form_config_manually);
        bindView();
        initConfig();
        return View_main;
    }

    private void bindView() {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "bindView"));
        SiteData.writeFile(NetworkSetting_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "bindView"));
        radio_dhcp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked){
                    form_config_dhcp.setVisibility(View.VISIBLE);
                    form_config_manually.setVisibility(View.INVISIBLE);
                }
            }
        });
        radio_manually.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked){
                    form_config_manually.setVisibility(View.VISIBLE);
                    form_config_dhcp.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initConfig() {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "initConfig"));
        SiteData.writeFile(NetworkSetting_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "initConfig"));
        if(NetworkSetting_activity.isConnected){
            if (NetworkSetting_activity.isConnectedMobile){
                radio_dhcp.setChecked(true);
            }else {
                radio_manually.setChecked(true);
            }
        }

        Log.d("ip address", NetworkSetting_activity.IP_address);
        //Log.d("subnet mask", NetworkSetting_activity.IP_subnet_mask);
    }

}
