package com.example.handoutlms.handoutmessager;

import com.example.handoutlms.ChatNotification.MyResponse;
import com.example.handoutlms.ChatNotification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAMHDLUWU:APA91bGKOHb8Zwc2b0prRX-8alcWNwb4skKeZdzpooR4mx7wbSb2H6QzFAw-sAjYlbRlamyOccohD6GtfO2EsbHwk9sSUpCSmvr9IVijD6VLCJTm1hJT3hTctoLVBuOlfVsj8qFi4Cm3"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
