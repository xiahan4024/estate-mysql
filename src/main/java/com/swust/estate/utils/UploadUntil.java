package com.swust.estate.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import lombok.Data;

@Data
@Component
public class UploadUntil {
	
	Logger logger = LoggerFactory.getLogger(UploadUntil.class);
	
	@Value("${tomcatpath}")
	public String tomcatPath;
	
	@Value("${picturepath}")
	public String picturePath;
	
	/*
	 * 读取网络图片到InputStream中
	 * urlString="http:\\zrzyhghj.my.gov.cn\\image20150109\\1491650.png"
	 * 
	 */
	public InputStream getPicByURL(String urlString) throws Exception {
		InputStream inStream = null;
		URL url = null;
		
		//创建一个url并打开链接  
		url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
		//设置连接参数
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);
		//得到url资源的输入流
		inStream = conn.getInputStream();
		return inStream;
	} 
	
	/*
	 * 输入流转换成byte
	 * inStream 输入流
	 * 
	 */
	public byte[] inputStream2byte(InputStream inStream) throws IOException {  
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
        byte[] buff = new byte[100];   //buff用于存放循环读取的临时数据 
        int rc = 0;  
        while ((rc = inStream.read(buff, 0, 100)) > 0) {  
            swapStream.write(buff, 0, rc);  
        }  
       byte[] in2b = swapStream.toByteArray();  
       return in2b;  
	}  
	
	/*
	 * jersey转发图片
	 * path：保存图片的服务器地址（自己的服务器）
	 * pic：图片的流
	 * 
	 */
	public void jerserySendPic(String path,byte[] pic) {
		// 实例化Jersey
		Client client = new Client();
		//设置请求路径
		WebResource resource = client.resource(path);
		//发送图片
		resource.put(String.class, pic);
	}
	
	/*
	 * 随机生成文件名
	 * 当前时间精确到毫秒+十位随机数
	 * 
	 */
	public String creatName(String name) {
		//扩展名
		String ext = FilenameUtils.getExtension(name);
		//图片名称生成策略
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		//图片名称一部分
		String format = df.format(new Date());
		//随机三位数
		Random r = new Random();
		// n 1000   0-999   99
		for(int i=0 ; i<10 ;i++){
			format += r.nextInt(10);
		}
		return format + "." + ext;
	}
	
	/*
	 * 删除服务器图片
	 * 
	 * 
	 */
	public void deletePic(String name) {
		String[] split = name.split("/");
		String URL =  this.getPicturePath() + split[split.length-1];
		System.out.println("图片所在地址" + URL);
		File file = new File(URL);
		if(file.exists()) {
			file.delete();
		}else {
			logger.info("服务器URL地址不存在，删除失败URL:" + URL);
		}
		
	}
	
	
	/*
	 * 发送图片到服务器
	 * URL：图片的绝对路径
	 * 返回：服务器上图片的绝对路径
	 */
	public String SendPic(String URL) {
		String url = URL;
		InputStream picByURL = null;
		byte[] inputStream2byte = null ;
		//获取流形式的图片
		try {
			picByURL = this.getPicByURL(url);
		} catch (Exception e) {
			logger.info("URL获取图片失败:" + e.getMessage());
		}
		//流转换成byte
		try {
			inputStream2byte = this.inputStream2byte(picByURL);
		} catch (IOException e) {
			logger.info("inputStream2byte 失败:" + e.getMessage());
		}
		//获取图片生成的名
		String creatName = this.creatName(url);
		//服务器路径
		String tomcatPath = this.getTomcatPath();
		//图片的完整路径
		String picPath = tomcatPath + creatName;
		this.jerserySendPic(picPath, inputStream2byte);
		return picPath;
	}
}
