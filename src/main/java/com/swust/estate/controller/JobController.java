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

import com.swust.estate.bean.EstateCron;
import com.swust.estate.bean.EstateJob;
import com.swust.estate.bean.MSG;
import com.swust.estate.service.CronService;
import com.swust.estate.service.JobService;
import com.swust.estate.utils.EstateQuartzManager;

@Controller
@RequestMapping("/setjob")
public class JobController {
	
	@Autowired
	JobService jobService;
	
	@Autowired
	CronService cronService;
	
	@Autowired
	private EstateQuartzManager manager;
	
	@RequestMapping("/index")
	public String getSetJobPage() {
		return "setup/job/index";
	}

	//得到 job 的详细信息
	@ResponseBody
	@GetMapping("/getjobdata")
	public MSG getJobDate() {
		EstateJob estateJob = jobService.getJob();
		return MSG.success().add("estateJob", estateJob);
	}
	
	//得到所有的 cron 表达式
	@ResponseBody
	@GetMapping("/getjobcron")
	public MSG getJobCron() {
		List<EstateCron> list = new ArrayList<>();
		list = cronService.getAllCron();
		return MSG.success().add("list", list);
	}
	
	//更新爬取时间间隔
	@ResponseBody
	@PutMapping("/updatejobtime/{id}")
	public MSG updateJobTime(@PathVariable("id")Integer id) {
		String cron = cronService.getCronWithID(id);
		manager.modifyJobTime("crawlerNews", "News", "crawlerNews", "News", cron);
		jobService.updateJobCycle(id);
		return MSG.success();
	}
}
