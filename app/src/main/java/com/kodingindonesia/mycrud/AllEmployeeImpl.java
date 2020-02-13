package com.kodingindonesia.mycrud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.kodingindonesia.mycrud.model.Employee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by muhammadyusuf on 01/19/2017.
 * kodingindonesia
 */

public class AllEmployeeImpl extends AppCompatActivity implements ListView.OnItemClickListener{

    private String TAG = AllEmployeeImpl.class.getSimpleName();

    private ListView listView;

    private ImageView image;

    private CircleImageView circleImage;

    private EmployeeArrayAdapter employeeArrayAdapter;

    private Toolbar toolbar;



    ArrayList<com.kodingindonesia.mycrud.model.Employee> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_employee);


        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CONTACT");
        getJSON();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_employee, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                Intent intent = new Intent(AllEmployeeImpl.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        finish();
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,Void>{

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AllEmployeeImpl.this,"Retrieve Data","Please Wait...",false,false);
            }

            @Override
            protected void onPostExecute(Void s) {
                super.onPostExecute(s);
                if (loading.isShowing())
                    loading.dismiss();

                employeeArrayAdapter = new EmployeeArrayAdapter(AllEmployeeImpl.this, 0, list);
                listView.setAdapter(employeeArrayAdapter);

            }

            @Override
            protected Void doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Configuration.URL_GET_ALL);
                Log.e(TAG, "Response from url: " + s);

                if (s != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray contacts = jsonObject.getJSONArray("data");

                        for (int i=0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);

                            String id = c.getString("id");
                            String firstName = c.getString("firstName");
                            String lastName = c.getString("lastName");
                            String age = c.getString("age");
                            String photo = c.getString("photo");

                            com.kodingindonesia.mycrud.model.Employee employee = new com.kodingindonesia.mycrud.model.Employee(id,firstName,lastName,age,photo);

                            list.add(employee);

                        }



                    }catch (final JSONException e) {
                        Log.e(TAG, "Json Parse Error" + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json Parsing Error: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    }
                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check logCat for Possible errors!",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
                return null;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, EmployeeImpl.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Employee emp = (Employee) parent.getItemAtPosition(position);

        String empId = emp.getId();
        String empFirstName = emp.getFirstName();
        String empLastName = emp.getLastName();
        String empAge = emp.getAge();
        String empPhoto = emp.getPhoto();

        intent.putExtra(Configuration.EMP_ID, empId);
        intent.putExtra(Configuration.KEY_EMP_FIRSTNAME, empFirstName);
        intent.putExtra(Configuration.KEY_EMP_LASTNAME, empLastName);
        intent.putExtra(Configuration.KEY_EMP_AGE, empAge);
        intent.putExtra(Configuration.KEY_EMP_PHOTO, empPhoto);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
