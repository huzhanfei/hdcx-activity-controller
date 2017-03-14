package cn.javacodes.util;

public class MediaTimeUtil {
	
	
	private int currentMin;
	
	private int currentSec;
	
	private int totalMin;
	
	private int totalSec;

	
	public void timeCalculator(long currentTimeMs, long totalTimeMs){
		
		long currenTimeS = currentTimeMs/1000;
		
		long totalTimeS = totalTimeMs/1000;	
		
		currentMin = (int)(currenTimeS / 60);
		
		currentSec = (int)(currenTimeS % 60);
		
		totalMin = (int)(totalTimeS / 60);
		
		totalSec = (int)(totalTimeS % 60);
		
	}


	public String getCurrentMin() {
		if (currentMin / 10 > 0) {
			return "" + currentMin;
		} else {
			return "0" + currentMin;
		}
	}


	public String getCurrentSec() {
		if (currentSec / 10 > 0) {
			return "" + currentSec;
		} else {
			return "0" + currentSec;
		}
		
	}


	public String getTotalMin() {
		if (totalMin / 10 > 0) {
			return "" + totalMin;
		} else {
			return "0" + totalMin;
		}
	}


	public String getTotalSec() {
		if (totalSec / 10 > 0) {
			return "" + totalSec;
		} else {
			return "0" + totalSec;
		}
	}
	
	
	

}
