package com.swust.estate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.swust.estate.bean.EstateUrl;

@Mapper
public interface UrlProMapper {

	List<EstateUrl> getAllUrl();

	EstateUrl getUrlWithID(Integer id);

	void deleteURLWithID(Integer id);

	Integer addUrl(EstateUrl estateUrl);

	void updateUrl(EstateUrl estateUrl);

}
