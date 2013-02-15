package com.dbfconsult.evernote.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteStore;
import com.evernote.edam.notestore.NoteStore.Client;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.userstore.UserStore;
import com.evernote.thrift.TException;
import com.evernote.thrift.protocol.TBinaryProtocol;
import com.evernote.thrift.transport.THttpClient;
import com.evernote.thrift.transport.TTransportException;

public class EvernoteServiceImpl implements EvernoteService {
	private String developerToken;
	private static final String userStoreUrl = "https://sandbox.evernote.com/edam/user";

	public EvernoteServiceImpl() {
		Properties properties = new Properties();
		try {
			properties
					.load(new FileInputStream(new File("./src/main/resources/evernote.properties")));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		developerToken = properties.getProperty("developerToken");
	}

	public List<Notebook> getNotebooks() {

		try {
			// Get the Evernote NoteStore URL
			TBinaryProtocol userStoreProt = new TBinaryProtocol(
					new THttpClient(userStoreUrl));
			new UserStore.Client(userStoreProt, userStoreProt);
			UserStore.Client userStore = new UserStore.Client(userStoreProt,
					userStoreProt);
			String notestoreUrl = userStore.getNoteStoreUrl(developerToken);

			// Set up the NoteStore client
			THttpClient noteStoreTrans;
			noteStoreTrans = new THttpClient(notestoreUrl);
			noteStoreTrans.setCustomHeader("User-Agent", "DBFConsult");
			TBinaryProtocol noteStoreProt = new TBinaryProtocol(noteStoreTrans);
			Client noteStore = new NoteStore.Client(noteStoreProt,
					noteStoreProt);

			// Make API calls, passing the developer token as the
			// authenticationToken param
			return noteStore.listNotebooks(developerToken);
		} catch (TTransportException e) {
			throw new RuntimeException(e);
		} catch (EDAMUserException e) {
			throw new RuntimeException(e);
		} catch (EDAMSystemException e) {
			throw new RuntimeException(e);
		} catch (TException e) {
			throw new RuntimeException(e);
		}
	}
}
