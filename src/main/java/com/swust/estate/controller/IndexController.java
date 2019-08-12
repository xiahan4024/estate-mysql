package com.swust.estate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	@RequestMapping("/")
	public String indexPage() {
		return "index/index";
	}
	
	@RequestMapping("/menu")
	public String menuPage() {
		return "index/menu";
	}
	
	@RequestMapping("/tips")
	public String tipsPage() {
		return "index/tips";
	}
}
