package com.swust.estate.bean;

import java.util.HashMap;
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
public class MSG {
	
	private int code;
	
	private String tips;
	
	private Map< String, Object> result = new HashMap<>();
	
	public static MSG success() {
		MSG msg = new MSG();
		msg.setCode(100);
		msg.setTips("处理成功");
		return msg;
	}
	
	public static MSG fail() {
		MSG msg = new MSG();
		msg.setCode(200);
		msg.setTips("处理失败");
		return msg;
	}
	
	public MSG add(String key, Object value) {
		this.getResult().put(key, value);
		return this;
	}

}
