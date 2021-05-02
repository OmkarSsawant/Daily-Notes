package com.college.miniProject1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TaskListAdapter adapter;
    private TaskRepo repo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DailyNoteApplication app = (DailyNoteApplication) getApplication() ;
        repo = new TaskRepo(this,app.exService,app.mainUIUpdater);
        final ListView lv  = findViewById(R.id.task_list);
        adapter = new TaskListAdapter();
        lv.setAdapter(adapter);
        final Button add  = findViewById(R.id.add_task);
        final EditText task_ET = findViewById(R.id.task);
        repo.getAll(new ResultCallback<ArrayList<Task>>() {
            @Override
            void onSuccess(ArrayList<Task> res) {
                for(Task t : res){
                    adapter.addTask(t);
                }
            }

            @Override
            void onFail() {
                super.onFail();
            }
        });


        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View _) {
                Task todo = new Task(task_ET.getText().toString(),false);
                repo.addTask(todo, new ResultCallback<Long>() {
                    @Override
                    void onSuccess(Long inserted_id) {
                        adapter.addTask(todo);
                    }

                    @Override
                    void onFail() {
                        Toast.makeText(MainActivity.this,"Failed to Add TODO",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }


}