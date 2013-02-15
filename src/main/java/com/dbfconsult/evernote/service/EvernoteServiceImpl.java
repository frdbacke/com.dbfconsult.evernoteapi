package com.dbfconsult.evernote.service;

import java.util.List;

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
	private static final String developerToken = "S=s1:U=5d375:E=14436f07a64:C=13cdf3f4e64:P=1cd:A=en-devtoken:H=435eefeaf0b12914a7d898fa9f6b1661";
	private static final String userStoreUrl = "https://sandbox.evernote.com/edam/user";

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
