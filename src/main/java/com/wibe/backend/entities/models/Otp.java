package com.wibe.backend.entities.models;

import org.springframework.data.annotation.Id;

public class Otp {

        @Id
        private String _id;

        private String number;

        private String otp;
        
        public Otp(){
        	
        }

        public Otp(String number, String otp){
                this.number =number;
                this.otp =otp;
        }
        
        public void setNumber(String number){
        	this.number = number;
        }
        
        public String getNumber(){
        	return this.number;
        }
        
        public void setOtp(String otp){
        	this.otp = otp;
        }
        
        public String getOtp(){
        	return this.otp;
        }
}
