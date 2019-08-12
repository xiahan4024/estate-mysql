package com.swust.estate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.swust.estate.bean.EstateCron;

@Mapper
public interface CronMapper {

	List<EstateCron> getAllCron();

	String getCronWithID(Integer id);

}
