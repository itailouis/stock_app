package talitha_koum.milipade.com.app.afdis.network;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import talitha_koum.milipade.com.app.afdis.models.Orders;
import talitha_koum.milipade.com.app.afdis.models.Threshold;
import talitha_koum.milipade.com.app.afdis.responses.InventoryHistoryResponse;
import talitha_koum.milipade.com.app.afdis.responses.LoginResponse;
import talitha_koum.milipade.com.app.afdis.responses.OrderHistoryResponse;
import talitha_koum.milipade.com.app.afdis.responses.OrderResponse;
import talitha_koum.milipade.com.app.afdis.responses.PlanResponse;
import talitha_koum.milipade.com.app.afdis.responses.ProductSizeResponse;
import talitha_koum.milipade.com.app.afdis.responses.SaveResponse;
import talitha_koum.milipade.com.app.afdis.responses.ShopResponse;
import talitha_koum.milipade.com.app.afdis.responses.StockResponse;
import talitha_koum.milipade.com.app.afdis.responses.StockSaveResponse;

/**
 * Created by TALITHA_KOUM on 3/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.network
 */
public interface ApiInterface {


    @GET("api/shop?")
    Call<ShopResponse> getShop(@Query("user_id") String user_id);


    @GET("api/stock?")
    Call<StockResponse> getStocks(@Query("shop_id") String shop_id);


    @GET("api/order?")
    Call<OrderResponse> getOrders(@Query("shop_id")String shop_id);


    @POST("api/order")
    Call<SaveResponse> saveOrder(@Body Orders order);

    @POST("api/stock")
    Call<SaveResponse> saveStock(@Body Orders order);

    @Multipart
    @POST("api/stocktake")
    Call<StockSaveResponse> saveStockTake(@Part("product_id") RequestBody product_id,
                                          @Part("date_created") RequestBody date_created,
                                          @Part("shop_id") RequestBody shop_id,
                                          @Part("user_id") RequestBody user_id,
                                          @Part("product_quantity") RequestBody product_quantity,
                                          @Part("breakages") RequestBody breakages,
                                          @Part("product_size") RequestBody product_size,
                                          @Part("inline") RequestBody inline,
                                          @Part("total_shelf") RequestBody total_shelf,
                                          @Part("price") RequestBody Price,
                                          @Part("competitor_name_a") RequestBody competitor_name_a,
                                          @Part("competitor_name_b") RequestBody competitor_name_b,
                                          @Part("competitor_price_a") RequestBody competitor_price_a,
                                          @Part("competitor_price_b") RequestBody competitor_price_b,
                                          @Part MultipartBody.Part image);




    @GET("api/product")
    Call<ProductSizeResponse> getProduct();

    @FormUrlEncoded
    @POST("api/login")
    Call<LoginResponse> login(@Field("email") String email, @Field("password")String password);

    @FormUrlEncoded
    @POST("api/stock")
    Call<SaveResponse> getBarcode(@Field("barcode")String dlg);

    @GET("api/stockhistory?")
    Call<InventoryHistoryResponse> getStockHistory(@Query("shop_id")String shop_id);

    @GET("api/orderhistory?")
    Call<OrderHistoryResponse> getOrderHistory(@Query("shop_id")String shop_id);

    @FormUrlEncoded
    @POST("api/visitplan")
    Call<PlanResponse> getVisitPlan(@Field("shops")String shop, @Field("date")String date, @Field("user_id")String user_id);

    @POST("api/threshold")
    Call<SaveResponse> saveThreshold(@Body Threshold threshold);


    @Multipart
    @POST("api/stocktake")
    Call<talitha_koum.milipade.com.app.afdis.mergeapp.responses.StockSaveResponse> saveStockTake(@Part("product_id") RequestBody product_id,
                                          @Part("date_created") RequestBody date_created,
                                          @Part("shop_id") RequestBody shop_id,
                                          @Part("user_id") RequestBody user_id,
                                          @Part("product_quantity") RequestBody product_quantity,
                                          @Part("breakages") RequestBody breakages,
                                          @Part("product_size") RequestBody product_size,
                                          @Part("inline") RequestBody inline,
                                          @Part("total_shelf") RequestBody total_shelf,

                                          @Part("price") RequestBody Price,
                                          @Part("competitor_name_a") RequestBody competitor_name_a,
                                          @Part("competitor_name_b") RequestBody competitor_name_b,
                                          @Part("competitor_price_a") RequestBody competitor_price_a,
                                          @Part("competitor_price_b") RequestBody competitor_price_b,

                                          @Part("expiry") RequestBody expiry,
                                          @Part("near_date") RequestBody near_date,
                                          @Part MultipartBody.Part image);


    //ProductSizeResponse

    @GET("api/product")
    Call<talitha_koum.milipade.com.app.afdis.mergeapp.responses.ProductSizeResponse> getProductList();



    //@GET("api/orderhistory?")
    //Call<OrderHistoryResponse> getOrderHistory(@Query("shop_id")String shop_id);

    //@GET("api/orderhistory/{cat}/{id}")
    //Call<OrderHistoryResponse> getOrderHistory(@Path("shop_id")String shop_id);
}

