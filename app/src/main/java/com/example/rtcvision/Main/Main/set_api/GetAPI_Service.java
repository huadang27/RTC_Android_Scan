package com.example.rtcvision.Main.Main.set_api;

import com.example.rtcvision.Main.Main.modal.InventoryWarehouseSerial;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetAPI_Service {
    @PUT("api/InventoryWarehouse/updateinventorywarehouse")
    Call<Void> updateInventoryWarehouse(@Body InventoryWarehouseSerial request);

}