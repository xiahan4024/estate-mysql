package com.swust.estate.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.DigestUtils;

import com.swust.estate.bean.EstateExpression;
import com.swust.estate.bean.EstateNews;
import com.swust.estate.bean.EstateUrl;
import com.swust.estate.service.JobService;
import com.swust.estate.service.NewsService;
import com.swust.estate.service.UrlService;
import com.swust.estate.utils.NewsUntil;

@DisallowConcurrentExecution
public class CrawlerNewsJob extends QuartzJobBean{
	
	Logger logger = LoggerFactory.getLogger(CrawlerNewsJob.class);
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private UrlService urlService;
	
	@Autowired
	private NewsUntil newsUntil;
	
	@Autowired
	private PoolingHttpClientConnectionManager pool;
	
	private SimpleDateFormat format = null;
	private String datetime = null;
	private Date thisTimeDate  = null;
	private List<EstateUrl> listURL = null;
	private List<EstateNews> listNews = null;
	private HashSet<String> set = new HashSet<>();
	private List<String> list = null;
	
	
	public void Crawler(EstateUrl estateUrl) {
		try {
			System.gc();
			EstateExpression expression = estateUrl.getExpression();
			String url = estateUrl.getUrl();
			String HTML =  newsUntil.getHTML(url,expression.getEncode());
			if(HTML == null || HTML.equals("") == true) {
				logger.error("网站ID:" + estateUrl.getId() + "	地址填写错误,newsUntil.getHTML方法返回为空/null");
				return ;
			}
			listNews = newsUntil.getMessage(HTML,expression,estateUrl.getId(),thisTimeDate,set);
			if(listNews.isEmpty() == false) {
				newsService.addNew(listNews);
			}
		} catch (Exception e) {
			logger.error("网站ID:" + estateUrl.getId() + "	爬取单个网页错误\n" + e);
		}
	}


	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("----->开始爬取");
		try {
			format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			datetime = format.format(new Date());
			thisTimeDate = jobService.getJobThisTime();
			
			//爬取的URL集合
			listURL = new ArrayList<>();
			//爬取到的新闻集合
			listNews = new ArrayList<EstateNews>();
			//title 集合
			//getTime得到毫秒  1秒=1000毫秒 七天前：1000 * 60 * 60 * 24 * 7
			Date titleDate = new Date(thisTimeDate.getTime() -  604800000) ;
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			List<String> listTitle = newsService.getTitle(titleDate);
			for (String str : listTitle) { 
				String md5DigestAsHex = DigestUtils.md5DigestAsHex(str.getBytes());
				set.add(md5DigestAsHex);
			}  
			listURL = urlService.getFlagUrl();
			for (EstateUrl estateUrl : listURL) {
				pool.closeExpiredConnections();
				this.Crawler(estateUrl);
			}
			String nestTime = format.format(context.getNextFireTime());
			logger.info("本次运行时间：" +datetime+"	下次运行时间："+nestTime);
			Date nextTimeDate = format.parse(nestTime);
			thisTimeDate = format.parse(datetime);
			jobService.updateJobThisNextTime(thisTimeDate,nextTimeDate);
		} catch (Exception e) {
			logger.error("爬取所有网站失败\n"+ e);
		}
		logger.info("----->结束爬取");
	}

}
