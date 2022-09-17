package com.completeloginapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.completeloginapi.Utils.ApiConstansts;
import com.completeloginapi.Utils.SavedPrefManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    TextView fullname,dateofbirth,phonenumber,gender,city,state,bloodgroup,usertype;
    CircleImageView profile_img;
    LinearLayout logout;

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
        setContentView( R.layout.activity_main );

        fullname=findViewById( R.id.fullname );
        dateofbirth=findViewById( R.id.dateofbirth );
        phonenumber=findViewById( R.id.phonenumber );
        gender=findViewById( R.id.gender );
        city=findViewById( R.id.city );
        state=findViewById( R.id.state );
        bloodgroup=findViewById( R.id.bloodgroup );
        usertype=findViewById( R.id.usertype );
        profile_img=findViewById( R.id.profile_img);



        Glide.with( getApplicationContext() ).
                load( ApiConstansts.BASE_IMG_URL +SavedPrefManager.getStringPreferences( getApplicationContext(),"user_image" ) )
                .into( profile_img );
        fullname.setText( SavedPrefManager.getStringPreferences( getApplicationContext(),"name"));
        dateofbirth.setText( SavedPrefManager.getStringPreferences( getApplicationContext(),"dob"));
        phonenumber.setText( SavedPrefManager.getStringPreferences( getApplicationContext(),"phonenumber"));
        gender.setText( SavedPrefManager.getStringPreferences( getApplicationContext(),"gender"));
        city.setText( SavedPrefManager.getStringPreferences( getApplicationContext(),"city"));
        state.setText( SavedPrefManager.getStringPreferences( getApplicationContext(),"state"));
        bloodgroup.setText( SavedPrefManager.getStringPreferences( getApplicationContext(),"blood_group"));
        usertype.setText( SavedPrefManager.getStringPreferences( getApplicationContext(),"user_type"));
    }


    public void user_logout(View view) {

        Toast.makeText( getApplicationContext(), "clicked", Toast.LENGTH_SHORT ).show();
    }
}