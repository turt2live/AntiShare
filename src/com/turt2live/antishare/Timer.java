package com.turt2live.antishare;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.turt2live.antishare.ASTimer.ASTimerListener;

public class Timer implements ASTimerListener {

	public class TimerVar {
		public String info;
		public long time;
		public long start;
	}

	private ConcurrentMap<Long, ConcurrentHashMap<Class<?>, TimerVar>> timers = new ConcurrentHashMap<Long, ConcurrentHashMap<Class<?>, TimerVar>>();

	@Override
	public void timerStart(Class<?> clazz, String extraInformation, long id){
		ConcurrentHashMap<Class<?>, TimerVar> timer = new ConcurrentHashMap<Class<?>, TimerVar>();
		TimerVar var = new TimerVar();
		var.info = extraInformation;
		var.start = System.currentTimeMillis();
		timer.put(clazz, var);
		timers.put(id, timer);
	}

	@Override
	public void timerStop(Class<?> clazz, String extraInformation, long id){
		long now = System.currentTimeMillis();
		ConcurrentHashMap<Class<?>, TimerVar> timer = timers.get(id);
		if(timer != null){
			for(Class<?> clazz2 : timer.keySet()){
				TimerVar var = timer.get(clazz2);
				long start = var.start;
				var.time = now - start;
			}
		}
	}

	@Override
	public String timerReport(Class<?> clazz){
		StringBuilder out = new StringBuilder();
		boolean found = false;
		for(Long id : timers.keySet()){
			ConcurrentHashMap<Class<?>, TimerVar> timer = timers.get(id);
			for(Class<?> clazz2 : timer.keySet()){
				if(clazz2.getSimpleName().equals(clazz.getSimpleName())){
					found = true;
					TimerVar var = timer.get(clazz2);
					out.append("CLASS [").append(id).append("]: ").append(clazz.getSimpleName()).append(" (").append(var.info).append(") TIME: ").append(var.time).append(" MS\r\n");
				}
			}
		}
		return found ? out.toString().trim() : "Not Found";
	}

}
