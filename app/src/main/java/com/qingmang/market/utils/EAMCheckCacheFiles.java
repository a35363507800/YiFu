package com.qingmang.market.utils;

import android.util.Log;


import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by liuyang on 2017/8/24.
 */

public class EAMCheckCacheFiles
{

    private static final String TAG = EAMCheckCacheFiles.class.getSimpleName();
    private static EAMCheckCacheFiles instance;

    public static String H5_PACKAGE_PATH = "";
    public static String H5_PACKAGE_VERSION = "";

    /**
     * filepath + h5cache
     * ├─── version.json
     * ├─── package.zip
     * ├─── (other .tmp files)
     * └─── files
     * └─── (h5 files)
     *
     * @param url
     * @param filepath
     */
    public static void synchronize(final String url, final String filepath)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                if (instance == null)
                    instance = new EAMCheckCacheFiles();

                String strDirPath = filepath + File.separator + "h5cache";
                File fileDir = new File(strDirPath);
                File fileUnzipDir = new File(strDirPath + File.separator + "package");
                //Log.i("cachecache",strDirPath);
                Logger.t(TAG).d("fileDir.exists():" + fileDir.exists() + " | fileUnzipDir.exists():" + fileUnzipDir.exists());
                if (!fileDir.exists() || !fileUnzipDir.exists())
                {

                    boolean d = fileDir.mkdir();
                    instance.downloadH5Package(url, strDirPath);
                    return;
                }


                // check version code if same;
                try
                {
                    URL jsonVersionUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) jsonVersionUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream stream = conn.getInputStream();
                    File fileName = new File(strDirPath + File.separator + "new_version.json");
                    // 建立文件
                    File file = new File(fileName.toString() + ".tmp");
                    if (!file.exists())
                    {
                        if (!file.getParentFile().exists())
                        {
                            file.getParentFile().mkdirs();
                        }
                        file.createNewFile();
                    }
                    FileOutputStream fs = new FileOutputStream(file);
                    byte[] buffer = new byte[2048];
                    int size = 0;
                    while ((size = stream.read(buffer)) != -1)
                    {
                        fs.write(buffer, 0, size);
                    }
                    fs.close();
                    file.renameTo(fileName);
                    stream.close();
                    conn.disconnect();

                    String strjson = instance.charStream2String(new FileReader(fileName));
                    JSONObject jsonObj = new JSONObject(strjson);
                    String newVersion = jsonObj.optString("version", "");

                    String strjsonOld = instance.charStream2String(new FileReader(new File(strDirPath + File.separator + "version.json")));
                    JSONObject jsonObjOld = new JSONObject(strjsonOld);
                    String oldVersion = jsonObjOld.optString("version", "");

                    //Log.i("cachecache", String.format("oldVer:%s |newVer:%s ",oldVersion,newVersion));

                    if (newVersion.equals(oldVersion))
                    {
                        EAMCheckCacheFiles.H5_PACKAGE_VERSION = oldVersion;
                        EAMCheckCacheFiles.H5_PACKAGE_PATH = strDirPath + File.separator + "package";
                    }
                    else
                    {
                        instance.downloadH5Package(url, strDirPath);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
//                instance.logFileDir(strDirPath);


                if (null != instance.cb)
                {
                    instance.cb.finishCheckFile();
                }
                instance = null;
            }
        }).start();
    }


    /**
     * 1. replace url with NetHelper.H5_ADDRESS
     * 2. if cache exists return "file:///path"
     * 3. nor return arg url;
     *
     * @param url
     * @return url or file path
     */
    public static String tryH5CacheFile(String url)
    {
        String resUrl = "";
        String filename = "";
      /// String filename = url.replace(Api.H5_ADDRESS, "");
        Logger.t(TAG).d("路径：" + H5_PACKAGE_PATH + filename);
        File file = new File(H5_PACKAGE_PATH + filename);
        if (file.exists() && !"".equals(H5_PACKAGE_PATH))
        {
            resUrl = "file://" + H5_PACKAGE_PATH + filename;
        }
        else
        {
            resUrl = url;
        }
        Logger.t("EAMCheckCacheFiles").d("H5 will use link: " + resUrl);
        return resUrl;
    }

    /**
     * static Path, Code, 此方法生成;
     * 会覆盖路径下文件，完全刷新
     *
     * @param url
     * @param strDirPath
     */
    private void downloadH5Package(String url, String strDirPath)
    {
        try
        {
            // pull json and save it
            URL jsonVersionUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) jsonVersionUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream stream = conn.getInputStream();
            File fileName = new File(strDirPath + File.separator + "version.json");
            // 建立文件
            File file = new File(fileName.toString() + ".tmp");
            if (file.exists())
            {
                file.delete();
            }
            if (!file.getParentFile().exists())
            {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();


            FileOutputStream fs = new FileOutputStream(file);
            byte[] buffer = new byte[2048];
            int size = 0;
            while ((size = stream.read(buffer)) != -1)
            {
                fs.write(buffer, 0, size);
            }
            fs.close();
            file.renameTo(fileName);
            stream.close();
            conn.disconnect();

//            String strjson = instance.charStream2String(new FileReader(fileName)).replace(" ","");
            String strjson = instance.charStream2String(new FileReader(fileName));
            //Log.i("cachecache",strjson);

            JSONObject jsonObj = new JSONObject(strjson);
            String download_url = jsonObj.optString("download", "");
            EAMCheckCacheFiles.H5_PACKAGE_VERSION = jsonObj.optString("version", "");
            //Log.i("cachecache",download_url + " | " + EAMCheckCacheFiles.H5_PACKAGE_VERSION);


            // pull package of download url
            URL newVersionUrl = new URL(download_url);
            HttpURLConnection connPackage = (HttpURLConnection) newVersionUrl.openConnection();
            connPackage.setDoInput(true);
            connPackage.connect();
            InputStream streamPackage = connPackage.getInputStream();
            File fileNamePackage = new File(strDirPath + File.separator + "package.zip");
            File filePackage = new File(fileNamePackage.toString() + ".tmp");
            if (!filePackage.exists())
            {
                if (!filePackage.getParentFile().exists())
                {
                    filePackage.getParentFile().mkdirs();
                }
                filePackage.createNewFile();
            }
            FileOutputStream fsPackage = new FileOutputStream(filePackage);
            byte[] bufferPackage = new byte[2048];
            int sizePackage = 0;
            while ((sizePackage = streamPackage.read(bufferPackage)) != -1)
            {
                fsPackage.write(bufferPackage, 0, sizePackage);
            }
            fsPackage.close();
            filePackage.renameTo(fileNamePackage);
            streamPackage.close();
            connPackage.disconnect();

            EAMCheckCacheFiles.H5_PACKAGE_PATH = strDirPath + File.separator + "package";
            instance.unZip(EAMCheckCacheFiles.H5_PACKAGE_PATH, fileNamePackage);

        } catch (Exception e)
        {
            e.printStackTrace();
            if (null != cb)
            {
                cb.downloadResult(EAMCheckCacheFiles.h5CacheDownlaodDefeat);
            }
            return;
        }
    }


    public static final int h5CacheExist = 1;
    public static final int h5CacheDownlaodSuccess = 2;
    public static final int h5CacheDownlaodDefeat = 3;

    private IH5CacheCallback cb = null;

    public void setCallback(IH5CacheCallback cb)
    {
        this.cb = cb;
    }

    interface IH5CacheCallback
    {
        void downloadResult(int errCode);

        void finishCheckFile();
    }


    private String charStream2String(Reader reader)
    {
        BufferedReader r = new BufferedReader(reader);
        StringBuilder b = new StringBuilder();
        String line;
        try
        {
            while ((line = r.readLine()) != null)
            {
                b.append(line);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return b.toString();
    }

    /**
     * 会递归删除 目录下文件
     *
     * @param targetDir
     * @param zipFile
     * @throws Exception
     */
    private void unZip(String targetDir, File zipFile) throws Exception
    {
        File targetDirectory = new File(targetDir);
        if (!targetDirectory.exists())
        {
            targetDirectory.mkdirs();
        }
        else
        {
            instance.deleteDir(targetDirectory);
        }

        try
        {
            ZipInputStream zis = new ZipInputStream(
                    new BufferedInputStream(
                            new FileInputStream(zipFile)));

            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null)
            {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                {
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                }

                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try
                {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally
                {
                    fout.close();
                }
            }


            zis.close();
        } catch (Exception exc)
        {
            throw exc;

        }

    }


    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    private boolean deleteDir(File dir)
    {
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++)
            {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success)
                {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }


    private void logFileDir(String filePath)
    {
        File f = new File(filePath);
        File[] files = f.listFiles();

        for (int i = 0; i < files.length; i++)
        {
            File file = files[i];
            Log.i("cachecache", "dir&file: " + file.getPath());
            if (file.isDirectory())
            {
                logFileDir(file.getPath());
            }
        }
    }
}
