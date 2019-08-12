package com.swust.estate.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swust.estate.bean.EstateUrl;
import com.swust.estate.bean.MSG;
import com.swust.estate.service.UrlService;

@Controller
@RequestMapping("/seturl")
public class UrlController {
	
	private Integer pagesize = 5;
	
	@Autowired
	private UrlService urlService;
	
	@GetMapping("/index")
	public String name() {
		return "setup/url/index";
	}
	
	//得到所有的网站信息
	@GetMapping("/getdata")
	@ResponseBody
	public MSG getData() {
		List<EstateUrl> list = new ArrayList<>();
		list = urlService.getAllUrl();
		return MSG.success().add("List", list);
	}
	
	// 跳转 pn 页
	@GetMapping("/gotoPage/{pn}")
	@ResponseBody
	public MSG gotoPage(@PathVariable("pn")Integer pn) {
		Integer tempPageSize = 100;
		List<EstateUrl> list = new ArrayList<>();
		Page pageHelper = PageHelper.startPage(pn, tempPageSize);
		list = urlService.getAllUrl();
		PageInfo pageInfo = new PageInfo<>(list, tempPageSize);
		return MSG.success().add("List", pageInfo);
	}
	
	// 更新爬取的种子网站  flag
	@PutMapping("/updateurl/{ids}")
	@ResponseBody
	public MSG updateUrl(@PathVariable("ids")String idsString) {
		List< Integer> ids =new ArrayList<>();
		if(idsString.contains("-")) {
			String[] idStr = idsString.split("-");
			for (String string : idStr) {
				ids.add(Integer.parseInt(string));
			}
			urlService.updateBatchUrl(ids);
		}else {
			urlService.updateSingle(Integer.parseInt(idsString));
		}
		return MSG.success();
	}

}
