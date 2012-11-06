package ch.ethz.inf.vs.californium.controller.utility;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;

import ch.ethz.inf.vs.californium.coap.GETRequest;
import ch.ethz.inf.vs.californium.coap.Option;
import ch.ethz.inf.vs.californium.coap.Response;
import ch.ethz.inf.vs.californium.coap.ResponseHandler;
import ch.ethz.inf.vs.californium.coap.TokenManager;
import ch.ethz.inf.vs.californium.coap.registries.CodeRegistry;
import ch.ethz.inf.vs.californium.coap.registries.OptionNumberRegistry;
import ch.ethz.inf.vs.californium.controller.Controller;

public class SensorResource{
	
	private static Logger logger = Logger.getLogger(SensorResource.class);
	
	private String path;
	private String newestValue;
	private String oldValue;
	private String context;
	private Date timestamp; 
	
	private String type;
	private boolean observable;
	private boolean alive;
	private boolean correct;
	
	private HashMap<String,String> tags;

	private ResponseHandler receiver;
	private Controller controller;
	
	
	public SensorResource(String path, String context, String type, boolean observable, Controller controller){
		this.path=path;
		this.type=type;
		this.context = context;
		this.observable = observable;
		this.oldValue="";
		this.newestValue="";
		receiver = new GETReceiver(this);
		alive=false;
		this.controller = controller;
		tags = new HashMap<String, String>();
		this.setCorrect(true);
		retrieveTags();
		register();	
	
	}
	
	public void retrieveTags(){
		GETRequest tagGetter = new GETRequest();
		tagGetter.addOption(new Option("res=\""+path+"\"", OptionNumberRegistry.URI_QUERY));
		tagGetter.addOption(new Option("ep=\""+controller.getIdFromContext(context)+"\"", OptionNumberRegistry.URI_QUERY));
		tagGetter.setURI(controller.getRdUriBase()+"/tags");
		tagGetter.enableResponseQueue(true);
		Response tagResponse = null;
		try {
			tagGetter.execute();
			tagResponse = tagGetter.receiveResponse();
		} catch (IOException e) {
			logger.error("retrieving Tags for " + context+path);
		} catch (InterruptedException e) {
			logger.error("Retrieving Tags for " + context+path);
		}
		if(tagResponse != null && tagResponse.getCode() == CodeRegistry.RESP_CONTENT){
			String payload = tagResponse.getPayloadString();
			tags.clear();
			for(String tag : payload.split(",")){
				if(tag.isEmpty()){continue;}
				tags.put(tag.substring(0,tag.indexOf("=")),tag.substring(tag.indexOf("=")+1));
			}
			
		}
		if(tags.containsKey("room")){
			controller.addRoom(tags.get("room"));
		}
		
	}
	
	public boolean containsExactTag(String name, String value){
		if (tags.containsKey(name.toLowerCase())){
			return tags.get(name.toLowerCase()).equals(value.toLowerCase());
		}
		return false;
	}
	
	public boolean containsTag(String name){
		return tags.containsKey(name.toLowerCase());
	}
	
	public String getTag(String name){
		return tags.get(name.toLowerCase());
	}
	
	public void register(){
		GETRequest getRequest = new GETRequest();
		getRequest.setURI(context + path);
		if (observable){
			getRequest.addOption(new Option(0, OptionNumberRegistry.OBSERVE));
			getRequest.setToken(TokenManager.getInstance().acquireToken());
		}
		getRequest.enableResponseQueue(true);
		getRequest.registerResponseHandler(receiver);
		try {
			getRequest.execute();
		} catch (IOException e) {
			logger.error("Register at " + context+path);
		} 
	}
	
	
	private class GETReceiver implements ResponseHandler{
		
		private SensorResource parent;
		
		public GETReceiver(SensorResource parent){
			this.parent = parent;
		}

		@Override
		public void handleResponse(Response response) {
			if (response.getCode() == CodeRegistry.RESP_CONTENT){
				oldValue = newestValue;
				newestValue = response.getPayloadString();
				timestamp = new Date();
				alive=true;				
				if(!newestValue.equals(oldValue)){
					logger.debug(getContext()+getPath()+": New Value: "+newestValue);
					controller.processChange(parent);
				}
				controller.getNode(getContext()).setReceivedLastHeatBeat();
			
			}
			
		}		
	}

	public String getPath() {
		return path;
	}

	public String getContext(){
		return context;
	}

	public String getNewestValue() {
		return newestValue;
	}

	public String getOldValue(){
		return oldValue;
	}
	
	public String getType() {
		return type;
	}

	public void ignoreNewest(){
		newestValue = oldValue;
	}
	
	public Date getTimeStamp(){
		return timestamp;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
	
	
}