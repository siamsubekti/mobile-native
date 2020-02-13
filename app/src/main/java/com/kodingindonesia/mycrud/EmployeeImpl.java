package com.kodingindonesia.mycrud;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by muhammadyusuf on 01/19/2017.
 * kodingindonesia
 */

public class EmployeeImpl extends AppCompatActivity implements View.OnClickListener{
    private EditText editId;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editAge;
    private EditText editPhoto;
    private CircleImageView imagePhoto;
    private FloatingActionButton mFabChoosePic;
    private TextView textTitle;
    private Toolbar toolbar;
    private CardView cardView;

    private Button buttonUpdate;
    private Button buttonDelete;

    private String id;

    private String firstName;

    private String lastName;

    private String age;

    private String photo;

    private Bitmap bitmap;

    private EmployeeArrayAdapter employeeArrayAdapter;

    ArrayList<com.kodingindonesia.mycrud.model.Employee> list = new ArrayList<>();

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_employee);

        toolbar = findViewById(R.id.toolbar);

        Intent intent = getIntent();

        id = intent.getStringExtra(Configuration.EMP_ID);

        firstName = intent.getStringExtra(Configuration.KEY_EMP_FIRSTNAME);

        lastName = intent.getStringExtra(Configuration.KEY_EMP_LASTNAME);

        age = intent.getStringExtra(Configuration.KEY_EMP_AGE);

        photo = intent.getStringExtra(Configuration.KEY_EMP_PHOTO);

        editId = findViewById(R.id.editId);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editAge = findViewById(R.id.editAge);
        imagePhoto = findViewById(R.id.imagePhoto);
        cardView = findViewById(R.id.card_view);

        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        mFabChoosePic = findViewById(R.id.fabChoosePic);
        toolbar = findViewById(R.id.toolbar);
        textTitle = findViewById(R.id.textTitle);
        textTitle.setText("Profile");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);


        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        cardView.setBackgroundResource(R.drawable.convert400);

        editId.setText(id);
        editId.setVisibility(View.GONE);
        editFirstName.setText(firstName);
        editLastName.setText(lastName);
        editAge.setText(age);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.person);
        requestOptions.error(R.drawable.person);

        Glide.with(EmployeeImpl.this)
                .load(photo)
                .apply(requestOptions)
                .into(imagePhoto);

        getEmployee();

        mFabChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_employee, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(EmployeeImpl.this, AllEmployeeImpl.class);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                imagePhoto.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void getEmployee(){
        class GetEmployee extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EmployeeImpl.this,"Please Waiting...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showEmployee(s);

            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Configuration.URL_GET_EMP,id);
                return s;
            }
        }
        GetEmployee ge = new GetEmployee();
        ge.execute();
    }

    private void showEmployee(String json){
        try {


            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Configuration.TAG_JSON_ARRAY);
            Log.i("RESULT", result.toString());
            JSONObject c = result.getJSONObject(0);
            String id = c.getString(Configuration.TAG_ID);
            String firstName = c.getString(Configuration.TAG_FIRSTNAME);
            String lastName = c.getString(Configuration.TAG_LASTNAME);
            String age = c.getString(Configuration.TAG_AGE);
            String image = c.getString(Configuration.TAG_PHOTO);

            editId.setText(id);
            editFirstName.setText(firstName);
            editLastName.setText(lastName);
            editAge.setText(age);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.skipMemoryCache(true);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.placeholder(R.drawable.person);
            requestOptions.error(R.drawable.person);

            Glide.with(getApplicationContext())
                    .load(image)
                    .into(imagePhoto);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateEmployee(){
        final String id = editId.getText().toString().trim();
        final String firstName = editFirstName.getText().toString().trim();
        final String lastName = editLastName.getText().toString().trim();
        final String age = editAge.getText().toString().trim();

        class UpdateEmployee extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EmployeeImpl.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(EmployeeImpl.this,"SUCCESS",Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> employee = new HashMap<>();

                employee.put(Configuration.KEY_EMP_FIRSTNAME,firstName);
                employee.put(Configuration.KEY_EMP_LASTNAME,lastName);
                employee.put(Configuration.KEY_EMP_AGE,age);
                employee.put(Configuration.KEY_EMP_PHOTO,photo);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendUpdateRequest(Configuration.URL_UPDATE_EMP,employee, id);

                return s;
            }
        }

        UpdateEmployee ue = new UpdateEmployee();
        ue.execute();
    }

    private void deleteEmployee(){
        class DeleteEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EmployeeImpl.this, "Updating...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(EmployeeImpl.this, "contact unavailable ", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Configuration.URL_DELETE_EMP, id);
                return s;
            }
        }

        DeleteEmployee de = new DeleteEmployee();
        de.execute();
    }

    private void confirmDeleteEmployee(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this contact?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteEmployee();
                        startActivity(new Intent(EmployeeImpl.this, AllEmployeeImpl.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public void onClick(View v) {
        if(v == buttonUpdate){

            updateEmployee();
            Intent intent = new Intent(EmployeeImpl.this, AllEmployeeImpl.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

        if(v == buttonDelete){
            confirmDeleteEmployee();
        }
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), AllEmployeeImpl.class);
        startActivity(back);
        finish();
    }
}
