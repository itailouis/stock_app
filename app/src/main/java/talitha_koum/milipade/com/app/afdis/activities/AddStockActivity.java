package talitha_koum.milipade.com.app.afdis.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.dialogs.ScanCodeDialog;
import talitha_koum.milipade.com.app.afdis.utils.Const;

public class AddStockActivity extends AppCompatActivity  {
    ImageView ShowSelectedImage;
    private static final int REQUEST_CAMERA = 23;
    private static final int SELECT_FILE = 1;
    Bitmap FixBitmap;
    private String mImageUrl = "";
    public static String shop_id;
    public static String shop_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        shop_id = intent.getStringExtra("shop_id");
        shop_name = intent.getStringExtra("shop_name");

        getSupportActionBar().setTitle(shop_name);
        ImageButton scanbtn = findViewById(R.id.scan_btn);
        ShowSelectedImage = (ImageView)findViewById(R.id.facing_image);

        ShowSelectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
               // UploadImageOnServerButton.setEnabled(true);
                //Intent intent = new Intent();
                //intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);

            }
        });
        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanCodeDialog dialog = new ScanCodeDialog();
                //Bundle b = new Bundle();
                ///b.putParcelable("message", message);
                // dialog.setArguments(b);
                dialog.show(getSupportFragmentManager(), "MyDialog");
            }
        });
       Button fab = (Button) findViewById(R.id.btn_addstock);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri selectedImageUri = I.getData();
            mImageUrl = getPath(selectedImageUri);
            try {

                FixBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                ShowSelectedImage.setImageBitmap(FixBitmap);
                InputStream is = getContentResolver().openInputStream(I.getData());

                //uploadImage(mImageUrl);
                Log.e("picked",mImageUrl);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("pic faied",e.getMessage());
            }
        }
        //UploadImageOnServerButton.setEnabled(false);
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddStockActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Const.checkPermission(AddStockActivity.this);
                if (items[item].equals("Take Photo")) {
                    //userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    //userChoosenTask="Choose from Library";
                    if(result) galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
}
