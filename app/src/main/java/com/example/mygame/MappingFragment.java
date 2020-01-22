package com.example.mygame;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.SupportMapFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MappingFragment extends Fragment implements OnMapReadyCallback{
    private String strLatitude;
    private String strLongtitude;
    private GoogleMap gMap;
    Winner userPassed;
    private double dLatitude;
    private double dLongtitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mapping, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().
                findFragmentById(R.id.mapp);
        mapFragment.getMapAsync(this);
    return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng location = new LatLng(dLatitude, dLongtitude);
        gMap.addMarker(new MarkerOptions().position(location));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,16F));
    }

    private String getCompleteAddress(double dLatitude, double dLongtitude) {
        String address = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(dLatitude, dLongtitude, 1);
            if (address != null) {
                Address returnAddress = addresses.get(0);
                StringBuilder stringBuilderReturnAddress = new StringBuilder("");
                for (int i = 0; i < returnAddress.getMaxAddressLineIndex(); i++) {
                    stringBuilderReturnAddress.append(returnAddress.getAddressLine(i)).append("\n");
                }
                address = stringBuilderReturnAddress.toString();
            } else {
                Toast.makeText(getContext(), "Address not found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        return address;
    }

    public void getGameUserInfo(Winner gameUser) {
        userPassed = gameUser;
        if(gameUser.getLatitude()!= null && gameUser.getLongitude()!=null){
        strLatitude = gameUser.getLatitude();
        strLongtitude = gameUser.getLongitude();
        dLatitude =Double.parseDouble(strLatitude);
        dLongtitude = Double.parseDouble(strLongtitude);
        onMapReady(gMap);
        }

    }



}
