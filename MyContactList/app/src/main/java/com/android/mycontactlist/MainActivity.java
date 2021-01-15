package com.android.mycontactlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mycontactlist.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jagerfield.mobilecontactslibrary.Contact.Contact;
import jagerfield.mobilecontactslibrary.ImportContactsAsync;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {



    private static final String[] requiredPermissions = {
            android.Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
    };
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_CODE = 1;
    RecyclerView recyclerView;
    TextView tv_alert;
    ArrayList<Contact> contactList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();


    }
    private void initViews(){
        recyclerView=findViewById(R.id.recyclerView);
        tv_alert=findViewById(R.id.tv_alert);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                checkPermissions();
            }else{
                getContactList();
                //getEmailIDs();
                //getAllContactList();
            }
        }
    }




    /**
     * checking  permissions at Runtime.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        final List<String> neededPermissions = new ArrayList<>();
        for (final String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    permission) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(permission);
            }
        }
        if (!neededPermissions.isEmpty()) {
            requestPermissions(neededPermissions.toArray(new String[]{}),
                    MY_PERMISSIONS_REQUEST_ACCESS_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_CODE: {
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    checkPermissions();
                }
                if ((grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getContactList();
                }
            }
        }
    }


    private  void getContactList(){
        new ImportContactsAsync(MainActivity.this, new ImportContactsAsync.ICallback()
        {
            @Override
            public void mobileContacts(ArrayList<Contact> contact_list)
            {
                //ArrayList<Contact> listItem = contactList;
                contactList=contact_list;
                /*suggestedContact=null;
                contacts=null;*/
                if(contactList==null)
                {
                    contactList = new ArrayList<Contact>() ;
                    //Log.i(C.TAG_LIB, "Error in retrieving contacts");
                }

                if(contactList.size()>0){
                    recyclerView.setAdapter(new ContactListAdapter(MainActivity.this, contactList));
                }
                if((contactList==null||contactList.size()==0)){
                     recyclerView.setVisibility(View.GONE);
                     tv_alert.setVisibility(View.VISIBLE);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    tv_alert.setVisibility(View.GONE);
                }

            }
        }).execute();
    }





}