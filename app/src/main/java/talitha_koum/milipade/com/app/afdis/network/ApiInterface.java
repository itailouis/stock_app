package talitha_koum.milipade.com.app.afdis.network;

import okhttp3.MultipartBody;
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
import talitha_koum.milipade.com.app.afdis.responses.InventoryHistoryResponse;
import talitha_koum.milipade.com.app.afdis.responses.LoginResponse;
import talitha_koum.milipade.com.app.afdis.responses.OrderHistoryResponse;
import talitha_koum.milipade.com.app.afdis.responses.OrderResponse;
import talitha_koum.milipade.com.app.afdis.responses.ProductSizeResponse;
import talitha_koum.milipade.com.app.afdis.responses.SaveResponse;
import talitha_koum.milipade.com.app.afdis.responses.ShopResponse;
import talitha_koum.milipade.com.app.afdis.responses.StockResponse;

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
    Call<SaveResponse> saveStockTake(@Field("productname") String productname,
                                     @Field("quantity") String quantity,
                                     @Field("price") String price,
                                     @Field("aName") String aName,
                                     @Field("bName") String bName,
                                     @Field("aPrice") String aPrice,
                                     @Field("bPrice") String bPrice,
                                     @Field("inline") String inline,
                                     @Field("facingNumber") String facingNumber,
                                     @Part MultipartBody.Part image);

    @GET("api/product")
    Call<ProductSizeResponse> getProduct();

    @FormUrlEncoded
    @POST("api/stock")
    Call<LoginResponse> login(@Field("username") String username, @Field("password")String password);

    @FormUrlEncoded
    @POST("api/stock")
    Call<SaveResponse> getBarcode(@Field("barcode")String dlg);

    @GET("api/stockhistory?")
    Call<InventoryHistoryResponse> getStockHistory(@Query("shop_id")String shop_id);

    @GET("api/orderhistory?")
    Call<OrderHistoryResponse> getOrderHistory(@Query("shop_id")String shop_id);

    //@GET("api/orderhistory/{cat}/{id}")
    //Call<OrderHistoryResponse> getOrderHistory(@Path("shop_id")String shop_id);
}

