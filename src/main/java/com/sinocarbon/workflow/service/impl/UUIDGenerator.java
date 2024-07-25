package com.sinocarbon.workflow.service.impl;

import java.util.UUID;

import org.activiti.engine.impl.cfg.IdGenerator;
import org.springframework.stereotype.Service;

@Service
public class UUIDGenerator implements IdGenerator{

	@Override
	public String getNextId() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
