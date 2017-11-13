package com.example.jianqiang.hybridapp.http;

import android.os.Environment;

import com.example.jianqiang.hybridapp.HttpCallback;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by struggle_liping on 2017/11/13.
 */

public class HttpUtils {

    /**
     *
     * @param urlPath url
     * @param filePath 文件全路径
     * @param callback 回调
     * @throws Exception
     */
    public static void downloadFile(String urlPath, String filePath, HttpCallback callback) throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(15000);
            // 获取到文件的大小
            int maxLength =conn.getContentLength();
            InputStream is = conn.getInputStream();
            File file = new File(filePath);
            // 目录不存在创建目录
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int progress = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                progress += len;
                // 获取当前下载量
                callback.progress(progress,maxLength,filePath);
            }

            fos.close();
            bis.close();
            is.close();
        } else {
            throw new IOException("未发现有SD卡");
        }
    }
}
