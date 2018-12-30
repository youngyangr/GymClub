package cn.edu.bjtu.gymclub.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.edu.bjtu.gymclub.R;
import cn.edu.bjtu.gymclub.activity.LoginActivity;
import cn.edu.bjtu.gymclub.activity.MainActivity;
import cn.edu.bjtu.gymclub.model.User;
import cn.edu.bjtu.gymclub.utils.Configure;


public class FragmentThree extends Fragment implements View.OnClickListener{
    int flag;
    Button bt_login;
    TextView tv_name;
    TextView tv_content;
    ImageView imageView;
    private UserInfo mInfo;
    public static Tencent mTencent;
    public static String mAppid="1106062414";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_three,container,false);
        initView(view);
        if(Configure.USERID == ""){
            view.findViewById(R.id.new_login_btn).setVisibility(View.VISIBLE);
            view.findViewById(R.id.login).setVisibility(View.VISIBLE);
            view.findViewById(R.id.new_login_close).setVisibility(View.GONE);
            tv_name= (TextView) view.findViewById(R.id.name);
            tv_content= (TextView) view.findViewById(R.id.content);
            tv_name.setText("Please sign in");
            tv_content.setText("Discover more！");
        }else if(Configure.USERID != ""){
            view.findViewById(R.id.new_login_btn).setVisibility(View.GONE);
            view.findViewById(R.id.login).setVisibility(View.GONE);
            view.findViewById(R.id.new_login_close).setVisibility(View.VISIBLE);
            tv_name= (TextView) view.findViewById(R.id.name);
            tv_content= (TextView) view.findViewById(R.id.content);
            tv_name.setText(Configure.USERID);
            tv_content.setText("Welcome！");
        }
        bt_login=view.findViewById(R.id.login);
        bt_login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initView(View view)
    {
        tv_name= (TextView) view.findViewById(R.id.name);
        tv_content= (TextView) view.findViewById(R.id.content);
        imageView= (ImageView) view.findViewById(R.id.user_logo);
        view.findViewById(R.id.new_login_btn).setOnClickListener(this);
        view.findViewById(R.id.new_login_close).setOnClickListener(this);
        view.findViewById(R.id.new_login_shareqq).setOnClickListener(this);
        view.findViewById(R.id.new_login_shareqzone).setOnClickListener(this);
        if (mTencent == null) {
            mTencent = Tencent.createInstance(mAppid, this.getActivity());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_login_btn:
                onClickLogin();
                flag = 1;
                break;
            case R.id.new_login_close:
                if(flag == 1){
                    mTencent.logout(this.getActivity());//注销登录
                    flag = 0;
                }
                else{
                    Configure.USERID = "";
                    Intent intent = new Intent(this.getActivity(), MainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.new_login_shareqq:
                onClickShare();
                break;
            case R.id.new_login_shareqzone:
                shareToQQzone();
                break;
        }
    }

    /**
     * 继承的到BaseUiListener得到doComplete()的方法信息
     */
    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {//得到用户的ID  和签名等信息  用来得到用户信息
            Log.i("lkei",values.toString());
            initOpenidAndToken(values);
            updateUserInfo();
        }
    };
    /***
     * QQ平台返回返回数据个体 loginListener的values
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Toast.makeText(getActivity(), "登录失败",Toast.LENGTH_LONG).show();
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Toast.makeText(getActivity(), "登录失败",Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(getActivity(), "登录成功",Toast.LENGTH_LONG).show();
            doComplete((JSONObject)response);
        }

        protected void doComplete(JSONObject values) {

        }
        @Override
        public void onError(UiError e) {
            //登录错误
        }

        @Override
        public void onCancel() {
            // 运行完成
        }
    }
    /**
     * 获取登录QQ腾讯平台的权限信息(用于访问QQ用户信息)
     * @param jsonObject
     */
    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }

    private void onClickLogin() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(this.getActivity(), "all", loginListener);
        }
    }

    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                }
                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
                    Log.i("tag", response.toString());
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
                @Override
                public void onCancel() {
                }
            };
            mInfo = new UserInfo(this.getActivity(), mTencent.getQQToken());
            mInfo.getUserInfo(listener);

        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {
                    try {
                        Gson gson=new Gson();
                        User user=gson.fromJson(response.toString(),User.class);
                        if (user!=null) {
                            tv_name.setText("昵称："+user.getNickname()+"  性别:"+user.getGender()+"  地址："+user.getProvince()+user.getCity());
                            tv_content.setText("头像路径："+user.getFigureurl_qq_2());
                            Picasso.with(getActivity()).load(response.getString("figureurl_qq_2")).into(imageView);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    //qq分享
    private void onClickShare() {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,
                "http://blog.csdn.net/DickyQie/article/list/1");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
                "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "切切歆语");
        params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
        mTencent.shareToQQ(getActivity(), params, new BaseUiListener1());
    }
    //回调接口  (成功和失败的相关操作)
    private class BaseUiListener1 implements IUiListener {
        @Override
        public void onComplete(Object response) {
            doComplete(response);
        }

        protected void doComplete(Object values) {
        }

        @Override
        public void onError(UiError e) {
        }

        @Override
        public void onCancel() {
        }
    }

    @SuppressWarnings("unused")
    private void shareToQQzone() {
        try {
            final Bundle params = new Bundle();
            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                    QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
            params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "切切歆语");
            params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "sss");
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
                    "http://blog.csdn.net/DickyQie/article/list/1");
            ArrayList<String> imageUrls = new ArrayList<String>();
            imageUrls.add("http://media-cdn.tripadvisor.com/media/photo-s/01/3e/05/40/the-sandbar-that-links.jpg");
            params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
            params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT,
                    QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
            Tencent mTencent = Tencent.createInstance("1106062414",
                    this.getActivity());
            mTencent.shareToQzone(this.getActivity(), params,
                    new BaseUiListener1());
        } catch (Exception e) {
        }
    }
}

