package com.bytedance.todolist.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import com.bytedance.todolist.database.TodoListDao;
import com.bytedance.todolist.database.TodoListDatabase;
import com.bytedance.todolist.database.TodoListEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.bytedance.todolist.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TodoListActivity extends AppCompatActivity {

    private TodoListAdapter mAdapter;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list_activity_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TodoListAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        //这里是点击了按钮和叉叉看是否相应的函数
        mAdapter.setOnItemClickListener(new TodoListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position, String theContent) {
                final View tempView = v;
                final int tempPosition = position;
                final String tempContent = theContent;
                //如果点击了checkbox
                if(v.getId()==R.id.chech_state){
                    final AlertDialog.Builder normalDiaog = new AlertDialog.Builder(TodoListActivity.this);
                    normalDiaog.setTitle("提示");
                    normalDiaog.setMessage("您确定要更新该条记录吗?");
                    normalDiaog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            changeState(tempView, tempPosition, tempContent);
                        }
                    });
                    normalDiaog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            loadFromDatabase();
                        }
                    });
                    normalDiaog.show();
                }
                //如果点击了叉叉
                else if(v.getId()==R.id.delete_record){
                    final AlertDialog.Builder normalDiaog = new AlertDialog.Builder(TodoListActivity.this);
                    normalDiaog.setTitle("提示");
                    normalDiaog.setMessage("您确定要删除此条记录吗?");
                    normalDiaog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteOneRecord(tempView, tempPosition, tempContent);
                        }
                    });
                    normalDiaog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    normalDiaog.show();
                }
            }
        });




        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                Intent tempItent = new Intent();
                tempItent.setClass(TodoListActivity.this, TodoListCreate.class);
                startActivity(tempItent);
            }
        });

        mFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                        dao.deleteAll();
                        for (int i = 0; i < 20; ++i) {
                            dao.addTodo(new TodoListEntity("This is " + i + " item", new Date(System.currentTimeMillis()), 0));
                        }
                        Snackbar.make(mFab, R.string.hint_insert_complete, Snackbar.LENGTH_SHORT).show();
                        loadFromDatabase();
                    }
                }.start();
                return true;
            }
        });
        loadFromDatabase();
    }

    private void loadFromDatabase() {
        new Thread() {
            @Override
            public void run() {
                TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                final List<TodoListEntity> entityListUndo = dao.loadAll(0);
                final List<TodoListEntity> entityListdo = dao.loadAll(1);
                Collections.reverse(entityListUndo);
                Collections.reverse(entityListdo);
                entityListUndo.addAll(entityListdo);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(entityListUndo);
                    }
                });
            }
        }.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadFromDatabase();
    }



    //点下checkbox改变记录的状态
    private void changeState(View v, int position, final String content) {
        new Thread(){
            @Override
            public void run() {
                TodoListEntity oneEntity = queryByContent(content);
                //先获取现在的check状态
                int nowCheck = oneEntity.getChecked();

                if(nowCheck==0) nowCheck=1;
                else nowCheck = 0;

                oneEntity.setChecked(nowCheck);
                //然后更新该checkBox
                TodoListDatabase.inst(TodoListActivity.this).todoListDao().updateEntity(oneEntity);
                Snackbar.make(mFab, "更新成功", Snackbar.LENGTH_SHORT).show();
                loadFromDatabase();
            }
        }.start();
    }

    //点下叉叉删除一条记录
    private void deleteOneRecord(View v, int position, final String content){
        new Thread(){
            @Override
            public void run() {
                final TodoListEntity oneEntity = queryByContent(content);
                TodoListDatabase.inst(TodoListActivity.this).todoListDao().deleteRecord(oneEntity);
                Snackbar.make(mFab, "删除记录成功", Snackbar.LENGTH_SHORT).show();
                loadFromDatabase();
            }
        }.start();
    }

    //根据字段查询
    private TodoListEntity queryByContent(String content){
        final TodoListEntity tempTodo = TodoListDatabase.inst(TodoListActivity.this).todoListDao().getEntity(content);
        return tempTodo;
    }
}
