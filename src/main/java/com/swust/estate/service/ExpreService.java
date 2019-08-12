package com.swust.estate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swust.estate.bean.EstateUrl;
import com.swust.estate.mapper.ExpreMapper;

@Service
public class ExpreService {
	
	@Autowired
	ExpreMapper expreMapper;
	
	public void addExpre(EstateUrl estateUrl) {
		expreMapper.addExpre(estateUrl);
	}

	public void delExpre(Integer expreid) {
		expreMapper.delExpre(expreid);
	}

	public void updateExpre(EstateUrl estateUrl) {
		expreMapper.updateExpre(estateUrl);
	}

}
