package hr.fer.zemris.java.tecaj_14.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 *	Singleton class which provides single
 *	instance of {@link EntityManagerFactory}. 
 */
public class JPAEMFProvider {

	public static EntityManagerFactory emf;
	
	/**
	 * @return	Returns single {@link EntityManagerFactory} instance.
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}
	
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}