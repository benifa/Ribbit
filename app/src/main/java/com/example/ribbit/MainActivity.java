package com.example.ribbit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

    public static final int LOAD_VIDEO = 3;
    public static final int LOAD_PICTURE = 2;
    public static final int TAKE_PICTURE = 0;
    public static final int TAKE_VIDEO = 1;
    public static final int VIDEO_SIZE_LIMIT = 1024 * 1024 * 10;
    public static final String KEY_PICTURE_TYPE = "picture";
    public static final String KEY_VIDEO_TYPE = "video";

    /**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

    protected Uri mediaUri;
    protected DialogInterface.OnClickListener dialogClikListner= new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (which){

                case TAKE_PICTURE:{

                    if(isExternalStorageAvailable()) {
                        Intent takaPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        mediaUri = getOutputMedaUri(4);
                        takaPhoto.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
                        startActivityForResult(takaPhoto, TAKE_PICTURE);
                    }

                    else

                        Toast.makeText(MainActivity.this,R.string.error_external_storage,Toast.LENGTH_LONG).show();

                    break;
                }

                case TAKE_VIDEO:{

                    if(isExternalStorageAvailable()) {

                        Intent takeVideo = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
                        takeVideo.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
                        takeVideo.putExtra(MediaStore.EXTRA_DURATION_LIMIT,10);
                        takeVideo.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                        mediaUri = getOutputMedaUri(5);
                        startActivityForResult(takeVideo, TAKE_VIDEO);
                        }

                        else
                            Toast.makeText(MainActivity.this,R.string.error_external_storage,Toast.LENGTH_LONG).show();

                    break;

                }


                case 2: {

                    Intent choosePicture= new Intent(Intent.ACTION_GET_CONTENT);
                    choosePicture.setType("image/*");
                    startActivityForResult(choosePicture,2);
                    break;

                }

                case LOAD_VIDEO:{

                    Intent chooseVideo= new Intent(Intent.ACTION_GET_CONTENT);
                    chooseVideo.setType("video/*");
                    Toast.makeText(MainActivity.this, getString(R.string.video_size_warning),Toast.LENGTH_LONG).show();
                    startActivityForResult(chooseVideo, LOAD_VIDEO);
                    break;


                }
            }

        }

        private Uri getOutputMedaUri(int i) {

            if(isExternalStorageAvailable()){
                File mediStorageDir=  new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        ,MainActivity.this.getString(R.string.app_name));

                if(!mediStorageDir.exists())
                    if(!mediStorageDir.mkdirs()){
                        Toast.makeText(MainActivity.this,R.string.error_external_storage,Toast.LENGTH_SHORT).show();
                        return null;

                    }
                File mediaFile;
                Date today= new Date();
                String timeStamp= new SimpleDateFormat("ddMMyyyy",Locale.US).format(today);
                String filePath= mediStorageDir.getPath()+File.separator;

                if(i==4){
                    mediaFile= new File(filePath+"IMG_"+timeStamp+".PNG");
                }
                else if (i==5){
                    mediaFile= new File(filePath+"VIDEO_"+timeStamp+".mp4");

                }
                else
                    return null;


                return Uri.fromFile(mediaFile);

            }


            else
                return null;
        }

        private boolean isExternalStorageAvailable(){

            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK ){

            if(requestCode== LOAD_PICTURE || requestCode== LOAD_VIDEO){
                     //Todo change request code into static constants
                if(data==null){
                    Toast.makeText(this,"sorry, there is an access error!",Toast.LENGTH_LONG).show();
                    //Todo make string messages for the toast
                }
                else{

                    mediaUri=data.getData();

                }

                if(requestCode== LOAD_VIDEO){

                    int fileSize=0;
                    InputStream mediaStream=null;

                    try {
                        mediaStream= getContentResolver().openInputStream(mediaUri);
                        fileSize= mediaStream.available();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,R.string.error_external_storage,Toast.LENGTH_LONG).show();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,R.string.error_external_storage,Toast.LENGTH_LONG).show();
                        return;
                    }
                    finally {
                        try {
                            mediaStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this,R.string.error_external_storage,Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    if(fileSize>= VIDEO_SIZE_LIMIT){

                        Toast.makeText(MainActivity.this,getString(R.string.error_out_limit_video),Toast.LENGTH_LONG).show();
                        return;
                    }

                }
            }
            else{

                Intent mediaAvailable= new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaAvailable.setData(mediaUri);
                sendBroadcast(mediaAvailable);


            }

            Intent startRecipient= new Intent(this,RecipientsActivity.class);
            startRecipient.setData(mediaUri);

            String fileType;
            if(requestCode==LOAD_PICTURE||requestCode==TAKE_VIDEO){

                fileType= KEY_PICTURE_TYPE;


            }

            else{
                fileType= KEY_VIDEO_TYPE;


            }
            startRecipient.putExtra(ParseConstans.KEY_FILE_TYPE,fileType);

            startActivity(startRecipient);

        }
        else if(requestCode!=RESULT_CANCELED){

            //Todo

        }
        else{

            Toast.makeText(this,"there was an error!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);
		ParseUser currentUser = ParseUser.getCurrentUser();
		
		if(currentUser==null){
			
			goToLoging();
			
		}
		
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(this,
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = TAKE_PICTURE; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}
	private void goToLoging() {
		Intent bIntent= new Intent(this,LoginActivity.class);
		bIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		bIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(bIntent);
	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub

        switch(item.getItemId()) {
            case R.id.action_logout:
            {
                ParseUser.logOut();
                goToLoging();
                break;
            }

            case R.id.edit_friend_menu:
            {
                Intent bIntent = new Intent(this, EditFriendsActivity.class);
                bIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                bIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(bIntent);
                break;
            }

            case R.id.action_camera:
            {
                AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this)
                        .setItems(R.array.camera_choices,dialogClikListner);
                Dialog dialog= builder.create();
                dialog.show();
                break;
            }

        }
    	return super.onOptionsItemSelected(item);
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}


}
