package com.homFood.activities;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.homFood.networking.Urls;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ma7MouD on 4/13/2018.
 */

public class OrderRequest extends StringRequest {

    private Map<String, String> params;

    public OrderRequest(String prod_name, String Product_img, String foodtype, String quantity, String code, String price, String seller_id,
                        String seller_name, String seller_email, String custom_id, String custom_name,
                        String custom_email, String custom_phone, Response.Listener<String> listener) {
        super(Method.POST, Urls.order_url, listener, null);

        params = new HashMap<>();
        params.put("Product_Name", prod_name);
        params.put("Product_img", Product_img);
        params.put("FoodType", foodtype);
        params.put("Quantity", quantity);
        params.put("code", MainActivity.customer_id + code);
        params.put("Price", price);
        params.put("Seller_id", seller_id);
        params.put("Seller_name", seller_name);
        params.put("Seller_mail", seller_email);
        params.put("Customer_id", custom_id);
        params.put("Customer_name", custom_name);
        params.put("Customer_mail", custom_email);
        params.put("Customer_phone", custom_phone);

        System.out.println("parameters: " + params);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
