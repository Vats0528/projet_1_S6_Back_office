#!/bin/bash

# Configuration
PROJECT_NAME="projet_1_s6"
SRC_DIR="src/main/java"
WEBAPP_DIR="src/main/webapp"
LIB_DIR="lib"
BUILD_DIR="build"
CLASSES_DIR="$BUILD_DIR/WEB-INF/classes"
WAR_FILE="$PROJECT_NAME.war"

# Couleurs pour l'affichage
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Créer les répertoires de build
mkdir -p "$CLASSES_DIR"
mkdir -p "$BUILD_DIR/WEB-INF/lib"

# Copier les fichiers webapp
cp -r "$WEBAPP_DIR"/* "$BUILD_DIR/"

# Copier les librairies
cp "$LIB_DIR"/*.jar "$BUILD_DIR/WEB-INF/lib/"

# Télécharger le driver PostgreSQL si nécessaire
POSTGRES_JAR="$BUILD_DIR/WEB-INF/lib/postgresql-42.7.1.jar"
if [ ! -f "$POSTGRES_JAR" ]; then
    echo " Téléchargement du driver PostgreSQL..."
    curl -L -o "$POSTGRES_JAR" "https://jdbc.postgresql.org/download/postgresql-42.7.1.jar" 2>/dev/null
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Driver PostgreSQL téléchargé${NC}"
    else
        echo -e "${RED}✗ Erreur lors du téléchargement du driver PostgreSQL${NC}"
        echo "  Veuillez télécharger manuellement depuis https://jdbc.postgresql.org/download/"
    fi
fi

# Compiler les sources Java
echo " Compilation des sources Java..."
CLASSPATH="$LIB_DIR/*:$BUILD_DIR/WEB-INF/lib/*"

find "$SRC_DIR" -name "*.java" > sources.txt
javac -cp "$CLASSPATH" -d "$CLASSES_DIR" @sources.txt 2>&1

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Compilation réussie${NC}"
    rm sources.txt
else
    echo -e "${RED}✗ Erreur de compilation${NC}"
    rm sources.txt
    exit 1
fi

# Créer le fichier WAR
echo " Création du fichier WAR..."
cd "$BUILD_DIR"
jar -cvf "../$WAR_FILE" . > /dev/null

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Fichier WAR créé: $WAR_FILE${NC}"
else
    echo -e "${RED}✗ Erreur lors de la création du WAR${NC}"
    exit 1
fi

cd ..

echo ""
echo " Pour déployer:"
echo "   1. Copiez $WAR_FILE dans le dossier webapps de Tomcat"
echo "   2. Démarrez Tomcat"
echo "   3. Accédez à http://localhost:8080/$PROJECT_NAME/api/tests"
echo ""
echo " Endpoints API disponibles:"
echo "   GET /api/tests      - Liste tous les tests"
echo "   GET /api/tests/{id} - Récupère un test par ID"
