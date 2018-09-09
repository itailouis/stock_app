package talitha_koum.milipade.com.app.afdis.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import talitha_koum.milipade.com.app.afdis.models.Orders;
import talitha_koum.milipade.com.app.afdis.responses.OrderResponse;
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
}

