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
