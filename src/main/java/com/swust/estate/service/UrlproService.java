package com.swust.estate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swust.estate.bean.EstateExpression;
import com.swust.estate.bean.EstateUrl;
import com.swust.estate.mapper.ExpreMapper;
import com.swust.estate.mapper.UrlProMapper;

@Service
public class UrlproService {
	
	@Autowired
	UrlProMapper urlproMapper;
	
	public List<EstateUrl> getAllUrl() {
		return urlproMapper.getAllUrl();
	}

	public EstateUrl getUrlWithID(Integer id) {
		return urlproMapper.getUrlWithID(id);
	}

	public void deleteURLWithID(Integer id) {
		urlproMapper.deleteURLWithID(id);
	}

	public Integer addUrl(EstateUrl estateUrl) {
		return urlproMapper.addUrl(estateUrl);
	}

	public void updateUrl(EstateUrl estateUrl) {
		urlproMapper.updateUrl(estateUrl);
	}

}
