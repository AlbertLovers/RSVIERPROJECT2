package firebird;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import exceptions.RSVIERException;
import interfaces.BestellingDAO;
import model.Artikel;
import model.Bestelling;

public class BestellingDAOFireBird extends AbstractDAOFireBird implements BestellingDAO{
	public static boolean bestellingWordGetest = false; //Kijken of een JUnit test loopt
	public static Bestelling aangeroepenBestellingInTest; //TODO nieuwe bestelling voor test maken

	public long nieuweBestelling(Bestelling bestelling) throws RSVIERException {
		try (Connection con = connPool.verkrijgConnectie();
				PreparedStatement statementBestelTabel = con.prepareStatement("INSERT INTO BESTELLING (klant_id) VALUES (?) RETURNING bestelling_id");){
			// Auto-commit uit om alles tegelijk door te voeren, voorkomt fouten in de database wanneer
			// de bestelling en de artikellijst niet beide goed gaan
			con.setAutoCommit(false);
			statementBestelTabel.setLong(1, bestelling.getKlant_id());
			schrijfAlleArtikelenNaarDeDatabase(con, statementBestelTabel, bestelling.getArtikelLijst());
			// Voer al de statements definitief uit
			con.commit();
		}catch(SQLException e){
			throw new RSVIERException("Error in: " + this.getClass() + ": nieuweBestelling(Bestelling bestelling): " + e.getMessage());
		}
		return 0;
	}

	public long nieuweBestelling(long klantId, List<Artikel> artikelList) throws RSVIERException {

		// De eerste try haalt het id van de nieuwe bestelling op, in de tweede try word de lijst met artikelen verwerkt
		try (Connection con = connPool.verkrijgConnectie();
				PreparedStatement statementBestelTabel = con.prepareStatement(
						"INSERT INTO BESTELLING (klant_id) VALUES (?) RETURNING bestelling_id");){

			// Auto-commit uit om alles tegelijk door te voeren, voorkomt fouten in de database wanneer
			// de bestelling en de artikellijst niet beide goed gaan
			con.setAutoCommit(false);
			statementBestelTabel.setLong(1, klantId);
			schrijfAlleArtikelenNaarDeDatabase(con, statementBestelTabel, artikelList);
			// Voer al de statements definitief uit
			con.commit();
		}catch(SQLException e){
			throw new RSVIERException("Error in: " + this.getClass() + ": nieuweBestelling(long klantId, List<Artikel> artikelList): " + e.getMessage());
		}
		return 0;
	}

	@Override
	public Iterator<Bestelling> getBestellingOpKlantId(long klantId) throws RSVIERException{
		try(Connection con = connPool.verkrijgConnectie();
				PreparedStatement statement = con.prepareStatement(
						"SELECT BESTELLING.klant_id, BESTELLING.bestelling_id, ARTIKEL.artikel_id, ARTIKEL.omschrijving, BESTELLING_HEEFT_ARTIKEL.aantal, "
								+ "BESTELLING_HEEFT_ARTIKEL.prijs_id_prijs, PRIJS.prijs, BESTELLING.datumAanmaak"

							+ " FROM BESTELLING_HEEFT_ARTIKEL, ARTIKEL, BESTELLING, PRIJS"

							+ " WHERE BESTELLING.klant_id = ?"
							+ " AND BESTELLING_HEEFT_ARTIKEL.prijs_id_prijs = PRIJS.prijs_id"
							+ " AND BESTELLING_HEEFT_ARTIKEL.bestelling_id_best = BESTELLING.bestelling_id"
							+ " AND BESTELLING_HEEFT_ARTIKEL.artikel_id_art = ARTIKEL.artikel_id;");){

			statement.setLong(1, klantId);

			return verwerkResultSetGetBestelling(statement).iterator();
		}catch(SQLException e){
			throw new RSVIERException("Error in: " + this.getClass() + ": getBestellingOpKlantId(long klantId): " + e.getMessage());
		}
	}

	@Override
	public Iterator<Bestelling> getBestellingOpBestellingId(long bestellingId) throws RSVIERException{
		try(Connection con = connPool.verkrijgConnectie();
				PreparedStatement statement = con.prepareStatement(
						"SELECT BESTELLING.klant_id, BESTELLING.bestelling_id, ARTIKEL.artikel_id, "
								+ "ARTIKEL.omschrijving, BESTELLING_HEEFT_ARTIKEL.aantal, "
								+ "BESTELLING_HEEFT_ARTIKEL.prijs_id_prijs, PRIJS.prijs, "
								+ "BESTELLING.datumAanmaak"

						+ " FROM BESTELLING_HEEFT_ARTIKEL, ARTIKEL, BESTELLING, PRIJS"

						+ " WHERE BESTELLING.bestelling_id = ?"
						+ " AND BESTELLING_HEEFT_ARTIKEL.prijs_id_prijs = PRIJS.prijs_id"
						+ " AND BESTELLING_HEEFT_ARTIKEL.bestelling_id_best = BESTELLING.bestelling_id"
						+ " AND BESTELLING_HEEFT_ARTIKEL.artikel_id_art = ARTIKEL.artikel_id;");){

			statement.setLong(1, bestellingId);

			return verwerkResultSetGetBestelling(statement).iterator();
		}catch(SQLException e){
			throw new RSVIERException("Error in: " + this.getClass() + ": getBestellingOpBestellingId(long bestellingId) " + e.getMessage());
		}
	}

	@Override
	public void updateBestelling(Bestelling bestelling) throws SQLException, RSVIERException {
		// TODO Auto-generated method stub

	}

	@Override
	public void verwijderAlleBestellingenKlant(long klantId) throws RSVIERException{
		try(Connection con = connPool.verkrijgConnectie();
				PreparedStatement updateStatement = con.prepareStatement(
						"UPDATE BESTELLING "
							+ "SET bestellingActief = 0 "
							+ "WHERE klant_id = ?;");){
			updateStatement.setLong(1, klantId);
			updateStatement.executeUpdate();
		}catch(SQLException e){
			throw new RSVIERException("Error in: " + this.getClass() + ": verwijderAlleBestellingenKlant(long klantId) " + e.getMessage());
		}
	}

	/* De echte verwijder methode staat in dit comment
	 * Dit staat hier alleen voor display purposes omdat we een verwijdermethode moesten maken voor de opdracht
	 * Alleen zetten wij de bestelling op inactief zodat er altijd een geschiedenis van bestellingen is
 	@Override
	public long verwijderAlleBestellingenKlant(long klantId) throws RSVIERException, SQLException {
		try(Connection con = MySQLConnectieLeverancier.getConnection();
			PreparedStatement statement = con.prepareStatement(
					"DELETE BESTELLING_HEEFT_ARTIKEL "
					+ "FROM BESTELLING_HEEFT_ARTIKEL "
					+ "INNER JOIN BESTELLING "
					+ "ON BESTELLING_HEEFT_ARTIKEL.bestelling_id_best = BESTELLING.bestelling_id "
					+ "WHERE BESTELLING.klant_id = ?;");){

			// Auto-commit uit want we werken op 2 tabellen
			// Dus het moet allebei goed gaan
			con.setAutoCommit(false);

			//Verwijder alle items met het klantId uit BESTELLING_HEEFT_ARTIKEL
			statement.setLong(1, klantId);
			statement.executeUpdate();

			// Verwijder bestelling
			try(PreparedStatement updateStatement = con.prepareStatement(
					"DELETE BESTELLING "
					+ "FROM BESTELLING"
					+ "WHERE klant_id = ?;");){
				updateStatement.setLong(1, klantId);
				updateStatement.executeUpdate();
			}
			con.commit();

		}
		return 0;
	}
	 */

	@Override
	public void verwijderEnkeleBestelling(long bestellingId) throws RSVIERException, SQLException {
		try(Connection con = connPool.verkrijgConnectie();
			PreparedStatement statement = con.prepareStatement(
					"UPDATE BESTELLING SET bestellingActief = 0 WHERE bestelling_id = ?")){
			statement.setLong(1, bestellingId);
			statement.executeUpdate();
		}catch(SQLException e){
			throw new RSVIERException("Error in: " + this.getClass() + ": verwijderEnkeleBestelling(long bestellingId) " + e.getMessage());
		}
	}

	/* Wederom staat de echte verwijdermethode in dit comment
	 	@Override
	public void verwijderEnkeleBestelling(long bestellingId) throws RSVIERException, SQLException {
		try(Connection con = MySQLConnectieLeverancier.getConnection();
			PreparedStatement statement = con.prepareStatement(
					"DELETE BESTELLING FROM BESTELLING WHERE bestelling_id = ?";
					PreparedStatement statement2 = con.prepareStatement(
					"DELETE BESTELLING_HEEFT_ARTIKEL FROM BESTELLING_HEEFT_ARTIKEL WHERE bestelling_id_best = ?")){

			con.setAutoCommit(false);
			statement.setLong(1, bestellingId);
			statement.executeUpdate();

			statement2.setLong(1, bestellingId);
			statement2.executeUpdate();
			con.commit();
		}
	}
	 * */

	private void schrijfAlleArtikelenNaarDeDatabase(Connection con, PreparedStatement statement, List<Artikel> artikelList) throws RSVIERException {
		try(ResultSet rs = statement.executeQuery();
			PreparedStatement statementBestelHeeftArtikelTabel = con.prepareStatement(
				"INSERT INTO BESTELLING_HEEFT_ARTIKEL (bestelling_id_best, artikel_id_art, prijs_id_prijs, aantal)"
				+ " VALUES (?, ?, ?, ?)");){

			// Nieuw aangemaakte bestellingId ophalen
			rs.next();
			long bestellingId = rs.getLong(1);

			for(Artikel artikel : artikelList){
				statementBestelHeeftArtikelTabel.setLong(1, bestellingId);
				statementBestelHeeftArtikelTabel.setLong(2, artikel.getArtikelId());
				statementBestelHeeftArtikelTabel.setLong(3, 1);
				statementBestelHeeftArtikelTabel.setLong(4, 1);
				statementBestelHeeftArtikelTabel.setLong(3, artikel.getArtikel_prijs_id());
				statementBestelHeeftArtikelTabel.setLong(4, artikel.getAantal());
				statementBestelHeeftArtikelTabel.executeUpdate();
			}
		}catch(SQLException e){
			throw new RSVIERException("Error in: " + this.getClass() + ": schrijfAlleArtikelenNaarDeDatabase " + e.getMessage());
		}

	}

	private LinkedHashSet<Bestelling> verwerkResultSetGetBestelling(PreparedStatement statement) throws SQLException{
		try(ResultSet rs = statement.executeQuery();){
			LinkedHashSet<Bestelling> bestellingSet = new LinkedHashSet<Bestelling>();

			// Eerste rij verwerken tot bestelling zodat ik straks een fixed bestellingId heb om
			// mee te vergelijken in de while loop
			Bestelling best = new Bestelling();
			Artikel art;

			// Voeg artikelen toe aan de bestelling zolang het bestellingId gelijk is aan
			// die op de vorige rij van de ResultSet, maak anders een nieuwe Bestelling aan
			while(rs.next()){
				// Kijk of het de eerste bestelling is
				if(best.getBestelling_id() == 0){
					setBestellingGegevens(rs, best);
				}

				// Wanneer er een volgende bestelling is, schrijf de vorige naar de ArrayList
				// en maak een nieuwe bestelling aan
				if(best.getBestelling_id() != (rs.getLong("bestelling_id"))){
					bestellingSet.add(best);
					best = new Bestelling();
					setBestellingGegevens(rs, best);
				}

				// Maak een nieuw Artikel object aan en voeg deze toe aan de ArrayList
				// met Artikel objecten in Bestelling
				art = new Artikel();
				
				art.setArtikelId(rs.getInt("artikel_id"));
				art.setArtikelNaam(rs.getString("omschrijving"));
				art.setArtikelPrijs(rs.getBigDecimal("prijs"));
				art.setAantalBesteld(rs.getInt("aantal"));
				best.voegArtikelToe(art);
			}

			//Laatste bestelling ook toevoegen aan de ArrayList
			bestellingSet.add(best);

			return bestellingSet;
		}catch(SQLException e){
			throw new RSVIERException("Error in: " + this.getClass() + ": verwerkResultSetGetBestelling(PreparedStatement statement) " + e.getMessage());
		}
	}

	private void setBestellingGegevens(ResultSet rs, Bestelling best) throws SQLException{
		best.setBestelling_id(rs.getLong("bestelling_id"));
		best.setKlant_id(rs.getLong("klant_id"));
		best.setDatumAanmaak(rs.getString("datumAanmaak"));
	}

}
