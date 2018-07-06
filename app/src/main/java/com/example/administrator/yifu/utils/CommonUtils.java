package com.example.administrator.yifu.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.administrator.yifu.AppController;
import com.example.administrator.yifu.R;
import com.google.gson.Gson;

import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;


/**
 * Created by wangben on 2016/4/29.
 */
public final class CommonUtils
{
    private static final String TAG = CommonUtils.class.getSimpleName();

    public static final String SEPARATOR = "!=end=!";

    private CommonUtils()
    {
    }



    /**
     * Returns the available screensize, including status bar and navigation bar
     */
    public static Size getScreenSize(Activity context)
    {
        Display display = context.getWindowManager().getDefaultDisplay();
        int realWidth;
        int realHeight;

        if (Build.VERSION.SDK_INT >= 17)
        {
            DisplayMetrics realMetrics = new DisplayMetrics();
            display.getRealMetrics(realMetrics);
            realWidth = realMetrics.widthPixels;
            realHeight = realMetrics.heightPixels;
        }
        else if (Build.VERSION.SDK_INT >= 14)
        {
            try
            {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                realWidth = (Integer) mGetRawW.invoke(display);
                realHeight = (Integer) mGetRawH.invoke(display);
            } catch (RuntimeException e)
            {
                throw e;
            } catch (Exception e)
            {
                //this may not be 100% accurate, but it's all we've got
                realWidth = display.getWidth();
                realHeight = display.getHeight();
            }
        }
        else
        {
            //This should be close, as lower API devices should not have window navigation bars
            realWidth = display.getWidth();
            realHeight = display.getHeight();
        }
        return new Size(realWidth, realHeight);
    }

    public static class Size
    {
        public final int width;
        public final int height;

        public Size(int width, int height)
        {
            this.width = width;
            this.height = height;
        }
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位转成为 dp
     */
    public static int px2dp(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @param sp
     * @return
     */
    public static int sp2px(Context context, int sp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp,
                context.getResources().getDisplayMetrics());
    }


    /**
     * 判断app是否处于前台
     *
     * @return
     */
    public static boolean isAppOnForeground(Context mActivity)
    {
        ActivityManager activityManager = (ActivityManager) mActivity.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = mActivity.getApplicationContext().getPackageName();
        /**
         * 获取Android设备中所有正在运行的App
         */
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses)
        {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 保留两位小数
     *
     * @return
     */
    public static String keep2Decimal(double inputNumber)
    {
        DecimalFormat df = new DecimalFormat("######0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(inputNumber);
    }

    /**
     * 获取设备号
     *
     * @param context
     * @return
     */
    public static String getDeviceId(@Nullable Context context)
    {
        String id = "";
        if (context == null)
            context = AppController.getInstance();
//        TelephonyManager mTeleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        if (!TextUtils.isEmpty(mTeleManager.getDeviceId()))//需要特定权限
//        {
//            id = mTeleManager.getDeviceId();
//        }
//        else
        try
        {
            id = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return id;
    }


    public static String getJsonFromFile(File file)
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String readline = "";
            StringBuffer sb = new StringBuffer();
            while ((readline = br.readLine()) != null)
            {
                sb.append(readline);
            }
            br.close();
            Logger.t(TAG).d("读取成功：" + sb.toString());
            return sb.toString();
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return "";
    }


    /**
     * Lst转数组
     *
     * @param list
     * @return
     */
    public static String[] listToStrArr(List<String> list)
    {
        String[] strArr = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
        {
            strArr[i] = list.get(i);
        }
        return strArr;
    }


    /**
     * 将字符串list拼接成含有分割付的串
     *
     * @param list
     * @param separator
     * @return
     */
    public static String listToStrWishSeparator(List<String> list, String separator)
    {
        if (list == null || list.size() == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++)
        {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0, sb.toString().length() - separator.length());
    }

    /**
     * 将使用分隔符分割的字符串转化为List
     *
     * @param sourceStr
     * @param separator
     * @return
     */
    public static List<String> strWithSeparatorToList(String sourceStr, String separator)
    {
        if (TextUtils.isEmpty(sourceStr))
            return new ArrayList<>();
        return Arrays.asList(sourceStr.split(separator));
    }

    /**
     * 将以!=end=!分割字符串转换为集合
     *
     * @param temp
     * @return
     */
    public static List<String> strToList(String temp)
    {
        if (!TextUtils.isEmpty(temp))
        {
            List<String> list = new ArrayList<>();
            String[] str = temp.split(CommonUtils.SEPARATOR);
            for (int i = 0; i < str.length; i++)
            {
                list.add(str[i]);
            }
            return list;
        }
        return new ArrayList<>();
    }

    /**
     * 打开拨号界面
     *
     * @param mContext
     * @param phoneNum
     */
    public static void makeCall(Activity mContext, String phoneNum)
    {
        Intent intentP = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
        intentP.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intentP);
    }

    /**
     * 将list转换成string
     */
    public static String sceneListToString(List sceneList) throws IOException
    {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(sceneList);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String sceneListString = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT), "UTF-8");
        // 关闭objectOutputStream
        objectOutputStream.close();
        return sceneListString;
    }

    /**
     * 将String 转换成list
     */
    public static List stringToSceneList(String sceneListString)
            throws StreamCorruptedException, IOException, ClassNotFoundException
    {
        byte[] mobileBytes = Base64.decode(sceneListString.getBytes("UTF-8"), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        List sceneList = (List) objectInputStream.readObject();
        objectInputStream.close();
        return sceneList;
    }

    /**
     * 将View转化为bitmap
     *
     * @param addViewContent
     * @return
     */
    public static Bitmap getViewBitmap(View addViewContent)
    {
        addViewContent.setDrawingCacheEnabled(true);
        addViewContent.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0, addViewContent.getMeasuredWidth(), addViewContent.getMeasuredHeight());
        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        return bitmap;
    }



    private static final double EARTH_RADIUS = 6378137.0;

    /**
     * 通过经纬度计算两点直接的距离
     *
     * @param lon1
     * @param lat1
     * @param lon2
     * @param lat2
     * @return 返回单位是米
     */
    public static double getDistance(double lon1, double lat1, double lon2, double lat2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);

        double radLon1 = rad(lon1);
        double radLon2 = rad(lon2);

        if (radLat1 < 0)
            radLat1 = Math.PI / 2 + Math.abs(radLat1);// south
        if (radLat1 > 0)
            radLat1 = Math.PI / 2 - Math.abs(radLat1);// north
        if (radLon1 < 0)
            radLon1 = Math.PI * 2 - Math.abs(radLon1);// west
        if (radLat2 < 0)
            radLat2 = Math.PI / 2 + Math.abs(radLat2);// south
        if (radLat2 > 0)
            radLat2 = Math.PI / 2 - Math.abs(radLat2);// north
        if (radLon2 < 0)
            radLon2 = Math.PI * 2 - Math.abs(radLon2);// west
        double x1 = EARTH_RADIUS * Math.cos(radLon1) * Math.sin(radLat1);
        double y1 = EARTH_RADIUS * Math.sin(radLon1) * Math.sin(radLat1);
        double z1 = EARTH_RADIUS * Math.cos(radLat1);

        double x2 = EARTH_RADIUS * Math.cos(radLon2) * Math.sin(radLat2);
        double y2 = EARTH_RADIUS * Math.sin(radLon2) * Math.sin(radLat2);
        double z2 = EARTH_RADIUS * Math.cos(radLat2);

        double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
        //余弦定理求夹角
        double theta = Math.acos((EARTH_RADIUS * EARTH_RADIUS + EARTH_RADIUS * EARTH_RADIUS - d * d) / (2 * EARTH_RADIUS * EARTH_RADIUS));
        double dist = theta * EARTH_RADIUS;
        return dist;
    }

    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }






    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVerCode(@Nullable Context context)
    {
        if (context == null)
            context = AppController.getInstance();
        int verCode = -1;
        try
        {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e)
        {
            Log.e("获取版本号失败", e.getMessage());
        }
        return verCode;
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVerName(Context context)
    {
        String verCode = "";
        try
        {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e)
        {
            Log.e("获取版本名称失败", e.getMessage());
        }
        return verCode;
    }

    /**
     * 获取程序包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context)
    {
        String packageName = "";
        try
        {
            packageName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).
                    packageName;
        } catch (PackageManager.NameNotFoundException e)
        {
            Log.e("获取程序名称失败", e.getMessage());
        }
        return packageName;
    }

    // 获取AndroidManifest.xml中<meta>标签中的数据
    public static String getMetaValue(Context context, String metaKey)
    {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null)
        {
            return null;
        }
        try
        {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai)
            {
                metaData = ai.metaData;
            }
            if (null != metaData)
            {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e)
        {

        }
        return apiKey;
    }

    /**
     * 获得进程名称
     */
    public static String getProcessName(Context cxt, int pid)
    {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null)
        {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps)
        {
            if (procInfo.pid == pid)
            {
                return procInfo.processName;
            }
        }
        return null;
    }


    /**
     * 输入验证
     *
     * @param verifyType 1=密码，2=邮箱，3=电话号码，4=qq号 5:汉字姓名 6:15位身份证号 7:18位身份证号
     * @param inputStr   要验证的输入值
     * @return
     */
    public static boolean verifyInput(int verifyType, String inputStr)
    {
        String rexStr = "";
        switch (verifyType)
        {
            case 1:
                //以字母开头，长度在8~16之间，只能包含字母、数字
                rexStr = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
                break;
            case 2:
                //邮箱
                rexStr = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
                break;
            case 3:
                //rexStr="^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
                rexStr = "^1\\d{10}$";
                break;
            case 4:
                rexStr = "^\\d{5,15}$";
                break;
            case 5:
                rexStr = "[\\u4e00-\\u9fa5]+";
                break;
            case 6:
                rexStr = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
                break;
            case 7:
                rexStr = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|[Xx])$";
                break;
            default:
                break;
        }
        Pattern password = Pattern.compile(rexStr);
        Matcher m1 = password.matcher(inputStr);
        return m1.matches();
    }



    /**
     * 6.0以下的系统检测权限
     *
     * @return
     */
    public static boolean cameraIsCanUse()
    {
        boolean isCanUse = true;
        Camera mCamera = null;
        try
        {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (RuntimeException e)
        {
            Logger.t(TAG).d("相机catch" + e.getMessage());
            e.printStackTrace();
            isCanUse = false;
        }

        if (mCamera != null)
        {
            try
            {
                mCamera.release();
            } catch (Exception e)
            {
                Logger.t(TAG).d("相机catch" + e.getMessage());
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

    public static boolean cameraIsCanUse(int i)
    {
        boolean isCanUse = true;
        Camera mCamera = null;
        try
        {
            if (Camera.getNumberOfCameras() == 1)
                i = 0;
            mCamera = Camera.open(i);
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (RuntimeException e)
        {
            isCanUse = false;
        }
        return isCanUse;
    }


    /**
     * 获取是否存在NavigationBar(虚拟按键)
     *
     * @param context
     * @return
     */

    public static boolean checkDeviceHasNavigationBar(Context context)
    {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0)
        {
            hasNavigationBar = rs.getBoolean(id);
        }
        try
        {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride))
            {
                hasNavigationBar = false;
            }
            else if ("0".equals(navBarOverride))
            {
                hasNavigationBar = true;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            Logger.t(TAG).d("检查是否存在虚拟键catch：" + e.getMessage());
            return false;
        }
        return hasNavigationBar;
    }


    /**
     * 获取 虚拟按键的高度
     *
     * @param context
     * @return
     */
    public static int getBottomStatusHeight(Context context)
    {
        int totalHeight = getDpi(context);
        int contentHeight = getScreenHeight(context);
        return totalHeight - contentHeight;
    }

    //获取屏幕原始尺寸高度，包括虚拟功能键高度
    public static int getDpi(Context context)
    {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try
        {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 字符串MD5加密
     *
     * @param s
     * @return
     */
    public static String toMD5(String s)
    {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try
        {
            byte[] btInput = s.getBytes("utf-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++)
            {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private static long lastClickTime;

    /**
     * 是否重复点击
     *
     * @return true 是，false 否
     */
    public static boolean isFastDoubleClick()
    {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800)
        {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    private static Map<String, Boolean> map = new HashMap<>();

    /**
     * TAG 不能重复 在这定一下命名规则 tag=类名+编号 (编号自己定，在同一个类里不重复就好)
     *
     * @param tag
     * @return
     */
    public static boolean getLock(String tag)
    {
        boolean lastClickBoolean = false;
        if (map.containsKey(tag))
            lastClickBoolean = map.get(tag);
        else
            map.put(tag, lastClickBoolean);

        return lastClickBoolean;
    }

    /**
     * TAG 不能重复 在这定一下命名规则 tag=类名+编号 (编号自己定，在同一个类里不重复就好)
     *
     * @param tag
     */
    public static void clickLock(String tag)
    {
        boolean clickLock = true;
        map.put(tag, clickLock);
    }

    /**
     * TAG 不能重复 在这定一下命名规则 tag=类名+编号 (编号自己定，在同一个类里不重复就好)
     *
     * @param tag
     */
    public static void removeClickLock(String tag)
    {
        boolean clickLock = false;
        map.put(tag, clickLock);
    }

    /**
     * 作用：用户是否同意录音权限
     *
     * @return true 同意 false 拒绝
     */
    // TODO: 2018/1/5 起航暂时采用这个方法，等出现问题后改为正规的权限处理方式 --wb
    public synchronized static boolean isVoicePermission()
    {
        try
        {
            AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, 22050, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, AudioRecord.getMinBufferSize(22050, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT));
            record.startRecording();
            int recordingState = record.getRecordingState();
            if (recordingState == AudioRecord.RECORDSTATE_STOPPED)
            {
                record.release();
                return false;
            }
            record.release();
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符
     *
     * @param str
     * @return
     */
    public static String removeBlank(String str)
    {
        String dest = "";
        if (str != null)
        {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 通过对比得到与宽高比最接近的尺寸（如果有相同尺寸，优先选择）
     *
     * @param surfaceWidth  需要被进行对比的原宽
     * @param surfaceHeight 需要被进行对比的原高
     * @param preSizeList   需要对比的预览尺寸列表
     * @return 得到与原宽高比例最接近的尺寸
     */
    public static Camera.Size getCloselyPreSize(int surfaceWidth, int surfaceHeight,
                                                List<Camera.Size> preSizeList)
    {

        int ReqTmpWidth;
        int ReqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
        if (true)
        {
            ReqTmpWidth = surfaceHeight;
            ReqTmpHeight = surfaceWidth;
        }
        else
        {
            ReqTmpWidth = surfaceWidth;
            ReqTmpHeight = surfaceHeight;
        }
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for (Camera.Size size : preSizeList)
        {
            if ((size.width == ReqTmpWidth) && (size.height == ReqTmpHeight))
            {
                return size;
            }
        }

        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) ReqTmpWidth) / ReqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : preSizeList)
        {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin)
            {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }
        return retSize;
    }


    //region 设置沉浸式状态栏的代码

    /**
     * 设置状态栏颜色 * * @param activity 需要设置的activity * @param color 状态栏颜色值
     */

    public static void setColor(Activity activity, int color)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusView(activity, color);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    //** * 生成一个和状态栏大小相同的矩形条 * * @param activity 需要设置的activity * @param color 状态栏颜色值 * @return 状态栏矩形条 *//*
    private static View createStatusView(Activity activity, int color)
    {
        // 获得状态栏高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);

        // 绘制一个和状态栏一样高的矩形
        View statusView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(color);
        return statusView;
    }


    /**
     * 使状态栏透明 * <p> * 适用于图片作为背景的界面,此时需要图片填充到状态栏 * * @param activity 需要设置的activity
     *//*
    public static void setTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }*/
    //endregion


    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth(Context context)
    {
        if (context == null)
            context = AppController.getInstance();
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight1(Context context)
    {
        if (context == null)
            context = AppController.getInstance();
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static void setTransparentTopBar(Activity act)
    {
        act.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
/*        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = act.getWindow();
*//*            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);*//*


            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Window window = act.getWindow();
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    //设置成白色的背景，字体颜色为黑色。
    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode)
    {
        try
        {
            Class<? extends Window> clazz = activity.getWindow().getClass();
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置成白色的背景，字体颜色为黑色。
     *
     * @param activity
     * @param isSystemUi isSystemUi=true:字黑   isSystemUi=false:字白
     */
    public static void setStatusBarDarkMode(Activity activity, boolean isSystemUi)
    {
//        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi"))
        setMiuiStatusBarDarkMode(activity, isSystemUi);
        //设置成白色的背景，字体颜色为黑色。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (isSystemUi)
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            else
            {
                activity.getWindow().getDecorView().setSystemUiVisibility(0);
            }
        }
    }

    public static int getStatusBarHeight(Activity act)
    {
        Rect rectangle = new Rect();
        Window window = act.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        int contentViewTop =
                window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentViewTop - statusBarHeight;
        Logger.t(TAG).d(String.format("statusBarHeight :%s  contentViewTop :%s titleBarHeight: %s", statusBarHeight, contentViewTop, titleBarHeight));
        return titleBarHeight;
    }


    public static String getHostName(String urlString)
    {
        String head = "";
        int index = urlString.indexOf("://");
        if (index != -1)
        {
            head = urlString.substring(0, index + 3);
            urlString = urlString.substring(index + 3);
        }
        index = urlString.indexOf("/");
        if (index != -1)
        {
            urlString = urlString.substring(0, index + 1);
        }
        return head + urlString;
    }









    /**
     * 获取图片MimeType
     *
     * @param filePath
     * @return
     */
    public static String getImageMimeType(String filePath)
    {
        String mime = "jpg";
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        String type = options.outMimeType;
        mime = type.substring(6, type.length());
        return mime;
    }


    /**
     * 获取安装 App(支持 8.0)的意图
     * <p>8.0 需添加权限
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param file      文件
     * @param authority 7.0 及以上安装需要传入清单文件中的{@code <provider>}的 authorities 属性
     *                  <br>参看 https://developer.android.com/reference/android/support/v4/content/FileProvider.html
     * @param isNewTask 是否开启新的任务栈
     * @return 安装 App(支持 8.0)的意图
     */
    public static Intent getInstallAppIntent(final File file,
                                             final String authority,
                                             final boolean isNewTask)
    {
        if (file == null) return null;
        Intent intent = new Intent(Intent.ACTION_VIEW);//I3CKBE  LHUW8P
        Uri data;
        String type = "application/vnd.android.package-archive";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
        {
            data = Uri.fromFile(file);
        }
        else
        {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            data = FileProvider.getUriForFile(AppController.getInstance(), authority, file);
        }
        intent.setDataAndType(data, type);
        return getIntent(intent, isNewTask);
    }

    private static Intent getIntent(final Intent intent, final boolean isNewTask)
    {
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }


}