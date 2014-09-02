package com.example.ribbit;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.net.ParseException;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.text.method.DialerKeyListener;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends Activity {
	
	protected EditText bUsername;
	protected EditText bPassword;
	protected EditText bEmail;
    protected Button bButton;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_sign_up);
		
		bUsername= (EditText) findViewById(R.id.username_signup_field);
		bPassword=(EditText) findViewById(R.id.password_sign_up_field);
		bEmail=(EditText) findViewById(R.id.email_signup_field);
		bButton=(Button) findViewById(R.id.signup_button);
		
		bButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String username= bUsername.getText().toString().trim();
				String password= bPassword.getText().toString().trim();
				String email= bEmail.getText().toString().trim();
				
				if(username.isEmpty() ||password.isEmpty() || email.isEmpty()){
					
					AlertDialog.Builder builder= new AlertDialog.Builder(SignUpActivity.this)
					.setTitle(R.string.signup_error_title)
					.setMessage(R.string.sign_up_error_message)
					.setPositiveButton(android.R.string.ok, null);
					Dialog dialog= builder.create();
					dialog.show();
				}
				
				else{
					
					ParseUser user = new ParseUser();
					user.setUsername(username);
					user.setPassword(password);
					user.setEmail(email);
					setProgressBarIndeterminateVisibility(true);
					user.signUpInBackground(new SignUpCallback() {
					
					@Override
					public void done(com.parse.ParseException e) {
						// TODO Auto-generated method stub
						setProgressBarIndeterminateVisibility(false);
						if (e == null) {
							
							Intent bIntent= new Intent(SignUpActivity.this,MainActivity.class);
							bIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							bIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(bIntent);
						      
						    } else {
						    	
						    	AlertDialog.Builder builder= new AlertDialog.Builder(SignUpActivity.this)
								.setTitle(R.string.signup_error_title)
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



}
