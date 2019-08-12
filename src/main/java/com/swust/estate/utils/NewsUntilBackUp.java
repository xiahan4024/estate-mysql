package com.swust.estate.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.swust.estate.bean.EstateExpression;
import com.swust.estate.bean.EstateNews;

public class NewsUntilBackUp {
	
	Logger logger = LoggerFactory.getLogger(NewsUntilBackUp.class);
	
	@Autowired
	private PoolingHttpClientConnectionManager  pool;
	
	public List<EstateNews> getMessage(String HTML, EstateExpression estateExpression,Integer sourceId, Date thisTimeDate, HashSet<String> set) {
		List<EstateNews> list = new ArrayList<>();
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date pdate = null;
		String tempString = "";
		Element aElement = null;
		EstateNews estateNew = null;
		
		Document document = Jsoup.parse(HTML);
		Elements dateElements = document.select(estateExpression.getDate());
		
		for(Element element : dateElements) {
			//转换时间格式
			try {
				pdate = simpleDateFormat.parse(element.text());
			} catch (Exception e) {
				logger.error("网站ID：" + sourceId + " 在时间转换错误！");
				continue;
			}
			
			//判断是否是没爬取日期的新闻
			if(!outOfTime(thisTimeDate,pdate)) continue;
			
			//获取正文链接
			aElement = element.parent().select("a").first();
			tempString = changeURL(aElement.attr("href"),estateExpression.getHref());
			try {
				estateNew = this.getSingleNew(tempString , estateExpression ,set).setSourceId(sourceId).setDate(pdate);
			} catch (Exception e) {
				logger.error("网站ID：" + sourceId + "单个链接获取新闻出现错误！");
				continue;
			}
			list.add(estateNew);
		}
		return list;
	}
	
	//从单个链接中获取  标题 、时间 、 正文 来源等。
	private EstateNews getSingleNew(String aString,EstateExpression estateExpression, HashSet<String> set)throws Exception {
		String contentHtml = getHTML(aString,estateExpression.getEncode());
		if(contentHtml == null || contentHtml.equals(""))  logger.error("网址：" + aString + "HTML获取失败");
		String title = this.getTitle(contentHtml, estateExpression.getTitle());
		if(title == null || title.equals(""))  logger.error("网址：" + aString + "title 获取失败");
		if(set.contains(title)) logger.info("网址：" + aString + "title 已存在");
		String content = this.getContent(contentHtml, estateExpression.getContent());
		if(content == null || content.equals(""))  logger.error("网址：" + aString + "content 获取失败");
		
		try {
//		对正文格式进行定制 和微信前端一样的展示效果,
			content = this.deelWithFormat(content);
		} catch (Exception e) {
			logger.error("网址：" + aString + "格式转化失败");
		}
		
		try {
//		进行对 img a 标签进行一定的处理   
			content = this.deelWithImgAndA(estateExpression.getImga(),estateExpression.getHref(),content.toString());
		} catch (Exception e) {
			logger.error("网址：" + aString + "针对img a 标签进行处理时出现错误" + e);
		}
		return new EstateNews(null, title, null, aString, content.toString(), null, null, null, null);
	}
	
	private String deelWithFormat(String content) {
	    String regEx = " style=\"(.*?)\"";
	    Pattern p = Pattern.compile(regEx);
	    Matcher m = p.matcher(content);
	    String okContent = null;
	    if (m.find()) {
	        okContent = m.replaceAll("");
	    }
	    if(okContent == null) {
	    	okContent = content;
	    }
	    okContent = okContent.replace("<img", "<img style=\"display:block;margin:auto\"");
	    String replacement = "<style type=\"text/css\">#message,#message p,#message span{font-family: &quot;Microsoft YaHei&quot;background-color: rgb(255, 255, 255);}</style>";
	    
	    //处理一些 display:none样式，但是内容只是？
	    okContent = okContent.replace(">?</", "></");
	    return (replacement + okContent);
	}

	//处理 img标签 a标签 相对路径 连接不对
	private String deelWithImgAndA(String imga, String herf, String content) {
		switch (imga) {
		case "0": //表示处理  四川国土资源局
			return  this.deelWithImgAndACaseSC(content,herf);
		case "1": //表示处理有点麻烦  中华人名共和国自然资源部
			return  this.deelWithImgAndACaseZG(content,herf);
		case "2": //表示处理更加麻烦  绵阳不动产官网
			return this.deelWithImgAndACaseMY(content,herf);
		default:
			return content;
		}
	}
	
	/*
	 * 部分img标签存在 相对路劲：
	 */
	private String deelWithImgAndACaseSC(String content, String herf) {
		Document document = Jsoup.parse(content);
		Elements elementImg = document.select("img");
		for (Element element : elementImg) {
			String srcStr = element.attr("src"); //得到： /image20150109/1471948.png
			if(srcStr.startsWith("/")) {
				content = content.replace(srcStr, herf + srcStr);
			}
		}
		return  content;
	}

	/*		处理 img a 标签 是绵阳不动产官网
	 *  ①：  /image20150109/1445263.pdf 转换成：http://gtj.my.gov.cn/image20150109/1445263.pdf
	 *  ②：   http://gtj.my.gov.cn/image20150109/1468820.png  http://www.my.gov.cn/image20150109/1468820.png
	 *  ③：  img标签是相对路径，src只需/image20150109/1471948.png http://gtj.my.gov.cn/image20150109/1471948.png
	 */
	private String deelWithImgAndACaseMY(String content, String herf) {
		Document document = Jsoup.parse(content);
		Elements elementA = document.select("a");
		content = this.estateReplace(elementA, content, herf,"href");
		// /image20150109/1445263.pdf 转换成：http://gtj.my.gov.cn/image20150109/1445263.pdf
		Elements elementImg = document.select("img");
		content = this.estateReplace(elementImg, content, herf,"src");
		return content;
	}
	
	// 两个replace在一起 会出现内存溢出这个问题。 单独抽取方法  释放不需要哦的内存
	private String estateReplace(Elements elementImg,String content, String herf,String attrString) {
		for (Element element : elementImg) {
			String srcStr = element.attr(attrString); //得到： /image20150109/1471948.png
			if(srcStr.startsWith("http:")) 	return content;
			if(srcStr !=null && srcStr.equals("") == false) content = content.replace(srcStr, herf + srcStr);
		}
		return content;
	}
	
	/*
	 * ① 处理 img a 标签 是中华人名共和国自然资源部
	 * ② img src ./W020190107396850808535.jpg  缺少  http://www.mnr.gov.cn/dt/ywbb/201901 
	 * 	    转换为：http://www.mnr.gov.cn/dt/ywbb/201901/W020190107396850808535.jpg 相对地址
	 */
	private String deelWithImgAndACaseZG(String content, String herf) {
		String copyContent = content;
		Document document = Jsoup.parse(copyContent);
		Elements select = document.select("img");
		for(Element element :select) {
			String srcStr = element.attr("src"); //得到: ./W020190103360757157397.jpg
			String urlTemp = srcStr.substring(4, 10); //得到： 201901
			String urlTemp2 = srcStr.substring(1); //得到： /W020190107396850808535.jpg
			content = content.replace(srcStr, herf + urlTemp + urlTemp2);
		}
		return content;
	}

	//处理原文链接的相对链接问题
	public String changeURL(String URL, String URLExpre) {
		if(URL.startsWith("http")) return URL;
		boolean flag = false;
		if(URL.contains("../")) {
			URL = URL.replace("../", "");
			flag = true;
		}
		if(URL.contains("./")) {
			URL = URL.replace("./", "");
			flag = true;
		}
		return flag ? (URLExpre + "/" + URL) :(URLExpre + URL);
	}
	
	//得到标题
	public String getTitle(String HTML,String titleEspre) {
		Document document = Jsoup.parse(HTML);
		String titleText = null;
		try {
			titleText = document.select(titleEspre).first().text();
			if(titleText != null && !titleText.equals("")) {
				return titleText;
			} else {
//				"未找到title";
				return "";
			}
		}catch (Exception e) {
			return "";
		}
	}
	
	//得到正文
	public String getContent(String HTML,String contentEspre) {
		Document document = Jsoup.parse(HTML);
		String contentHtml = null;
		try {
			contentHtml = document.select(contentEspre).first().html();
			if(contentHtml != null && !contentHtml.equals("")) {
				return contentHtml;
			} else {
//				"未找到contentHtml");
				return "";
			}
		}catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 根据 url 获取整个HTML文档
	 * @param url
	 * @return
	 */
	public String getHTML(String url,String encode) {
		// 使用连接池管理器获取连接
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(pool).build();
		// 创建httpGet请求
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
		httpGet.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset="+ encode));
		httpGet.setHeader(new BasicHeader("Accept", "text/plain;charset=" + encode));
		CloseableHttpResponse response = null;
		try {
			// 发起请求
			response = httpClient.execute(httpGet);
			// 判断请求是否成功
			if (response.getStatusLine().getStatusCode() == 200) {
				// 判断是否有响应体
				if (response.getEntity() != null) {
					// 如果有响应体，则进行解析
					String html = EntityUtils.toString(response.getEntity(), encode);
					// 返回
					return html;
				}
			}
		} catch (Exception e) {
		} finally {
			// 释放连接
			if (response != null) {
				try {
					response.close();
				} catch (Exception e) {
				}
			}
		}
		return "";
	}
	
	//是否是时间上没爬取过的内容
	/**
	 * @param thisTimeDate 2018-11-10
	 * @param Pdate  2018-12-3
	 * @return  true
	 */
	public Boolean outOfTime(Date thisTimeDate, Date Pdate) {
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strToday = simpleDateFormat.format(thisTimeDate);
		try {
			thisTimeDate = simpleDateFormat.parse(strToday);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Long thisTime =  thisTimeDate.getTime();
		Long dateTime = Pdate.getTime();
		if(thisTime > dateTime) {
			return false;
		}else {
			return true;
		}
	}
}
