package com.sinocarbon.workflow.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.sinocarbon.polaris.commons.utils.Constant;

public class PublicServiceUtils {

	public static Map<String, Object> assableOperationLogs(String operatorName, String operatorType,
			String operatedIP) {
		Map<String, Object> loginLogMessages = new HashMap<>();
		loginLogMessages.put(Constant.OPERATOR_NAME, operatorName);
		loginLogMessages.put(Constant.OPERATOR_TYPE, operatorType);
		loginLogMessages.put(Constant.OPERATOR_TIME, new Date());
		loginLogMessages.put(Constant.OPERATOR_IP, operatedIP);
		return loginLogMessages;
	}

}
