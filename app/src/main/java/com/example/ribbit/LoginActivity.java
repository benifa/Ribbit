package com.example.ribbit;

import org.w3c.dom.Text;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
    protected TextView bSignupTextView;
	protected EditText bUsername;
	protected EditText bPassword;
	protected Button bButton;
	    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_login);
		bSignupTextView= (TextView) findViewById(R.id.signup_text);
		
		bUsername= (EditText) findViewById(R.id.usernameField);
		bPassword=(EditText) findViewById(R.id.passwordField);

        SharedPreferences settings = getSharedPreferences("PREFS",0);
        bUsername.setText(settings.getString("username", ""));
		bButton=(Button) findViewById(R.id.login);
		bSignupTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent bIntent= new Intent(LoginActivity.this,SignUpActivity.class);
				
				startActivity(bIntent);
				
			}
		});
		
		bButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String username= bUsername.getText().toString().trim();
				String password= bPassword.getText().toString().trim();
				
				if(username.isEmpty() || password.isEmpty()){
					
					AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this)
					.setTitle(R.string.login_error_title)
					.setMessage(R.string.login_error_message)
					.setPositiveButton(android.R.string.ok, null);
					Dialog dialog= builder.create();
					dialog.show();
				}
				
				else{
					
					ParseUser user = new ParseUser();
					user.setUsername(username);
					user.setPassword(password);
					setProgressBarIndeterminateVisibility(true);
					ParseUser.logInInBackground(username, password, new LogInCallback() {
						
						@Override
						public void done(ParseUser user, ParseException e) {
							// TODO Auto-generated method stub
							setProgressBarIndeterminateVisibility(false);
							if (e == null) {
								
								Intent bIntent= new Intent(LoginActivity.this,MainActivity.class);
								bIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								bIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(bIntent);
							      
							    } else {
							    	
							    	AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this)
									.setTitle(R.string.login_error_title)
									.setMessage(e.getMessage())
									.setPositiveButton(android.R.string.ok, null);
									Dialog dialog= builder.create();
									dialog.show();
							      
							    }
						}	
					}); 
					
					
				}
				
			}
		});
		
	}

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences settings = getSharedPreferences("PREFS",0);
        SharedPreferences.Editor editor= settings.edit();
        editor.putString("username",bUsername.getText().toString());
        editor.commit();

    }

    @Override
    protected void onPause() {
        super.onPause();

//        SharedPreferences settings = getSharedPreferences("PREFS",0);
//        SharedPreferences.Editor editor= settings.edit();
//        editor.putString("username",bUsername.getText().toString());
//        editor.commit();
    }
}
