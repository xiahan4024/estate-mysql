package com.swust.estate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.swust.estate.bean.EstateUrl;

@Mapper
public interface UrlMapper {

	List<EstateUrl> getFlagUrl();

	List<EstateUrl> getAllUrl();

	void updateBatchUrl(List<Integer> ids);

	void updateSingle(int id);

	void initUrl();

}
