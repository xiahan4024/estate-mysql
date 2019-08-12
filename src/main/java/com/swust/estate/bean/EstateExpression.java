package com.swust.estate.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
@SuppressWarnings("unused")
public class EstateExpression {
	private Integer id;
	
	private String title;
	
	private String date;
	
	private String content;
	
	private String href;
	
	private String encode;
	
	private String imga;
}

