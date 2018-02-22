package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModulePasswordFragment extends Fragment {

    private static String TAG = "ModulePasswordFragment";
    private MainActivity Main_activity;
    private View View_main;

    public ModulePasswordFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ModulePasswordFragment(MainActivity context) {
        // Required empty public constructor
        Main_activity = context;
        SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModulePasswordFragment"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_password, container, false);
        bindView();
        return View_main;
    }

    private void bindView() {
        SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "bindView"));
        final EditText text_pass = View_main.findViewById(R.id.edittext_password);

        Button btn_cancel = View_main.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_cancel", "Button"));
                text_pass.setText("");
                getFragmentManager().beginTransaction().remove(ModulePasswordFragment.this).commit();
            }
        });

        Button btn_ok = View_main.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_ok", "Button"));
                EditText text_pass = View_main.findViewById(R.id.edittext_password);
                String password = text_pass.getText().toString();
                if (Objects.equals(password, SiteData.Password)) {
                    //Main_activity.updateUi(false);
                    Main_activity.getFragmentManager().beginTransaction().remove(Main_activity.manage_main_fragment).commit();
                    Main_activity.getFragmentManager().beginTransaction().add(R.id.fragment_main_activity_container_front, Main_activity.module_setting_fragment, "TAG").commit();
                    text_pass.setText("");
                    try {
                        getFragmentManager().beginTransaction().remove(ModulePasswordFragment.this).commit();
                    } catch (IllegalStateException e) {
                        getFragmentManager().beginTransaction().remove(ModulePasswordFragment.this).commitAllowingStateLoss();
                    }
                }else {
                    text_pass.setText("");
                    Main_activity.module_frame_alert_popup_fragment.Alert("Password is incorrect");
                }
            }
        });
    }

}
