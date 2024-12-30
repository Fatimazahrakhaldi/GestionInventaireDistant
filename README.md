# Système de gestion d'inventaire avec accès distant

## Contexte
Ce projet est une application Java permettant à une entreprise de gérer son inventaire de produits à travers une interface cliente et serveur. 

Le système permet de gérer les données des produits (ajout, mise à jour, suppression, recherche) dans une base de données. L'accès distant est implémenté via **RMI (Remote Method Invocation)**, permettant aux employés de se connecter au serveur pour interagir avec l'inventaire via des requêtes RMI.


## Instructions d'installation et d'exécution

### Prérequis

- **Java 8** ou version supérieure  
- **MySQL 5.7** ou version supérieure  

#### Versions des packages utilisés  

| JAR                  | Version  |  
| --------------------- | -------- |  
| HikariCP             | 5.1.0    |  
| mysql-connector      | 9.1.0    |  
| log4j-api            | 2.24.3   |  
| log4j-core           | 2.24.3   |  
| log4j-jdbc-dbcp2     | 2.24.3   |  
| log4j-slf4j-impl     | 2.24.3   |  
| slf4j-api            | 1.7.36   |  
| slf4j-simple         | 2.0.16   |  

---
### Utilisation d’un IDE  

Vous pouvez utiliser un IDE comme **Eclipse** :  

1. **Importer le projet :**  
   - Ajoutez le projet dans Eclipse.  

2. **Ajouter les dépendances :**  
   - Ajoutez les fichiers JAR nécessaires au classpath du projet.  

3. **Configurer une exécution RMI :**  
   - Configurez une configuration d’exécution (Run Configuration) en ajoutant l'argument nécessaire JAVAFX pour le client.
```VM arguments
--module-path "C:\Users\votrepath\eclipse-workspace\ClientRMI\lib\javafx-sdk-23.0.1\lib" --add-modules javafx.controls,javafx.fxml
```  
4. **Lancer le projet :**  
   - Lancez le projet directement depuis l’IDE.  

**Note :** Vous pouvez également utiliser les commandes via le terminal pour executer le serveur et le client.  

---

### Configuration de la base de données  

1. **Création de la base de données :**  
   - Utilisez le fichier **`schema.sql`** pour créer la structure des tables.  
2. **Insertion de données de test :**  
   - Importez le fichier **`data.sql`** pour insérer des données initiales dans les tables.
   
---

### Configuration du fichier Hikari properties 
- Configurer les informations de connexion (hôte, utilisateur, mot de passe) dans le fichier **`ServerRMI/resources/hikari.properties`**

---

### Exécution du serveur  

1. **Compiler le code serveur (si nécessaire) :**  
   - Si le projet est déjà compilé, assurez-vous que les répertoires suivants sont présents :  
     - **`bin`** : contient le code compilé du serveur.  
     - **`lib`** : contient les dépendances (JAR).  

2. **Lancer le serveur avec la commande suivante :**  
   Accédez au répertoire du projet :  

   ```bash  
   cd C:\Users\\eclipse-workspace\ServerRMI  
   ```

Puis, exécutez le serveur :

```bash  
java -cp ".;bin;C:/Users/votrepath/eclipse-workspace/ServerRMI/lib/*" -Djava.rmi.server.codebase=file:/C:/Users/votrepath/eclipse-workspace/ServerRMI/bin/ serviceRMI.Server start 1099  
```
### Exécution du client

1. **Compiler le code client :**
Accédez au répertoire du projet client :
```bash  
cd C:\Users\votrepath\eclipse-workspace\ClientRMI  
```
Ensuite, compilez le code client :

```bash  
javac -cp ".;C:/Users/votrepath/eclipse-workspace/ClientRMI/lib/*;C:/Users/votrepath/eclipse-workspace/ClientRMI/lib/javafx-sdk-23.0.1/lib/*" -d bin src/application/Main.java src/serviceRMI/Client.java
```
2. **Lancer le client :**
Utilisez la commande suivante pour démarrer le client :

```bash 
java --module-path "C:/Users/ProBook/eclipse-workspace/ClientRMI/lib/javafx-sdk-23.0.1/lib" --add-modules javafx.controls,javafx.fxml -cp ".;bin;C:/Users/ProBook/eclipse-workspace/ClientRMI/lib/*;C:/Users/ProBook/eclipse-workspace/ClientRMI/lib/javafx-sdk-23.0.1/lib/*" -Djava.rmi.server.codebase=file:/C:/Users/ProBook/eclipse-workspace/ServerRMI/bin/ application.Main 
```

## Notes importantes
- Assurez-vous que le serveur MySQL est démarré et que les fichiers **schema.sql** et **data.sql** ont été correctement importés avant d’exécuter le projet.
- Vérifiez les chemins d’accès dans les commandes pour qu’ils correspondent à votre configuration locale.
- Si vous rencontrez des erreurs liées à **RMI** ou **JavaFX**, assurez-vous que les dépendances nécessaires sont bien incluses.
