package org.exadel.task.board.service;

import java.util.List;

import org.exadel.task.board.dao.*;
import org.exadel.task.board.model.*;
import org.hibernate.Criteria;

public class TaskService {

	private GenericDao<User> userDao = new GenericDaoHibernate<User>(User.class);
	private GenericDao<CardList> listDao = new GenericDaoHibernate<CardList>(
			CardList.class);
	private GenericDao<Card> cardDao = new GenericDaoHibernate<Card>(Card.class);
	private GenericDao<Comment> commentDao = new GenericDaoHibernate<Comment>(
			Comment.class);

	public TaskService() {
	}
	
	public List<CardList> getLists() {
		beginTransaction();
		
		Criteria criteria = listDao.getSession().createCriteria(CardList.class);
		@SuppressWarnings("unchecked")
		final List<CardList> lists = (List<CardList>) criteria.list();
		
		closeSession();
		
		return lists;
	}

	public int createUser(User user) {
		beginTransaction();

		int id = userDao.create(user);

		commit();
		return id;
	}

	public User createUser(String login, String name) {
		beginTransaction();

		final User user = new User(login, name);
		userDao.create(user);

		commit();
		return user;
	}

	public User getUser(int id) {
		beginTransaction();

		final User user = userDao.read(id);

		closeSession();
		return user;
	}

	public User getUser(String login) {
		beginTransaction();

		final User user = (User) userDao.getSession().byNaturalId(User.class)
				.using("login", login).load();

		closeSession();
		return user;
	}
	
	public boolean updateUser(User user) {
		beginTransaction();
		
		if (userDao.read(user.getId()) != null) {

			final User mergedUser = (User) cardDao.getSession().merge(user);
			userDao.update(mergedUser);
			
			commit();
			return true;
		}
		
		closeSession();
		return false;
	}

	public int createList(CardList list) {
		beginTransaction();

		int id = listDao.create(list);

		commit();
		return id;
	}

	public CardList getList(int id) {
		beginTransaction();

		CardList list = listDao.read(id);

		closeSession();
		return list;
	}
	
	public boolean updateList(CardList list) {
		beginTransaction();
		
		if (listDao.read(list.getId()) != null) {

			final CardList mergedList = (CardList) listDao.getSession().merge(list);
			listDao.update(mergedList);
			
			commit();
			return true;
		}

		closeSession();
		return false;
	}

	public int createCard(Card card) {
		beginTransaction();

		int id = cardDao.create(card);

		commit();
		return id;
	}

	public Card getCard(int id) {
		beginTransaction();

		Card card = cardDao.read(id);

		closeSession();
		return card;
	}

	public boolean updateCard(Card card) {
		beginTransaction();
		
		if (cardDao.read(card.getId()) != null) {
			
			final Card mergedCard = (Card) cardDao.getSession().merge(card);
			cardDao.update(mergedCard);
			
			commit();
			return true;
		}

		closeSession();
		return false;
	}
	
	public int createComment(Comment comment) {
		beginTransaction();

		int id = commentDao.create(comment);

		commit();
		return id;
	}

	public Comment getComment(int id) {
		beginTransaction();

		Comment comment = commentDao.read(id);

		closeSession();
		return comment;
	}

	public boolean moveCard(Card card, CardList fromList, CardList toList) {
		beginTransaction();

		final Card mergedCard = (Card) userDao.getSession().merge(card);
		final CardList mergedFromList = (CardList) userDao.getSession().merge(
				fromList);
		final CardList mergedToList = (CardList) userDao.getSession().merge(
				toList);

		if (!mergedFromList.contains(mergedCard)) {
			closeSession();
			return false;
		}
		if (mergedToList.contains(mergedCard)) {
			closeSession();
			return true;
		}

		mergedFromList.removeCard(mergedCard);
		mergedToList.addCard(mergedCard);

		commit();

		return true;
	}

	public void deleteUser(User user) {
		beginTransaction();
		userDao.delete(user);
		commit();
	}

	public void deleteList(CardList list) {
		beginTransaction();
		listDao.delete(list);
		commit();
	}

	public void deleteCard(Card card) {
		beginTransaction();
		cardDao.delete(card);
		commit();
	}

	public void deleteComment(Comment comment) {
		beginTransaction();
		commentDao.delete(comment);
		commit();
	}

	private void beginTransaction() {
		userDao.getSession().beginTransaction();
	}

	private void commit() {
		userDao.getSession().getTransaction().commit();
	}

	private void rollback() {
		userDao.getSession().getTransaction().rollback();
	}

	private void closeSession() {
		userDao.getSession().close();
	}

}
