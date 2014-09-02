package com.example.ribbit;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;

import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;


import android.os.Bundle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class EditFriendsActivity extends ListActivity {
	
	protected ParseRelation<ParseUser> friendShip;
	protected ParseUser currentUser;
	protected List<ParseUser> bUsers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_edit_friends);
		// Show the Up button in the action bar.
		setupActionBar();
		
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	
    	super.onResume();
    	currentUser=ParseUser.getCurrentUser();
    	friendShip=currentUser.getRelation("friendship");
    	ParseQuery<ParseUser> query = ParseUser.getQuery();
    	query.orderByAscending(ParseConstans.KEY_USERNAME);
    	query.setLimit(1000);
    	setProgressBarIndeterminateVisibility(true);
    	query.findInBackground(new FindCallback<ParseUser>() {
    			
			@Override
			public void done(List<ParseUser> users, ParseException error) {
				// TODO Auto-generated method stub
				setProgressBarIndeterminateVisibility(false);
				if(error==null){
					bUsers=users;
					String[] usernames= new String[users.size()];
					int i=0;
					for(ParseUser user:users){
						
						usernames[i]=user.getString(ParseConstans.KEY_USERNAME).toString();
						i++;
					}
					
					setListAdapter(new ArrayAdapter<String>(EditFriendsActivity.this,
							android.R.layout.simple_list_item_checked, usernames));
					
					setFriendCheckMarks();
					
				}
				else{
					
					AlertDialog.Builder builder= new AlertDialog.Builder(EditFriendsActivity.this)
					.setTitle(R.string.friend_querry_error_title)
					.setMessage(R.string.friend_querry_error_message)
					.setPositiveButton(android.R.string.ok, null);
					Dialog dialog= builder.create();
					dialog.show();
				}
					
			}
		});
    	
    }
    
    protected void setFriendCheckMarks() {
       
    	friendShip.getQuery().findInBackground(new FindCallback<ParseUser>() {

			@Override
			public void done(List<ParseUser> friends, ParseException error) {
				// TODO Auto-generated method stub
				
				if(error==null){
					for(int i=0; i<bUsers.size();i++){
						
						for(ParseUser user:friends){
							if(bUsers.get(i).getObjectId().equals(user.getObjectId())){
								getListView().setItemChecked(i, true);
							}
						}
					}
				}
				
			}
    		
		});

		
	}

	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	// TODO Auto-generated method stub
    	
    	if(getListView().isItemChecked(position)){
    		
    		super.onListItemClick(l, v, position, id);
        	friendShip.add(bUsers.get(position));
        	currentUser.saveInBackground(new SaveCallback() {
    			
    			@Override
    			public void done(ParseException error) {
    				
    				if(error==null){
    					
    				}
    				
    			}
    		});
    		
    	}
    	else{
    		
    		friendShip.remove(bUsers.get(position));
            currentUser.saveInBackground(new SaveCallback() {
    			
    			@Override
    			public void done(ParseException error) {
    				
    				if(error==null){
    					
    				}
    				
    			}
    		});
    		
    	}
    }
    
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
