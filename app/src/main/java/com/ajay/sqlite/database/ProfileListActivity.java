package com.ajay.sqlite.database;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ajay.sqlite.BaseActivity;
import com.ajay.sqlite.R;
import com.ajay.sqlite.adapter.CustomListAdapter;
import com.ajay.sqlite.adapter.DatabaseAdapter;
import com.ajay.sqlite.databinding.ActivityProfileListBinding;
import com.ajay.sqlite.databinding.LayoutProfileBinding;
import com.ajay.sqlite.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import com.ajay.sqlite.model.StudentData;

public class ProfileListActivity extends BaseActivity implements AdapterView.OnItemLongClickListener {
    private ActivityProfileListBinding binding;
    LayoutProfileBinding profileBinding, profileBindingUpdate;
    private DatabaseAdapter adapter;
    private String imageInBase64;
    private Cursor cursor;
    private int clickedPosition;
    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new DatabaseAdapter(this);
        adapter.openDatabase();

        loadDataInListview();

        binding.listView.setOnItemLongClickListener(this);
        registerForContextMenu(binding.listView);

    }

    private void loadDataInListview() {
        CustomListAdapter adapter = new CustomListAdapter(getAllStudentList(), this);
        binding.listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_create_profile:
                profileBinding = LayoutProfileBinding.inflate(getLayoutInflater());
                Dialog dialog = new Dialog(this);
                dialog.setContentView(profileBinding.getRoot());
                dialog.setCancelable(false);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                profileBinding.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(ProfileListActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                            callCamera();
                        }else {
                            ActivityCompat.requestPermissions(ProfileListActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                        }
                    }
                });

                profileBinding.btnCreateProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.insertData(ProfileListActivity.this, profileBinding.etFirstName.getText().toString(), profileBinding.etLastName.getText().toString(), imageInBase64, profileBinding.etContactNumber.getText().toString(), profileBinding.etEmailId.getText().toString(), profileBinding.etAddress.getText().toString());
                        dialog.dismiss();
                        loadDataInListview();
                    }
                });

                break;
            case R.id.item_delete_all_profile:
                adapter.deleteAllRecords(ProfileListActivity.this);
                loadDataInListview();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO: DO UR JOB
                callCamera();
            } else {
                showLongToast("Permission Denied!");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageInBase64 = Utility.convertBitmaptoBase64(bitmap);
            if (isUpdate){
                profileBindingUpdate.imageView.setImageBitmap(bitmap);
            }else {
                profileBinding.imageView.setImageBitmap(bitmap);
            }

        }
    }

    private List<StudentData> getAllStudentList(){
        List<StudentData> studentDataList = new ArrayList<>();
        cursor = adapter.getAllData();
        if (cursor.getCount() > 0) {
            Log.d("DATA", "" + cursor);
            cursor.moveToFirst();
            do {
                StudentData studentData = new StudentData();
//            String rowId = cursor.getString(0);
//            String fName = cursor.getString(1);
//            String lName = cursor.getString(1);
//            String image = cursor.getString(3);
                studentData.setRowId(cursor.getString(0));
                studentData.setfName(cursor.getString(1));
                studentData.setlName(cursor.getString(2));
                studentData.setImage(cursor.getString(3));
                studentData.setcNumber(cursor.getString(4));
                studentData.seteMail(cursor.getString(5));
                studentData.setAddress(cursor.getString(6));
                studentDataList.add(studentData);
//            Log.d("DATA1", rowId+" "+fName+" "+lName+" "+image);
            } while (cursor.moveToNext());
        }
        return studentDataList;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.profile_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_delete_record:
                cursor.moveToPosition(clickedPosition);
                String rowId = cursor.getString(0);
                adapter.deleteSingleRecord(ProfileListActivity.this, rowId);

                loadDataInListview();
                break;
            case R.id.item_update_record:
                isUpdate = true;
                cursor.moveToPosition(clickedPosition);

                profileBindingUpdate = LayoutProfileBinding.inflate(getLayoutInflater());
                Dialog dialog = new Dialog(this);
                dialog.setContentView(profileBindingUpdate.getRoot());
                profileBindingUpdate.btnCreateProfile.setText("Update Profile");
                dialog.setCancelable(false);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                profileBindingUpdate.etFirstName.setText(cursor.getString(1));
                profileBindingUpdate.etLastName.setText(cursor.getString(2));
                profileBindingUpdate.etContactNumber.setText(cursor.getString(3));
                profileBindingUpdate.etEmailId.setText(cursor.getString(4));
                profileBindingUpdate.etAddress.setText(cursor.getString(5));
                profileBindingUpdate.imageView.setImageBitmap(Utility.convertBase64ToBitmap(cursor.getString(6)));

                profileBindingUpdate.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(ProfileListActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                            callCamera();
                        }else {
                            ActivityCompat.requestPermissions(ProfileListActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                        }
                    }
                });

                profileBindingUpdate.btnCreateProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (imageInBase64 == null){
                            showLongToast("Please clicked image.");
                            return;
                        }else {
                            adapter.updateRecord(ProfileListActivity.this, profileBindingUpdate.etFirstName.getText().toString(), profileBindingUpdate.etLastName.getText().toString(), profileBindingUpdate.etContactNumber.getText().toString(), profileBindingUpdate.etEmailId.getText().toString(), profileBindingUpdate.etAddress.getText().toString(), imageInBase64, cursor.getString(0));
                            dialog.dismiss();
                            loadDataInListview();
                        }
                    }
                });
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        clickedPosition = position;
        return false;
    }


    public void myAlert(Context mContext){
        new AlertDialog.Builder(mContext)
                .setIcon(R.drawable.ic_close)
                .setTitle("Exit?")
                .setMessage("Do you want to exit my app?")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                })
                .setNegativeButton("Cancel",null)
                .show();



    }
}

