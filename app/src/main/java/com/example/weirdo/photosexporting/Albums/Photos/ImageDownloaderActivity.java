package com.example.weirdo.photosexporting.Albums.Photos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.weirdo.photosexporting.R;
import com.facebook.internal.ImageDownloader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageDownloaderActivity extends AppCompatActivity {
public void Display(List<String> fbPhotosList){

    /** Here i'm passing the images urls that i fetched via the fetchphotos class */

    // convert the list to an array
    fetchPhotos m=new fetchPhotos();
    fbPhotosList= m.getFbPhotosList();
        String[] imageUrls = new String[fbPhotosList.size()];
        imageUrls = fbPhotosList.toArray(imageUrls);

    // now that we have the urls, we'll try to fetch images via their urls
    class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public ImageDownloader(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("MyApp", e.getMessage());
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

     int currImage = 0;
    // getting the layout for displaying images
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_downloader);

        setInitialImage();
        setImageRotateListener();
    };

    // this method adds a button that allow to rotate the various pictures within the album
    private void setImageRotateListener() {
        final Button rotatebutton = (Button) findViewById(R.id.btnRotateImage);
        rotatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                currImage++;
                if (currImage == 3) {
                    currImage = 0;
                }
                setCurrentImage();
            }
        });
    }

    private void setInitialImage() {
        setCurrentImage();
    }
    // Displaying images
    private void setCurrentImage() {
        final ImageView imageView = (ImageView) findViewById(R.id.imageDisplay);
        ImageDownloader imageDownLoader = new ImageDownloader(imageView);
        imageDownLoader.execute(imageUrls[currImage]);
    }
}
}
