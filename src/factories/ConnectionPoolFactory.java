package factories;

import com.mysql.jdbc.StringUtils;
import connection_pools.C3POAdapter;
import connection_pools.HikariCPAdapter;
import connection_pools.OudeMySQLConnectorAdapter;
import exceptions.RSVIERException;
import interfaces.VerkrijgConnectie;

/**
 * Created by Milan_Verheij on 19-06-16.
 *
 * Dit is de factory voor de juiste connection Pool. Er wordt door middel
 * van een unieke waarde per type connectionpool de juiste factory gemaakt.
 *
 */

public class ConnectionPoolFactory {

    /**
     * Geeft op basis van de gekozen Database Soort (1 = MySQL, 2 = FireBird) en
     * gekozen Connection Pool (1 = C3PO, 2 = HikariCP, 3 = MySQlConnectieLeverancier)
     * de juiste Connection Pool Adapter terug.
     *
     * @param connectionPoolKeuze Keuze voor de connection pool (zie keuzes hierboven).
     * @param DBKeuze Keuze voor type database (zie keuzes hierboven).
     * @return Adapter behorend bij bovenstaande keuzes.
     * @throws RSVIERException Foutmelding met omschrijving.
     */
    public static VerkrijgConnectie getConnectionPool(String connectionPoolKeuze, int DBKeuze) throws RSVIERException {
        if (connectionPoolKeuze.length() > 1 || connectionPoolKeuze.length() == 0) {
            throw new RSVIERException("ConnectionPoolFactory: U moet 1 karakter invullen als keuze" +
                    "\nKeuzes: 1: C3PO, 2: HikariCP, 3: MySQLConnectieLeverancier");
        } else
        if (!StringUtils.isStrictlyNumeric(connectionPoolKeuze)) {
            throw new RSVIERException("ConnectionPoolFactory: U moet een getal als keuze in vullen" +
                    "\nKeuzes: 1: C3PO, 2: HikariCP, 3: MySQLConnectieLeverancier");
        }

        switch (connectionPoolKeuze.charAt(0)) {
            case '1':
                    return new C3POAdapter(DBKeuze);
            case '2':
                    return new HikariCPAdapter(DBKeuze);
            case '3':
                    return new OudeMySQLConnectorAdapter();
            default:
                    throw new RSVIERException("ConnectionPoolFactory: VERKEERDE KEUZE VOOR CONNECTION POOL: " + connectionPoolKeuze +
                            "\nKeuzes: 1: C3PO, 2: HikariCP, 3: MySQLConnectieLeverancier");
        }
    }
}
