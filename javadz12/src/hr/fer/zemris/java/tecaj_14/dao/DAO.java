package hr.fer.zemris.java.tecaj_14.dao;

import hr.fer.zemris.java.tecaj_14.model.BlogComment;
import hr.fer.zemris.java.tecaj_14.model.BlogEntry;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;

import java.util.List;

public interface DAO {

	/**
	 * Returns an entry with provided id or <code>null</code> if not found.
	 * 
	 * @param id 				Entry ID
	 * @return					{@link BlogEntry} with requested ID
	 * 							or <code>null</code> if not found.
	 * @throws DAOException		If there was an error while fetching the data.
	 */
	public BlogEntry getBlogEntry(long id) throws DAOException;
	
	/**
	 * Returns the list of all blog entries in the database by some
	 * user.
	 * 
	 * @param user 				Fetch all the entries created by this user.
	 * @return					List of {@link BlogEntry} created by the
	 * 							<code>user</code>.
	 * @throws DAOException		If there was an error while fetching the data.
	 */
	public List<BlogEntry> getBlogEntries(BlogUser user) throws DAOException;
	
	/**
	 * Updates the user in the database or creates a new one if it does
	 * not exist.
	 * 
	 * @param user				{@link BlogUser} you want to update or create.
	 * @throws DAOException		If there was an error while fetching the data.
	 */
	public void createOrUpdate(BlogUser user) throws DAOException;
	
	/**
	 * Updates the entry in the database or creates a new one if it does
	 * not exist.
	 * 
	 * @param user				{@link BlogEntry} you want to update or create.
	 * @throws DAOException		If there was an error while fetching the data.
	 */
	public void createOrUpdate(BlogEntry entry) throws DAOException;
	
	/**
	 * Updates the comment in the database or creates a new one if it does
	 * not exist.
	 * 
	 * @param user				{@link BlogComment} you want to update or create.
	 * @throws DAOException		If there was an error while fetching the data.
	 */
	public void createOrUpdate(BlogComment comment) throws DAOException;
	
	/**
	 * Returns a user with provided nick or <code>null</code> if not found.
	 * 
	 * @param id 				Entry ID
	 * @return					{@link BlogUser} with requested nick
	 * 							or <code>null</code> if not found.
	 * @throws DAOException		If there was an error while fetching the data.
	 */
	public BlogUser getUser(String nick) throws DAOException;
	
	/**
	 * Returns a user with provided nick and hashed password
	 * or <code>null</code> if not found.
	 * 
	 * @param id 				Entry ID
	 * @return					{@link BlogUser} with requested nick and
	 * 							hashed password or <code>null</code> if not found.
	 * @throws DAOException		If there was an error while fetching the data.
	 */
	public BlogUser getUser(String nick, String hashedPassword) throws DAOException;
	
	/**
	 * Returns the list of all users entries in the database.
	 * 
	 * @return					List of all {@link BlogUser} in the
	 * 							database.
	 * @throws DAOException		If there was an error while fetching the data.
	 */
	public List<BlogUser> getAllUsers() throws DAOException;
	
}