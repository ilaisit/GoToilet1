package dataTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class SuccessDbStatistics {
	HashMap<String, Integer> successesCounter;
	HashMap<String, Integer> failuresCuonter;
	
	
	public SuccessDbStatistics() {
		successesCounter = new HashMap<String, Integer>();
		failuresCuonter = new HashMap<String, Integer>();
	}
	
	public void add(String date, int success, int failure) {
		if(success > 0) {
			if(!successesCounter.containsKey(date)) {
				successesCounter.put(date, 0);
			}
			successesCounter.put(date, successesCounter.get(date) + 1);
		}
		
		if(failure > 0) {
			if(!failuresCuonter.containsKey(date)) {
				failuresCuonter.put(date, 0);
			}
			failuresCuonter.put(date, failuresCuonter.get(date) + 1);
		}
	}
	
	public void addSuccess(String date) {
		add(date, 1, 0);
	}
	
	public void addFailure(String date) {
		add(date, 0, 1);
	}
	
	public List<Statistics> toSuccessStatistics() {
		List<Statistics> retVal = new ArrayList<>();
		for(Entry<String, Integer> currentDate : successesCounter.entrySet()) {
			Statistics currentStat = new Statistics();
			Integer failureValue = failuresCuonter.get(currentDate) == null ? 0 : failuresCuonter.get(currentDate);
			double value = (double)currentDate.getValue() / (currentDate.getValue() + failureValue) * 100;
			currentStat.setDate(currentDate.getKey());
			currentStat.setValue("" + value);
			retVal.add(currentStat);
		}
		return retVal;
	}
	
	public List<Statistics> toTakesStatistics() {
		List<Statistics> retVal = new ArrayList<>();
		for(Entry<String, Integer> currentDate : successesCounter.entrySet()) {
			Statistics currentStat = new Statistics();
			Integer failureValue = failuresCuonter.get(currentDate) == null ? 0 : failuresCuonter.get(currentDate);
			double value = (double)currentDate.getValue() / (currentDate.getValue() + failureValue) * 100;
			currentStat.setDate(currentDate.getKey());
			currentStat.setValue("" + value);
			retVal.add(currentStat);
		}
		return retVal;
	}
}
