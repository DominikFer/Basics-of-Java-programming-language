package hr.fer.zemris.java.tecaj_14.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class which models single blog comment.
 */
@Entity
@Table(name="blog_comments")
public class BlogComment {

	private Long id;
	private BlogEntry blogEntry;
	private String usersEMail;
	private String message;
	private Date postedOn;
	
	/**
	 * @return Returns the comment id.
	 */
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * Set an comment id.
	 * 
	 * @param id	Comment id.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns the blog entry on which this comment
	 * 		   is created.
	 */
	@ManyToOne
	@JoinColumn(nullable=false)
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}
	
	/**
	 * Set a blog entry on which this comment is created.
	 * 
	 * @param blogEntry	Blog entry
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * @return Returns the user email (comment creator).
	 */
	@Column(length=100,nullable=false)
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Set the user email (comment creator).
	 * 
	 * @param usersEMail	Comment creator email.
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * @return Returns the comment message.
	 */
	@Column(length=4096,nullable=false)
	public String getMessage() {
		return message;
	}

	/**
	 * Set the comment message.
	 * 
	 * @param message Comment message.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return Returns the time when this comment was created/posted.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Set the creation date for this comment.
	 * 
	 * @param postedOn	Creation date.
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
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
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
