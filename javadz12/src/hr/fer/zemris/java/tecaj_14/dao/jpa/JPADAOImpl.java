package hr.fer.zemris.java.tecaj_14.dao.jpa;

import hr.fer.zemris.java.tecaj_14.dao.DAO;
import hr.fer.zemris.java.tecaj_14.dao.DAOException;
import hr.fer.zemris.java.tecaj_14.model.BlogComment;
import hr.fer.zemris.java.tecaj_14.model.BlogEntry;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;

import java.util.List;

import javax.persistence.EntityManager;

public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(long id) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		
		BlogEntry blogEntry = em.find(BlogEntry.class, id);
		return blogEntry;
	}
	
	@Override
	public List<BlogEntry> getBlogEntries(BlogUser user) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		
		@SuppressWarnings("unchecked")
		List<BlogEntry> entries = 
				(List<BlogEntry>) em.createQuery("select b from BlogEntry as b where b.creator=:creator")
					.setParameter("creator", user)
					.getResultList();
		
		return entries;
	}
	
	@Override
	public BlogUser getUser(String nick) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		
		@SuppressWarnings("unchecked")
		List<BlogUser> users = 
				(List<BlogUser>) em.createQuery("select b from BlogUser as b where b.nick=:nick")
					.setParameter("nick", nick)
					.getResultList();
		
		if(users.size() == 0) return null;
		
		return users.get(0);
	}
	
	@Override
	public BlogUser getUser(String nick, String passwordHash) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		
		System.out.println("Loggin in user " + nick + " with password hash " + passwordHash + ".");
		
		@SuppressWarnings("unchecked")
		List<BlogUser> users = 
				(List<BlogUser>) em.createQuery("select b from BlogUser as b where b.nick=:nick and b.passwordHash=:passwordHash")
					.setParameter("nick", nick)
					.setParameter("passwordHash", passwordHash)
					.getResultList();
		
		if(users.size() == 0) return null;
		
		return users.get(0);
	}
	
	@Override
	public List<BlogUser> getAllUsers() throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		
		@SuppressWarnings("unchecked")
		List<BlogUser> users = 
				(List<BlogUser>) em.createQuery("from BlogUser")
					.getResultList();
		
		return users;
	}

	@Override
	public void createOrUpdate(BlogEntry entry) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		
		if(entry.getId() != null) {
			em.merge(entry);
		} else {
			em.persist(entry);
		}
		
		em.getTransaction().commit();
	}


	@Override
	public void createOrUpdate(BlogUser user) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		
		if(user.getId() != null) {
			em.merge(user);
		} else {
			em.persist(user);
		}
		
		em.getTransaction().commit();
	}
	
	@Override
	public void createOrUpdate(BlogComment comment) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		
		if(comment.getId() != null) {
			em.merge(comment);
		} else {
			em.persist(comment);
		}
		
		em.getTransaction().commit();
	}
}