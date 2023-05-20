drop database PROM;
create database PROM;
use PROM;
CREATE TABLE utilisateurs (
    id_util INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255),
    prenom VARCHAR(255),
    cin CHAR(8) UNIQUE,
    fonction VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    mdp VARCHAR(255),
    tel CHAR(8)UNIQUE
);
CREATE TABLE enseignants (
    id_enseignant INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255),
    prenom VARCHAR(255),
    cin CHAR(8) UNIQUE,
    specialite VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    mdp VARCHAR(255),
    tel CHAR(8) UNIQUE
);
CREATE TABLE groupes (
    id_groupe INT PRIMARY KEY AUTO_INCREMENT,
    nom_groupe VARCHAR(255) UNIQUE
);
CREATE TABLE etudiants (
    id_etud INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255),
    prenom VARCHAR(255),
    cin CHAR(8) UNIQUE,
    num_inscription CHAR(5) UNIQUE,
    email VARCHAR(255) UNIQUE,
    mdp VARCHAR(255),
    tel CHAR(8) UNIQUE,
    id_groupe INT,
    FOREIGN KEY (id_groupe) REFERENCES groupes(id_groupe)
);
CREATE TABLE matieres (
    id_matiere INT PRIMARY KEY AUTO_INCREMENT,
    nom_matiere VARCHAR(255),
    nb_heures FLOAT,
    coef INT,
    id_groupe INT,
    FOREIGN KEY (id_groupe) REFERENCES groupes(id_groupe)
);
CREATE TABLE cours (
    id_cours INT PRIMARY KEY AUTO_INCREMENT,
    id_enseignant INT,
    id_matiere INT,
    id_groupe INT,
    FOREIGN KEY (id_enseignant) REFERENCES enseignants(id_enseignant),
    FOREIGN KEY (id_matiere) REFERENCES matieres(id_matiere),
    FOREIGN KEY (id_groupe) REFERENCES groupes(id_groupe)
);
CREATE TABLE sessions (
    id_session INT PRIMARY KEY AUTO_INCREMENT,
    id_cours INT,
    nb_heures FLOAT,
    date DATE,
    FOREIGN KEY (id_cours) REFERENCES cours(id_cours)
);
CREATE TABLE notes (
    id_etud INT,
    id_groupe INT,
    id_matiere INT,
    note_cc FLOAT,
    note_projet FLOAT,
    note_examen FLOAT,
    moyenne FLOAT,
    FOREIGN KEY (id_etud) REFERENCES etudiants(id_etud),
    FOREIGN KEY (id_groupe) REFERENCES groupes(id_groupe),
    FOREIGN KEY (id_matiere) REFERENCES matieres(id_matiere)
);
CREATE TABLE resultat (
    id_groupe INT,
    id_etud INT,
    moyenne_generale FLOAT,
    FOREIGN KEY (id_groupe) REFERENCES groupes(id_groupe),
    FOREIGN KEY (id_etud) REFERENCES etudiants(id_etud)
);

