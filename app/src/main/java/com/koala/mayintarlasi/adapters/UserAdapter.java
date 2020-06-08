package com.koala.mayintarlasi.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.koala.mayintarlasi.R;
import com.koala.mayintarlasi.models.UserModel;

import java.util.List;

import static android.provider.Settings.System.getString;

public class UserAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<UserModel> list;


    public UserAdapter(Context c, List<UserModel> mList)
    {
        this.context =c;
        this.list = mList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater==null)
        {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.row_global_score,null);
        }
        String textEasy = convertView.getContext().getResources().getString(R.string.easy);
        String textMedium = convertView.getContext().getResources().getString(R.string.medium);
        String textHard = convertView.getContext().getResources().getString(R.string.hard);
        TextView userid = convertView.findViewById(R.id.text_id);
        TextView userscore = convertView.findViewById(R.id.text_score);
        TextView difficulty = convertView.findViewById(R.id.text_difficulty);

        final UserModel veri = list.get(position);
        userid.setText(String.valueOf(veri.getUserID()));
        userscore.setText(String.valueOf(veri.getScore()));
        if(veri.getDifficulty()==0){
        difficulty.setText(textEasy);
        }
        else if(veri.getDifficulty()==1)
        {
            difficulty.setText(textMedium);
        }
        else
            difficulty.setText(textHard);

        return convertView;
    }
}
