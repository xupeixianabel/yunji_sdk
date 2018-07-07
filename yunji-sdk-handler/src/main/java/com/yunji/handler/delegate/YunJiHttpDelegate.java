package com.yunji.handler.delegate;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import android.content.Context;
import android.os.Build;

import com.yunji.handler.api.IResultCallback;
import com.yunji.handler.http.HttpCallBack;
import com.yunji.handler.http.HttpFacade;
import com.yunji.handler.http.HttpRequestParams;
import com.yunji.handler.http.IFailTaskHandler;
import com.yunji.handler.log.CommonLog;
import com.yunji.handler.log.LogFactory;
import com.yunji.handler.model.CookieModel;
import com.yunji.handler.utils.CommonConstants;
import com.yunji.handler.vo.RobotInfoVO;

/**
 * @author vincent
 *
 */
public class YunJiHttpDelegate
{

    protected CommonLog logger = LogFactory.createLog(getClass());

    protected HttpFacade httpFacade;

    protected static String HTTP_ROOT_URL = CookieModel.getInstance().getValueByKey(CommonConstants.Config.HTTP_URL);

    protected static String AUTH_HTTP_URL = CookieModel.getInstance().getValueByKey(CommonConstants.Config.AUTH_HTTP_URL);

    protected String USER_AGENT = "YUNJICHINA.COM 1.0(Android{%s}; zh_CN)";

    private IFailTaskHandler failTaskHandler;

    protected String TOKEN_URL = AUTH_HTTP_URL +"/openapi/v1/access/token";

    protected String OPEN_URL = HTTP_ROOT_URL + "/api/doorcontrol/open";

    protected String CLOSE_URL = HTTP_ROOT_URL + "/api/doorcontrol/close";

    protected String SETTARGET_NOBACK_URL = HTTP_ROOT_URL + "/api/settarget_noback";

    protected final String CANCEL_URL = HTTP_ROOT_URL + "/api/cancel";

    protected final String FORCE_CHARGE_URL = HTTP_ROOT_URL + "/api/forcecharge";

    protected final String TASK_STATUS_URL = HTTP_ROOT_URL + "/api/taskstatus";

    protected final String QUERY_MARKER_LIST_URL = HTTP_ROOT_URL + "/api/markers";

    protected final String QUERY_MARKER_POSE_LIST_URL = HTTP_ROOT_URL + "/api/queryMarkerPoseList";

    protected final String SETTARGET_WITH_CALLBACK_URL = HTTP_ROOT_URL + "/api/settarget_with_callback";

    protected final String SETTARGET_NOBACK_WITH_CALLBACK_URL = HTTP_ROOT_URL + "/api/settarget_noback_with_callback";

    protected final String FLOW_URL = HTTP_ROOT_URL + "/api/flow";

    protected final String MOVE_URL = HTTP_ROOT_URL + "/api/move";

    protected final String GET_LANG_URL = HTTP_ROOT_URL + "/api/lang";

    protected final String SET_LANG_URL = HTTP_ROOT_URL + "/api/lang";

    protected final String GET_HOTEL_ID_URL = HTTP_ROOT_URL + "/api/hotelid";

    protected final String GET_AD_ENABLE_URL = HTTP_ROOT_URL + "/api/ad_enable";

    protected final String SET_AD_ENABLE_URL = HTTP_ROOT_URL + "/api/ad_enable";

    protected final String GET_ROBOT_NO_URL = HTTP_ROOT_URL + "/api/robot_no";

    protected final String GET_VOLUME_URL = HTTP_ROOT_URL + "/api/volume";

    protected final String SET_VOLUME_URL = HTTP_ROOT_URL + "/api/volume";

    protected final String GET_VERSION_URL = HTTP_ROOT_URL + "/api/version";

    protected final String PLAY_HANDLER_URL = HTTP_ROOT_URL + "/api/play/";

    protected final String START_CRUISE_URL = HTTP_ROOT_URL + "/api/start_cruise";

    protected final String ROBOT_INFO_URL=HTTP_ROOT_URL + "/api/robot_base_info";

    public YunJiHttpDelegate(Context context)
    {
        httpFacade = createDefaultHttpFacade(true);
        httpFacade.configRequestExecutionRetryCount(8);
        httpFacade.setGobalContentType(HttpFacade.X_WWW_FORM_URLENCODE);
        String devicModel = Build.MODEL;
        int versionSdk = Build.VERSION.SDK_INT;
        String versionRelease = Build.VERSION.RELEASE;
        USER_AGENT = String.format(USER_AGENT, devicModel + ":" + versionSdk + ":" + versionRelease);
        httpFacade.addHeader("user-agent", USER_AGENT);
    }

    public IFailTaskHandler getFailTaskHandler()
    {
        return failTaskHandler;
    }

    public void setFailTaskHandler(IFailTaskHandler failTaskHandler)
    {
        this.failTaskHandler = failTaskHandler;
        httpFacade.setFailTaskHandler(failTaskHandler);
    }

    public void updateTokenHandler(String accessToken)
    {
        httpFacade.addHeader("token", accessToken);
    }

    protected HttpFacade createDefaultHttpFacade(boolean ignoreSSL)
    {
        HttpFacade http = new HttpFacade(ignoreSSL);
        return http;
    }

    /**
     * # 开门(返回只表示发出指令，不代表门开成功，可通过门的状态判断门是否开成功)
     * @param callback
     */
    public void open(HttpCallBack<String> callback)
    {
        httpFacade.getRequestWithQueryModel(OPEN_URL, new HttpRequestParams(), callback);
    }

    /**
     *
     * @param appName
     * @param ts
     * @param sign
     * @param callback
     * @param <T>
     */
    public <T> void register(String appName, String ts, String sign, HttpCallBack<T> callback)
    {
        HttpRequestParams requestParams = new HttpRequestParams();
        requestParams.put("appname", appName);
        requestParams.put("ts", ts);
        requestParams.put("sign", sign);
        httpFacade.getRequestWithQueryModel(TOKEN_URL, requestParams, callback);
    }

    /**
     * ## 关门
     * @param callback
     */
    public void close(HttpCallBack<String> callback)
    {
        httpFacade.getRequestWithQueryModel(CLOSE_URL, new HttpRequestParams(), callback);
    }

    /**
     * ## 发任务(不自动返程，不打电话，任务结束才会返回)
     * @param marker
     * @param callback
     */
    public void excuteTaskWithNoBack(String marker, HttpCallBack<String> callback)
    {
        HttpRequestParams params = new HttpRequestParams();
        params.put("marker", marker);
        httpFacade.getRequestWithPathModel(SETTARGET_NOBACK_URL, params, callback);
    }

    /**
     *## 发任务(自动返程,带回调)
     * @param marker
     * @param startCallback
     * @param successCallback
     * @param faildCallback
     * @param callback
     */
    public void excuteTaskWithCallback(String marker, String startCallback, String successCallback,
                String faildCallback, HttpCallBack<String> callback)
    {
        HttpRequestParams params = new HttpRequestParams();
        params.put("marker", marker);
        String requestURL = params.getRequestString(SETTARGET_WITH_CALLBACK_URL);
        HttpRequestParams finalParams = new HttpRequestParams();
        finalParams.put("start_callback", startCallback);
        finalParams.put("success_callback", successCallback);
        finalParams.put("faild_callback", faildCallback);
        httpFacade.getRequestWithQueryModel(requestURL, finalParams, callback);
    }

    /**
     *## 发任务(不返程,带回调)
     * @param marker
     * @param startCallback
     * @param successCallback
     * @param faildCallback
     * @param callback
     */
    public void excuteTaskNobackWithCallback(String marker, String startCallback, String successCallback,
                String faildCallback, HttpCallBack<String> callback)
    {
        HttpRequestParams params = new HttpRequestParams();
        params.put("marker", marker);
        String requestURL = params.getRequestString(SETTARGET_NOBACK_WITH_CALLBACK_URL);
        HttpRequestParams finalParams = new HttpRequestParams();
        finalParams.put("start_callback", startCallback);
        finalParams.put("success_callback", successCallback);
        finalParams.put("faild_callback", faildCallback);
        httpFacade.getRequestWithQueryModel(requestURL, finalParams, callback);

    }



    /**
     * ## 发任务(不自动返程，不打电话，任务结束才会返回)
     * @param marker
     * @param callback
     */
    public void moveHandler(String marker, HttpCallBack<String> callback)
    {
        HttpRequestParams params = new HttpRequestParams();
        params.put("marker", marker);
        httpFacade.getRequestWithPathModel(MOVE_URL, params, callback);
    }

    /**
     * 取消任务
     * @param callback
     */
    public void cancelTask(HttpCallBack<String> callback)
    {
        httpFacade.getRequestWithQueryModel(CANCEL_URL, new HttpRequestParams(), callback);
    }

    /**
     * 获取机器人状态
     * @param callback
     */
    public void getTaskStatus(HttpCallBack<String> callback)
    {
        httpFacade.getRequestWithQueryModel(TASK_STATUS_URL, new HttpRequestParams(), callback);
    }

    /**
     * ## 回充电桩
     * @param callback
     */
    public void forceCharge(HttpCallBack<String> callback)
    {
        httpFacade.getRequestWithQueryModel(FORCE_CHARGE_URL, new HttpRequestParams(), callback);
    }

    /**
     *## 获取所有点位
     * @param callback
     */
    public void queryMarkerList(HttpCallBack<String> callback)
    {
        httpFacade.getRequestWithQueryModel(QUERY_MARKER_LIST_URL, new HttpRequestParams(), callback);
    }

    public void queryMarkerPoseList(HttpCallBack<String> callback)
    {
        httpFacade.getRequestWithQueryModel(QUERY_MARKER_POSE_LIST_URL, new HttpRequestParams(), callback);
    }

    /**
     *
     * @param taskType
     * @param //array
     *            JsonArray array =
     *            Json.createArrayBuilder().add(1).add(2).build();
     * @param callback
     */

    public void flow(String taskType, JsonArray message, HttpCallBack<String> callback)
    {
        HttpRequestParams params = new HttpRequestParams();
        params.put("taskType", taskType);
        params.put("message", message);
        httpFacade.postHandler(FLOW_URL, params, callback);
    }

    // 获取当前语言
    public void getLangHandler(HttpCallBack<String> callback)
    {
        httpFacade.getRequestWithQueryModel(GET_LANG_URL, new HttpRequestParams(), callback);

    }

    // 设置当前语言
    public void setLangHandler(String lang, HttpCallBack<String> callback)
    {
        HttpRequestParams params = new HttpRequestParams();
        params.put("lang", lang);
        httpFacade.postHandler(SET_LANG_URL, params, callback);
    }

    // 获取当前酒店id
    public void getHotelIdHandler(HttpCallBack<String> callback)
    {
        httpFacade.getRequestWithQueryModel(GET_HOTEL_ID_URL, new HttpRequestParams(), callback);
    }

    // 获取站定语音开关
    public void getAdEnableHandler(HttpCallBack<String> callback)
    {
        httpFacade.getRequestWithQueryModel(GET_AD_ENABLE_URL, new HttpRequestParams(), callback);
    }

    // ## 设置站定语音开关
    public <T> void setAdEnableHandler(Boolean enable, HttpCallBack<String> callback)
    {
        HttpRequestParams httpRequestParams = new HttpRequestParams();
        httpRequestParams.put("enable", enable);
        httpFacade.postHandler(SET_AD_ENABLE_URL, httpRequestParams, callback);
    }

    // 获取机器人编号
    public void getRobotNoHandler(HttpCallBack<String> callback)
    {
        httpFacade.getRequestWithQueryModel(GET_ROBOT_NO_URL, new HttpRequestParams(), callback);
    }

    // 获取声音
    public void getVolumeHandler(HttpCallBack<String> callback)
    {
        httpFacade.getRequestWithQueryModel(GET_VOLUME_URL, new HttpRequestParams(), callback);
    }

    // 设置声音
    public <T> void setVolumeHandler(String dayVolume, String nightVolume, HttpCallBack<String> callback)
    {
        HttpRequestParams httpRequestParams = new HttpRequestParams();
        httpRequestParams.put("day_volume", dayVolume);
        httpRequestParams.put("night_volume", nightVolume);
        httpFacade.postHandler(SET_VOLUME_URL, httpRequestParams, callback);
    }

    // 获取版本
    public <T> void getVersionHandler(HttpCallBack<String> callback)
    {
        httpFacade.getRequestWithQueryModel(GET_VERSION_URL, new HttpRequestParams(), callback);
    }

    // 语音text: 文本，如果中英文混合请用"|"隔开
    public void playHandler(String text, HttpCallBack<String> callback)
    {
        HttpRequestParams params = new HttpRequestParams();
        params.put("text", text);
        httpFacade.getRequestWithPathModel(PLAY_HANDLER_URL, params, callback);
    }

    // 开始巡游

    public <T> void startCruiseHandler(String type, int time, int times, String name, String soundType,
                JsonArray targets, HttpCallBack<String> callback)
    {

        HttpRequestParams params = new HttpRequestParams();
        params.put("type", type);
        params.put("times", times);
        params.put("time",time);
        JsonObject data =
                    Json.createObjectBuilder().add("name", name).add("sound_type", soundType).add("targets", targets)
                                .build();
        params.put("data", data);
        httpFacade.postHandler(START_CRUISE_URL, params, callback);
    }


    //获取机器人基本信息
    public void getRobotInfo(HttpCallBack<String> callback)
    {
        httpFacade.getRequestWithQueryModel(ROBOT_INFO_URL, new HttpRequestParams(), callback);
    }

}
