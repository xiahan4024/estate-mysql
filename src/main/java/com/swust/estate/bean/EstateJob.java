package com.swust.estate.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
@SuppressWarnings("unused")
public class EstateJob {
	
	private Integer id;
	
	private String name;

	private String jobgroup;
	
	private Integer status;
	
	private String des;
	
	private String object;
	
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date nextTime;
	
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date thisTime;
	
	private Integer cronid;
	
	private String cron;
}

