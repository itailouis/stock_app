package talitha_koum.milipade.com.app.afdis.mergeapp.models;

import java.util.List;

/**
 * Created by TALITHA_KOUM on 13/11/2018.
 * file for the  Superviewer. project
 * in talitha_koum.milipade.com.app.superviewer.models
 */
public class ServerProduct {
    private String  product_id;
    private String product_name;
    private String product_identifier;
    private String product_category;
    private String product_size;
    private List<Size> products_size;

    public ServerProduct() {
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_identifier() {
        return product_identifier;
    }

    public void setProduct_identifier(String product_identifier) {
        this.product_identifier = product_identifier;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }

    public List<Size> getProducts_size() {
        return products_size;
    }

    public void setProducts_size(List<Size> products_size) {
        this.products_size = products_size;
    }
}
