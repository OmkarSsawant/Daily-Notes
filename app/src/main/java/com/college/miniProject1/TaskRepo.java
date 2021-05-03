package com.college.miniProject1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskRepo {
    final TaskDatabase tdb;
    final SQLiteDatabase db;
    final Executor background;
    final Handler mainThread;

    public TaskRepo(Context context, Executor executor, Handler mainPoster,TaskDatabase tdb) {
        this.tdb = tdb;
        db = tdb.getWritableDatabase();
        background = executor;
        mainThread = mainPoster;
    }


    void addTask(Task t, ResultCallback<Long> rc) {

        background.execute(new Runnable() {
            @Override
            public void run() {

                ContentValues cv = new ContentValues();
                cv.put(Table.Column.taskName, t.mTask);
                cv.put(Table.Column.isDone, t.isDone);
                long insert_id = db.insert(Table.name, null, cv);
                if (insert_id != -1) {
                    mainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            rc.onSuccess(insert_id);
                        }
                    });
                } else {
                    mainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            rc.onFail();
                        }
                    });
                }
            }
        });

    }

    void getTasks(Status status, ResultCallback<ArrayList<Task>> callback) {

        background.execute(new Runnable() {
            @Override
            public void run() {
                final ArrayList<Task> _tasks = new ArrayList<>();
                Cursor cr;
                if (status == Status.COMPLETED) {
                    cr = db.query(Table.name, null, Table.Column.isDone + "=?", new String[]{"1"}, null, null, null);

                } else if (status == Status.INCOMPLETE) {
                    cr = db.query(Table.name, null, Table.Column.isDone + "=?",  new String[]{"0"}, null, null, null);

                } else {
                    cr = db.query(Table.name, null, null, null, null, null, null);
                }

                while (cr.moveToNext()) {
                    _tasks.add(new Task(cr.getString(1), cr.getInt(2) == 1));
                }
                cr.close();
                            mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(_tasks);
                    }
                });
            }
        });

    }


    void updateIsDone(Task todo,ResultCallback<Boolean> rc){
        background.execute(new Runnable() {
            @Override
            public void run() {
                ContentValues cv = new ContentValues();
                cv.put(Table.Column.isDone,todo.isDone);
                int updated_rows = db.update(Table.name,cv, Table.Column.taskName+"=?",new String[]{todo.mTask});
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                            rc.onSuccess(updated_rows>0);
                    }
                });
            }
        });

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        db.close();
    }


    public static class Table {
        static final String name = "tasks";

        public static class Column {
            static final String taskName = "task_name";
            static final String isDone = "isDone";
        }

        public static class Queries {
            static final String ALL = "SELECT * FROM " + name;
            static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + name + "(id INTEGER PRIMARY KEY AUTOINCREMENT," + Column.taskName + " TEXT," + Column.isDone + " BOOLEAN" + ")";
        }
    }

    enum Status {
        COMPLETED, INCOMPLETE, ALL
    }
}
