package com.bytedance.todolist.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
@Entity(tableName = "lfy_todo")
public class TodoListEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long mId;

    @ColumnInfo(name = "content")
    private String mContent;

    @ColumnInfo(name = "time")
    private Date mTime;

    @ColumnInfo(name = "ischecked")
    private int mChecked;

    public TodoListEntity(String mContent, Date mTime, int mChecked) {
        this.mContent = mContent;
        this.mTime = mTime;
        this.mChecked = mChecked;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public Date getTime() {
        return mTime;
    }

    public void setTime(Date mTime) {
        this.mTime = mTime;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long mId) {
        this.mId = mId;
    }

    public int getChecked() {return mChecked;}

    public void setChecked(int mchecked) {this.mChecked = mchecked;}
}
