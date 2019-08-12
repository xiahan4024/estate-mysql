package com.swust.estate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swust.estate.bean.EstateCron;
import com.swust.estate.mapper.CronMapper;

@Service
public class CronService {
	
	@Autowired
	CronMapper cronMapper;
	

	public List<EstateCron> getAllCron() {
		return cronMapper.getAllCron();
	}

	public String getCronWithID(Integer id) {
		return cronMapper.getCronWithID(id);
	}

}
