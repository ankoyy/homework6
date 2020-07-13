package com.bytedance.todolist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bytedance.todolist.R;
import com.bytedance.todolist.database.TodoListDao;
import com.bytedance.todolist.database.TodoListDatabase;
import com.bytedance.todolist.database.TodoListEntity;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

public class TodoListCreate extends AppCompatActivity {

    Button create_btn;
    EditText create_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_create);
        initAll();
    }

    private void initAll(){
        create_btn = findViewById(R.id.create_btn);
        create_txt = findViewById(R.id.create_text);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
                        String temptxt = create_txt.getText().toString();
                        Log.i("input","text is"+temptxt);
                        if(temptxt.equals("")){
                            Snackbar.make(create_btn, "请输入相关信息", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        TodoListDao dao = TodoListDatabase.inst(TodoListCreate.this).todoListDao();
                        dao.addTodo(new TodoListEntity(temptxt, new Date(System.currentTimeMillis()), 0));
                        TodoListCreate.this.finish();
                    }
                }.start();
            }
        });
    }
}
