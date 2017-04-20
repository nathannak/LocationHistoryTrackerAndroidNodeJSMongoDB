package notificationslab.labs.course.volleyposttest;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //
        Log.v("myapp", "getIntent().getIntExtra('size',0) : " + getIntent().getIntExtra("size",0) );
        //
        Log.v("myapp", "getIntent().getDoubleExtra('lat1',0) : " + getIntent().getDoubleExtra("lat1",0));
        Log.v("myapp", "getIntent().getDoubleExtra('lat2',0) : " + getIntent().getDoubleExtra("lat2",0));
        Log.v("myapp", "getIntent().getDoubleExtra('lat3',0) : " + getIntent().getDoubleExtra("lat3",0));
        //
        Log.v("myapp","getIntent().getDoubleExtra('lng1',0) :" + getIntent().getDoubleExtra("lng1",0));
        Log.v("myapp","getIntent().getDoubleExtra('lng2',0) :" + getIntent().getDoubleExtra("lng2",0));
        Log.v("myapp","getIntent().getDoubleExtra('lng3',0) :" + getIntent().getDoubleExtra("lng3",0));
        //

        //
        for (int i =0; i< getIntent().getIntExtra("size",0);i++)
        {
            String latkey = "lat" + (i+1);
            String lngkey = "lng" + (i+1);

            LatLng sydney = new LatLng(getIntent().getDoubleExtra(latkey,0), getIntent().getDoubleExtra(lngkey,0));
            //
            //assuming we are interested in THREE locations only
            if(latkey.equals("lat1"))
            {
                MarkerOptions marker = new MarkerOptions().position(sydney).title("thirs loc").icon(BitmapDescriptorFactory.fromResource(R.mipmap.image3));
                mMap.addMarker(marker);
            }
            else if(latkey.equals("lat2"))
            {
                MarkerOptions marker = new MarkerOptions().position(sydney).title("second loc").icon(BitmapDescriptorFactory.fromResource(R.mipmap.image2));
                mMap.addMarker(marker);
            }
            else if(latkey.equals("lat3"))
            {
                MarkerOptions marker = new MarkerOptions().position(sydney).title("first loc").icon(BitmapDescriptorFactory.fromResource(R.mipmap.image1));
                mMap.addMarker(marker);
            }
            //
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
            //
        }
        //
    }
    //
}
