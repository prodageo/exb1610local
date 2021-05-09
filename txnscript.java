// squelette pour mettre au point le client qui l'appelle

import java.util.Date;


// types retournés par les opérations JDBC
/*
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.CallableStatement ; 
import java.sql.Connection ; 
import java.sql.DriverManager ; 
import java.sql.Statement ; 
import java.sql.ResultSet ; 
import java.sql.PreparedStatement ;
import java.sql.CallableStatement ;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class txnscript
{
	
	

// exemple MYSQL LOCAL
	private static String jdbcMysqlMachine = "localhost" ;
	private static String jdbcMysqlDatabase = "exb1610" ;
	private static String jdbcMysqlUser = "root" ;
	private static String jdbcMysqlPass = "tsimiski4" ;
	private static String jdbcMysqlIntricacies = "zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC" ;
	
	private static String jdbcUrl = "" ;
	private static String jdbcMachine = "" ;
	private static String jdbcDatabase = "" ;
	private static String jdbcUser = "" ;
	private static String jdbcPass = "" ;	

	/*
	static Connection cnx = null ;
	static Statement stmt = null ;
	static PreparedStatement pstmt = null ;
	static ResultSet resultSet = null ;
	*/
	
    public static txnscript getTxnscript() {
        return new txnscript();
    }

    // cette fonction doit initialiser la structure (DDL) et les éventuels enregistrements au démarrage
    public static boolean initDb()	
    {
	    boolean the_result = true ;
	    // TODO
	    return the_result ;
    }    
	
    // cette fonction doit vérifier que la connection avec le SGBD est opérationnelle et vérifier que la base est correcte (présence des tables, par exemple)
    public static boolean check()
    {
        boolean the_result = false ;
	// TODO 
	return the_result ;
    }

    /*************** INTERACTION AVEC LA TABLE VILLES ***************/

	
	private villesIterator getVillesIterator(String the_sql)
	{
		// TODO
	}


	public class VilleIterator
	{
		boolean hasNext()
		{
			boolean the_result = false ;
			// TODO
			return true ;
		}

		Integer nextIdVille()
		{
			Integer the_result = 0 ;
			// TODO
			return the_result ;
		}
		
		
		String nextNomVille()
		{
			String the_result ="" ;
			// TODO
			return the_result ;
		}

		Integer nextCodePostalVille()
		{
			Integer the_result = 0 ;
			// TODO
			return the_result ;
		}
	}
	
	
    // insére un enregistrement dans la table Ville : renvoie une chaine avec les valeurs intégrées dans la table
    public static String insertVille (String nom, Integer codePostal)
    {
	    String the_result = "" ;
	    return the_result ;
    }

	
    // recherche tous les enregistrements de la table Ville dont le code postal correspondant
    
    public static VillesIterator searchByCodePostal(Integer searchedCodePostal)
    {
	String the_sql = "" ;
	IteratorVilles the_it = getVillesIterator(String the_sql) ;
	return the_it ;
    }

    public static VillesIterator listVilles()
    {
	String the_sql = "" ;
	IteratorVilles the_it = getVillesIterator(String the_sql) ;
	return the_it ;
    }

    // met à jour un enregistrement existant dans la table Ville : renvoie une chaine avec les valeurs intégrées dans la table	
    public static String updateVille (Integer id, String nom, Integer codePostal)
    {
        String the_result = "" ;
	return the_result ;
    }
	

    // clot la connection au SGBD et renvoie true si la fermeture s'est bien déroulée, false sinon
    public static boolean close()
    {
	boolean the_result = true ;
	return the_result ;
    }
}
