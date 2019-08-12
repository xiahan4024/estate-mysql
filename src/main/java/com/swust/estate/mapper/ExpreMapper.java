package com.swust.estate.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.swust.estate.bean.EstateExpression;
import com.swust.estate.bean.EstateUrl;

@Mapper
public interface ExpreMapper {

	void addExpre(EstateUrl estateUrl);

	void delExpre(Integer id);

	void updateExpre(EstateUrl estateUrl);
	

}
