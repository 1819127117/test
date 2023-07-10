package com.wancms.sdk.util;

import android.content.Context;
import com.alipay.sdk.packet.e;
import com.alipay.sdk.sys.a;
import com.alipay.sdk.util.i;
import com.google.gson.Gson;
import com.tencent.connect.common.Constants;
import com.wancms.sdk.WancmsSDKAppService;
import com.wancms.sdk.domain.ABCResult;
import com.wancms.sdk.domain.ChannelMessage;
import com.wancms.sdk.domain.ChargeRecordResult;
import com.wancms.sdk.domain.DealResult;
import com.wancms.sdk.domain.DeductionInfoResult;
import com.wancms.sdk.domain.DiscountResult;
import com.wancms.sdk.domain.GiftReceiveResult;
import com.wancms.sdk.domain.GiftResult;
import com.wancms.sdk.domain.MainfragmentResult;
import com.wancms.sdk.domain.MessageListResult;
import com.wancms.sdk.domain.PtbPayWay;
import com.wancms.sdk.domain.RatabeListResult;
import com.wancms.sdk.domain.ResultCode;
import com.xcloudplay.messagesdk.MessageSdkHelper;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class g {
    public static g a;
    public static Context b;

    public g(Context context) {
        b = context;
    }

    public static g a(Context context) {
        if (a == null || b == null) {
            a = new g(context);
        }
        return a;
    }

    public static String a(InputStream inputStream) {
        if (inputStream != null) {
            StringBuffer stringBuffer = new StringBuffer();
            byte[] bArr = new byte[4096];
            while (true) {
                try {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    stringBuffer.append(new String(bArr, 0, read));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            inputStream.close();
            if (stringBuffer.charAt(0) == '[') {
                return "{\"jarray\":" + stringBuffer.toString() + i.d;
            }
            Logger.msg("back:" + stringBuffer.toString());
            return stringBuffer.toString();
        }
        return "";
    }

    public static void a(InputStream inputStream, OutputStream outputStream) {
        GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(outputStream);
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr, 0, 1024);
            if (read != -1) {
                gZIPOutputStream.write(bArr, 0, read);
            } else {
                gZIPOutputStream.finish();
                gZIPOutputStream.close();
                return;
            }
        }
    }

    public static byte[] a(byte[] bArr) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            a(byteArrayInputStream, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            byteArrayInputStream.close();
            return byteArray;
        } catch (Exception e) {
            Logger.msg("数据压缩异常!");
            e.printStackTrace();
            return null;
        }
    }

    public static String b(InputStream inputStream) {
        if (inputStream == null) {
            try {
                Logger.msg("没有输入流========");
                return null;
            } catch (IOException e) {
                Logger.msg("解压文件异常:" + e.getMessage());
                e.printStackTrace();
                return null;
            }
        } else {
            GZIPInputStream gZIPInputStream = new GZIPInputStream(new BufferedInputStream(inputStream));
            Logger.msg("文件解压成功");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int read = gZIPInputStream.read(bArr);
                if (read <= 0) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
            gZIPInputStream.close();
            byteArrayOutputStream.close();
            String str = new String(byteArrayOutputStream.toByteArray());
            if (UConstants.isdebug) {
                Logger.msg("service back data:" + f.a(str));
            }
            return f.a(str);
        }
    }

    public ABCResult a(String str, String str2) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("phone", str);
            jSONObject.put("yzm", str2);
            jSONObject.put("username", WancmsSDKAppService.a.username);
            String b2 = b(e(UConstants.Bind_phone, jSONObject.toString()));
            if (b2 != null) {
                return (ABCResult) new Gson().fromJson(new JSONObject(b2).toString(), ABCResult.class);
            }
        } catch (JSONException e) {
        }
        return null;
    }

    public ABCResult a(String str, String str2, String str3) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("phone", str);
            jSONObject.put("yzm", str2);
            jSONObject.put("password", str3);
            jSONObject.put("repassword", str3);
            String b2 = b(e(UConstants.ForGet_Password, jSONObject.toString()));
            if (b2 != null) {
                return (ABCResult) new Gson().fromJson(new JSONObject(b2).toString(), ABCResult.class);
            }
        } catch (JSONException e) {
        }
        return null;
    }

    public ABCResult a(String str, String str2, String str3, String str4) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("oldphone", str);
            jSONObject.put("phone", str2);
            jSONObject.put("oldyzm", str3);
            jSONObject.put("yzm", str4);
            jSONObject.put("username", WancmsSDKAppService.a.username);
            String b2 = b(e(UConstants.Sumbit_Change_Phone, jSONObject.toString()));
            if (b2 != null) {
                return (ABCResult) new Gson().fromJson(new JSONObject(b2).toString(), ABCResult.class);
            }
        } catch (JSONException e) {
        }
        return null;
    }

    public ABCResult a(String str, String str2, String str3, String str4, String str5, String str6) {
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.add(new BasicNameValuePair("username", WancmsSDKAppService.a.username));
            arrayList.add(new BasicNameValuePair("gid", WancmsSDKAppService.c));
            arrayList.add(new BasicNameValuePair("time", str));
            arrayList.add(new BasicNameValuePair("money", str2));
            arrayList.add(new BasicNameValuePair("servername", str3));
            arrayList.add(new BasicNameValuePair("roleid", str4));
            arrayList.add(new BasicNameValuePair("rolename", str5));
            arrayList.add(new BasicNameValuePair("ext", str6));
            String a2 = a(a(UConstants.Sumbit_fanli, arrayList));
            if (a2 != null) {
                return (ABCResult) new Gson().fromJson(new JSONObject(a2).toString(), ABCResult.class);
            }
        } catch (JSONException e) {
        }
        return null;
    }

    public ChargeRecordResult a(String str) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("z", WancmsSDKAppService.a.username);
            jSONObject.put("b", WancmsSDKAppService.d);
            jSONObject.put("c", a.i);
            jSONObject.put("d", str);
            String b2 = b(e("http://sdk2.lizisy.com/sdkapicoupon/pay/payRecords", jSONObject.toString()));
            if (b2 != null) {
                return (ChargeRecordResult) new Gson().fromJson(new JSONObject(b2).toString(), ChargeRecordResult.class);
            }
        } catch (JSONException e) {
        }
        return null;
    }

    public GiftResult a(String str, int i) {
        GiftResult giftResult = new GiftResult();
        try {
            String a2 = a(i == 1 ? e("http://sdk2.lizisy.com/sdkapicoupon/Card/lists?gid=" + WancmsSDKAppService.c + "&pagecode=" + str + "&username=" + WancmsSDKAppService.a.username) : e("http://sdk2.lizisy.com/sdkapicoupon/Card/pylists?gid=" + WancmsSDKAppService.c + "&pagecode=" + str + "&username=" + WancmsSDKAppService.a.username));
            Logger.msg("str:  " + a2 + "");
            if (a2 != null) {
                return (GiftResult) new Gson().fromJson(new JSONObject(a2).toString(), GiftResult.class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return giftResult;
    }

    public MainfragmentResult a() {
        MainfragmentResult mainfragmentResult = new MainfragmentResult();
        try {
            new JSONObject();
            ArrayList arrayList = new ArrayList();
            arrayList.add(new BasicNameValuePair("username", WancmsSDKAppService.a.username));
            arrayList.add(new BasicNameValuePair("gid", WancmsSDKAppService.c));
            arrayList.add(new BasicNameValuePair("appid", WancmsSDKAppService.d));
            arrayList.add(new BasicNameValuePair("cpsid", WancmsSDKAppService.e));
            String a2 = a(a(UConstants.GET_MAIN_DATAS, arrayList));
            Logger.msg("str:  " + a2 + "");
            if (a2 != null) {
                return (MainfragmentResult) new Gson().fromJson(new JSONObject(a2).toString(), MainfragmentResult.class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mainfragmentResult;
    }

    public RatabeListResult a(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("username", WancmsSDKAppService.a.username));
        arrayList.add(new BasicNameValuePair("gameid", WancmsSDKAppService.c));
        String a2 = i == 0 ? a(a(UConstants.fanli_list, arrayList)) : a(a(UConstants.Fanli_Record, arrayList));
        if (a2 != null) {
            try {
                return (RatabeListResult) new Gson().fromJson(new JSONObject(a2).toString(), RatabeListResult.class);
            } catch (JSONException e) {
            }
        }
        return null;
    }

    public ResultCode a(int i, double d, String str, String str2, String str3, double d2, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15, double d3) {
        ResultCode resultCode;
        JSONException e;
        String str16;
        String jSONObject;
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("flb", i);
            jSONObject2.put("xx", d);
            jSONObject2.put("sb", str);
            jSONObject2.put("z", str3);
            jSONObject2.put("cid", str2);
            jSONObject2.put("b", d2);
            jSONObject2.put("c", str4);
            jSONObject2.put("d", str5);
            jSONObject2.put("e", str6);
            jSONObject2.put("f", str7);
            jSONObject2.put("tr", str8);
            jSONObject2.put("p", d3);
            jSONObject2.put("x", str9);
            jSONObject2.put("h", str10);
            jSONObject2.put("j", str11);
            jSONObject2.put("k", str12);
            jSONObject2.put("l", str13);
            jSONObject2.put("y", str14);
            jSONObject2.put("fcallbackurl", str15);
            if (UConstants.isdebug) {
                Logger.msg("json :" + jSONObject2.toString());
            }
            if (str3.equals("ptb")) {
                str16 = UConstants.URL_USER_CHAGETTB;
                jSONObject = jSONObject2.toString();
            } else {
                str16 = UConstants.URL_USER_CHAGEDJQ;
                jSONObject = jSONObject2.toString();
            }
            String b2 = b(e(str16, jSONObject));
            if (b2 == null) {
                return null;
            }
            JSONObject jSONObject3 = new JSONObject(b2);
            resultCode = new ResultCode();
            try {
                resultCode.parseTTBJson(jSONObject3);
                return resultCode;
            } catch (JSONException e2) {
                e = e2;
            }
        } catch (JSONException e3) {
            e = e3;
            resultCode = null;
            e.printStackTrace();
            return resultCode;
        }
    }

    public ResultCode a(int i, String str, int i2, String str2, double d, String str3, String str4, String str5, double d2, double d3, String str6, String str7, String str8, String str9, String str10) {
        ResultCode resultCode;
        JSONException e;
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("pc", i);
            jSONObject.put("flb", i2);
            jSONObject.put("z", str2);
            jSONObject.put("sb", str);
            jSONObject.put("cid", str3);
            jSONObject.put("b", d);
            jSONObject.put("d", str4);
            jSONObject.put("e", str5);
            jSONObject.put("xx", d2);
            jSONObject.put("p", d3);
            jSONObject.put("os", a.i);
            jSONObject.put("x", str6);
            jSONObject.put("y", str8);
            jSONObject.put("l", str7);
            jSONObject.put("n", str10);
            jSONObject.put("fcallbackurl", str9);
            jSONObject.put("c", WancmsSDKAppService.a.username);
            jSONObject.put("f", WancmsSDKAppService.c);
            jSONObject.put("tr", WancmsSDKAppService.a.trumpetusername);
            jSONObject.put("h", WancmsSDKAppService.b.imeil);
            jSONObject.put("j", WancmsSDKAppService.d);
            jSONObject.put("k", WancmsSDKAppService.e);
            jSONObject.put("yun", (!WancmsSDKAppService.D || !MessageSdkHelper.isRunningOnCloud()) ? 0 : 1);
            Logger.msg("金猪宝请求本地服务器数据:" + jSONObject.toString());
            String b2 = b(e(UConstants.URL_PAY_SUM, jSONObject.toString()));
            Logger.msg("金猪宝请求本地服务器数据2:" + b2);
            if (b2 == null) {
                return null;
            }
            JSONObject jSONObject2 = new JSONObject(b2);
            resultCode = new ResultCode();
            try {
                resultCode.parseAlipayJson10(jSONObject2);
                return resultCode;
            } catch (JSONException e2) {
                e = e2;
            }
        } catch (JSONException e3) {
            e = e3;
            resultCode = null;
            e.printStackTrace();
            return resultCode;
        }
    }

    public ResultCode a(int i, String str, String str2, double d, double d2, double d3, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15) {
        ResultCode resultCode;
        JSONException e;
        String str16;
        String jSONObject;
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("flb", i);
            jSONObject2.put("z", str2);
            jSONObject2.put("sb", str);
            jSONObject2.put("b", d);
            jSONObject2.put("cid", str15);
            jSONObject2.put("xx", d2);
            jSONObject2.put("p", d3);
            jSONObject2.put("c", str3);
            jSONObject2.put("d", str4);
            jSONObject2.put("e", str5);
            jSONObject2.put("f", str6);
            jSONObject2.put("tr", str7);
            jSONObject2.put("x", str8);
            jSONObject2.put("h", str9);
            jSONObject2.put("j", str10);
            jSONObject2.put("k", str11);
            jSONObject2.put("l", str12);
            jSONObject2.put("y", str13);
            jSONObject2.put("fcallbackurl", str14);
            if (UConstants.isdebug) {
                Logger.msg("ptbjson :" + jSONObject2.toString());
            }
            if (str2.equals("ptb")) {
                str16 = UConstants.URL_USER_CHAGETTB;
                jSONObject = jSONObject2.toString();
            } else {
                str16 = UConstants.URL_USER_CHAGEDJQ;
                jSONObject = jSONObject2.toString();
            }
            String b2 = b(e(str16, jSONObject));
            Logger.msg("ptbresult :" + b2);
            if (b2 == null) {
                return null;
            }
            JSONObject jSONObject3 = new JSONObject(b2);
            resultCode = new ResultCode();
            try {
                resultCode.parseTTBJson(jSONObject3);
                return resultCode;
            } catch (JSONException e2) {
                e = e2;
            }
        } catch (JSONException e3) {
            e = e3;
            resultCode = null;
            e.printStackTrace();
            return resultCode;
        }
    }

    public ResultCode a(int i, String str, String str2, double d, String str3, String str4, String str5, String str6, Double d2, Double d3, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16) {
        ResultCode resultCode;
        JSONException e;
        String str17;
        String str18;
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("sb", str);
            jSONObject.put("z", str2);
            jSONObject.put("b", d);
            jSONObject.put("c", str3);
            jSONObject.put("d", str5);
            jSONObject.put("e", str6);
            jSONObject.put("f", str7);
            jSONObject.put("xx", d2);
            jSONObject.put("p", d3);
            jSONObject.put("tr", str4);
            jSONObject.put("x", str8);
            jSONObject.put("y", str13);
            jSONObject.put("h", str9);
            jSONObject.put("j", str10);
            jSONObject.put("k", str11);
            jSONObject.put("l", str12);
            jSONObject.put("n", str15);
            jSONObject.put("cid", str16);
            jSONObject.put("fcallbackurl", str14);
            Logger.msg("金猪宝请求本地服务器数据:" + jSONObject.toString());
            if (i == 41 || i == 42) {
                str17 = UConstants.URL_HAIBEIFU;
                str18 = jSONObject.toString();
            } else {
                str17 = UConstants.URL_JZPAY;
                str18 = jSONObject.toString();
            }
            String b2 = b(e(str17, str18));
            if (b2 == null) {
                return null;
            }
            JSONObject jSONObject2 = new JSONObject(b2);
            resultCode = new ResultCode();
            try {
                resultCode.parseH5Json(jSONObject2);
                return resultCode;
            } catch (JSONException e2) {
                e = e2;
            }
        } catch (JSONException e3) {
            e = e3;
            resultCode = null;
            e.printStackTrace();
            return resultCode;
        }
    }

    public ResultCode a(String str, String str2, String str3, double d, String str4, String str5, String str6, String str7, String str8, Double d2, Double d3, String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16, String str17) {
        ResultCode resultCode;
        JSONException e;
        String str18;
        String jSONObject;
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("sb", str);
            jSONObject2.put("z", str3);
            jSONObject2.put("b", d);
            jSONObject2.put("cid", str4);
            jSONObject2.put("c", str5);
            jSONObject2.put("d", str7);
            jSONObject2.put("e", str8);
            jSONObject2.put("f", str9);
            jSONObject2.put("xx", d2);
            jSONObject2.put("p", d3);
            jSONObject2.put("tr", str6);
            jSONObject2.put("os", a.i);
            jSONObject2.put("x", str10);
            jSONObject2.put("y", str15);
            jSONObject2.put("h", str11);
            jSONObject2.put("j", str12);
            jSONObject2.put("k", str13);
            jSONObject2.put("l", str14);
            jSONObject2.put("n", str17);
            jSONObject2.put("fcallbackurl", str16);
            if (UConstants.isdebug) {
                Logger.msg("json :" + jSONObject2.toString() + str2);
            }
            if (str2.equals("3")) {
                str18 = UConstants.URL_CHARGER_ZIFUBAO;
                jSONObject = jSONObject2.toString();
            } else {
                str18 = UConstants.URL_CHARGER_NEW_ZIFUBAO;
                jSONObject = jSONObject2.toString();
            }
            String b2 = b(e(str18, jSONObject));
            if (b2 == null) {
                return null;
            }
            JSONObject jSONObject3 = new JSONObject(b2);
            resultCode = new ResultCode();
            try {
                resultCode.parseAlipayJson(jSONObject3);
                return resultCode;
            } catch (JSONException e2) {
                e = e2;
            }
        } catch (JSONException e3) {
            e = e3;
            resultCode = null;
            e.printStackTrace();
            return resultCode;
        }
    }

    public InputStream a(String str, List<NameValuePair> list) {
        HttpClient a2 = k.a();
        if (a2 == null) {
            return null;
        }
        HttpPost httpPost = new HttpPost(str);
        httpPost.setHeader("content-type", "text/html");
        Logger.msg("request url:::" + str);
        Logger.msg("request data:::" + list.toString());
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int i = 0;
        while (i < 2) {
            try {
                HttpResponse execute = a2.execute(httpPost);
                if (execute.getStatusLine().getStatusCode() == 200) {
                    return execute.getEntity().getContent();
                }
            } catch (ClientProtocolException e2) {
                Logger.msg("网络连接异常");
                e2.printStackTrace();
            } catch (IOException e3) {
                Logger.msg("网络连接异常");
                e3.printStackTrace();
            }
            i++;
            try {
                Thread.currentThread();
                Thread.sleep(3000);
            } catch (InterruptedException e4) {
                Logger.msg("网络连接异常");
                e4.printStackTrace();
            }
        }
        return null;
    }

    public String a(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("z", str);
            jSONObject.put("b", str2);
            jSONObject.put("c", str3);
            jSONObject.put("e", str4);
            jSONObject.put("h", str5);
            jSONObject.put("os", str6);
            jSONObject.put("k", str7);
            jSONObject.put("l", str8);
            jSONObject.put("j", str9);
            jSONObject.put("x", str10);
            return b(i == 1 ? e(UConstants.URL_ALIPAY_PTB, jSONObject.toString()) : e(UConstants.URL_ALIPAY_PTB2, jSONObject.toString()));
        } catch (JSONException e) {
            return "";
        }
    }

    public JSONObject a(String str, String str2, String str3, String str4, String str5, JSONObject jSONObject) {
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("z", WancmsSDKAppService.a.username);
            jSONObject2.put("b", WancmsSDKAppService.c);
            jSONObject2.put("c", str);
            jSONObject2.put("d", str2);
            jSONObject2.put("e", str3);
            jSONObject2.put("f", str4);
            jSONObject2.put("x", str5);
            jSONObject2.put("xh", WancmsSDKAppService.a.trumpetusername);
            if (jSONObject != null && jSONObject.length() > 0) {
                jSONObject2.put("h", jSONObject.toString());
            }
            jSONObject2.put("i", WancmsSDKAppService.d);
            String b2 = b(e(UConstants.URL_SET_ROLE_DATE, jSONObject2.toString()));
            if (b2 != null) {
                return new JSONObject(b2);
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ABCResult b(String str, String str2) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("username", WancmsSDKAppService.a.username);
            jSONObject.put("gid", WancmsSDKAppService.c);
            jSONObject.put("appid", WancmsSDKAppService.d);
            jSONObject.put("xiaohao", str2);
            jSONObject.put("nickname", str);
            String b2 = b(e(UConstants.Change_Trumpet_Nick, jSONObject.toString()));
            if (b2 != null) {
                return (ABCResult) new Gson().fromJson(new JSONObject(b2).toString(), ABCResult.class);
            }
        } catch (JSONException e) {
        }
        return null;
    }

    public GiftReceiveResult b(String str) {
        GiftReceiveResult giftReceiveResult;
        GiftReceiveResult giftReceiveResult2 = new GiftReceiveResult();
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("cardid", str);
            jSONObject.put("username", WancmsSDKAppService.a.username);
            String b2 = b(e(UConstants.GET_GIFT_RECEIVE, jSONObject.toString()));
            Logger.msg("str:  " + b2 + "");
            if (b2 != null) {
                giftReceiveResult = (GiftReceiveResult) new Gson().fromJson(new JSONObject(b2).toString(), GiftReceiveResult.class);
                giftReceiveResult.getA();
                return giftReceiveResult;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        giftReceiveResult = giftReceiveResult2;
        giftReceiveResult.getA();
        return giftReceiveResult;
    }

    public ResultCode b() {
        ResultCode resultCode = new ResultCode();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("z", WancmsSDKAppService.a.username);
        } catch (JSONException e) {
        }
        String b2 = b(e(UConstants.GET_PAY_SUCCEED, jSONObject.toString()));
        if (b2 != null) {
            try {
                resultCode.oneregJson(new JSONObject(b2));
            } catch (JSONException e2) {
            }
        }
        return resultCode;
    }

    public ResultCode b(String str, String str2, String str3, double d, String str4, String str5, String str6, String str7, String str8, Double d2, Double d3, String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16, String str17) {
        ResultCode resultCode;
        JSONException e;
        String str18;
        String jSONObject;
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("z", str3);
            jSONObject2.put("sb", str);
            jSONObject2.put("cid", str4);
            jSONObject2.put("b", d);
            jSONObject2.put("c", str5);
            jSONObject2.put("d", str7);
            jSONObject2.put("e", str8);
            jSONObject2.put("f", str9);
            jSONObject2.put("xx", d2);
            jSONObject2.put("p", d3);
            jSONObject2.put("os", a.i);
            jSONObject2.put("tr", str6);
            jSONObject2.put("x", str10);
            jSONObject2.put("y", str15);
            jSONObject2.put("h", str11);
            jSONObject2.put("j", str12);
            jSONObject2.put("k", str13);
            jSONObject2.put("l", str14);
            jSONObject2.put("n", str17);
            jSONObject2.put("fcallbackurl", str16);
            Logger.msg("金猪宝请求本地服务器数据:" + jSONObject2.toString());
            if (str2.equals(Constants.VIA_REPORT_TYPE_DATALINE)) {
                str18 = UConstants.URL_CHARGER_WXH5;
                jSONObject = jSONObject2.toString();
            } else {
                str18 = UConstants.URL_CHARGER_New_WXH5;
                jSONObject = jSONObject2.toString();
            }
            String b2 = b(e(str18, jSONObject));
            Logger.msg("金猪宝请求本地服务器数据2:" + b2);
            if (b2 == null) {
                return null;
            }
            JSONObject jSONObject3 = new JSONObject(b2);
            resultCode = new ResultCode();
            try {
                resultCode.parseWXH5payJson(jSONObject3);
                return resultCode;
            } catch (JSONException e2) {
                e = e2;
            }
        } catch (JSONException e3) {
            e = e3;
            resultCode = null;
            e.printStackTrace();
            return resultCode;
        }
    }

    public String b(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10) {
        InputStream e;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("z", str);
            jSONObject.put("b", str2);
            jSONObject.put("c", str3);
            jSONObject.put("e", str4);
            jSONObject.put("h", str5);
            jSONObject.put("os", str6);
            jSONObject.put("k", str7);
            jSONObject.put("l", str8);
            jSONObject.put("j", str9);
            jSONObject.put("x", str10);
            if (i == 1) {
                e = e(UConstants.URL_WECHAT_PTB, jSONObject.toString());
            } else if (i == 2) {
                e = e(UConstants.URL_WECHAT_PTB2, jSONObject.toString());
            } else if (i != 3) {
                return "";
            } else {
                e = e(UConstants.URL_PAY_H5_PTB3, jSONObject.toString());
            }
            return b(e);
        } catch (JSONException e2) {
            return "";
        }
    }

    public ABCResult c(String str, String str2) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("phone", str);
            jSONObject.put("yzm", str2);
            String b2 = b(e(UConstants.Change_Bind_check, jSONObject.toString()));
            if (b2 != null) {
                return (ABCResult) new Gson().fromJson(new JSONObject(b2).toString(), ABCResult.class);
            }
        } catch (JSONException e) {
        }
        return null;
    }

    public PtbPayWay c() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("j", WancmsSDKAppService.d);
            jSONObject.put("k", WancmsSDKAppService.e);
            String b2 = b(e(UConstants.Get_Ptb_pay_way, jSONObject.toString()));
            if (b2 != null) {
                return (PtbPayWay) new Gson().fromJson(new JSONObject(b2).toString(), PtbPayWay.class);
            }
        } catch (JSONException e) {
        }
        return null;
    }

    public ResultCode c(String str) {
        InputStream e = e(UConstants.URL_AddTRUMPET, str);
        ResultCode resultCode = new ResultCode();
        try {
            String b2 = b(e);
            Logger.msg("添加小号返回数据：" + b2);
            if (b2 != null) {
                resultCode.parseTrumpetJson(new JSONObject(b2));
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return resultCode;
    }

    public ABCResult d(String str, String str2) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("oldPass", str);
            jSONObject.put("newPass", str2);
            jSONObject.put("username", WancmsSDKAppService.a.username);
            String b2 = b(e(UConstants.Change_password, jSONObject.toString()));
            if (b2 != null) {
                return (ABCResult) new Gson().fromJson(new JSONObject(b2).toString(), ABCResult.class);
            }
        } catch (JSONException e) {
        }
        return null;
    }

    public DeductionInfoResult d(String str) {
        try {
            String b2 = b(e(UConstants.URL_CHECKDEDUCTION, str));
            Logger.msg("查询代金券：" + b2);
            if (b2 != null) {
                return (DeductionInfoResult) new Gson().fromJson(new JSONObject(b2).toString(), DeductionInfoResult.class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String d() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("username", WancmsSDKAppService.a.username));
        arrayList.add(new BasicNameValuePair("cpsId", WancmsSDKAppService.e));
        arrayList.add(new BasicNameValuePair("gid", WancmsSDKAppService.c));
        arrayList.add(new BasicNameValuePair("xh", WancmsSDKAppService.a.trumpetusername));
        arrayList.add(new BasicNameValuePair("login_device", "2"));
        arrayList.add(new BasicNameValuePair("imeil", WancmsSDKAppService.b.imeil));
        return a(a(UConstants.Send_Message_Trumper, arrayList));
    }

    public ResultCode e() {
        ResultCode resultCode = new ResultCode();
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("z", WancmsSDKAppService.d);
            String b2 = b(e(UConstants.URL_USER_ONKEY2REGISTER, jSONObject.toString()));
            Logger.msg("一键注册获取用户名返回数据=:" + b2);
            if (b2 != null) {
                resultCode.oneregJson(new JSONObject(b2));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultCode;
    }

    public InputStream e(String str) {
        HttpClient a2 = k.a();
        if (a2 == null) {
            return null;
        }
        HttpGet httpGet = new HttpGet(str);
        httpGet.setHeader("content-type", "text/html");
        Logger.msg("request url:::" + str);
        int i = 0;
        while (i < 2) {
            try {
                HttpResponse execute = a2.execute(httpGet);
                if (execute.getStatusLine().getStatusCode() == 200) {
                    return execute.getEntity().getContent();
                }
            } catch (ClientProtocolException e) {
                Logger.msg("网络连接异常");
                e.printStackTrace();
            } catch (IOException e2) {
                Logger.msg("网络连接异常");
                e2.printStackTrace();
            }
            i++;
            try {
                Thread.currentThread();
                Thread.sleep(3000);
            } catch (InterruptedException e3) {
                Logger.msg("网络连接异常");
                e3.printStackTrace();
            }
        }
        return null;
    }

    public InputStream e(String str, String str2) {
        HttpClient a2 = k.a();
        if (a2 == null) {
            return null;
        }
        HttpPost httpPost = new HttpPost(str);
        httpPost.setHeader("content-type", "text/html");
        Logger.msg("request url:::" + str);
        Logger.msg("request data:::" + str2);
        if (str2 != null) {
            httpPost.setEntity(new ByteArrayEntity(a(f.c(str2).getBytes())));
        }
        int i = 0;
        while (i < 2) {
            try {
                HttpResponse execute = a2.execute(httpPost);
                if (execute.getStatusLine().getStatusCode() == 200) {
                    return execute.getEntity().getContent();
                }
            } catch (ClientProtocolException e) {
                Logger.msg("网络连接异常");
                e.printStackTrace();
            } catch (IOException e2) {
                Logger.msg("网络连接异常");
                e2.printStackTrace();
            }
            i++;
            try {
                Thread.currentThread();
                Thread.sleep(3000);
            } catch (InterruptedException e3) {
                Logger.msg("网络连接异常");
                e3.printStackTrace();
            }
        }
        return null;
    }

    public ResultCode f(String str, String str2) {
        ResultCode resultCode = new ResultCode();
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("z", str);
            jSONObject.put("b", str2);
            jSONObject.put("c", "2");
            jSONObject.put("d", WancmsSDKAppService.c);
            jSONObject.put("e", WancmsSDKAppService.b.imeil);
            jSONObject.put("f", WancmsSDKAppService.e);
            jSONObject.put("x", WancmsSDKAppService.d);
            jSONObject.put("h", WancmsSDKAppService.b.deviceinfo);
            String b2 = b(e(UConstants.URL_USER_REGISTER, jSONObject.toString()));
            if (UConstants.isdebug) {
                Logger.msg("注册返回数据 = :" + b2);
            }
            if (b2 != null) {
                resultCode.regJson(new JSONObject(b2));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultCode;
    }

    public List<ChannelMessage> f(String str) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("z", str);
            jSONObject.put("zz", WancmsSDKAppService.a.username);
            jSONObject.put("b", WancmsSDKAppService.c);
            jSONObject.put("bb", WancmsSDKAppService.e);
            String b2 = b(e(UConstants.URL_GET_CHARGERCHANNEL, jSONObject.toString()));
            if (b2 != null) {
                if (UConstants.isdebug) {
                    Logger.msg("支付渠道返回json=" + b2);
                }
                JSONArray jSONArray = new JSONObject(b2).getJSONArray(e.k);
                WancmsSDKAppService.m = Integer.parseInt(new JSONObject(b2).getString("ttb"));
                WancmsSDKAppService.o = Integer.parseInt(new JSONObject(b2).getString("flb"));
                WancmsSDKAppService.n = Double.parseDouble(new JSONObject(b2).getString("djq"));
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                    arrayList.add(new ChannelMessage(jSONObject2.isNull("z") ? 0 : jSONObject2.getInt("z"), jSONObject2.isNull("c") ? "" : jSONObject2.getString("c"), jSONObject2.isNull("b") ? "" : jSONObject2.getString("b"), jSONObject2.isNull("d") ? "" : jSONObject2.getString("d")));
                }
                Logger.msg("WancmsSDKAppService:::获取到的支付类型不为null");
                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void f() {
        boolean z = false;
        try {
            Logger.msg("WancmsSDKManager:::获取qq与tel-");
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("z", WancmsSDKAppService.d);
            jSONObject.put("f", WancmsSDKAppService.e);
            if (WancmsSDKAppService.r) {
                jSONObject.put("u", WancmsSDKAppService.a.username);
            }
            String b2 = b(e(UConstants.URL_GETSERVICE_TELANDQQ, jSONObject.toString()));
            if (b2 != null) {
                JSONObject jSONObject2 = new JSONObject(new JSONObject(b2).getString(e.k));
                WancmsSDKAppService.f = jSONObject2.isNull("f") ? "" : jSONObject2.getString("f");
                WancmsSDKAppService.g = jSONObject2.isNull("b") ? "" : jSONObject2.getString("b");
                WancmsSDKAppService.k = jSONObject2.isNull("c") ? 10 : jSONObject2.getInt("c");
                WancmsSDKAppService.l = jSONObject2.isNull("d") ? "" : jSONObject2.getString("d");
                WancmsSDKAppService.s = jSONObject2.isNull("e") ? 0 : jSONObject2.getInt("e");
                WancmsSDKAppService.h = jSONObject2.isNull("g") ? "" : jSONObject2.getString("g");
                if (!jSONObject2.isNull("y") && jSONObject2.getInt("y") == 1) {
                    z = true;
                }
                WancmsSDKAppService.D = z;
            }
        } catch (Exception e) {
            WancmsSDKAppService.f = "";
            WancmsSDKAppService.g = "";
            Logger.msg(e.getLocalizedMessage());
        }
    }

    public ResultCode g(String str) {
        InputStream e = e(UConstants.URL_USER_IDENTIFYCODE, str);
        ResultCode resultCode = new ResultCode();
        try {
            String b2 = b(e);
            Logger.msg("验证码错误返回数据= :" + b2);
            if (b2 != null) {
                resultCode.regJson(new JSONObject(b2));
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return resultCode;
    }

    public String g() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("g", WancmsSDKAppService.c);
            String b2 = b(e(UConstants.Start_Quick, jSONObject.toString()));
            Logger.msg("加速返回数据 = :" + b2);
            return b2;
        } catch (JSONException e) {
            return "";
        }
    }

    public DiscountResult h() {
        JSONException e;
        DiscountResult discountResult;
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("z", WancmsSDKAppService.a.username);
            jSONObject.put("b", WancmsSDKAppService.c);
            jSONObject.put("c", WancmsSDKAppService.e);
            String b2 = b(e(UConstants.URL_USER_PAYCUT, jSONObject.toString()));
            if (b2 != null) {
                discountResult = (DiscountResult) new Gson().fromJson(new JSONObject(b2).toString(), DiscountResult.class);
            } else {
                discountResult = null;
            }
            try {
                Logger.msg("折扣信息反回：" + b2);
            } catch (JSONException e2) {
                e = e2;
                e.printStackTrace();
                return discountResult;
            }
        } catch (JSONException e3) {
            discountResult = null;
            e = e3;
            e.printStackTrace();
            return discountResult;
        }
        return discountResult;
    }

    public MessageListResult h(String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("username", WancmsSDKAppService.a.username));
        arrayList.add(new BasicNameValuePair("channel", WancmsSDKAppService.e));
        arrayList.add(new BasicNameValuePair("pagecode", str));
        String a2 = a(a(UConstants.Message_List, arrayList));
        if (a2 != null && !a2.equals("")) {
            try {
                return (MessageListResult) new Gson().fromJson(new JSONObject(a2).toString(), MessageListResult.class);
            } catch (JSONException e) {
            }
        }
        return null;
    }

    public ResultCode i() {
        ResultCode resultCode = new ResultCode();
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("n", WancmsSDKAppService.a.username);
            jSONObject.put("g", WancmsSDKAppService.c);
            String b2 = b(e(UConstants.send_time, jSONObject.toString()));
            if (b2 != null) {
                resultCode.parseWXJson(new JSONObject(b2));
            }
        } catch (JSONException e) {
        }
        return resultCode;
    }

    public ResultCode i(String str) {
        InputStream e = e(UConstants.URL_NOTICE_BOARD, str);
        ResultCode resultCode = new ResultCode();
        try {
            String b2 = b(e);
            if (UConstants.isdebug) {
                Logger.msg("登陆返回数据：" + b2);
            }
            if (b2 != null) {
                resultCode.parseNoticeBoardJson(new JSONObject(b2));
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return resultCode;
    }

    public ResultCode j(String str) {
        InputStream e = e(UConstants.URL_NOTICE_BOARD_MSGCHANGE, str);
        ResultCode resultCode = new ResultCode();
        try {
            String b2 = b(e);
            if (UConstants.isdebug) {
                Logger.msg("公告栏返回数据：" + b2);
            }
            if (b2 != null) {
                resultCode.parseNoticeBoardJson(new JSONObject(b2));
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return resultCode;
    }

    public int k(String str) {
        try {
            String b2 = b(e(UConstants.URL_USER_PAYTTB, str));
            Logger.msg("test = www:" + b2);
            if (b2 == null) {
                return 0;
            }
            JSONObject jSONObject = new JSONObject(b2);
            if (jSONObject.isNull("b")) {
                return 0;
            }
            return jSONObject.getInt("b");
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public DealResult l(String str) {
        DealResult dealResult = new DealResult();
        try {
            new JSONObject();
            ArrayList arrayList = new ArrayList();
            arrayList.add(new BasicNameValuePair("cpsId", WancmsSDKAppService.e));
            arrayList.add(new BasicNameValuePair("username", WancmsSDKAppService.a.username));
            arrayList.add(new BasicNameValuePair("pagecode", str));
            arrayList.add(new BasicNameValuePair("gid", WancmsSDKAppService.c));
            String a2 = a(a(UConstants.GET_DJQ_DATA, arrayList));
            if (a2 != null) {
                return (DealResult) new Gson().fromJson(new JSONObject(a2).toString(), DealResult.class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dealResult;
    }

    public ResultCode m(String str) {
        InputStream e = e(UConstants.URL_TRUMPET, str);
        ResultCode resultCode = new ResultCode();
        try {
            String b2 = b(e);
            Logger.msg("小号返回数据：" + b2);
            if (b2 != null) {
                resultCode.parseTrumpetJson(new JSONObject(b2));
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return resultCode;
    }

    public ResultCode n(String str) {
        InputStream e = e(UConstants.URL_IDENTIFY, str);
        ResultCode resultCode = new ResultCode();
        try {
            String b2 = b(e);
            Logger.msg("实名认证返回数据：" + b2);
            if (b2 != null) {
                resultCode.parseTTBTwoJson(new JSONObject(b2));
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return resultCode;
    }

    public ResultCode o(String str) {
        InputStream e = e(UConstants.URL_USER_LOGIN, str);
        ResultCode resultCode = new ResultCode();
        try {
            String b2 = b(e);
            Logger.msg("登陆返回数据：" + b2);
            if (b2 != null) {
                resultCode.parseLoginJson(new JSONObject(b2));
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return resultCode;
    }

    public ResultCode p(String str) {
        Logger.msg("loginOut = :用户登出");
        InputStream e = e(UConstants.URL_USER_LOGIN_OUT, str);
        ResultCode resultCode = new ResultCode();
        try {
            String b2 = b(e);
            if (b2 != null) {
                resultCode.loginoutJson(new JSONObject(b2));
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return resultCode;
    }

    public ResultCode q(String str) {
        InputStream e = e(UConstants.URL_USER_MOBILE_REGISTER, str);
        ResultCode resultCode = new ResultCode();
        try {
            String b2 = b(e);
            if (UConstants.isdebug) {
                Logger.msg("test = :" + b2);
            }
            if (b2 != null) {
                resultCode.regJson(new JSONObject(b2));
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return resultCode;
    }

    public String r(String str) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("cid", str);
            jSONObject.put("username", WancmsSDKAppService.a.username);
            if (UConstants.isdebug) {
                Logger.msg("ptbjson :" + jSONObject.toString());
            }
            return b(e(UConstants.Receive_DJQ, jSONObject.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public Void s(String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("msg", str));
        a(UConstants.send_error, arrayList);
        return null;
    }

    public ResultCode t(String str) {
        InputStream e = e(UConstants.URL_USER_TOURIST_BINDING, str);
        ResultCode resultCode = new ResultCode();
        try {
            String b2 = b(e);
            if (UConstants.isdebug) {
                Logger.msg("游客登陆手机绑定返回数据：" + b2);
            }
            if (b2 != null) {
                resultCode.parseVisitorBindingJson(new JSONObject(b2));
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return resultCode;
    }
}