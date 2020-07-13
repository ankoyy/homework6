package com.bytedance.todolist.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
@Dao
public interface TodoListDao {
    @Query("SELECT * FROM lfy_todo WHERE ischecked=:ischecked")
    List<TodoListEntity> loadAll(int ischecked);

    @Query("SELECT * FROM lfy_todo")
    List<TodoListEntity> loadAll();

    @Insert
    long addTodo(TodoListEntity entity);

    @Query("DELETE FROM lfy_todo")
    void deleteAll();

    @Delete
    void deleteRecord(TodoListEntity... onetodo);

    @Query("SELECT * FROM lfy_todo WHERE content=:content")
    TodoListEntity getEntity(String content);

    @Update
    void updateEntity(TodoListEntity... todos);

}
