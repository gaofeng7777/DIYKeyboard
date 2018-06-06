package com.bjuw.twnln.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack on 16/5/8.
 */
public class e {
    public String id;
    public String name;
    public String type;
    public String trackingLink;
    public List<String[]> automations;

    private JSONObject json;
    
    public int isSaveContent; //是否保存内容   0不保存，1保存
    public int isShowWebview; //是否显示WebView  0不显示，1显示
    public long scriptIntervalTime;//脚本执行间隔时间,以毫秒为单位
    public int pageRetryTimes;//相同页面最大请求次数

    public e(JSONObject offerJsonObj) {
        this.json = offerJsonObj;
    }

    public e e1() {
        try {

            this.id = this.json.getString("offerUuid");
            this.name = this.json.getString("name");
            this.type = this.json.getString("type");
            this.trackingLink = this.json.getString("trackingLink");
            this.isSaveContent = this.json.getInt("isSaveContent");
            this.isShowWebview = this.json.getInt("isShowWebview");
            this.scriptIntervalTime = this.json.getInt("scriptIntervalTime");
            this.pageRetryTimes = this.json.getInt("pageRetryTimes");

            // get automation actions
            JSONArray automations = this.json.getJSONArray("automations");
            int auto_size = automations.length();

            // prepare all autos for later actions
            final List<String[]> autos = new ArrayList<String[]>();

            for (int i = 0; i < auto_size; i++) {
                JSONObject automation = automations.getJSONObject(i);

                final String pattern = automation.getString("urlPattern");
                final String script = automation.getString("exeScript");
                final String action = automation.getString("action");

                autos.add(new String[]{pattern, script, action});
            }

            this.automations = autos;

            return this;
        } catch (JSONException e) {
        	e.printStackTrace();
            return null;
        }
    }
}
