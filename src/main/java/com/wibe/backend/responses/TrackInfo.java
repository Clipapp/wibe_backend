package com.wibe.backend.responses;

public class TrackInfo {
	
	private String name;
	
	private String artist;
	
	private String genre;
	
	private int year;
	
	public TrackInfo(){
		
	}
	
	public TrackInfo(String name, String artist){
		this.name = name;
		this.artist = artist;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getArtist(){
		return this.artist;
	}
	
	public void setArtist(String artist){
		this.artist = artist;
	}
	
	public String getGenre(){
		return this.genre;
	}
	
	public void setGenre(String genre){
		this.genre = genre;
	}
	
	public int getYear(){
		return this.year;
	}
	
	public void setYear(int year){
		this.year = year;
	}

}
