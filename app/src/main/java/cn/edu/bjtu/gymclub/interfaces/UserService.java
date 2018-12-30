package cn.edu.bjtu.gymclub.interfaces;

import cn.edu.bjtu.gymclub.model.ResObj;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {

    @POST("login/")
    @FormUrlEncoded
    Call<ResObj> login(@Field("username") String username, @Field("password") String password);

    @POST("register/")
    @FormUrlEncoded
    Call<ResponseBody> logup(@Field("username") String username, @Field("password") String password);
}
