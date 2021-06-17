package com.hk.meditechuser;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.hk.meditechuser.Model.Photos;
import com.hk.meditechuser.Model.PlaceDetail;
import com.hk.meditechuser.Remote.GoogleApiService;
import com.hk.meditechuser.databinding.ActivityViewPlaceBinding;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPlaceActivity extends AppCompatActivity {
    private ActivityViewPlaceBinding binding;
    GoogleApiService mService;
    private PlaceDetail mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_place);

        mService = Common.getGoogleApiService();

        //photo
        if (Common.currentResults.getPhotos() != null && Common.currentResults.getPhotos().length > 0) {
            Picasso.get().load(getPhotoPlace(Common.currentResults.getPhotos()[0].getPhoto_reference(), 1000)).placeholder(R.drawable.ic_sample_image).into(binding.photo);
        }

        //rating
        if (Common.currentResults.getRating() != null && !TextUtils.isEmpty(Common.currentResults.getRating())) {

            binding.ratingBar.setRating(Float.parseFloat(Common.currentResults.getRating()));
        } else {
            binding.ratingBar.setVisibility(View.GONE);
        }

        //opening hour
        if (Common.currentResults.getOpening_hours() != null) {

            binding.openHour.setText("Open Now: " + Common.currentResults.getOpening_hours().getOpen_now());
        } else {
            binding.openHour.setVisibility(View.GONE);
        }


        //get Address
        mService.getDetailPlaces(getPlaceDetailUrl(Common.currentResults.getPlace_id()))
                .enqueue(new Callback<PlaceDetail>() {
                    @Override
                    public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {

                        mPlace = response.body();
                        binding.address.setText(mPlace.getResult().getFormatted_address());
                        binding.placeName.setText(mPlace.getResult().getName());
                    }

                    @Override
                    public void onFailure(Call<PlaceDetail> call, Throwable t) {

                    }
                });

        //map button
        binding.showMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPlace.getResult().getUrl()));
                startActivity(mapIntent);

            }
        });

        //direction
        binding.showDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewPlaceActivity.this,ViewDirectionActivity.class));
            }
        });
    }

    private String getPlaceDetailUrl(String place_id) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
        url.append("?place_id=" + place_id);
        url.append("&key=" + getResources().getString(R.string.browser_key));

        return url.toString();
    }

    private String getPhotoPlace(String photos, int maxWidth) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        url.append("?maxwidth=" + maxWidth);
        url.append("&photoreference=" + photos);
        url.append("&key=" + getResources().getString(R.string.browser_key));
        return url.toString();
    }

    public void goBack(View view) {
        finish();
    }
}
