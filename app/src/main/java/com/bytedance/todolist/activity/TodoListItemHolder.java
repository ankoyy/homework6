package com.bytedance.todolist.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.todolist.R;
import com.bytedance.todolist.database.TodoListEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
public class TodoListItemHolder extends RecyclerView.ViewHolder {
    private TextView mContent;
    private TextView mTimestamp;
    private CheckBox mCheckBox;

    public TodoListItemHolder(@NonNull View itemView) {
        super(itemView);
        mContent = itemView.findViewById(R.id.tv_content);
        mTimestamp = itemView.findViewById(R.id.tv_timestamp);
        mCheckBox = itemView.findViewById(R.id.chech_state);
    }

    public void bind(TodoListEntity entity) {
        mContent.setText(entity.getContent());
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mTimestamp.setText(dateformat.format(entity.getTime()));
        if(entity.getChecked()==1){
            mCheckBox.setChecked(true);
            mContent.setPaintFlags(mContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            mContent.setTextColor(Color.GRAY);
        }
        else{
            mCheckBox.setChecked(false);
            mContent.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mContent.setPaintFlags(mContent.getPaintFlags()&(~Paint.STRIKE_THRU_TEXT_FLAG));
            mContent.setTextColor(Color.BLACK);
        }
    }

}
