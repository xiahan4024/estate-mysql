package com.swust.estate.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swust.estate.bean.EstateNews;
import com.swust.estate.bean.EstateReview;
import com.swust.estate.mapper.NewsMapper;
import com.swust.estate.mapper.ReviewMapper;
import com.swust.estate.utils.NewsUntil;
import com.swust.estate.utils.UploadUntil;

@Service
public class NewsService {
	
	@Autowired
	private NewsMapper newsMapper;
	
	@Autowired 
	private ReviewMapper reviewMapper;
	
	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private NewsUntil newsUntil;
	
	public List<EstateNews> gotoPage(Integer pn, String text,Integer allFlag) {
		return newsMapper.gotoPage(pn, text,allFlag);
	}

	public List<String> getTitle(Date date) {
		return newsMapper.getTitle(date);
	}

	public void addNew(List<EstateNews> listNews) {
		NewsMapper mapper = sqlSession.getMapper(NewsMapper.class);
		mapper.addNew(listNews);
	}

	public void deleBatch(List<Integer> ids) {
		NewsMapper mapper = sqlSession.getMapper(NewsMapper.class);
		mapper.deleBatch(ids);
	}

	public void deleSingle(int id) {
		newsMapper.deleSingle(id);
	}

	public EstateNews getNewWithID(Integer id) {
		Integer flag = newsMapper.getNewsFlag(id);
		if(flag == 1) {
			return newsMapper.getReviewNewWithID(id);
		}else {
			return newsMapper.getNewWithID(id);
		}
		
	}

	public void deleteEexamineNews(Integer idInt) {
		Integer reviewid = reviewMapper.getReviewIDBynewsId(idInt);
		newsUntil.deleteLocalhostPic(reviewid);
		Integer flag = 0;
		newsMapper.updateNewFlag(idInt,flag);
		reviewMapper.deleteReviewWithID(idInt);
	}
	
	public void examineSingle(EstateReview estateReview) {
		Integer flag = 1;
		newsMapper.updateNewFlag(estateReview.getNewsid(),flag);
		reviewMapper.addreview(estateReview);
	}

	public List<EstateNews> gotoPageWithRelease(Integer pn, String text) {
		return newsMapper.gotoPageWithRelease(pn, text);
	}

}
