package com.wibe.backend.library;

import java.util.Random;

public class MajorCities {

	
	public static Location[] cities = {new Location(28.3760609,79.3518991),new Location(25.4021974,81.661497),new Location(32.2394177,77.1696102),new Location(30.818024, 75.287174),new Location(29.831798, 76.737370),new Location(28.816491, 76.319889),new Location(30.401987, 78.978580),new Location(28.043591, 73.485417),new Location(26.834579, 75.902409),new Location(25.434066, 74.847721),new Location(25.810492, 72.167057),new Location(22.726252, 76.078190),new Location(24.077280, 77.989811),new Location(23.655311, 80.362858),new Location(22.340644, 79.615788),new Location(22.624881, 82.691959),new Location(20.273244, 81.307682),new Location(18.177919, 81.131901),new Location(21.033974, 85.526432),new Location(19.114775, 83.724674),new Location(23.514350, 84.977116),new Location(23.130983, 87.503971),new Location(26.422096, 84.977116),new Location(25.671947, 87.064518),new Location(25.136053, 84.999088),new Location(19.239295, 73.836979),new Location(19.798460, 76.012272),new Location(12.844707, 77.286686)};
	
	public static Location randomCity(){
		 Random randomGenerator = new Random();
		 return cities[randomGenerator.nextInt(cities.length)];
	}
	
	public static class Location{
		private double lat;
		
		private double lon;
		
		public Location(){
			
		}
		
		public Location(double lat, double lon){
			this.lat = lat;
			this.lon = lon;
		}

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLon() {
			return lon;
		}

		public void setLon(double lon) {
			this.lon = lon;
		}
	}
}
