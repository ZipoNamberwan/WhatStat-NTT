package ntt.bps.namberwan.allstatntt;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;
import com.tonyodev.fetch2core.Func;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by zipo on 16/04/16.
 */
public class AppUtils {

    private static final String MY_SHARED_PREFERENCE = "bps provinsi ntt shared preference";

    private static final String TOKEN_KEY = "token key";

    private static final String CHANNEL_ID = "id notifikasi channel indikator strategis NTT";

    private static final int NOTIFICATION_DOWNLOAD_ID = 116951;

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

    public static void downloadFile(final Activity activity, String url, final String title, String file){

        //Buat format notifikasi
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(activity, CHANNEL_ID);
        mBuilder.build();

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);

        //Konfigurasi download
        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(activity)
                .setDownloadConcurrentLimit(3)
                .enableFileExistChecks(false)
                .build();
        final Fetch fetch = Fetch.Impl.getInstance(fetchConfiguration);

        //Listener download
        FetchListener listener = new FetchListener() {
            @Override
            public void onAdded(@NotNull Download download) {
                mBuilder.setSmallIcon(R.drawable.baseline_save_24)
                        .setProgress(100, 0, true)
                        .setContentTitle(title.substring(0, 35) + "...")
                        .setOnlyAlertOnce(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                notificationManager.notify(NOTIFICATION_DOWNLOAD_ID, mBuilder.build());
            }

            @Override
            public void onQueued(@NotNull Download download, boolean b) {

            }

            @Override
            public void onWaitingNetwork(@NotNull Download download) {

            }

            @Override
            public void onCompleted(@NotNull Download download) {

                String filePath = download.getFile();
                File file = new File(filePath);
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String ext=file.getName().substring(file.getName().indexOf(".")+1);
                String type = mime.getMimeTypeFromExtension(ext);

                Uri uri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".ntt.bps.namberwan.allstatntt.provider", file);

                Intent openFile = new Intent(Intent.ACTION_VIEW, uri);
                openFile.setDataAndType(uri, type);
                openFile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                openFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, openFile, 0);

                mBuilder.setSmallIcon(R.drawable.baseline_check_24)
                        .setOngoing(false)
                        .setContentText("Download Selesai")
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

                notificationManager.notify(NOTIFICATION_DOWNLOAD_ID, mBuilder.build());

                fetch.remove(download.getId());

            }

            @Override
            public void onError(@NotNull Download download, @NotNull Error error, @Nullable Throwable throwable) {
                mBuilder.setSmallIcon(R.drawable.baseline_error_24)
                        .setOngoing(false)
                        .setContentText("Download Gagal :(");

                notificationManager.notify(NOTIFICATION_DOWNLOAD_ID, mBuilder.build());

                fetch.remove(download.getId());
            }

            @Override
            public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {

            }

            @Override
            public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {

            }

            @Override
            public void onProgress(@NotNull Download download, long l, long l1) {
                mBuilder.setProgress(100, download.getProgress(), false)
                        .setOngoing(true)
                        .setContentText(getETAString(activity, l));

                notificationManager.notify(NOTIFICATION_DOWNLOAD_ID, mBuilder.build());
            }

            @Override
            public void onPaused(@NotNull Download download) {

            }

            @Override
            public void onResumed(@NotNull Download download) {

            }

            @Override
            public void onCancelled(@NotNull Download download) {

            }

            @Override
            public void onRemoved(@NotNull Download download) {

            }

            @Override
            public void onDeleted(@NotNull Download download) {

            }
        };

        //Download File
        String destinationFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + file;

        final Request request = new Request(url, destinationFile);
        request.setPriority(Priority.HIGH);
        request.setNetworkType(NetworkType.ALL);

        fetch.addListener(listener);
        fetch.enqueue(request, new Func<Request>() {
            @Override
            public void call(@NotNull Request result) {
                int i = 0;
            }
        }, new Func<Error>() {
            @Override
            public void call(@NotNull Error result) {
                int i = 0;
            }
        });
    }

    private static List<String> getShareApplication(){
        List<String> mList = new ArrayList<>();
        mList.add("com.facebook.katana");
        mList.add("com.twitter.android");
        mList.add("com.facebook.lite");
        mList.add("com.instagram.android");
        mList.add("com.whatsapp");
        mList.add("org.telegram.messenger");
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

    public static void saveToken(Activity activity, String token){
        SharedPreferences.Editor editor = activity.getSharedPreferences(MY_SHARED_PREFERENCE, MODE_PRIVATE).edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    public static String getToken(Activity activity){
        SharedPreferences prefs = activity.getSharedPreferences(MY_SHARED_PREFERENCE, MODE_PRIVATE);
        return prefs.getString(TOKEN_KEY, null);
    }

    public static void createNotificationChannel(Activity activity) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = activity.getString(R.string.channel_name);
            String description = activity.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other data behaviors after this
            NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private static String getETAString(Activity activity, final long etaInMilliSeconds) {
        if (etaInMilliSeconds < 0) {
            return "";
        }
        int seconds = (int) (etaInMilliSeconds / 1000);
        long hours = seconds / 3600;
        seconds -= hours * 3600;
        long minutes = seconds / 60;
        seconds -= minutes * 60;
        if (hours > 0) {
            return activity.getString(R.string.download_eta_hrs, hours, minutes, seconds);
        } else if (minutes > 0) {
            return activity.getString(R.string.download_eta_min, minutes, seconds);
        } else {
            return activity.getString(R.string.download_eta_sec, seconds);
        }
    }

    public static String getUrlShare(String prefix, String tanggal, String id, String title){

        String tanggalUrl = tanggal.replace("-", "/");
        String titleUrl = title.toLowerCase().replaceAll("[^A-Za-z0-9]", "-");

        return prefix + tanggalUrl + "/" + id +"/" + titleUrl + ".html";

    }

    public static String getVarId(String indikatorId){
        String s = "";
        switch (indikatorId) {
            case "1":
                //IPM
                s = "46";
                break;
            case "2":
                //jumlah penduduk
                s = "28";
                break;
            case "3":
                //inflasi
                s =  "1";
                break;
            case "4":
                //jml penduduk miskin
                s = "584";
                break;
            case "5":
                //pengangguran
                s = "522";
                break;
            case "7":
                //pertumbuhan ekonomi
                s = "438";
                break;
            case "8":
                //Harapan Hidup
                s = "583";
                break;
            case "9":
                //Ekspor
                s = "107";
                break;
            case "10":
                //Impor
                s = "109";
                break;
            case "11":
                //NTP
                s = "104";
                break;
            case "12":
                //Jumlah Wisman
                s = "67";
                break;
            case "13":
                //Gini Rasio
                s = "616";
                break;
        }
        return s;
    }
}
