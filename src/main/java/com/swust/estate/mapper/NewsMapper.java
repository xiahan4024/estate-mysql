package com.swust.estate.mapper;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.swust.estate.bean.EstateNews;

@Mapper
public interface NewsMapper {

	List<EstateNews> gotoPage(Integer pn, String text,Integer allFlag);

	List<String> getTitle(@Param("date")Date date);

	void addNew(List<EstateNews> listNews);

	void deleBatch(List<Integer> ids);

	void deleSingle(int id);

	void deleteEexamineNews(Integer idInt);

	EstateNews getNewWithID(Integer id);

	void updateNewFlag(@Param("newsid")Integer newsid,@Param("flag")Integer flag);

	List<EstateNews> gotoPageWithRelease(Integer pn, String text);

	Integer getNewsFlag(Integer id);

	EstateNews getReviewNewWithID(Integer id);

}
