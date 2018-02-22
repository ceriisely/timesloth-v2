package com.example.obberertest.timesloth_sm10;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.conn.util.InetAddressUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.pow;

public class NetworkSettingActivity extends Activity {

    static String TAG = "NetworkSettingActivity";

    private FragmentTransaction fragmentTransaction;
    private ModuleConfigEthernetFragment config_ethernet_fragment;
    private ModuleConfigWiFiFragment config_wifi_fragment;
    boolean isConnected;
    boolean isConnectedWifi;
    boolean isConnectedMobile;
    String IP_address;
    String IP_subnet_mask;
    private WifiManager wifii;
    private DhcpInfo d;
    private String s_dns1;
    private String s_dns2;
    private String s_gateway;
    private String s_ipAddress;
    private String s_leaseDuration;
    private String s_netmask;
    private String s_serverAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onCreate"));
        setContentView(R.layout.activity_network_setting);
        wifii= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        d=wifii.getDhcpInfo();

        s_dns1="DNS 1: "+intToIp(d.dns1);
        s_dns2="DNS 2: "+intToIp(d.dns2);
        s_gateway="Default Gateway: "+intToIp(d.gateway);
        s_ipAddress="IP Address: "+intToIp(d.ipAddress);
        s_leaseDuration="Lease Time: "+intToIp(d.leaseDuration);
        s_netmask="Subnet Mask: "+intToIp(d.netmask);
        s_serverAddress="Server IP: "+intToIp(d.serverAddress);

        //dispaly them
        //info= (TextView) findViewById(R.id.infolbl);
        Log.d(TAG, "Network Info "+s_dns1+" "+s_dns2+" "+s_gateway+" "+s_ipAddress+" "+s_leaseDuration+" "+s_netmask+" "+s_serverAddress);

        Class<?> SystemProperties = null;
        try {
            SystemProperties = Class.forName("android.os.SystemProperties");
            Method method = SystemProperties.getMethod("get", new Class[] { String.class });
            ArrayList<String> servers = new ArrayList<String>();
            for (String name : new String[] { "net.dns1", "net.dns2", "net.dns3", "net.dns4", }) {
                String value = (String) method.invoke(null, name);
                Log.d(TAG, "value " + value);
                value = (String) method.getName();
                Log.d(TAG, "value gateway " + value);
                if (value != null && !"".equals(value) && !servers.contains(value))
                    servers.add(value);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        config_ethernet_fragment = new ModuleConfigEthernetFragment(this);
        config_wifi_fragment = new ModuleConfigWiFiFragment();
        isConnectedFast(NetworkSettingActivity.this);
        try {
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            fragmentTransaction.commitAllowingStateLoss();
        }
        bindView();

        checkTypeConnection();
    }
    public String intToIp(int addr) {
        return  ((addr & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF));
    }
    private void bindView() {
        Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "bindView"));
        final RelativeLayout layout_ethernet = findViewById(R.id.layout_ethernet);
        final RelativeLayout layout_wifi = findViewById(R.id.layout_wifi);

        layout_ethernet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, TAG_MODIFIED.tagOnClick("layout_ethernet", "RelativeLayout"));
                showLayoutTab("ethernet");
            }
        });
        layout_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, TAG_MODIFIED.tagOnClick("layout_wifi", "RelativeLayout"));
                showLayoutTab("wifi");
            }
        });
    }

    private void showLayoutTab(String key){
        Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "showLayoutTab") + " - " + TAG_MODIFIED.tagArgument("String", "key", key));

        RelativeLayout layout_ethernet = findViewById(R.id.layout_ethernet);
        RelativeLayout layout_wifi = findViewById(R.id.layout_wifi);

        switch (key){
            case "ethernet" :   {
                for(int index = 0; index< layout_ethernet.getChildCount(); ++index) {
                    View nextChild = layout_ethernet.getChildAt(index);
                    nextChild.setBackground(getResources().getDrawable(R.drawable.network_setting_activity_tab_select));
                }
                for(int index = 0; index< layout_wifi.getChildCount(); ++index) {
                    View nextChild = layout_wifi.getChildAt(index);
                    nextChild.setBackground(getResources().getDrawable(R.drawable.network_setting_activity_tab_notselect));
                }
                if(config_wifi_fragment.isAdded()){
                    try {
                        getFragmentManager().beginTransaction().remove(config_wifi_fragment).commit();
                    } catch (IllegalStateException e) {
                        getFragmentManager().beginTransaction().remove(config_wifi_fragment).commitAllowingStateLoss();
                    }
                }
                if(!config_ethernet_fragment.isAdded()){
                    try {
                        getFragmentManager().beginTransaction().add(R.id.fragment_network_setting_activity_container, config_ethernet_fragment).commit();
                    } catch (IllegalStateException e) {
                        getFragmentManager().beginTransaction().add(R.id.fragment_network_setting_activity_container, config_ethernet_fragment).commitAllowingStateLoss();
                    }
                }
                break;
            }
            case "wifi" :   {
                for(int index=0; index<layout_ethernet.getChildCount(); ++index) {
                    View nextChild = layout_ethernet.getChildAt(index);
                    nextChild.setBackground(getResources().getDrawable(R.drawable.network_setting_activity_tab_notselect));
                }
                for(int index = 0; index< layout_wifi.getChildCount(); ++index) {
                    View nextChild = layout_wifi.getChildAt(index);
                    nextChild.setBackground(getResources().getDrawable(R.drawable.network_setting_activity_tab_select));
                }
                if(config_ethernet_fragment.isAdded()){
                    try {
                        getFragmentManager().beginTransaction().remove(config_ethernet_fragment).commit();
                    } catch (IllegalStateException e) {
                        getFragmentManager().beginTransaction().remove(config_ethernet_fragment).commitAllowingStateLoss();
                    }
                }
                if(!config_wifi_fragment.isAdded()){
                    try {
                        getFragmentManager().beginTransaction().add(R.id.fragment_network_setting_activity_container, config_wifi_fragment).commit();
                    } catch (IllegalStateException e) {
                        getFragmentManager().beginTransaction().add(R.id.fragment_network_setting_activity_container, config_wifi_fragment).commitAllowingStateLoss();
                    }
                }
                break;
            }
            default :   {
                break;
            }
        }
    }

    private void checkTypeConnection(){
        Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "checkTypeConnection"));

        Context context = NetworkSettingActivity.this;
        isConnected = isConnected(context);
        isConnectedWifi = isConnectedWifi(context);
        isConnectedMobile = isConnectedMobile(context);

        Log.d("isConnected", String.valueOf(isConnected));
        Log.d("isConnectedWifi", String.valueOf(isConnectedWifi));
        Log.d("isConnectedMobile", String.valueOf(isConnectedMobile));

        if (isConnected){
            IP_address = getDeviceIPAddress(true);
            if(isConnectedWifi){
                showLayoutTab("wifi");
            }else if(isConnectedMobile){
                showLayoutTab("ethernet");
            }
        }
    }

    /**
     * Get the network info
     * @param context
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context){
        Log.d(TAG, TAG_MODIFIED.tagMethod("public static", "NetworkInfo", "getNetworkInfo"));

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     * @param context
     * @return
     */
    public static boolean isConnected(Context context){
        Log.d(TAG, TAG_MODIFIED.tagMethod("public static", "boolean", "isConnected"));

        NetworkInfo info = NetworkSettingActivity.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is any connectivity to a Wifi network
     * @param context
     * @return
     */
    public static boolean isConnectedWifi(Context context){
        Log.d(TAG, TAG_MODIFIED.tagMethod("public static", "boolean", "isConnectedWifi"));
        NetworkInfo info = NetworkSettingActivity.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     * @param context
     * @return
     */
    public static boolean isConnectedMobile(Context context){
        Log.d(TAG, TAG_MODIFIED.tagMethod("public static", "boolean", "isConnectedMobile"));
        NetworkInfo info = NetworkSettingActivity.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Check if there is fast connectivity
     * @param context
     * @return
     */
    public static boolean isConnectedFast(Context context){
        Log.d(TAG, TAG_MODIFIED.tagMethod("public static", "boolean", "isConnectedFast"));
        NetworkInfo info = NetworkSettingActivity.getNetworkInfo(context);
        return (info != null && info.isConnected() && NetworkSettingActivity.isConnectionFast(info.getType(),info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     * @param type
     * @param subType
     * @return
     */
    public static boolean isConnectionFast(int type, int subType){
        Log.d(TAG, TAG_MODIFIED.tagMethod("public static", "boolean", "isConnectionFast"));
        if(type==ConnectivityManager.TYPE_WIFI){
            return true;
        } else if (type == ConnectivityManager.TYPE_ETHERNET) {
            Log.d(TAG, "ethernettttttt");
            return true;
        }

        else if(type==ConnectivityManager.TYPE_MOBILE){
            switch(subType){
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
            /*
             * Above API level 7, make sure to set android:targetSdkVersion
             * to appropriate level to use these
             */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        }else{
            return false;
        }
    }

    public String getDeviceIPAddress(boolean useIPv4) {
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "String", "getDeviceIPAddress") + " - " + TAG_MODIFIED.tagArgument("boolean", "useIPv4", String.valueOf(useIPv4)));
        int count = 0;
        try {
            List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : networkInterfaces) {
                Log.d(TAG, "displayname " + networkInterface.getDisplayName());
                Log.d(TAG, "interface " + Arrays.toString(new List[]{networkInterface.getInterfaceAddresses()}));
                List<InetAddress> inetAddresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress inetAddress : inetAddresses) {
                    count++;
                    Log.d(TAG, "inetaddress " + Arrays.toString(new String[]{inetAddress.getHostAddress()}));
                    //Log.d(TAG, "inetaddress ffff" + inetAddress.getCanonicalHostName());
//                    if (!inetAddress.isLoopbackAddress()) {
//                        String sAddr = inetAddress.getHostAddress().toUpperCase();
//                        //String gateway = inetAddress.getHostName().toUpperCase();
//                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
//                        //getDeviceSubnetmask(inetAddress, count);
//                        if (useIPv4) {
//                            if (isIPv4) {
//                                //
//                                getDeviceSubnetmask(inetAddress, count);
//                                return sAddr;
//                            }
//                        } else {
//                            if (!isIPv4) {
//                                // drop ip6 port suffix
//                                getDeviceSubnetmask(inetAddress, count);
//                                int delim = sAddr.indexOf('%');
//                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
//                            }
//                        }
//                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public void getDeviceSubnetmask(InetAddress inetAddr, int count){
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "getDeviceSubnetmask") + " - " + TAG_MODIFIED.tagArgument("InetAddress", "inetAddr", String.valueOf(inetAddr)) + " - " + TAG_MODIFIED.tagArgument("int", "count", String.valueOf(count)));
        NetworkInterface temp;
        InetAddress iAddr = null;
        Log.d(TAG, "subnet mask " + "start");
        int _count = 0;
        try {
            temp = NetworkInterface.getByInetAddress(inetAddr);
            Log.d("temp", Arrays.toString(temp.getHardwareAddress()));
            List<InterfaceAddress> addresses = temp.getInterfaceAddresses();

            for (InterfaceAddress inetAddress : addresses){
                //
                _count++;
                if (count == _count){
                    Log.d(TAG, "Subnet mask " + String.valueOf(inetAddress.getNetworkPrefixLength()));
                    subnetByteToIPString(inetAddress.getNetworkPrefixLength());
                }
            }

        } catch (SocketException e) {
            Log.d(TAG, "subnet mask " + "error");
            e.printStackTrace();
        }
    }

    private void subnetByteToIPString(short networkPrefixLength) {
        Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "subnetByteToIPString") + " - " + TAG_MODIFIED.tagArgument("short", "networkPrefixLength", String.valueOf(networkPrefixLength)));
        String ip_string = "";
        int[] ip_bit = new int[32];
        for (int i=0;i<32;i++){
            if (i < networkPrefixLength){
                ip_bit[i] = 1;
            }else {
                ip_bit[i] = 0;
            }
        }
        int value = 0;
        int power_bit = 7;
        for (int i=0;i<32;i++){
            if (i > 0 && i % 8 == 0){
                ip_string += '.';
            }
            value += ip_bit[i] * (pow(2, power_bit));
            power_bit--;
            if (i%8 == 7){
                ip_string += String.valueOf(value);
                value = 0;
                power_bit = 7;
            }
        }
        IP_subnet_mask = ip_string;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onStart"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onResume"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onPause"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onDestroy"));
        //Here we are clearing back stack fragment entries
        int backStackEntry = getFragmentManager().getBackStackEntryCount();
        Log.d(TAG, "backStackEntry " + String.valueOf(backStackEntry));
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getFragmentManager().popBackStackImmediate();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onRestart"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onStop"));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);
        if((event.getAction() == KeyEvent.ACTION_DOWN) && (event.getAction() == KeyEvent.KEYCODE_ENTER)){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
        }
        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            assert w != null;
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event " + event.getRawX() + "," + event.getRawY() + " " + x + "," + y + " rect " + w.getLeft() + "," + w.getTop() + "," + w.getRight() + "," + w.getBottom() + " coords " + scrcoords[0] + "," + scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);


            }
        }
        return ret;
    }

    public static void setIpAssignment(String assign , WifiConfiguration wifiConf)
            throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
        setEnumField(wifiConf, assign, "ipAssignment");
    }

    public static void setIpAddress(InetAddress addr, int prefixLength, WifiConfiguration wifiConf)
            throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException,
            NoSuchMethodException, ClassNotFoundException, InstantiationException, InvocationTargetException{
        Object linkProperties = getField(wifiConf, "linkProperties");
        if(linkProperties == null)return;
        Class laClass = Class.forName("android.net.LinkAddress");
        Constructor laConstructor = laClass.getConstructor(new Class[]{InetAddress.class, int.class});
        Object linkAddress = laConstructor.newInstance(addr, prefixLength);

        ArrayList mLinkAddresses = (ArrayList)getDeclaredField(linkProperties, "mLinkAddresses");
        mLinkAddresses.clear();
        mLinkAddresses.add(linkAddress);
    }

    public static void setGateway(InetAddress gateway, WifiConfiguration wifiConf)
            throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        Object linkProperties = getField(wifiConf, "linkProperties");
        if(linkProperties == null)return;
        Class routeInfoClass = Class.forName("android.net.RouteInfo");
        Constructor routeInfoConstructor = routeInfoClass.getConstructor(new Class[]{InetAddress.class});
        Object routeInfo = routeInfoConstructor.newInstance(gateway);

        ArrayList mRoutes = (ArrayList)getDeclaredField(linkProperties, "mRoutes");
        mRoutes.clear();
        mRoutes.add(routeInfo);
    }

    public static void setDNS(InetAddress dns, WifiConfiguration wifiConf)
            throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
        Object linkProperties = getField(wifiConf, "linkProperties");
        if(linkProperties == null)return;

        ArrayList<InetAddress> mDnses = (ArrayList<InetAddress>)getDeclaredField(linkProperties, "mDnses");
        mDnses.clear(); //or add a new dns address , here I just want to replace DNS1
        mDnses.add(dns);
    }

    public static Object getField(Object obj, String name)
            throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        Field f = obj.getClass().getField(name);
        Object out = f.get(obj);
        return out;
    }

    public static Object getDeclaredField(Object obj, String name)
            throws SecurityException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        Object out = f.get(obj);
        return out;
    }

    private static void setEnumField(Object obj, String value, String name)
            throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        Field f = obj.getClass().getField(name);
        f.set(obj, Enum.valueOf((Class<Enum>) f.getType(), value));
    }
}
