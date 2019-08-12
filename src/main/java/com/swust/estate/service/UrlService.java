package com.swust.estate.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swust.estate.bean.EstateUrl;
import com.swust.estate.mapper.UrlMapper;

@Service
public class UrlService {
	
	@Autowired
	UrlMapper urlMapper;
	
	@Autowired
	private SqlSession sqlSession;

	public List<EstateUrl> getFlagUrl() {
		return urlMapper.getFlagUrl();
	}

	public List<EstateUrl> getAllUrl() {
		return urlMapper.getAllUrl();
	}

	public void updateBatchUrl(List<Integer> ids) {
		urlMapper.initUrl();
		UrlMapper mapper = sqlSession.getMapper(UrlMapper.class);
		mapper.updateBatchUrl(ids);
	}

	public void updateSingle(int id) {
		urlMapper.initUrl();
		urlMapper.updateSingle(id);
	}

}
