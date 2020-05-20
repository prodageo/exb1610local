// for more information, please visit : http://prodageo.insa-rouen.fr/wiki/pmwiki.php?n=FilRouge.CoderTransactionScript

import java.util.Date;

// import libinsa.txnscriptUtil ;
// import libinsa.insaLogger ;

// types retournés par les opérations JDBC
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
// import java.sql.ResultSet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class txnscript
{
	
	// coller ici les paramètres issu de Heroku
/*
	private static String jdbcHerokuMachine = "" ;
	private static String jdbcHerokuDatabase = "" ;
	private static String jdbcHerokuUser = "" ;
	private static String jdbcHerokuPass = "" ;
*/
    // adminer4uemf postgres://tbirrsdacaypsu:
/*	
	
	2d90a7a6eaf2a0295d8e0f7dd67d8805d37f0a2fec306e11b58178eca57a841d
	ec2-54-247-85-251.eu-west-1.compute.amazonaws.com
	ddo2st0cqvo7ag
*/

/*
	private static String jdbcHerokuMachine = "ec2-54-246-121-32.eu-west-1.compute.amazonaws.com" ;
	private static String jdbcHerokuDatabase = "d6v79l0erm7t35" ;
	private static String jdbcHerokuUser = "rxfftsrckuwnsp" ;
	private static String jdbcHerokuPass = "a1b7022433ceb48f90e2759a4319f73d3af2bbdee4f214477c90588caf8ae71f" ;	
*/
	
	// associe a adminer4uemf
	private static String jdbcHerokuMachine = "ec2-54-247-85-251.eu-west-1.compute.amazonaws.com" ;
	private static String jdbcHerokuDatabase = "ddo2st0cqvo7ag" ;
	private static String jdbcHerokuUser = "tbirrsdacaypsu" ;
	private static String jdbcHerokuPass = "2d90a7a6eaf2a0295d8e0f7dd67d8805d37f0a2fec306e11b58178eca57a841d" ;	
	

// exemple MYSQL LOCAL
	private static String jdbcMysqlMachine = "localhost" ;
	private static String jdbcMysqlDatabase = "exb1610" ;
	private static String jdbcMysqlUser = "root" ;
	private static String jdbcMysqlPass = "tsimiski4" ;
	private static String jdbcMysqlIntricacies = "zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC" ;
	
	private static String jdbcUrl ;
	
	private static String jdbcMachine = "" ;
	private static String jdbcDatabase = "" ;
	private static String jdbcUser = "" ;
	private static String jdbcPass = "" ;	
	
	static Connection cnx = null ;
	static Statement stmt = null ;
	static PreparedStatement pstmt = null ;
	static ResultSet resultSet = null ;
	// private static insaLogger logger = insaLogger.getLogger(txnscript.class);
	private static String saut_de_ligne = "\n" ;

    private txnscript()
    {
		boolean b = connection() ;
		
	}
	
    private static boolean connection()
    {
		boolean result = true ;
		
		// identifier le pilote charge
		boolean bCheckMySQL = checkMySQL () ;
		if ( bCheckMySQL == true )
		{
			try
			{
				jdbcMachine = jdbcMysqlMachine ;
				jdbcPass = jdbcMysqlPass ;
				jdbcDatabase = jdbcMysqlDatabase ;
				jdbcUser = jdbcMysqlUser ;

				jdbcUrl = "jdbc:mysql://" + jdbcMachine + ":3306/" + jdbcDatabase ;
			
				// https://downloads.mysql.com/docs/connector-j-8.0-en.a4.pdf
				// https://www.developpez.net/forums/d1876029/java/general-java/server-time-zone-non-reconnu/
				jdbcUrl = jdbcUrl + "?user=" + jdbcUser + "&password=" + jdbcPass + "&" + jdbcMysqlIntricacies ;
				
				
				// jdbcUrl = "jdbc:mysql://root:tsimiski4@localhost:3306/exb1610" ;
				cnx = DriverManager.getConnection(jdbcUrl);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				// logger.error ( "JDBC_DATABASE_URL : " + jdbcUrl ) ;
				System.out.println( "txnscript - Probleme de connection MySQL : " + jdbcUrl + " user = " + jdbcUser + " - pass = " + jdbcPass  );
				result = false ;
			}   
		}
		
		boolean bCheckPostgreSQL = checkPostgreSQL () ;
		if ( bCheckPostgreSQL == true )
		{
			jdbcUrl = System.getenv("JDBC_DATABASE_URL");
			
			if ( jdbcUrl == null )
			{
				// execution de simplissimeCmdline hors de HEROKU (local ou Github Actions)
				
				jdbcMachine = jdbcHerokuMachine ;
				jdbcPass = jdbcHerokuPass ;
				jdbcDatabase = jdbcHerokuDatabase ;
				jdbcUser = jdbcHerokuUser ;
				jdbcUrl = "jdbc:postgresql://" + jdbcMachine + ":5432/" + jdbcDatabase + "?user=" + jdbcUser + "&password=" + jdbcPass + "&sslmode=require" ;
			}
			
			try
			{
				cnx = DriverManager.getConnection(jdbcUrl);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				// logger.error ( "JDBC_DATABASE_URL : " + jdbcUrl ) ;
				System.out.println( "txnscript - Probleme de connection PostgreSQL : " + jdbcUrl );
				result = false ;
			}   
		}
		return result ;
    }   

	public static String getJdbcUrl ()
	{
		return jdbcUrl ;
	}

    public static txnscript getTxnscript() {
        return new txnscript();
    }



    public static boolean checkPostgreSQL()
    {
		try
		{
				Class.forName("org.postgresql.Driver");
			return true ;
		}
		catch (ClassNotFoundException e)
		{
			// System.out.println( "\n\nLa bibliotheque PostgreSQL est absente !");
			// e.printStackTrace();
			return false ;
		}
		catch (Exception e) {
			System.out.println( "\nException\n" ) ;
			e.printStackTrace();
			return false ;
		}
	}


    	
    public static boolean initDb()	
    {
		String sql = "DROP TABLE IF EXISTS personnes ;" ; 
		sql = sql + "DROP TABLE IF EXISTS villes ;" ;
	    sql = sql + "CREATE TABLE villes ( id SERIAL NOT NULL, nom VARCHAR(70), code_postal INTEGER, PRIMARY KEY(id) );" ;
		sql = sql + "CREATE TABLE personnes ( id SERIAL NOT NULL, nom VARCHAR(70), prenom VARCHAR(70), fk_ville INTEGER, PRIMARY KEY(id) );" ;
		sql = sql + "ALTER TABLE personnes ADD CONSTRAINT cle_etrangere_personnes_vers_villes FOREIGN KEY (fk_ville) REFERENCES villes(id) MATCH SIMPLE ;" ;

		try
		{
				PreparedStatement pstmt = cnx.prepareStatement(sql) ;
				pstmt.executeUpdate();
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}

	    return true ;
    }    
	
	
	
    public static boolean checkMySQL()
    {
		// mySQL library
		// https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-usagenotes-connect-drivermanager.html
		try
		{
				Class.forName("com.mysql.cj.jdbc.Driver");
				return true ;
		}
		catch (ClassNotFoundException e)
		{
			// System.out.println( "\n\nClassNotFoundException\n" ) ;
			// e.printStackTrace();
			return false ;
		}
		catch (Exception e) {
			System.out.println( "\nException\n" ) ;
			e.printStackTrace();
			return false ;
		}
	}
	
    public static boolean check()
    {
        boolean result = false ;
		StringBuilder out = new StringBuilder() ;
		
		// https://docs.oracle.com/javase/8/docs/api/java/sql/package-summary.html#package.description
        // auto java.sql.Driver discovery -- no longer need to load a java.sql.Driver class via Class.forName

        // register JDBC driver, optional since java 1.6
		if ( jdbcUrl.isEmpty() )
		{
			out.append("<p>Le mot de passe pour accéder à la base est vide. Connectez-vous à https://jdbc4uemf.herokuapp.com/hello?pass=xxx pour avoir les 4 paramètres de connexion de VOTRE base de données.</p>" ) ;
		}
		
		// vérifier dispo d'une bibliotheque mettant en oeuvre l'interface JDBC
		//  postgreSQL library



		boolean bCheckMySQL = checkMySQL () ;
		if ( bCheckMySQL == false )
		{
			System.out.println( "Le pilote MySQL est absent !");
		}
		else
		{
			System.out.println( "Test connection avec pilote MySQL!");
		}
			
		boolean bCheckPostgreSQL = checkPostgreSQL () ;
		if ( bCheckPostgreSQL == false )
		{
			System.out.println( "Le pilote PostgreSQL est absent !");
		}
		else
		{
			System.out.println( "Test connection avec pilote PostgreSQL.");
		}
			
	

					
		try
		{
			Connection cnx = DriverManager.getConnection( jdbcUrl );
			if (cnx != null)
			{
				out.append("<p>Connected to the database!</p>\n");
				result = true ;				
			} else {
				out.append("<p>Failed to make connection!</p>");
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
					
		try
		{
			String[] tables = {"villes", "personnes" }; //thats table names that I need to create if not exists
			DatabaseMetaData metadata = cnx.getMetaData();

			for(int i=0; i< tables.length; i++)
			{
				ResultSet rs = metadata.getTables(null, null, tables[i], null);
				if(!rs.next()) {
					// createTable(tables[i]);
					out.append("/!\\ Table non existante : " + tables[i] );
					System.out.println( "/!\\ Table non existante : " + tables[i] ) ;
				}
				else
				{
					out.append("Table existante : " + tables[i] );
					System.out.println("Table existante : " + tables[i]);
				}
			}
		} catch(SQLException e) {
			System.out.println("checkTables() " + e.getMessage());
		}
		
		
		return result ;
    }

	
    public static String insertVille (String nom, Integer codePostal)
    {
        String result = "" ;
		
		String sql = "INSERT INTO Villes (nom, code_postal) VALUES(?,?)" ;

		try
		{
				PreparedStatement pstmt = cnx.prepareStatement(sql) ;
				pstmt.setString(1, nom);
				pstmt.setDouble(2, codePostal);
				pstmt.executeUpdate();
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
			
		result = result + codePostal ;
		result = result + "/" + nom ;
		result = result + saut_de_ligne ;		
		return result ;
    }
	
    public static String list()
    {
		String result = "" ;
			
		String sql = "SELECT * FROM Villes";

		try
		{
			PreparedStatement preparedStatement = cnx.prepareStatement(sql) ;
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				Integer id = resultSet.getInt("id");
				result = result + "/" + id ;
				Integer codePostal = resultSet.getInt("code_postal");
				result = result + "/" + codePostal ;
				String name = resultSet.getString("nom");
				result = result + "/" + name ;
				result = result + saut_de_ligne ;
				// Timestamp createdDate = resultSet.getTimestamp("CREATED_DATE");
			}
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
	    
		return result ;
    }


    public static String searchByCodePostal(Integer searchedCodePostal)
    {
		String result = "" ;
			
		String sql = "SELECT * FROM Villes WHERE code_postal = ?" ;

		try
		{
			PreparedStatement preparedStatement = cnx.prepareStatement(sql) ;
			preparedStatement.setInt(1, searchedCodePostal);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				Integer id = resultSet.getInt("id");
				result = result + "/" + id ;
				Integer codePostal = resultSet.getInt("code_postal");
				result = result + "/" + codePostal ;
				String name = resultSet.getString("nom");
				result = result + "/" + name ;
				result = result + saut_de_ligne ;
			}
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
	    
		return result ;
    }

	



    public static Integer getIdVilleByNom(String nom)
    {

		Integer result = 0 ;
/*
 		String sql = "SELECT * FROM Villes WHERE nom = ?" ;

		try
		{
			PreparedStatement preparedStatement = cnx.prepareStatement(sql) ;
			preparedStatement.setString(1, nom);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				result = resultSet.getInt("id");
			}
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
*/    

        /*
		   java.sql.Types les plus frequents
		     VARCHAR (12)
			 CHAR (1)
			 NUMERIC (2)
			 DECIMAL (3)
			 INTEGER (4)
		
		*/

 		String sql = "{? = call idVille(?)}" ;

		try
		{
			CallableStatement stmt=cnx.prepareCall(sql);  
			stmt.setString(2,nom);  
			stmt.registerOutParameter(1,Types.INTEGER);  
			stmt.execute();  
			result = stmt.getInt(1) ;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
		return result ;

		
    }

    // select * from codePostalVille ( 5 ) ;
    public static Integer getCodePostalVilleById(Integer id)
    {

		Integer result = 0 ;

 		String sql = "{? = call codePostalVille(?)}" ;

		try
		{
			CallableStatement stmt=cnx.prepareCall(sql);  
			stmt.setInt(2,id);  
			stmt.registerOutParameter(1,Types.INTEGER);  
			stmt.execute();  
			result = stmt.getInt(1) ;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
		return result ;
    }
	
	
	












	
    public static String updateVille (Integer id, String nom, Integer codePostal)
    {
        String result = "" ;
		
		String sql = "UPDATE Villes SET nom = ?, code_postal = ? WHERE id = ?" ;

		try
		{
				PreparedStatement pstmt = cnx.prepareStatement(sql) ;
				pstmt.setString(1, nom);
				pstmt.setDouble(2, codePostal);
				pstmt.setInt(3, id);
				pstmt.executeUpdate();
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
			
		result = result + id ;
		result = result + "/" + codePostal ;
		result = result + "/" + nom ;
		result = result + saut_de_ligne ;		
		return result ;
    }
	


	
	
	
	
	
	
	
    public List <dtoVille> getVilles()
	{
		List<dtoVille> result = new ArrayList<dtoVille>() ; 
        String sql = "SELECT * FROM listVilles ()";
        try
		{
			PreparedStatement pstmt = cnx.prepareStatement(sql) ;
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
				dtoVille currentDtoVille = new dtoVille ( rs.getString("nom") , rs.getInt("code_postal") ) ;
				result.add ( currentDtoVille ) ;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		return result ;
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    public static String close()
    {
		String result = "" ;
		boolean isClotureOK = true ;
	
		try
		{
			if(resultSet!=null)
			{
				resultSet.close();
				resultSet=null;
			}
		}
		catch (SQLException e)
		{
			isClotureOK = false ;
			e.printStackTrace();
		}
		
		try
		{
			if(stmt!=null)
			{
				stmt.close();
				stmt=null;
			}
		}
		catch (SQLException e)
		{
			isClotureOK = false ;
			e.printStackTrace();
		}
		
		try
		{
			if(pstmt!=null)
			{
				pstmt.close();
				pstmt=null;
			}
		}
		catch (SQLException e)
		{
			isClotureOK = false ;
			e.printStackTrace();
		}

		try
		{
			if(cnx!=null)
			{
				cnx.close();
				cnx=null;
			}
		}
		catch (SQLException e)
		{
			isClotureOK = false ;
			e.printStackTrace();
		}

		if ( isClotureOK == true )
		{
			result = "Cloture objets JDBC OK" ;
		}
		else
		{
			result = "Cloture objets JDBC KO" ;
		}

		return result ;
	}
}
