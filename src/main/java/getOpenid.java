import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

@WebServlet("/openId")
public class getOpenid extends HttpServlet {
    HashMap map=new HashMap();//保存openId
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path="https://api.weixin.qq.com/sns/jscode2session?appid="+req.getParameter("appid")+"&secret="+req.getParameter("secret");
        JSONObject jsonObject=new JSONObject();
        //创建模拟客户端
        CloseableHttpClient httpClient= HttpClientBuilder.create().build();
        //创建GET请求类对象
        HttpGet httpGet=new HttpGet(path+"&js_code="+req.getParameter("js_code"));
        //响应类
        CloseableHttpResponse response=httpClient.execute(httpGet);
        //获得返回实体
        HttpEntity entiry=response.getEntity();
        resp.setContentType("applcation/json;charset=utf-8");
        //登录 保存openId
        if(req.getParameter("func").equals("login")){
            map.put(req.getParameter("openId"),req.getParameter("openId"));
            map.put(req.getParameter("openId")+"sid",req.getParameter("sid"));
            map.put(req.getParameter("openId")+"sname",req.getParameter("sname"));
            jsonObject.put("status","200");
            jsonObject.put("msg","返回成功");
//            jsonObject.put("data",EntityUtils.toString(entiry));
            jsonObject.put("dataMap",map);
            resp.getWriter().write(jsonObject.toString());
        }
        //每次访问获得openID
        if(req.getParameter("func").equals("getOpenId")){
            jsonObject.put("status","200");
            jsonObject.put("msg","返回成功");
            jsonObject.put("data",EntityUtils.toString(entiry));
            resp.getWriter().write(jsonObject.toString());
        }
        //是否为第一次登录
        if(req.getParameter("func").equals("isFirst")){
            if(map.keySet().contains(req.getParameter("openId"))==true){
                jsonObject.put("status","200");
                jsonObject.put("msg","返回成功");
                jsonObject.put("sid",map.get(req.getParameter("openId")+"sid"));
                jsonObject.put("sname",map.get(req.getParameter("openId")+"sname"));
                resp.getWriter().write(jsonObject.toString());
            }else{
                jsonObject.put("status","202");
                jsonObject.put("msg","返回失败");
                resp.getWriter().write(jsonObject.toString());
            }
        }
    }
}
