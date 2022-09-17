package com.completeloginapi.Utils;

import com.completeloginapi.model.OtpResponse;
import com.completeloginapi.model.RegisterUser;
import com.completeloginapi.model.UserLogin;
import com.completeloginapi.model.VerifyOtp;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @Headers("Authkey:OPEWIRODFMCDSAB")
    @POST("getOtp")
    @FormUrlEncoded
    Call<OtpResponse> GetOtp(@Field("phonenumber") String packageName);

    @Headers("Authkey:OPEWIRODFMCDSAB")
    @POST("verifyotp")
    @FormUrlEncoded
        //in field pass only parameter
    Call<VerifyOtp> VERIFY_OTP_CALL(@Field("phonenumber") String phonenumber,
                                    @Field( "otp" ) String otp);

    @Headers("Authkey:OPEWIRODFMCDSAB")
    @POST("loginuser")
    @FormUrlEncoded
    Call<UserLogin> USER_LOGIN_CALL(@Field("phonenumber") String login);

    @Multipart
    @Headers("Authkey:OPEWIRODFMCDSAB")
    @POST("registeruser")
    Call<RegisterUser> REGISTER_USER_CALL(
                                          @Part( "name" )RequestBody name,
                                          @Part( "dateofbirth" )RequestBody dateofbirth,
                                          @Part("phonenumber") RequestBody phonenumber,
                                          @Part( "gender" )RequestBody gender,
                                          @Part( "city" )RequestBody city,
                                          @Part( "state" )RequestBody state,
                                          @Part( "bloodgroup" )RequestBody bloodgroup,
                                          @Part( "usertype" )RequestBody usertype,
                                          @Part MultipartBody.Part image);
}
