package com.cocoagarage.application.goodspeaks;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;

import com.cocoagarage.application.goodspeaks.Data.DataManager;
import com.cocoagarage.application.goodspeaks.Models.SpeechProject;
import com.cocoagarage.application.goodspeaks.R;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Utility {
    public static void onNaviationItemSelected(Activity currentActivity, @NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int menuItemId = -1;
        if (currentActivity.getClass() == MainActivity.class) {
            menuItemId = R.id.cc;
        } else if (currentActivity.getClass() == About.class) {
            menuItemId = R.id.about;
        } else if (currentActivity.getClass() == Competent_Leadership.class) {
            menuItemId = R.id.cl;
        } else if (currentActivity.getClass() == Mentorship.class) {
            menuItemId = R.id.mentor;
        }  else {
            menuItemId = -1;
        }

        int id = item.getItemId();
        if (id == menuItemId) {
            // Already this menu item selected.
            DrawerLayout drawer = (DrawerLayout)currentActivity.findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return ;
        }


        if (id==R.id.cc) {
            if(currentActivity.getClass() != competentCommunication.class)
            {
                currentActivity.startActivity(new Intent(currentActivity, competentCommunication.class));
            }
        }
        if(id==R.id.cl) {
            if(currentActivity.getClass() != Competent_Leadership.class)
            {
                currentActivity.startActivity(new Intent(currentActivity, Competent_Leadership.class));
            }
        }
        else if(id==R.id.rate) {
            final Activity acvt = currentActivity;
            String[] options = new String[]{currentActivity.getString(R.string.rate_us_now), currentActivity.getString(R.string.rate_later), currentActivity.getString(R.string.rate_not_happy)};
            AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
            final Activity appActivity = currentActivity;
            builder.setTitle(R.string.rate_screen_title)
                    .setItems(options, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0: // TODO: Open the Play Store link.
                                    final String appPackageName =  appActivity.getPackageName(); // getPackageName() from Context or Activity object
                                    try {
                                        appActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        appActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                    dialog.dismiss();
                                    break;
                                case 1: dialog.dismiss();
                                    break;
                                case 2:
                                    dialog.dismiss();
                                    Utility.sendEmail(new String[]{"kn.neeraj.89@gmail.com"},"App Feedback", "Your feedback here..", null,acvt, null);
                                    break;
                            }
                        }
                    });
            builder.create().show();
        } else if(id==R.id.about) {
            if(currentActivity.getClass() != About.class)
            {
                currentActivity.startActivity(new Intent(currentActivity,About.class));
            }
        } else if(id==R.id.tandc) {
            Utility.sendEmail(new String[]{"kn.neeraj.89@gmail.com"},"App Feedback", "Your feedback here..", null,currentActivity, null);
        }
//        else if (id==R.id.export_data) {
//            File attachment = Utility.exportDb(currentActivity);
//            Utility.sendEmail(new String[]{"kn.neeraj.89@gmail.com"},"App Feedback", "Your feedback here..", null,currentActivity, attachment);
//        }
        DrawerLayout drawer = (DrawerLayout)currentActivity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public static String convertDateToString(Date date) {
        Format formatter = new SimpleDateFormat("yyyy/MM/dd");
        try {
            return formatter.format(date);
        } catch(Exception e) {
            return null;
        }
    }

    public static Date convertStringToDate(String date) {
        Format formatter = new SimpleDateFormat("yyyy/MM/dd");
        try {
            return (Date)formatter.parseObject(date);
        }catch(Exception e) {
                return null;
        }
    }

    public static Date convertToDate(int year, int monthOfYear, int dayOfMonth) {
        Format formatter = new SimpleDateFormat("yyyy/MM/dd");
        try {
            return (Date)formatter.parseObject(String.valueOf(year) + "/" + String.valueOf(monthOfYear) + "/" + String.valueOf(dayOfMonth));
        } catch (Exception e) {
            Log.d("test", e.getLocalizedMessage());
            return null;
        }
    }

    public static Date todaysDate() {
        return (new Date());
    }

    public static void sendEmail(String[] recipients, String subject, String text, String cc, Activity fromActivity, File attachment) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        // intent.putExtra(Intent.EXTRA_CC,"ghi");
        if (attachment != null) {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + attachment.getAbsolutePath()));
        }
        intent.setType("text/html");
        fromActivity.startActivity(Intent.createChooser(intent, "Send mail"));
    }


    public static Intent refreshActivityIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(context.getString( R.string.refresh_activity_intent_action));
       return intent;
    }

    public static File exportDb(Context context) {
        // TODO: To be tested.
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "csvname.csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            csvWrite.writeNext(new String[]{ "Project Name" , "Title", "Status", "CompletionDate"});
            ArrayList<SpeechProject> speechProjects =  DataManager.sharedManager().getSpeechProjects(context);
            for (SpeechProject speechProject : speechProjects) {
                String arrStr[] = { speechProject.getProjectTitle(), speechProject.getSpeechTitle(), speechProject.getCompleted()?"Yes" : "No", Utility.convertDateToString(speechProject.getCompletionDate())};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            return file;
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
            return null;
        }
    }
}
