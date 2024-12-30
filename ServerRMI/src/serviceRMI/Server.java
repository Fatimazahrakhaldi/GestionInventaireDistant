package serviceRMI;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.EmployeDaoIpml;
import dao.ProduitDaoImpl;

/**
 * La classe Server démarre le serveur RMI, enregistre les objets distants
 * et les lie au registre RMI pour permettre aux clients d'y accéder.
 */
public class Server {
    
    // Logger pour enregistrer les messages à différents niveaux (info, erreur, etc.)
    private static final Logger logger = LogManager.getLogger(Server.class);

    /**
     * Méthode principale qui démarre le serveur RMI, crée un registre RMI
     * et lie les objets distants 'Produit' et 'Employe' à des noms dans le registre.
     */
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);

            Naming.rebind("rmi://localhost:1099/Produit", new ProduitDaoImpl());
            Naming.rebind("rmi://localhost:1099/Employe", new EmployeDaoIpml());
            
            // Enregistre un message d'information indiquant que le serveur RMI a démarré avec succès
            logger.info("Serveur RMI démarré avec succès !");

        } catch (Exception e) {
            logger.error("Message serveur => {}", e);
        }
    }
}
