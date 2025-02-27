# Installation
### Installer java 21
-	ubuntu : sudo apt install openjdk-21-jdk
-	mac : https://docs.oracle.com/en/java/javase/21/install/installation-jdk-macos.html#GUID-2FE451B0-9572-4E38-A1A5-568B77B146DE
### Vérifier l'installation 
`java -version`
### Cloner le dépôt
### Aller dans le répertoire créé
### Lancer 
`./mvnw spring-boot:run`
	
# Test coverage
`./mvnw clean verify`
`python3 -m http.server 8080`
http://0.0.0.0:8080/target/site/jacoco/index.html