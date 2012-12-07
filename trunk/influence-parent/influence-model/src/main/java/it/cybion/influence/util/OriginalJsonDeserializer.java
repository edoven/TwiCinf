package it.cybion.influence.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.cybion.influence.model.User;

public class OriginalJsonDeserializer {

	Gson gson;
	
	public OriginalJsonDeserializer(){
		gson = new GsonBuilder()
		.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
					// "Wed Oct 17 19:59:40 +0000 2012"
		.setDateFormat("EEE MMM dd hh:mm:ss ZZZZZ yyyy")
		.registerTypeAdapter(org.joda.time.DateTime.class, new JodaDateTimeTypeDeserializer())
		.create(); 
	}
	
	public User deserializeJsonStringsToUser(String userJsonString) {
		return gson.fromJson(userJsonString, User.class);
	}
}
