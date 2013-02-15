package com.dbfconsult.evernote.service;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.evernote.edam.type.Notebook;

public class EvernoteServiceImplITCase {

	private EvernoteService evernoteService;

	@Before
	public void setUp() {
		this.evernoteService = new EvernoteServiceImpl();
	}

	@Test
	public void testGetNotebooks() {
		List<Notebook> notebooks = evernoteService.getNotebooks();
		for (Notebook notebook : notebooks) {
			System.out.println(notebook.getName());
		}
	}

}
