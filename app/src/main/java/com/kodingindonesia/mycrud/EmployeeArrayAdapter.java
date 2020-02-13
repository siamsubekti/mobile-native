package com.kodingindonesia.mycrud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kodingindonesia.mycrud.model.Employee;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EmployeeArrayAdapter extends ArrayAdapter<Employee> {

    List<Employee> employees;

    public EmployeeArrayAdapter(@NonNull Context context, int resource, @NonNull List<Employee> objects) {
        super(context, resource, objects);
        employees = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);

        TextView firstNameText = listItem.findViewById(R.id.firstName);
        TextView lastName = listItem.findViewById(R.id.lastName);
        TextView age = listItem.findViewById(R.id.age);
        ImageView imageView = listItem.findViewById(R.id.photo);

        Employee employee = employees.get(position);

        firstNameText.setText(employee.getFirstName());
        lastName.setText(employee.getLastName());
        age.setText(employee.getAge());
        Picasso.get().load(employee.getPhoto()).into(imageView);

        return listItem;
    }
}
