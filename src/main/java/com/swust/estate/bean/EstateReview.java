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
public class EstateReview {

	private Integer id;
	
	private String title;
	
	private String message;
	
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date date;
	
	private Integer newsid;
	
}
