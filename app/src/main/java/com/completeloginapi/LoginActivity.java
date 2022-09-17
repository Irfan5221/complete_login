package com.completeloginapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.completeloginapi.Utils.ApiClients;
import com.completeloginapi.Utils.ApiInterface;
import com.completeloginapi.Utils.SavedPrefManager;
import com.completeloginapi.model.OtpResponse;
import com.completeloginapi.model.UserLogin;
import com.completeloginapi.model.VerifyOtp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView register_new_user;
    EditText login_phone_number,verify_otp;
    Button verify_button,get_otp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags( WindowManager.LayoutParams.ALPHA_CHANGED);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor( R.color.white));
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView( R.layout.activity_login );

        register_new_user=findViewById( R.id.register_new_user );
        login_phone_number=findViewById( R.id.login_phone_number );
        verify_otp=findViewById( R.id.verify_otp );
        verify_button=findViewById( R.id.verify_button );
        get_otp=findViewById( R.id.get_otp );
        progressDialog=new ProgressDialog( LoginActivity.this );
        progressDialog.setTitle( "please wait" );

        register_new_user.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(getApplicationContext(),RegisterActivity.class) );
                finish();
            }
        } );

        get_otp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.dismiss();
                if (login_phone_number.getText().equals( "" )){
                    Toast.makeText( getApplicationContext(), "Enter your login number", Toast.LENGTH_SHORT ).show();
                }else if (login_phone_number.getText().length()<10){
                    Toast.makeText( getApplicationContext(), "Enter valid phone number", Toast.LENGTH_SHORT ).show();
                }else {
                    get_otpmethod(login_phone_number.getText().toString());
                }

            }
        } );

        verify_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify_otpmethod(login_phone_number.getText().toString(),verify_otp.getText().toString());
            }
        } );
    }

    private void verify_otpmethod(String login_phone_number,String otp ) {
        ApiInterface apiInterface=ApiClients.getClient().create( ApiInterface.class );
        Call<VerifyOtp>verifyOtpCall=apiInterface.VERIFY_OTP_CALL( login_phone_number,otp );
        verifyOtpCall.enqueue( new Callback<VerifyOtp>() {
            @Override
            public void onResponse(Call<VerifyOtp> call, Response<VerifyOtp> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    if (response.body().getSuccess()){
                        loginSuccessMethod( login_phone_number );

                    }else {
                        Toast.makeText( getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<VerifyOtp> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText( getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT ).show();

            }
        } );
    }

    private void loginSuccessMethod(String phonenumber) {
        ApiInterface apiInterface=ApiClients.getClient().create( ApiInterface.class );
        Call<UserLogin> userLoginCall=apiInterface.USER_LOGIN_CALL( phonenumber );
        userLoginCall.enqueue( new Callback<UserLogin>() {
            @Override
            public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    if (response.body().getSuccess()){

                        SavedPrefManager.savePreferenceBoolean( getApplicationContext(),"islogin", true );
                        SavedPrefManager.saveStringPreferences( getApplicationContext(),"name",response.body().getData().getName() );
                        SavedPrefManager.saveStringPreferences( getApplicationContext(),"dob",response.body().getData().getDateofbirth() );
                        SavedPrefManager.saveStringPreferences( getApplicationContext(),"phonenumber",response.body().getData().getMobile() );
                        SavedPrefManager.saveStringPreferences( getApplicationContext(),"gender",response.body().getData().getGender() );
                        SavedPrefManager.saveStringPreferences( getApplicationContext(),"city",response.body().getData().getCity() );
                        SavedPrefManager.saveStringPreferences( getApplicationContext(),"state",response.body().getData().getState() );
                        SavedPrefManager.saveStringPreferences( getApplicationContext(),"blood_group",response.body().getData().getBloodgroup() );
                        SavedPrefManager.saveStringPreferences( getApplicationContext(),"user_type",response.body().getData().getUsertype() );
                        SavedPrefManager.saveStringPreferences( getApplicationContext(),"user_image",response.body().getData().getUserimage() );
                        startActivity( new Intent(getApplicationContext(),MainActivity.class) );
                        finish();

                    }else {
                        Toast.makeText( getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserLogin> call, Throwable t) {
                Toast.makeText( getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    private void get_otpmethod(String Phonenumber) {
        ApiInterface apiInterface= ApiClients.getClient().create( ApiInterface.class );
        Call<OtpResponse>call=apiInterface.GetOtp( Phonenumber );
        call.enqueue( new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    if (response.body().getSuccess()){
                        Toast.makeText(getApplicationContext() ,response.body().getOtp() , Toast.LENGTH_SHORT ).show();
                    }else {
                        Toast.makeText( getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                Toast.makeText( getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );

    }


}