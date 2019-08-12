package com.swust.estate.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.swust.estate.bean.EstateReview;

@Mapper
public interface ReviewMapper {

	void addreview(EstateReview estateReview);

	void deleteReviewWithID(Integer idInt);
	
	Integer getReviewIDBynewsId(Integer newsid);

}
