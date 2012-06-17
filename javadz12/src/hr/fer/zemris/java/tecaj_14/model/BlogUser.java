package hr.fer.zemris.java.tecaj_14.model;

import hr.fer.zemris.java.tecaj_14.hashing.CalculateSHA;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *	Class which models single blog user. 
 */
@Entity
@Table(name="blog_users")
public class BlogUser {

	private Long id;
	private String firstName;
	private String lastName;
	private String nick;
	private String email;
	private String password;
	private String passwordHash;
	private List<BlogEntry> entries;
	
	/**
	 * @return	Returns the unique user id.
	 */
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * Set the user id.
	 * 
	 * @param id	User id.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return	Returns the list of entries created by this user.
	 */
	@OneToMany(mappedBy="creator",fetch=FetchType.LAZY, cascade=CascadeType.PERSIST, orphanRemoval=true)
	public List<BlogEntry> getEntries() {
		return entries;
	}
	
	/**
	 * Set the list of entries that were created by this user.
	 * 
	 * @param entries	Blog entries.
	 */
	public void setEntries(List<BlogEntry> entries) {
		this.entries = entries;
	}
	
	/**
	 * @return	Returns the user first name.
	 */
	@Column(length=200, nullable=false)
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Set the user first name.
	 * 
	 * @param firstName	User first name.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return	Returns the user last name.
	 */
	@Column(length=200, nullable=false)
	public String getLastName() {
		return lastName;
	}

	/**
	 * Set the user last name.
	 * 
	 * @param lastName	User last name.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * @return	Returns user nick name.
	 */
	@Column(length=200, nullable=false)
	public String getNick() {
		return nick;
	}

	/**
	 * Set the user nick name.
	 * 
	 * @param nick	User nick name.
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	/**
	 * @return	Returns user email address.
	 */
	@Column(length=200, nullable=false)
	public String getEmail() {
		return email;
	}

	/**
	 * Set the user email address.
	 * 
	 * @param email	User email address.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return	Returns hashed user password.
	 */
	@Column(length=40, nullable=false)
	public String getPasswordHash() {
		return passwordHash;
	}
	
	/**
	 * Set the hashed user password.
	 * 
	 * @param passwordHash	User hashed password.
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogUser other = (BlogUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * @return	Returns user password.
	 */
	@Transient
	public String getPassword() {
		return password;
	}

	/**
	 * Set the user password and hash it (SHA-1).
	 * 
	 * @param password	Raw user password.
	 */
	public void setPassword(String password) {
		this.password = password;
		this.passwordHash = new CalculateSHA(password).calculateDigest().getDigestAsHexString();
	}
}
