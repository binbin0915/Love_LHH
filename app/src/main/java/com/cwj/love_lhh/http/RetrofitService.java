package com.cwj.love_lhh.http;

import com.cwj.love_lhh.http.cookie.CookiesManager;
import com.cwj.love_lhh.http.gson.BaseConverterFactory;
import com.yechaoa.yutils.YUtils;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Description : RetrofitService
 *
 * @author XuCanyou666
 * @date 2020/2/8
 */


public class RetrofitService {

    private volatile static RetrofitService apiRetrofit;
    private API.WAZApi apiServer;

    /**
     * 单例调用
     *
     * @return RetrofitService
     */
    public static RetrofitService getInstance() {
        if (apiRetrofit == null) {
            synchronized (Object.class) {
                if (apiRetrofit == null) {
                    apiRetrofit = new RetrofitService();
                }
            }
        }
        return apiRetrofit;
    }


    /**
     * 获取api对象
     *
     * @return api对象
     */
    public API.WAZApi getApiService() {
        return apiServer;
    }


    /**
     * 初始化retrofit
     */
    private RetrofitService() {

        //配置okHttp并设置时间、日志信息和cookies
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                //设置超时时间
                .connectTimeout(15, TimeUnit.SECONDS)
                //设置Cookie持久化
//                .cookieJar(new CookiesManager(YUtils.getApplication()))
                .build();

        //关联okHttp并加上rxJava和Gson的配置和baseUrl
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(BaseConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(API.BASE_URL)
                .build();

        apiServer = retrofit.create(API.WAZApi.class);
    }

}
