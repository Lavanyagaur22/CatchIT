package com.codingblocks.device_admin;

import android.app.Activity;
import android.content.Intent;

public class themeUtils {

    private static int cTheme;



    public final static int Nobita = 4;

    public final static int BLUE = 1;

    public static final int Default =0;
    public static final int Grey =3;

    public static void changeToTheme(Activity activity, int theme)

    {

        cTheme = theme;

        activity.finish();



        activity.startActivity(new Intent(activity, activity.getClass()));


    }

    public static void onActivityCreateSetTheme(Activity activity)

    {

        switch (cTheme)

        {


            default:

            case Default:
                activity.setTheme(R.style.AppTheme);
                break;


            case Nobita:

                activity.setTheme(R.style.BlackTheme); //paperlight

                break;

            case BLUE:

                activity.setTheme(R.style.BlueTheme); //paperdark

                break;

            case Grey:

                activity.setTheme(R.style.GreyTheme); //paperdark

                break;


        }



}
}
