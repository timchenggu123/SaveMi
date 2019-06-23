package me.timgu.enghack2019;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabelDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions;
import com.snapchat.kit.sdk.SnapCreative;
import com.snapchat.kit.sdk.creative.api.SnapCreativeKitApi;
import com.snapchat.kit.sdk.creative.exceptions.SnapMediaSizeException;
import com.snapchat.kit.sdk.creative.media.SnapMediaFactory;
import com.snapchat.kit.sdk.creative.media.SnapPhotoFile;
import com.snapchat.kit.sdk.creative.models.SnapPhotoContent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_TAKE_PHOTO = 1;

    File mCurrentImage;
    Boolean mAllergyListStarted;
    //view
    RecyclerView mRecyclerView;
    MainListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAllergyListStarted = false;

       FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        mRecyclerView = findViewById(R.id.main_recyclerView);
        mAdapter = new MainListAdapter(this,new ArrayList<String>());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this,"cannot open photo file", Toast.LENGTH_SHORT);
                ex.printStackTrace();
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "me.timgu.enghack2019.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            imageLabelling();
            uploadToSnapChat("Hello World");
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentImage = image;
        return image;
    }

    private void imageLabelling() {
        FirebaseVisionImage image = null;
        Uri uri = Uri.fromFile(mCurrentImage);
        final List<String> labelList = new ArrayList<>();

        try{
            image = FirebaseVisionImage.fromFilePath(getApplicationContext(),uri);
        } catch(IOException e){
            e.printStackTrace();
        }
        /*
        FirebaseVisionCloudLabelDetector detector = FirebaseVision.getInstance()
                .getVisionCloudLabelDetector();

        mAdapter.updateList(labelList);
        Task<List<FirebaseVisionCloudLabel>> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionCloudLabel>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionCloudLabel> labels) {
                                        for (FirebaseVisionCloudLabel label:labels){
                                            String label_text = label.getLabel();
                                            String entity_id = label.getEntityId();
                                            float confidence = label.getConfidence();
                                            labelList.add(label_text);
                                        }
                                        mAdapter.updateList(labelList);
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
                                    }
                                }); */

       FirebaseVisionLabelDetector detector = FirebaseVision.getInstance()
                .getVisionLabelDetector();

        Task<List<FirebaseVisionLabel>> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionLabel>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionLabel> labels) {
                                        String msg = "";
                                        for (FirebaseVisionLabel label:labels){
                                            String label_text = label.getLabel();
                                            String entity_id = label.getEntityId();
                                            float confidence = label.getConfidence();
                                            msg += label_text + ",";
                                            labelList.add(label_text);
                                        }
                                        msg = msg.substring(0,msg.length()-1);
                                        uploadToStdlib(msg);
                                        mAdapter.updateList(labelList);
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
                                    }
                                });
    }

    public void uploadToSnapChat(String caption){
        SnapCreativeKitApi snapCreativeKitApi = SnapCreative.getApi(this);
        SnapMediaFactory snapMediaFactory = SnapCreative.getMediaFactory(this);
        SnapPhotoFile photoFile;
        try {
            photoFile = snapMediaFactory.getSnapPhotoFromFile(mCurrentImage);
        } catch (SnapMediaSizeException e) {
            e.printStackTrace();
            return;
        }
        SnapPhotoContent snapPhotoContent = new SnapPhotoContent(photoFile);
        snapPhotoContent.setCaptionText(caption);
        snapCreativeKitApi.send(snapPhotoContent);
    }

    public void uploadToStdlib(String msg){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url_base ="https://inboxes.api.stdlib.com/save-mi@dev/field_update/?food=";
        String url = url_base + msg;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(stringRequest);
    }

    public void launchAllergyActivity(MenuItem item) {

        Intent intent = new Intent(this,AllergyListActivity.class);
        startActivity(intent);
        mAllergyListStarted = true;
    }
}
