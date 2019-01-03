package ntt.bps.namberwan.allstatntt;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by zipo on 16/04/16.
 */
public class AppUtil {

    public static String getDate(String dateString, boolean isSection){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String result = "";
        String day = "";
        String month = "";
        try {
            Date date = dateFormat.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            switch (cal.get(Calendar.DAY_OF_WEEK)){
                case 1:
                    day = "Minggu";
                    break;
                case 2:
                    day = "Senin";
                    break;
                case 3:
                    day = "Selasa";
                    break;
                case 4:
                    day = "Rabu";
                    break;
                case 5:
                    day = "Kamis";
                    break;
                case 6:
                    day = "Jumat";
                    break;
                case 7:
                    day = "Sabtu";
                    break;
            }

            switch (cal.get(Calendar.MONTH)){
                case 0:
                    month = "Januari";
                    break;
                case 1:
                    month = "Februari";
                    break;
                case 2:
                    month = "Maret";
                    break;
                case 3:
                    month = "April";
                    break;
                case 4:
                    month = "Mei";
                    break;
                case 5:
                    month = "Juni";
                    break;
                case 6:
                    month = "Juli";
                    break;
                case 7:
                    month = "Agustus";
                    break;
                case 8:
                    month = "September";
                    break;
                case 9:
                    month = "Oktober";
                    break;
                case 10:
                    month = "November";
                    break;
                case 11:
                    month = "Desember";
                    break;
            }
            if(isSection){
                result = result + month +" "+cal.get(Calendar.YEAR);
            }else {
                result = result + day+", "+cal.get(Calendar.DATE)+ " "+ month +" "+cal.get(Calendar.YEAR);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int getCurrentYear(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int tahun = 0;
        try {
            Date date = format.parse(format.format(calendar.getTime()));
            calendar.setTime(date);
            tahun = calendar.get(Calendar.YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tahun;
    }

    public static int getColorTheme(int kategori){
        if (kategori == 1){
            return R.color.blue;
        }else if (kategori == 2){
            return R.color.orange;
        }else if (kategori == 3){
            return R.color.green;
        }else {
            return R.color.black;
        }
    }

    public static void downloadFile(Activity activity, String url, String title){
        DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(title);
        downloadManager.enqueue(request);

    }

    private static List<String> getShareApplication(){
        List<String> mList = new ArrayList<>();
        mList.add("com.facebook.katana");
        mList.add("com.twitter.android");
        mList.add("com.google.android.apps.plus");
        mList.add("com.facebook.lite");
        mList.add("com.tumblr");
        mList.add("com.yahoo.mobile.client.android.flickr");
        mList.add("com.instagram.android");
        mList.add("com.pinterest");
        return mList;
    }

    public static void share(Activity activity, String judul, String url) {
        try
        {
            List<Intent> targetedShareIntents = new ArrayList<>();
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            List<ResolveInfo> resInfo = activity.getPackageManager().queryIntentActivities(share, 0);
            if (!resInfo.isEmpty()){
                for (ResolveInfo info : resInfo) {
                    Intent targetedShare = new Intent(android.content.Intent.ACTION_SEND);
                    targetedShare.setType("text/plain");
                    if (getShareApplication().contains(info.activityInfo.packageName.toLowerCase())) {
                        targetedShare.putExtra(Intent.EXTRA_SUBJECT,judul);
                        targetedShare.putExtra(Intent.EXTRA_TEXT,url);
                        targetedShare.setPackage(info.activityInfo.packageName.toLowerCase());
                        targetedShareIntents.add(targetedShare);
                    }
                }
                Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Bagikan");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[targetedShareIntents.size()]));
                activity.startActivity(chooserIntent);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String formatNumberSeparator(float f){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        //formatter.applyPattern("#.####");
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');
        symbols.setDecimalSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);

        return formatter.format(f);
    }

}
