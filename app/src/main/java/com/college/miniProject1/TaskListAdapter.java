package com.college.miniProject1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class TaskListAdapter extends BaseAdapter {
    OnTaskStatusUpdate mListener;
    public TaskListAdapter(OnTaskStatusUpdate listener){
        mListener = listener;
    }

   final ArrayList<Task> _tasks = new ArrayList<>();
    @Override
    public int getCount() {
        return _tasks.size();
    }

    @Override
    public Task getItem(int position) {
        return _tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            final LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             convertView =  li.inflate(R.layout.task_tile,parent,false);
            convertView.setTag(new TaskViewHolder(convertView));
        }

        TaskViewHolder vh = (TaskViewHolder) convertView.getTag();
        vh.taskTV.setText(getItem(position).mTask);
        vh.isDoneTV.setChecked(getItem(position).isDone);
        vh.isDoneTV.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mListener.onTaskUpdate(position,vh.isDoneTV.isChecked());
            }
        });
        return vh.view;
    }
    void clearAll(){
    _tasks.clear();
    notifyDataSetChanged();
    }

    void addTask(Task t){
        _tasks.add(t);
        notifyDataSetChanged();
    }

    class TaskViewHolder{
        final CheckBox isDoneTV;
        final TextView taskTV;
        final View view;
        TaskViewHolder(View v){
            view = v;
            isDoneTV = v.findViewById(R.id.isDone);
            taskTV = v.findViewById(R.id.task_name);
        }
    }
}
