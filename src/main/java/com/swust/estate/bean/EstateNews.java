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
public class EstateNews {
	
	private Integer id;
	
	private String title;
	
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date date;
	
	private String a;
	
	private String message;
	
	private Integer sourceId;
	
	private String source;
	
	private EstateUrl url;
	
	private Integer flag;
}
