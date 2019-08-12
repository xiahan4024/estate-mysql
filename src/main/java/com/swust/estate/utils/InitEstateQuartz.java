package com.swust.estate.utils;

import org.springframework.beans.factory.annotation.Autowired;

import com.swust.estate.bean.EstateJob;
import com.swust.estate.job.CrawlerNewsJob;
import com.swust.estate.service.JobService;

public class InitEstateQuartz {
	@Autowired
	private EstateQuartzManager manager;
	
	@Autowired
	private JobService jobService;
	
	public void init() {
		EstateJob  estateJob = jobService.getJob();
		if(estateJob.getStatus() == 1) {
			Class class1 = this.getClassFun(estateJob.getObject());
			manager.addJob(estateJob.getName(), estateJob.getJobgroup(), estateJob.getName(), estateJob.getJobgroup(), class1, estateJob.getCron());
		}
		
//		manager.addJob("close", "close", "close", "close", CloseConnectJob.class, "0 0 8 * * ? ");
	}

	private Class getClassFun(String object) {
		// TODO Auto-generated method stub
		return CrawlerNewsJob.class;
	}
}
