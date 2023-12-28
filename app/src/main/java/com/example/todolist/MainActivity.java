package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.todolist.Adapter.ToDoAdapter;
import com.example.todolist.DAO.ToDoDAO;
import com.example.todolist.Model.ToDoModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView todoListView;
    private ToDoModel toDoModel;
    private ToDoAdapter toDoAdapter;
    private ToDoModel selectedToDo; // Biến để lưu ToDoModel được chọn
    Button buttonAdd;
    Button buttonUpdate;
    Button buttonDelete;
    EditText editTextTitle;
    EditText editTextContent;
    EditText editTextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        editTextDate = findViewById(R.id.editTextDate);
        todoListView = findViewById(R.id.todoListView);
        ToDoDAO toDoDAO = new ToDoDAO(this);
        ArrayList<ToDoModel> list = toDoDAO.getListToDo();
        toDoAdapter = new ToDoAdapter(this, list);
        todoListView.setAdapter(toDoAdapter);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString().trim();
                String content = editTextContent.getText().toString().trim();
                String date = editTextDate.getText().toString().trim();

                // Kiểm tra xem các trường có trống không
                if (!title.isEmpty() && !content.isEmpty() && !date.isEmpty()) {
                    // Tạo đối tượng ToDoModel mới
                    ToDoModel newToDo = new ToDoModel(0, title, content, date);

                    // Thêm mới ToDo vào cơ sở dữ liệu
                    boolean success = toDoDAO.AddListToDo(newToDo);

                    if (success) {
                        // Nếu thêm mới thành công, cập nhật giao diện và xóa nội dung EditText
                        toDoAdapter.add(newToDo);
                        toDoAdapter.notifyDataSetChanged();
                        clearInputFields();
                    } else {
                        // Xử lý khi thêm mới thất bại (ví dụ: hiển thị thông báo lỗi)
                    }
                } else {
                    // Xử lý khi một hoặc nhiều trường đang trống (ví dụ: hiển thị cảnh báo)
                }
            }
        });

        // Set the item click listener for the ListView
        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy ToDoModel được chọn từ danh sách
                selectedToDo = (ToDoModel) parent.getItemAtPosition(position);

                // Hiển thị thông tin của ToDoModel lên EditText
                editTextTitle.setText(selectedToDo.getTitle());
                editTextContent.setText(selectedToDo.getContent());
                editTextDate.setText(selectedToDo.getDate());

                // Enable the "Update" button
                buttonUpdate.setEnabled(true);
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString().trim();
                String content = editTextContent.getText().toString().trim();
                String date = editTextDate.getText().toString().trim();

                // Kiểm tra xem các trường có trống không
                if (!title.isEmpty() && !content.isEmpty() && !date.isEmpty() && selectedToDo != null) {
                    // Cập nhật dữ liệu trong đối tượng selectedToDo
                    selectedToDo.setTitle(title);
                    selectedToDo.setContent(content);
                    selectedToDo.setDate(date);

                    // Cập nhật ToDo trong cơ sở dữ liệu
                    boolean success = toDoDAO.UpdateListToDo(selectedToDo);

                    if (success) {
                        // Nếu cập nhật thành công, cập nhật giao diện và xóa nội dung EditText
                        toDoAdapter.notifyDataSetChanged();
                        clearInputFields();
                    } else {
                        // Xử lý khi cập nhật thất bại (ví dụ: hiển thị thông báo lỗi)
                    }
                } else {
                    // Xử lý khi một hoặc nhiều trường đang trống hoặc không có ToDo được chọn
                    // (ví dụ: hiển thị cảnh báo)
                }
                selectedToDo = null;
                buttonUpdate.setEnabled(false);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });


    }
    private void clearInputFields() {
        editTextTitle.setText("");
        editTextContent.setText("");
        editTextDate.setText("");
    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this ToDo?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xóa ToDo từ cơ sở dữ liệu
                deleteSelectedToDo();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng hộp thoại
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void deleteSelectedToDo() {
        if (selectedToDo != null) {
            ToDoDAO toDoDAO = new ToDoDAO(MainActivity.this);
            boolean success = toDoDAO.DeleteListToDo(selectedToDo.getId());

            if (success) {
                // Nếu xóa thành công, cập nhật giao diện và xóa nội dung EditText
                toDoAdapter.remove(selectedToDo);
                toDoAdapter.notifyDataSetChanged();
                clearInputFields();
            } else {
                // Xử lý khi xóa thất bại (ví dụ: hiển thị thông báo lỗi)
            }
        }
    }
}
