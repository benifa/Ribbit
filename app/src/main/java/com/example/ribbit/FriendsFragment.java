package com.example.ribbit;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FriendsFragment extends ListFragment {
	
	protected ParseRelation<ParseUser> friendShip;
	protected ParseUser currentUser;
	protected List<ParseUser> bUsers;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_friends,
				container, false);
		return rootView;
	}
	
	  @Override
	    public  void onResume() {
	    	// TODO Auto-generated method stub
	    	
	    	super.onResume();
	    	currentUser=ParseUser.getCurrentUser();
	    	friendShip=currentUser.getRelation("friendship");
	    	ParseQuery<ParseUser> query = friendShip.getQuery();
	    	query.orderByAscending(ParseConstans.KEY_USERNAME);
	    	getActivity().setProgressBarIndeterminateVisibility(true);
	    	friendShip.getQuery().findInBackground(new FindCallback<ParseUser>() {

				@Override
				public void done(List<ParseUser> friends, ParseException error) {
					getActivity().setProgressBarIndeterminateVisibility(false);
					if(error==null){
						String[] usernames= new String[friends.size()];
						int i=0;
						for(ParseUser user:friends){
							
							usernames[i]=user.getString(ParseConstans.KEY_USERNAME).toString();
							i++;
						}
						
						setListAdapter(new ArrayAdapter<String>(getListView().getContext(),
								android.R.layout.simple_list_item_checked, usernames));
						
					}
					
					else{	
						
						AlertDialog.Builder builder= new AlertDialog.Builder(getListView().getContext())
						.setTitle(R.string.friend_querry_error_title)
						.setMessage(R.string.friend_querry_error_message)
						.setPositiveButton(android.R.string.ok, null);
						Dialog dialog= builder.create();
						dialog.show();
						
					}
					
				}
	    		
			});

	  }

}
