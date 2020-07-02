package com.order.track.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import com.google.common.base.Splitter;

import lombok.Getter;
import lombok.Setter;
import lombok.var;

@Getter
@Setter
@Service
public class GlobalConfiguration {

	@Value("#{${statusMatrix}}")
	private Map<String, String> statusMatrix;

	@Value("#{${users}}")
	private Map<String, String> users;

	@Value("#{${statusConfigExternal}}")
	private Map<String, String> statusConfigExternal;

	@Value("#{${statusConfigInternal}}")
	private Map<String, String> statusConfigInternal;

	@Value("#{${fulfillmentTypes}}")
	private Map<String, String> fulfillmentTypes;

	@Value("#{${orderStatusMatrix}}")
	private Map<String, Integer> orderStatusMatrix;
	
	@Value("#{${orderStateMap}}")
	private Map<Integer, String> orderStateMap;
	
	@Value("#{${deliveryGroupMsgKey}}")
	private Map<String, String> deliveryGroupMsgKey;
	
	@Bean
	    public ResourceBundleMessageSource messageSource() {

	        var source = new ResourceBundleMessageSource();
	        source.setBasenames("resourceBundle");
	        source.setUseCodeAsDefaultMessage(true);

	        return source;
	    }

	public Map<String, Map<String, String>> fetchStatusMetrix() {

		final Map<String, Map<String, String>> result = new HashMap<>();

		for (final Map.Entry<String, String> entry : statusMatrix.entrySet()) {

			result.put(entry.getKey(), Splitter.on(",").withKeyValueSeparator("=").split(entry.getValue()));

		}

		return result;
	}

	public Map<String, String> getStatusConfig(final String type) {

		return "external".equals(type) ? statusConfigExternal : statusConfigInternal;

	}

	public Map<String, String> fetchUserDetails(final String username) {

		final Map<String, String> result = new HashMap<>();

		final String user = users.get(username);

		final String[] userInfo = user.split(",");

		result.put("Password", userInfo[0]);
		result.put("Role", userInfo[1]);

		return result;
	}

}
