package com.wibe.backend.responses;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StandardResponse {
	
	private boolean status;
	
	private String message;
	
	private String data;
	
	public StandardResponse(boolean status, String message){
		this.status = status;
		this.message = message;
	}
	
	public StandardResponse(boolean status){
		this.status = status;
	}
	
	public StandardResponse(boolean status, Object object){
		this.status = status;
		try {
			this.message = fromJavaToJson(object);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public StandardResponse(boolean status, String message, Object data){
		this(status,message);
		try {
			this.data = fromJavaToJson(data);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean getStatus(){
		return this.status;
	}
	
	public void setStatus(boolean status){
		this.status = status;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	
	/**
     * Convert object to JSON String
     * @param object
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
     public static String fromJavaToJson(Object object)
             throws JsonGenerationException, JsonMappingException, IOException {
         ObjectMapper jsonMapper = new ObjectMapper();
         return jsonMapper.writeValueAsString(object);
     }

	public Object getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

		
}
