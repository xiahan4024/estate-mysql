package com.swust.estate.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swust.estate.bean.EstateNews;
import com.swust.estate.bean.EstateReview;
import com.swust.estate.bean.MSG;
import com.swust.estate.mapper.PictureMapper;
import com.swust.estate.mapper.ReviewMapper;
import com.swust.estate.service.NewsService;
import com.swust.estate.utils.NewsUntil;

@Controller
@RequestMapping("/news")
public class NewsController {
	
	private Integer pageSize = 5;
	
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private NewsUntil newsUntil;
	
	@Autowired
	private ReviewMapper reviewMapper;
	
	@Autowired
	private PictureMapper pictureMapper;
	
	@GetMapping("/index")
	public String Index() {
		return "news/index";
	}
	
	//去 pn 页
	@ResponseBody
	@PostMapping("/gotopage/{pn}")
	public MSG gotopage(HttpServletRequest request,@PathVariable(value="pn")Integer pn,
			@RequestParam(value="text")String text,@RequestParam(value="flag") String flag,
			@RequestParam(value="allflag") Integer allFlag) {
		HttpSession session = request.getSession();
		/*
		 * 当前端不输入数据时，传入到后台的是 ""
		 * 当前端输入数据时，传入的就是string
			    if("".equals(text)) {
					System.out.println(" text is ''");
				}else if (text == null) {
					System.out.println(" text is null");
				}
		 */
		String Sessiontext = (String) session.getAttribute("key");
		/*
		 * flag：
		 * 		1：输入了关键字  
		 *  	0:还未输入关键字
		 *  	-1:第一页 
		 */
		if(flag.equals("-1")) {
			pn = 1;
		}
		if(flag.equals("1")) {
			session.setAttribute("key", text);
		}else if(Sessiontext !=null){
			text = Sessiontext;
		}else {
			text = "";
		}
		List<EstateNews> list = new ArrayList<>();
		Page<Object> startPage = PageHelper.startPage(pn, pageSize);
		list = newsService.gotoPage(pn,text,allFlag);
		/*if(allFlag == 1) {
			list = newsService.gotoPageWithRelease(pn,text);
		}else {
			list = newsService.gotoPage(pn,text,allFlag);
		}*/
		PageInfo pageInfo = new PageInfo<>(list, pageSize);
		return MSG.success().add("pageInfo", pageInfo).add("key", text);
	}
	
	// 删除 单条/多条 新闻(已发布的只能取消发布后才能删除)
	@ResponseBody
	@DeleteMapping("/dele/{ids}")
	public MSG deleteNews(@PathVariable("ids")String idsString) {
		if(idsString.contains("-")) {
			List<Integer> idIntegers = new ArrayList<>(); 
			String [] idsStr = idsString.split("-");
			for (String string : idsStr) {
				idIntegers.add(Integer.parseInt(string));
			}
			newsService.deleBatch(idIntegers);
		}else {
			newsService.deleSingle(Integer.parseInt(idsString));
		}
		return MSG.success();
	}
	
	//取消发布  
	@ResponseBody
	@DeleteMapping("/release/{id}")
	public MSG deleteReleaseNews(@PathVariable("id")Integer id) {
		newsService.deleteEexamineNews(id);
		return MSG.success();
	}
	
	
	/*
	 * 发布 单条、  
	 *  多条 新闻  考虑取消这个功能
	 * 爬取新闻的id
	 * 
	 */
	@ResponseBody
	@GetMapping("/release/{id}")
	public MSG releaseNews(@PathVariable("id")Integer id) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Date parse = null;
		String format = simpleDateFormat.format(date);
		try {
			parse = simpleDateFormat.parse(format);
		} catch (Exception e) {
			e.printStackTrace();
		}
		EstateNews estateNew = newsService.getNewWithID(id);
		
		String tempMessage = estateNew.getMessage();
		tempMessage = newsUntil.urlPicUpload(tempMessage,id);
		EstateReview estateReview = new EstateReview(null, estateNew.getTitle(), tempMessage, null, id);
		estateReview.setDate(parse);
		//已经审核该消息
		newsService.examineSingle(estateReview);
		Integer reviewID = reviewMapper.getReviewIDBynewsId(id);
		pictureMapper.updateID(id,reviewID);
		return MSG.success();
	}
	
	//单个新闻详细页面请求
	@GetMapping("/gotonewpage/{id}")
	public String newPage(@PathVariable("id")Integer id) {
		 return "news/page";
	}
		
	//单个新闻详细页面
	@GetMapping("/gotonewpage/{id}/detail")
	@ResponseBody
	public MSG gotoNewPage(HttpServletRequest request,@PathVariable(value="id")Integer id) {
		HttpSession session = request.getSession();
		String text = "";
		String Temptext = (String) session.getAttribute("key");
		if(Temptext !=null){
			text = Temptext;
		}
		EstateNews estateNew = newsService.getNewWithID(id);
		return MSG.success().add("result", estateNew).add("key", text);
	}
	
}
