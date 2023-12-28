package com.example.todolist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.todolist.Model.ToDoModel;
import com.example.todolist.R;

import java.util.List;

public class ToDoAdapter extends ArrayAdapter<ToDoModel> {
    private Context context;
    private List<ToDoModel> tasks;

    public ToDoAdapter(Context context, List<ToDoModel> tasks)
    {
        super(context, 0, tasks);
        this.context = context;
        this.tasks = tasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ToDoModel task = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }
        TextView taskTitle = convertView.findViewById(R.id.task_title);
        TextView taskContent = convertView.findViewById(R.id.task_content);
        TextView taskDate = convertView.findViewById(R.id.task_date);
        taskTitle.setText(task.getTitle());
        taskContent.setText(task.getContent());
        taskDate.setText(task.getDate());
        return convertView;
    }
}
