package talitha_koum.milipade.com.app.afdis.mergeapp.networks;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import talitha_koum.milipade.com.app.afdis.mergeapp.responses.ProductSizeResponse;
import talitha_koum.milipade.com.app.afdis.mergeapp.responses.StockSaveResponse;


/**
 * Created by TALITHA_KOUM on 3/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.network
 */
public interface ApiInterface {



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

                                          @Part("expiry") RequestBody expiry,
                                          @Part("near_date") RequestBody near_date,
                                          @Part MultipartBody.Part image);




    @GET("api/product")
    Call<ProductSizeResponse> getProduct();


}

