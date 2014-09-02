package com.example.ribbit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class InboxFragment extends ListFragment {

    protected List<ParseObject> bMessages;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_inbox,
				container, false);
		return rootView;
	}


    @Override
    public void onResume() {
        super.onResume();
        getActivity().setProgressBarIndeterminateVisibility(true);
        ParseQuery<ParseObject> query= new ParseQuery(ParseConstans.CLASS_MESSAGES);
        query.whereEqualTo(ParseConstans.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
        query.addAscendingOrder(ParseConstans.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {
                getActivity().setProgressBarIndeterminateVisibility(false);

                if(e==null){
                    bMessages=messages;

                    String[] usernames= new String[bMessages.size()];
                    int i=0;
                    for(ParseObject message:bMessages){

                        usernames[i]=message.getString(ParseConstans.KEY_SENDER_NAME).toString();
                        i++;
                    }

                   setListAdapter(new MessageAdapter(getListView().getContext(),
                           messages));

                }


            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ParseObject message=bMessages.get(position);
        String messgeType= message.getString(ParseConstans.KEY_FILE_TYPE);
        ParseFile file= message.getParseFile(ParseConstans.KEY_FILE);
        Uri fileUri= Uri.parse(file.getUrl());

        if(messgeType.equals(MainActivity.KEY_PICTURE_TYPE)){

            Intent intent= new Intent(getActivity(),ViewImageActivity.class);
            intent.setData(fileUri);
            startActivity(intent);
        }
        else{
            Intent intent =new Intent(Intent.ACTION_VIEW,fileUri);
            intent.setDataAndType(fileUri,"video/*");
            startActivity(intent);

        }

        List<String> ids= message.getList(ParseConstans.KEY_RECIPIENT_IDS);
        if(ids.size()==1){

                message.deleteInBackground();

        }
        else{

            ids.remove(ParseUser.getCurrentUser().getObjectId());
            ArrayList<String> removeThis= new ArrayList<String>();
            removeThis.add(ParseUser.getCurrentUser().getObjectId());
            message.removeAll(ParseConstans.KEY_RECIPIENT_IDS,removeThis);
            message.saveInBackground();



        }

    }
}
