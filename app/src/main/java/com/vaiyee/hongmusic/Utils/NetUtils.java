package com.vaiyee.hongmusic.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.vaiyee.hongmusic.MyApplication;

/**
 * Created by Administrator on 2018/2/23.
 */


    public class NetUtils {

        private static ConnectivityManager mConnectivityManager;
        static {
            mConnectivityManager = (ConnectivityManager) MyApplication.getQuanjuContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        public enum NetType {
            NET_WIFI,
            NET_4G,
            NET_3G,
            NET_2G,
            NET_UNKNOW,
            NET_NONE
        }

        public static synchronized boolean isNetConnected() {
            NetworkInfo activeNetInfo = null;
            if (mConnectivityManager != null) {
                // 修改有些ROM getActiveNetworkInfo 报NullPointerException异常的问题
                try {
                    activeNetInfo = mConnectivityManager.getActiveNetworkInfo();
                } catch (NullPointerException e) {
                }
            }
            return activeNetInfo == null ? false : activeNetInfo.isConnected();
        }

        public static synchronized NetType getNetType() {
            NetType iType = NetType.NET_NONE;
            NetworkInfo networkInfo = null;
            if (mConnectivityManager != null) {
                // 修改有些ROM getActiveNetworkInfo 报NullPointerException异常的问题
                try {
                    networkInfo = mConnectivityManager.getActiveNetworkInfo();
                } catch (NullPointerException e) {
                }
            }
            if (networkInfo == null) {
                return NetType.NET_NONE;
            }
            int nType = networkInfo.getType();
            switch (nType) {
                case ConnectivityManager.TYPE_MOBILE:
                    int subType = networkInfo.getSubtype();
                    if (android.os.Build.VERSION.SDK_INT < 15) {
                        switch (subType) {
                            case TelephonyManager.NETWORK_TYPE_GPRS:
                            case TelephonyManager.NETWORK_TYPE_EDGE:
                            case TelephonyManager.NETWORK_TYPE_CDMA:
                            case TelephonyManager.NETWORK_TYPE_1xRTT:
                            case TelephonyManager.NETWORK_TYPE_IDEN:
                                iType = NetType.NET_2G;
                                break;
                            case TelephonyManager.NETWORK_TYPE_UMTS:
                            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                            case TelephonyManager.NETWORK_TYPE_HSDPA:
                            case TelephonyManager.NETWORK_TYPE_HSUPA:
                            case TelephonyManager.NETWORK_TYPE_HSPA:
                            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                                iType = NetType.NET_3G;
                                break;
                            default:
                                iType = NetType.NET_UNKNOW;
                                break;
                        }
                    } else {
                        // 4.1开始判断4G网络
                        switch (subType) {
                            case TelephonyManager.NETWORK_TYPE_GPRS:
                            case TelephonyManager.NETWORK_TYPE_EDGE:
                            case TelephonyManager.NETWORK_TYPE_CDMA:
                            case TelephonyManager.NETWORK_TYPE_1xRTT:
                            case TelephonyManager.NETWORK_TYPE_IDEN:
                                iType = NetType.NET_2G;
                                break;
                            case TelephonyManager.NETWORK_TYPE_UMTS:
                            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                            case TelephonyManager.NETWORK_TYPE_HSDPA:
                            case TelephonyManager.NETWORK_TYPE_HSUPA:
                            case TelephonyManager.NETWORK_TYPE_HSPA:
                            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                            case TelephonyManager.NETWORK_TYPE_EHRPD:
                            case TelephonyManager.NETWORK_TYPE_HSPAP:
                                iType = NetType.NET_3G;
                                break;
                            case TelephonyManager.NETWORK_TYPE_LTE:
                                iType = NetType.NET_4G;
                                break;
                            default:
                                iType = NetType.NET_UNKNOW;
                                break;
                        }
                    }
                    break;
                case ConnectivityManager.TYPE_WIFI:
                    iType = NetType.NET_WIFI;
                    break;
                default:
                    iType = NetType.NET_UNKNOW;
                    break;
            }
            return iType;
        }
    }

