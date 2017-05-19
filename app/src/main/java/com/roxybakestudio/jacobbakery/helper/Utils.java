package com.roxybakestudio.jacobbakery.helper;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.roxybakestudio.jacobbakery.R;

public class Utils {

    private final static String NUTELLA_PIE = "Nutella Pie";
    private final static String BROWNIES = "Brownies";
    private final static String YELLOW_CAKE = "Yellow Cake";
    private final static String CHEESE_CAKE = "Cheesecake";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        return info != null && info.isConnectedOrConnecting();
    }

    public static int getImage(String name) {
        switch (name) {
            case NUTELLA_PIE:
                return R.drawable.display_nutella_pie;
            case BROWNIES:
                return R.drawable.display_brownies;
            case YELLOW_CAKE:
                return R.drawable.display_yellow_cake;
            case CHEESE_CAKE:
                return R.drawable.display_cheesecake;
            default:
                return R.drawable.display_nutella_pie;
        }
    }

    public static String capitalizeFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String getMeasure(String measure) {
        switch (measure) {
            case "CUP":
                return "Cup";
            case "G":
                return "Gram";
            case "UNIT":
                return "Unit";
            case "K":
                return "Kilo";
            case "TBLSP":
                return "TBLSP";
            case "TSP":
                return "TSP";
            default:
                return measure;
        }
    }

    public static String doubleToString(Double doubleNumber) {
        return String.valueOf(doubleNumber.intValue());
    }
}
