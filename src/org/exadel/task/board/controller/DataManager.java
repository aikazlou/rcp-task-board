package org.exadel.task.board.controller;

import java.util.LinkedList;
import java.util.List;

import org.exadel.task.board.model.*;

public class DataManager {

	private final TaskService service = new TaskService();

	private final User user = new User("login", "Alex");
	
	public DataManager() {
	}
	
	public List<CardList> getCardLists() {		
		return service.getLists();
	}
	
	public CardList getCardList(int id) {
		return service.getList(id);
	}
	
	public boolean moveCard(Card card, CardList fromList, CardList toList) {
		return service.moveCard(card, fromList, toList);
	}	
}
