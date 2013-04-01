package controllers;

import java.io.IOException;

import play.mvc.Controller;
import cdm.Datastore;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

public class Cdm extends Controller {

	private static String API_KEY = "AIzaSyAFrye3sbqNldkOoPYeuPC2n61RrkxvjAo";

	public static void register(String regId) {
		System.out.println("registered: "+regId);
		Datastore.register(regId);
	}

	public static void unregister(String regId) {
		System.out.println("unregistered: "+regId);
		Datastore.unregister(regId);
	}

	public static void send(String msg) throws Exception {
		
		Sender sender = new Sender(API_KEY);
		Message message = new Message.Builder().addData("msg", msg).build();
		MulticastResult result = sender.send(message, Datastore.getDevices(), 5);
		
	}
}
