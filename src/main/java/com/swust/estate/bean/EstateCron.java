package com.swust.estate.bean;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
@SuppressWarnings("unused")
public class EstateCron {
	
	private Integer id;
	
	private String expre;
	
	private String des;

}
