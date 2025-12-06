
+# PROJET-JEE-L3
+
+Application Java EE simple pour la gestion des dons, des donneurs et des receveurs.
+Implémentée avec des servlets et des JSP (structure créée pour Eclipse / Tomcat).
+
+**Structure principale**
+- `src/main/java` : code Java (servlets, services, DAO, modèles)
+- `src/main/webapp` : pages JSP, ressources statiques (`css/`, `js/`, `assets/`)
+- `build/classes` : classes compilées 
+
+**Prérequis**
+- JDK 8+ installé
+- Apache Tomcat 9
+- IDE recommandé : Eclipse (Dynamic Web Project) ou tout IDE supportant les projets web Java
+
+**Exécution (rapide)**
+- Importer le projet dans Eclipse : `File > Import > Existing Projects into Workspace` ou ouvrir comme projet dynamique Web.
+- Configurer un serveur Tomcat dans Eclipse, puis clic droit sur le projet > `Run On Server`.
+- Ou générer un WAR et le déployer dans le dossier `webapps` de Tomcat.
+
+**Remarques**
+- Les sources sont dans `src/main/java` et les JSP/ressources dans `src/main/webapp/pages`.
+- L'initialisation des jeux de données utilise la servlet `InitDataServlet` (voir `com.donsang.util`).
+- Aucune configuration de base de données externe fournie par défaut — adapter selon vos besoins.
+
+**Auteur / Mainteneur**
+- KAMSAMI et BANAON YASSER
