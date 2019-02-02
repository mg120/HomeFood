package com.homFood.networking;


import com.homFood.model.AboutModel;
import com.homFood.model.CardModel;
import com.homFood.model.CategoriesModel;
import com.homFood.model.ContactModel;
import com.homFood.model.EditProductModel;
import com.homFood.model.LoginModel;
import com.homFood.model.PicModel;
import com.homFood.model.SignupModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Ma7MouD on 9/18/2018.
 */

public interface ApiService {

    //************** LogIn Method **************  //
    @FormUrlEncoded
    @POST("/CookApp/User/Login.php")
    Call<LoginModel> login(@Field("Name") String email_address,
                           @Field("Password") String password,
                           @Field("token") String firebase_token);

    //************* Sign Up ********************* //
    @FormUrlEncoded
    @POST("/CookApp/create_account1.php")
    Call<List<SignupModel>> signUp(@Field("Name") String name,
                                   @Field("Email") String email,
                                   @Field("Password") String password,
                                   @Field("Phone") String phone,
                                   @Field("Lat") String lat,
                                   @Field("Lan") String lan,
                                   @Field("Address") String address,
                                   @Field("type") String type,
                                   @Field("img") String image);


    // ---------------------- Categories --------------------------

    @GET("/CookApp/User/SearchProductAll.php")
    Call<CategoriesModel> getCategories();


    // ---------------------- Add Product -------------------------
    @Multipart
    @POST(Urls.uploadPic)
    Call<PicModel> uploadImage(
            @Part("Name") RequestBody name,
            @Part("FoodType") RequestBody foodType,
            @Part("Timeee") RequestBody time,
            @Part("Price") RequestBody price,
            @Part("Description") RequestBody desc,
            @Part("Customer_id") RequestBody customer_id,
            @Part("customer_name") RequestBody customer_name,
            @Part("customer_mail") RequestBody customer_email,

            @Part MultipartBody.Part image1,
            @Part MultipartBody.Part image2,
            @Part MultipartBody.Part image3);


    // ------------- EdiT Product -----------------
    @Multipart
    @POST(Urls.updateProduct)
    Call<EditProductModel> edit_Product(
            @Part("Product_ID") RequestBody product_id,
            @Part("Name") RequestBody name,
            @Part("FoodType") RequestBody foodType,
            @Part("Timeee") RequestBody time,
            @Part("Price") RequestBody price,
            @Part("Description") RequestBody desc,

            @Part MultipartBody.Part image1,
            @Part MultipartBody.Part image2,
            @Part MultipartBody.Part image3);


    //    // ----------------- Cart Data ---------------------------//
    @FormUrlEncoded
    @POST("/api/cartitems")
    Call<CardModel> getCartData(@Field("owner") String owner_id);


    //    // ------------------- About -----------------------------------//
    @GET("/CookApp/ShowData/Users/ShowAbout.php")
    Call<AboutModel> aboutApp();


    //    // ------------------- Contact Us ------------------------------//
    @FormUrlEncoded
    @POST("/CookApp/sendemail.php")
    Call<ContactModel> contact_Us(@Field("to") String to,
                                  @Field("from") String from,
                                  @Field("subject") String subject,
                                  @Field("message") String message);

}