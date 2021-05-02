package com.college.miniProject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnTaskStatusUpdate {

    private TaskListAdapter adapter;
    private TaskRepo repo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb = findViewById(R.id.toolbar);
        setupMenu(tb);

        DailyNoteApplication app = (DailyNoteApplication) getApplication() ;



        repo = new TaskRepo(this,app.exService,app.mainUIUpdater);
        final ListView lv  = findViewById(R.id.task_list);
        adapter = new TaskListAdapter(this);
        lv.setAdapter(adapter);
        final Button add  = findViewById(R.id.add_task);
        final EditText task_ET = findViewById(R.id.task);

            getTasksAndUpdate(TaskRepo.Status.ALL);

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View _) {
                String text = task_ET.getText().toString();
                if(text.isEmpty()) return;
                Task todo = new Task(text,false);
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


    void setupMenu(Toolbar tb){
        Menu m =  tb.getMenu();

        MenuItem all =  m.findItem(R.id.all);
        MenuItem completed =  m.findItem(R.id.completed);
        MenuItem incomplete =  m.findItem(R.id.incomplete);

        all.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getTasksAndUpdate(TaskRepo.Status.ALL);
                return true;
            }
        });
        completed.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getTasksAndUpdate(TaskRepo.Status.COMPLETED);
                return true;
            }
        });
        incomplete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getTasksAndUpdate(TaskRepo.Status.INCOMPLETE);
                return true;
            }
        });
    }

    void getTasksAndUpdate(TaskRepo.Status status){
        adapter.clearAll();
        repo.getTasks(status,new ResultCallback<ArrayList<Task>>() {
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
    }



    @Override
    public void onTaskUpdate(int position, boolean isDone) {
        Task t = adapter.getItem(position);
        t.isDone = isDone;
        repo.updateIsDone(t, new ResultCallback<Boolean>() {
            @Override
            void onSuccess(Boolean res) {
                super.onSuccess(res);
                Toast.makeText(MainActivity.this,"Successfully Updated Status",Toast.LENGTH_SHORT).show();
            }

            @Override
            void onFail() {
                super.onFail();
            }
        });
    }
}