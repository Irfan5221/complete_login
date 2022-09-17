package com.completeloginapi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.completeloginapi.Utils.ApiClients;
import com.completeloginapi.Utils.ApiInterface;
import com.completeloginapi.Utils.SavedPrefManager;
import com.completeloginapi.model.RegisterUser;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    ImageView upload_register_image;
    TextView fullname,dob,mobilenumber,gender,city,state,blood_group,user_type,alreday_have_account;
    String image_path="";
    ProgressDialog progressDialog;
    private static final String TAG = ".RegisterActivity";

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

        setContentView( R.layout.activity_register );

        if (SavedPrefManager.getBooleanPreferences( getApplicationContext(),"islogin" )==true){
            startActivity( new Intent(getApplicationContext(),MainActivity.class) );
            finish();
        }else {
            startActivity( new Intent(getApplicationContext(),LoginActivity.class) );
            finish();
        }

        fullname=findViewById( R.id.register_user_fullname );
        dob=findViewById( R.id.register_user_dob );
        mobilenumber=findViewById( R.id.register_user_mobile );
        gender=findViewById( R.id.register_user_gender );
        city=findViewById( R.id.register_user_city );
        state=findViewById( R.id.register_user_state );
        blood_group=findViewById( R.id.register_user_bloodgroup );
        user_type=findViewById( R.id.register_user_usertype );
        alreday_have_account=findViewById( R.id.already_have_account );
        upload_register_image=findViewById( R.id.upload_register_image );
        progressDialog=new ProgressDialog( RegisterActivity.this );
        progressDialog.setTitle( "Please wait" );

        upload_register_image.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(RegisterActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        } );

        alreday_have_account.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(getApplicationContext(),LoginActivity.class) );
                finish();
            }
        } );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (resultCode==RESULT_OK){

            Uri uri=data.getData();
            image_path=uri.getPath();
            upload_register_image.setImageURI( uri );

        }
    }

    public void RegisterUser( View view){
        if (image_path.equals( "" )){
            Toast.makeText( getApplicationContext(), "select profile image", Toast.LENGTH_SHORT ).show();
        }else {
            registerUser(fullname.getText().toString(),dob.getText().toString(),mobilenumber.getText().toString(),
                         gender.getText().toString(),city.getText().toString(),state.getText().toString(),blood_group.getText().toString(),
                         user_type.getText().toString(),image_path);
        }
    }

    private void registerUser(String fullname,String dob,String mobilenumber,String gender,String city,String state,String blood_group,
                              String user_type,String image_path) {
        progressDialog.show();
        File file=new File( image_path );
        RequestBody requestBody=RequestBody.create( MediaType.parse( "image/jpeg" ),file );
        MultipartBody.Part body=MultipartBody.Part.createFormData( "userimage","image.jpeg",requestBody );

        RequestBody fullname1=RequestBody.create( MediaType.parse( "text/plain" ),fullname );
        RequestBody dob1=RequestBody.create( MediaType.parse( "text/plain" ),dob );
        RequestBody mobilenumber1=RequestBody.create( MediaType.parse( "text/plain" ),mobilenumber );
        RequestBody gender1=RequestBody.create( MediaType.parse( "text/plain" ),gender );
        RequestBody city1=RequestBody.create( MediaType.parse( "text/plain" ),city );
        RequestBody state1=RequestBody.create( MediaType.parse( "text/plain" ),state );
        RequestBody blood_group1=RequestBody.create( MediaType.parse( "text/plain" ),blood_group );
        RequestBody user_type1=RequestBody.create( MediaType.parse( "text/plain" ),user_type );

        ApiInterface apiInterface= ApiClients.getClient().create( ApiInterface.class );
        Call<RegisterUser> call=apiInterface.REGISTER_USER_CALL( fullname1,dob1,mobilenumber1,gender1,city1,state1,
                            blood_group1,user_type1,body);

        call.enqueue( new Callback<RegisterUser>() {
            @Override
            public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    if (response.body().getSuccess()){
                        Toast.makeText( getApplicationContext(), "Registration Success", Toast.LENGTH_SHORT ).show();
                        startActivity( new Intent(getApplicationContext(),MainActivity.class) );
                        finish();
                    }else {
                        Toast.makeText( getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterUser> call, Throwable t) {
                Log.d( TAG, "onFailure: "+t.getMessage() );
                progressDialog.dismiss();

            }
        } );

    }
}