package com.example.supermarketuser.Utils.Config;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.example.supermarketuser.Utils.Config.Constants.GPS_REQUEST;
import static com.google.android.gms.common.api.CommonStatusCodes.RESOLUTION_REQUIRED;

public class GpsUtils {
    private Context context;
    SettingsClient msettings;
    LocationSettingsRequest mlocationsettingrequest;
    LocationManager locationManager;
    LocationRequest locationRequest;

    public GpsUtils(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        msettings = LocationServices.getSettingsClient(context);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10l * 1000L);
        locationRequest.setFastestInterval(2l * 1000L);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        mlocationsettingrequest = builder.build();
        builder.setAlwaysShow(true);
    }

    public void turnGpsOn(OnGpsListener onGpsListener) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (onGpsListener != null) {
                onGpsListener.gpsStatus(true);
            }
        } else {
            msettings
                    .checkLocationSettings(mlocationsettingrequest)
                    .addOnSuccessListener((Activity) context, new OnSuccessListener<LocationSettingsResponse>() {
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                            if (onGpsListener != null) {
                                onGpsListener.gpsStatus(true);
                            }
                        }
                    }).addOnFailureListener((Activity) context, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case RESOLUTION_REQUIRED:

                            ResolvableApiException rae = (ResolvableApiException) e;
                            try {
                                rae.startResolutionForResult((Activity) context, GPS_REQUEST);
                            } catch (IntentSender.SendIntentException sendIntentException) {
                                Log.i("TAG", "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage ="Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e("TAG", errorMessage);
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                            break;
                        default:
                            break;
                    }

                }
            });
        }
    }

    public interface OnGpsListener {
        void gpsStatus(boolean isGPSEnable);
    }
}
