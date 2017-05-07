package com.example.tania_nikos.remotesensing;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.tania_nikos.remotesensing.ActivitiesSindesis.EidosSindesisActivity;
import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PictureActivity extends ActionBarActivity {

    /**
     * Camera code constant
     */
    static final int REQUEST_IMAGE_CAPTURE = 1777;

    /**
     * Local path for image storage
     */
    protected String mCurrentPhotoPath;

    Button retake;
    Button connect;
    /**
     * Initialize view open image capture
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        connect = (Button) findViewById(R.id.button_connect);
        connect.setEnabled(false);

        retake = (Button) findViewById(R.id.button_retake);

    }

    /**
     * Open camera to take picture
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Uri fileUri = getOutputMediaFileUri();
            takePictureIntent.putExtra( MediaStore.EXTRA_OUTPUT, fileUri );
            this.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "FarmPhotoTool");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("FarmPhotoTool", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        mCurrentPhotoPath = mediaFile.getAbsolutePath();
        return mediaFile;
    }

    /**
     * Habdle result after image has been captured
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            ImageView imageView = (ImageView) findViewById(R.id.imageView4);
            Bitmap bitmap = decodeSampledBitmapFromFile(mCurrentPhotoPath, imageView.getWidth(), imageView.getHeight());

            try {
                ExifInterface exif = new ExifInterface(mCurrentPhotoPath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                Matrix matrix = new Matrix();
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        break;
                    default:
                        break;
                }

                // Rotate bitmap
                Bitmap rotatedBitmap = Bitmap.createBitmap(
                        bitmap,
                        0,
                        0,
                        bitmap.getWidth(),
                        bitmap.getHeight(),
                        matrix,
                        true
                );

                imageView.setImageBitmap(rotatedBitmap);

//                Log.d("MyApp", "after show with store Dir : "+Environment.getExternalStoragePublicDirectory(
//                        Environment.DIRECTORY_PICTURES) + " PATH : " + mCurrentPhotoPath);

                /**
                 * Save The Rotated Bitmap
                 */
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(mCurrentPhotoPath);
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * Take picture again
     *
     * @param view
     */
    public void retakePicture(View view)
    {
        this.dispatchTakePictureIntent();
        retake.setText("Επανάληψη");
        connect.setEnabled(true);
    }

    /**
     * Connect picture
     * @param view
     */
    public void connectPicture(View view)
    {
        Intent intent = new Intent(PictureActivity.this, EidosSindesisActivity.class);
        intent.putExtra("photo_path", mCurrentPhotoPath);
        startActivity(intent);
    }

    /**
     * Set up bitmap
     *
     * @param path
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
}
