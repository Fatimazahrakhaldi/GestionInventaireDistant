package serviceRMI;

import java.rmi.Naming;
import java.rmi.RemoteException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.GenericDao;
import model.Employe;
import model.Produit;

/**
 * La classe Client établit la connexion avec le serveur RMI et récupère les stubs RMI
 * pour accéder aux services distants relatifs à 'Produit' et 'Employe'.
 */
public class Client {
    
    // Logger pour enregistrer les messages à différents niveaux (info, erreur, etc.)
    private static final Logger logger = LogManager.getLogger(Client.class);

    private static GenericDao<Produit, Integer> produitStub;
    private static GenericDao<Employe, Integer> employeStub;

    /**
     * Cette méthode initialise la connexion au registre RMI et récupère les stubs RMI
     * pour les services 'Produit' et 'Employe'.
     * Les stubs sont utilisés pour interagir avec les objets distants exposés par le serveur RMI.
     */
    @SuppressWarnings("unchecked")
    public static void initialize() {
        try {
            produitStub = (GenericDao<Produit, Integer>) Naming.lookup("rmi://localhost:1099/Produit");
            employeStub = (GenericDao<Employe, Integer>) Naming.lookup("rmi://localhost:1099/Employe");

            // Enregistrer la connexion réussie aux stubs RMI
            logger.info("Connexion réussie aux stubs RMI.");

        } catch (RemoteException e) {
            logger.error("RemoteException lors de la connexion à RMI : {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Exception lors de la connexion à RMI : {}", e.getMessage());
        }
    }

    /**
     * Retourne le stub RMI pour accéder aux opérations liées à 'Produit'.
     * 
     * @return Le stub RMI pour 'Produit'.
     */
    public static GenericDao<Produit, Integer> getProduitStub() {
        return produitStub;
    }

    /**
     * Retourne le stub RMI pour accéder aux opérations liées à 'Employe'.
     * 
     * @return Le stub RMI pour 'Employe'.
     */
    public static GenericDao<Employe, Integer> getEmployeStub() {
        return employeStub;
    }
}
