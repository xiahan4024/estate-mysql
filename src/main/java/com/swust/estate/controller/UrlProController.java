package com.swust.estate.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.swust.estate.bean.EstateUrl;
import com.swust.estate.bean.MSG;
import com.swust.estate.service.ExpreService;
import com.swust.estate.service.UrlproService;

@Controller
@RequestMapping("/seturlpro")
public class UrlProController {

	private Integer pagesize = 5;
	
	@Autowired
	private UrlproService urlProService;
	
	@Autowired
	private ExpreService expreService;
	
	//url种子页面
	@GetMapping("/index")
	public String name() {
		return "setup/urlpro/index";
	}
	
	//得到网址信息页面
	@GetMapping("/getdata")
	@ResponseBody
	public MSG getData() {
		List<EstateUrl> list = new ArrayList<>();
		list = urlProService.getAllUrl();
		return MSG.success().add("list", list);
	}

	// id 获取 url 的详细信息，填充表格
	@GetMapping("/editpage/{id}")
	@ResponseBody
	public MSG updata(@PathVariable(value="id")Integer id) {
		EstateUrl url =  new EstateUrl();
		url = urlProService.getUrlWithID(id);
		return MSG.success().add("list", url);
	}
	
	// 删除 一条种子网站
	@DeleteMapping("/delete/{id}")
	@ResponseBody
	public MSG deleteURL(@PathVariable("id")Integer id) {
		urlProService.deleteURLWithID(id);
		return MSG.success();
	}
	
	// 更新 一条种子网站
	@PostMapping("/update")
	@ResponseBody
	public MSG updata(EstateUrl estateUrl,@RequestParam("flagstr")String string) {
		if("爬取".equals(string)) {
			estateUrl.setFlag(1);
		}else if("放弃爬取".equals(string)) {
			estateUrl.setFlag(0);
		}
		if("不处理".equals(estateUrl.getExpression().getImga())) {
			estateUrl.getExpression().setImga("0");
		}else if("中华人名共和国自然资源部".equals(estateUrl.getExpression().getImga())){
			estateUrl.getExpression().setImga("1");
		}else if("绵阳不动产官网".equals(estateUrl.getExpression().getImga())){
			estateUrl.getExpression().setImga("2");
		}
		if(estateUrl.getId() == null) {
			expreService.addExpre(estateUrl);
			if(estateUrl.getExpression().getId() == null) {
				return MSG.fail();
			}
			estateUrl.setExpreid(estateUrl.getExpression().getId());
			urlProService.addUrl(estateUrl);
			if(estateUrl.getId() == null) {
				expreService.delExpre(estateUrl.getExpreid());
				return MSG.fail();
			}
		}else {
				expreService.updateExpre(estateUrl);
				urlProService.updateUrl(estateUrl);
		}
		return MSG.success();
	}
}
