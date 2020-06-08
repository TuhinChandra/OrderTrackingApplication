package com.order.track.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Splitter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class GlobalConfiguration {

    @Value("#{${statusMatrix}}")
    private Map<String, String> statusMatrix;
    
    @Value("#{${users}}")
    private Map<String, String> users;
    
    @Value("#{${statusConfig}}")
    private Map<String, String> statusConfig;

    public Map<String, Map<String, String>> fetchStatusMetrix() {

	final Map<String, Map<String, String>> result = new HashMap<>();

	for (final Map.Entry<String, String> entry : statusMatrix.entrySet()) {

	    result.put(entry.getKey(), Splitter.on(",").withKeyValueSeparator("=").split(entry.getValue()));

	}

	return result;
    }
    
    public Map<String,String> fetchUserDetails(String username) {
	
	final Map<String, String> result = new HashMap<>();

	String user = users.get(username);
	
	String[] userInfo = user.split(",");
	
	result.put("Password",userInfo[0]);
	result.put("Role",userInfo[1]);
	
	return result;
    }

}
