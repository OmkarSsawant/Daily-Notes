package com.college.miniProject1;



public class Task {
     String mTask;
     boolean isDone;
    public  Task(String task,boolean isDone){
        mTask = task;
        this.isDone = isDone;
    }

    @Override
    public String toString() {
        return "Task{" +
                "mTask='" + mTask + '\'' +
                ", isDone=" + isDone +
                '}';
    }
}
