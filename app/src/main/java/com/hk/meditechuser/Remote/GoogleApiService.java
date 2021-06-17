package com.hk.meditechuser.Remote;

import com.hk.meditechuser.Model.MyPlace;
import com.hk.meditechuser.Model.PlaceDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GoogleApiService {
    @GET
    Call<MyPlace> getNearbyPlaces(@Url String url);

    @GET
    Call<PlaceDetail> getDetailPlaces(@Url String url);

    @GET("maps/api/directions/json")
    Call<String> getDirections(@Query("origin") String origin,@Query("destination") String destination);
}
