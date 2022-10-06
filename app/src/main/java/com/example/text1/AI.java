package com.example.text1;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AI {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final MediaType JPEG = MediaType.get("image/jpeg");
    //    private static final String URL = "http://192.168.43.201:8000/hand/hand_recognization";
    private static final String URL = "http://192.168.196.1:8000/hand/hand_recognization";
    private static final String HOME = "home";
    private static final String WORK = "work";
    private static final String STOP = "stop";
    private String response_raw = "-1"; //初始化为-1;
    private String gesture_name; //current gesture name
    private File file;

    public AI(String gesture_name, File file) {
        this.file = file;
        this.gesture_name = gesture_name;
    }

    public boolean request_server() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(160, TimeUnit.SECONDS)
                .callTimeout(160, TimeUnit.SECONDS)
                .pingInterval(160, TimeUnit.SECONDS)
                .readTimeout(160, TimeUnit.SECONDS)
                .writeTimeout(160, TimeUnit.SECONDS)
                .build();
//        final OkHttpClient client = new OkHttpClient().readTimeoutMillis(20, TimeUnit.SECONDS).build();
        RequestBody body = RequestBody.create(file, JPEG);


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                // 此处可添加上传 参数
                // photoFile 表示上传参数名,logo.png 表示图片名字\
                .addFormDataPart("name", gesture_name)
                .addFormDataPart("photo",
                        file.getName(),
                        RequestBody.create(JPEG, file)

                )//文件
                .build();


        Request request = new Request.Builder()
                .url(URL)
                .post(requestBody)

                .build();

        try (Response response = client.newCall(request).execute()) {
            response_raw = response.body().string();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
//            Response response = okHttpClient.newCall(request).execute();


    }

    public boolean get_result() {
        System.out.println("服务器的回应" + response_raw);
        if (response_raw == "-1") {
            System.out.println("网络错误或服务器出错");
            return false;
        } else if (response_raw.contains(HOME) || response_raw.contains(WORK) || response_raw.contains(STOP)) {
            System.out.println("服务器起码返回了一个结果");
            System.out.println("服务器说：" + response_raw);
            return true;
        } else {
            return false;
        }
    }
}



