package com.glenwood.glaceemr.server.utils;
import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Component
public class JsonDateSerializer extends JsonSerializer<Date>{
	
	
	

	@Override
	public void serialize(Date date, JsonGenerator generator, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		
		generator.writeString(date.toString());
		
	}

}