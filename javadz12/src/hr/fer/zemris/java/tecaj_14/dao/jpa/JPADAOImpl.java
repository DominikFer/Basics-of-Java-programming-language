package hr.fer.zemris.java.tecaj_14.dao.jpa;

import hr.fer.zemris.java.tecaj_14.dao.DAO;
import hr.fer.zemris.java.tecaj_14.dao.DAOException;
import hr.fer.zemris.java.tecaj_14.model.BlogEntry;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;

import java.util.List;

import javax.persistence.EntityManager;

public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
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
	public void update(BlogUser user) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		
		em.persist(user);
		em.getTransaction().commit();
	}
	
	@Override
	public BlogUser read(BlogUser user) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		
		@SuppressWarnings("unchecked")
		List<BlogUser> users = 
				(List<BlogUser>) em.createQuery("select b from BlogUser as b where b.nick=:nick")
					.setParameter("nick", user.getNick())
					.getResultList();
		
		if(users.size() == 0) return null;
		
		return users.get(0);
	}
	
	@Override
	public List<BlogUser> readAllUsers() throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		
		@SuppressWarnings("unchecked")
		List<BlogUser> users = 
				(List<BlogUser>) em.createQuery("from BlogUser")
					.getResultList();
		
		return users;
	}
}