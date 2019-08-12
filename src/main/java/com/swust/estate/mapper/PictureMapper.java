package com.swust.estate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PictureMapper {
	
	public void addPicture(@Param("name")String name,@Param("reviewId")Integer reviewId);
	
	public void deleteUrlByReviewId(Integer reviewId);
	
	public List<String> getpictureUrlByReviewId(Integer reviewId);

	public void updateID(@Param("newsID")Integer newsID, @Param("reviewID")Integer reviewID);

}
