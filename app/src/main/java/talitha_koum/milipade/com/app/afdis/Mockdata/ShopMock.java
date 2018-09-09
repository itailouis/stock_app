package talitha_koum.milipade.com.app.afdis.Mockdata;

import java.util.ArrayList;

import talitha_koum.milipade.com.app.afdis.models.Orders;
import talitha_koum.milipade.com.app.afdis.models.Shop;
import talitha_koum.milipade.com.app.afdis.models.Stock;

/**
 * Created by TALITHA_KOUM on 3/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.Mockdata
 */
public class ShopMock {
    private static ArrayList<Orders> orderList = new ArrayList<>() ;
    private static ArrayList<Shop> shops = new ArrayList<>() ;
    private static ArrayList<Stock> stocks = new ArrayList<>() ;

    public static ArrayList<Shop> getShopList(){


        Shop  shop = new Shop();
        shop.setShop_id("modock_id");
        shop.setShop_name("Spar Mabvuku");
        shop.setLast_visited("");
        shops.add(shop);

        Shop shop1 = new Shop();
        shop1.setShop_id("modock_id");
        shop1.setShop_name("TM Marimba");
        shop1.setLast_visited("");
        shops.add(shop1);

        return shops;
    }


    public static ArrayList<Orders> getOrderList() {

       Orders order = new Orders();
        order.setProduct_id("Chateu");
        order.setQuantity_order("400");
       orderList.add(order);

        Orders order1 = new Orders();
        order1.setProduct_id("TwoKeys");
        order1.setQuantity_order("400");
        orderList.add(order1);

        return orderList;
    }

    public static ArrayList<Stock> getStocksList() {
        Stock order = new Stock();
        order.setProduct_name("Chateu");
        order.setProduct_quantity("400");
        stocks.add(order);

        Stock order1 = new Stock();
        order1.setProduct_name("twokes");
        order1.setProduct_quantity("400");
        stocks.add(order1);
            return stocks;
    }
}
