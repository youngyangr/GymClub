package cn.edu.bjtu.gymclub.utils;


import cn.edu.bjtu.gymclub.interfaces.UserService;

public class ApiUtils {

    public static final String BASE_URL = "http://149.248.21.145:9000/";

    public static UserService getUserService(){
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }
}

