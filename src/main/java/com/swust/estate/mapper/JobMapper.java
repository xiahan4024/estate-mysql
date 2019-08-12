package com.swust.estate.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;

import com.swust.estate.bean.EstateJob;

@Mapper
public interface JobMapper {

	Date getJobThisTime();

	void updateJobThisTime(Date thisTimeDate);

	void updateJobNextTime(Date nextTimeDate);

	EstateJob getJob();

	void updateJobCycle(Integer id);

}
