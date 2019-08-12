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
public class EstateUrl {
	
	private Integer id;
	
	private String url;
	
	private String source;
	
	private Integer flag;

	private Integer expreid;
	
	private EstateExpression expression;
}
