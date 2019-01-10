package com.view.movedata.exp;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.view.tool.HelpProperties;

public class AsynchronousBathLoadDataBase{
	

	private static Object o = new Object();
	private static AsynchronousBathLoadDataBase asyn;
	public static AsynchronousBathLoadDataBase getAsynchronousBathLoadDataBase(){
		if(asyn==null){
			synchronized (o) {
				if(asyn==null){
					asyn = new AsynchronousBathLoadDataBase();
				}
			}
		}
		return asyn;
	}
	private ThreadPoolExecutor threadPoolExecutor ;
	private AsynchronousBathLoadDataBase(){
		Integer corepoolsize = new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "asynch.bathload.corepoolsize"));
		Integer maxpoolsize = new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "asynch.bathload.maxpoolsize"));
		Integer keepalivetime = new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "asynch.bathload.keepalivetime"));
		Integer queuecapacity = new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "asynch.bathload.queuecapacity"));

		 threadPoolExecutor = new ThreadPoolExecutor(corepoolsize, maxpoolsize, keepalivetime, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(queuecapacity),
				new ThreadPoolExecutor.CallerRunsPolicy());
		 threadPoolExecutor.allowCoreThreadTimeOut(true);
	}
	
	public Future submit(Runnable job){
		return threadPoolExecutor.submit(job);
	}
	public Future submit(Callable job){
		return threadPoolExecutor.submit(job);
	}
	
}
