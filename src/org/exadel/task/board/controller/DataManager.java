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

		final List<CardList> lists = new LinkedList<>();
		
		final Card card1 = new Card(user, "first", "request", "adb");
		final Card card2 = new Card(user, "second", "task", "gds");
		final Card card3 = new Card(user, "third", "response", "kdf");
		final Card card4 = new Card(user, "forth", "request", "lew");
		
		final CardList list1 = new CardList(user, "first");
		final CardList list2 = new CardList(user, "second");

		list1.addCard(card1);
		list2.addCard(card2);
		list2.addCard(card3);
		list2.addCard(card4);
		lists.add(list1);
		lists.add(list2);

		return lists;
	}
	
	public CardList getCardList(int id) {
		return new CardList(user, String.valueOf(id));
	}
	
	public boolean moveCard(Card card, CardList fromList, CardList toList) {
		return true;
	}	
}
