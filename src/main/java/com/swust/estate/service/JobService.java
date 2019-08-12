package com.swust.estate.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swust.estate.bean.EstateJob;
import com.swust.estate.mapper.JobMapper;

@Service
public class JobService {
	
	@Autowired
	private JobMapper jobMapper;

	public Date getJobThisTime() {
		return jobMapper.getJobThisTime();
	}

	public void updateJobThisNextTime(Date thisTimeDate, Date nextTimeDate) {
		jobMapper.updateJobThisTime(thisTimeDate);
		jobMapper.updateJobNextTime(nextTimeDate);
	}

	public EstateJob getJob() {
		return jobMapper.getJob();
	}

	public void updateJobCycle(Integer id) {
		jobMapper.updateJobCycle(id);
	}

}
