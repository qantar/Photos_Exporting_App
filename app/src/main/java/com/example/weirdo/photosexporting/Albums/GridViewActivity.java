package com.example.weirdo.photosexporting.Albums;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.example.weirdo.photosexporting.Albums.Photos.fetchPhotos;
import com.example.weirdo.photosexporting.MainFragment;
import com.example.weirdo.photosexporting.R;

public class GridViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        GridView gridview = (GridView)findViewById(R.id.gridview);

        /**Here i should pass the album names array i fetched albumName from
         * the MainFragment.java file (don't know how to pass the array here)*/

        final String[] albumName = null;

        ArrayAdapter<String> ad = new ArrayAdapter<String>(
                getApplicationContext(), android.R.layout.simple_list_item_1,
                albumName);

        gridview.setNumColumns(2);
        gridview.setGravity(Gravity.CENTER);
        gridview.setAdapter(ad);
        /* when the album name is clicked this method will display the images in each album, by:
            calling the fetchPhotosFromAlbumId.
        */
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // Here i should fetch the images and display them
                fetchPhotos m=new fetchPhotos();
                m.fetchPhotosFromAlbumId(album_id,activity);
            }
        });
    }
}
