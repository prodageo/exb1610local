-- inserer dans ce fichier le script SQL exporte depuis GenMyModel, aprèsa voir modifier les champs id du type INTEGER en SERIAL

-- DEBUT SCRIPT
CREATE TABLE IF NOT EXISTS Personnes
(
    id SERIAL NOT NULL,
    nom VARCHAR(70),
    prenom VARCHAR(70),
    fk_ville INTEGER,
    PRIMARY KEY(id)
);
 
CREATE TABLE IF NOT EXISTS villes
(
    id SERIAL NOT NULL,
    nom VARCHAR(70),
    code_postal INTEGER,
    PRIMARY KEY(id)
);
 
 
-- Create FKs
ALTER TABLE Personnes
    ADD    FOREIGN KEY (fk_ville)
    REFERENCES villes(id)
    MATCH SIMPLE
;
-- FIN SCRIPT
