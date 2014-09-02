package com.example.ribbit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by fabrice.benimana on 7/24/14.
 */
public class MessageAdapter extends ArrayAdapter<ParseObject> {

    protected  Context bContext;
    protected List<ParseObject> bMessages;

    public MessageAdapter(Context context,List<ParseObject> messages) {
        super(context, R.layout.imagelayout,messages);
        bContext=context;
        bMessages=messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder bHolder;
        ImageView icon;
        TextView id;
        if(convertView== null){
            /*
            convertView= LayoutInflater.from(bContext).inflate(R.layout.imagelayout,null);
            icon= (ImageView) convertView.findViewById(R.id.imagelabel);
            id= (TextView)convertView.findViewById(R.id.senderId_label);
            */
            convertView= LayoutInflater.from(bContext).inflate(R.layout.imagelayout,null);
            bHolder=new ViewHolder();
            bHolder.iconImageView=(ImageView)convertView.findViewById(R.id.imagelabel);
            bHolder.nameLabel=(TextView)convertView.findViewById(R.id.senderId_label);
            convertView.setTag(bHolder);

       }


        else{
            bHolder=(ViewHolder)convertView.getTag();
        }

        ParseObject message=bMessages.get(position);
        if(message.getString(ParseConstans.KEY_FILE_TYPE).equals(MainActivity.KEY_PICTURE_TYPE))
        bHolder.iconImageView.setImageResource(R.drawable.ic_action_picture);
            //icon.setImageResource(R.drawable.ic_action_picture);
        else
            bHolder.iconImageView.setImageResource(R.drawable.ic_action_video);

        //icon.setImageResource(R.drawable.ic_action_video);
        //id.setText(message.getString(ParseConstans.KEY_SENDER_NAME));
        bHolder.nameLabel.setText(message.getString(ParseConstans.KEY_SENDER_NAME));


        return convertView;
    }

    private static class ViewHolder {
        ImageView iconImageView;
        TextView nameLabel;

    }
}
