package hr.fer.zemris.java.tecaj_13.dao;

import hr.fer.zemris.java.tecaj_13.model.Unos;

import java.util.List;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author marcupic
 *
 */
public interface DAO {

	/**
	 * Dohvaća sve postojeće unose u bazi, ali puni samo dva podatka:
	 * id i title.
	 * 
	 * @return listu unosa
	 * @throws DAOException u slučaju pogreške
	 */
	public List<Unos> dohvatiOsnovniPopisUnosa() throws DAOException;
	
	/**
	 * Dohvaća sve postojeće unose u bazi i puni sve podatke.
	 * 
	 * @return listu unosa
	 * @throws DAOException u slučaju pogreške
	 */
	public List<Unos> dohvatiDetaljanPopisUnosa() throws DAOException;
	
	/**
	 * Dohvaća Unos za zadani id. Ako unos ne postoji, vraća <code>null</code>.
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	public Unos dohvatiUnos(long id) throws DAOException;
	
	/**
	 * Sprema novi zapis u bazu.
	 * 
	 * @param unos				Podaci u novom zapisu.
	 * @throws DAOException		DAOException u slučaju pogreške
	 */
	public void save(Unos unos) throws DAOException;
	
	/**
	 * Sprema postojeći zapis u bazi.
	 * 
	 * @param unos				Podaci u zapisu.
	 * @throws DAOException		DAOException u slučaju pogreške
	 */
	public void update(Unos unos) throws DAOException;
}
