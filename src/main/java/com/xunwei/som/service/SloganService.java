package com.xunwei.som.service;

import org.springframework.stereotype.Service;

@Service
public interface SloganService {
	
	public String selectSlogan();
	
	public int updateSlogan(String slogan);
	
	
}
