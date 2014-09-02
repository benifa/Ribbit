package com.example.ribbit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ribbit.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

public class RecipientsActivity extends ListActivity {


    protected ParseRelation<ParseUser> friendShip;
    protected ParseUser currentUser;
    protected List<ParseUser> bFriends;
    protected MenuItem sendMenu;
    protected String messages;
    protected Uri mediaUri;
    protected String bFileType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_recipients);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mediaUri=getIntent().getData();
        bFileType=getIntent().getExtras().getString(ParseConstans.KEY_FILE_TYPE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recipients, menu);
        sendMenu=menu.getItem(0);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        super.onResume();
        currentUser= ParseUser.getCurrentUser();
        friendShip=currentUser.getRelation("friendship");
        ParseQuery<ParseUser> query = friendShip.getQuery();
        query.orderByAscending(ParseConstans.KEY_USERNAME);
        setProgressBarIndeterminateVisibility(true);
        friendShip.getQuery().findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> friends, ParseException error) {
                setProgressBarIndeterminateVisibility(false);
                if(error==null){
                    String[] usernames= new String[friends.size()];
                    bFriends= new ArrayList<ParseUser>();
                    int i=0;
                    for(ParseUser user:friends){
                        bFriends.add(user);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id ) {

            case R.id.action_send: {
                ParseObject message= createMessage();

                if(message==null){

                    AlertDialog.Builder builder= new AlertDialog.Builder(RecipientsActivity.this)
                            .setTitle(getString(R.string.error_sending_dialog_title))
                            .setMessage(getString(R.string.error_sending_dialog_message))
                            .setPositiveButton(android.R.string.ok, null);
                    Dialog dialog= builder.create();
                    dialog.show();
                }

                else{
                    sendMessage(message);
                    finish();
                }

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendMessage(ParseObject message) {

        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e==null){

                    Toast.makeText(RecipientsActivity.this,getString(R.string.message_sent_notification),Toast.LENGTH_LONG).show();
                }

                else{

                    AlertDialog.Builder builder= new AlertDialog.Builder(RecipientsActivity.this)
                            .setTitle(getString(R.string.error_sending_dialog_title))
                            .setMessage(getString(R.string.error_sending_dialog_message))
                            .setPositiveButton(android.R.string.ok, null);
                    Dialog dialog= builder.create();
                    dialog.show();


                }

            }
        });
    }


    private ParseObject createMessage() {

        ParseObject message= new ParseObject(ParseConstans.CLASS_MESSAGES);
        message.put(ParseConstans.KEY_SENDER_ID,ParseUser.getCurrentUser().getObjectId());
        message.put(ParseConstans.KEY_SENDER_NAME,ParseUser.getCurrentUser().getUsername());
        message.put(ParseConstans.KEY_RECIPIENT_IDS,getRecipientIds());
        message.put(ParseConstans.KEY_FILE_TYPE,bFileType);

        byte[] fileByte= FileHelper.getByteArrayFromFile(this,mediaUri);

        if(fileByte==null){

            return null;
        }

        else{
            if(bFileType==MainActivity.KEY_PICTURE_TYPE){

                fileByte=FileHelper.reduceImageForUpload(fileByte);
            }

            String fileName=FileHelper.getFileName(this,mediaUri,bFileType);
            ParseFile file= new ParseFile(fileName,fileByte);
            message.put(ParseConstans.KEY_FILE,file);
        }
        return message;


    }



    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);

        if(!(l.getCheckedItemCount()==0))

            sendMenu.setVisible(true);
        else
            sendMenu.setVisible(false);

    }

    private ArrayList<String> getRecipientIds() {

        ArrayList<String> recipientIds= new ArrayList<String>();

        for(int i=0;i<getListView().getCount();i++){
            if(getListView().isItemChecked(i)){
                recipientIds.add(bFriends.get(i).getObjectId());
            }

        }

        return recipientIds;
    }
}
