package pub3.war.bsdiffdemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by turbo on 2017/8/10.
 */

public class Utils {
    public static String apkExtract(Context context) {
        context = context.getApplicationContext();
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        String apkPath = applicationInfo.sourceDir;
        Log.d("hongyang", apkPath);
        return apkPath;
    }

    static {
        System.loadLibrary("bsdiff");
    }

    public static native int bspatch(String oldApk, String newApk, String patch);

    public static void doBspatch(Context context) {
        final File destApk = new File(Environment.getExternalStorageDirectory(), "dest.apk");
        final File patch = new File(Environment.getExternalStorageDirectory(), "PATCH.patch");

        //一定要检查文件都存在

        bspatch(apkExtract(context),
                destApk.getAbsolutePath(),
                patch.getAbsolutePath());

        if (destApk.exists())
            install(context, destApk.getAbsolutePath());
    }

    public static void install(Context context, String apkPath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.fromFile(new File(apkPath)),
                "application/vnd.android.package-archive");
        context.startActivity(i);
    }
}
