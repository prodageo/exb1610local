// for more information, please visit : http://prodageo.insa-rouen.fr/wiki/pmwiki.php?n=FilRouge.CoderTransactionScript

// package lib ;

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


public class jdbcUnitUtilities
{

	private static String jdbcUrl = "" ;
	private static String saut_de_ligne = "\n" ;

	static Connection connection = null ;
	static Statement stmt = null ;

    private jdbcUnitUtilities( String jdbcUrl )
    {
		// recuperer la JDBC URL
		try {
			// jdbcUrl = System.getenv("JDBC_DATABASE_URL");
			connection = DriverManager.getConnection(jdbcUrl);
		}
		catch (Exception e)
		{
			// logger.error ( "JDBC_DATABASE_URL : " + jdbcUrl ) ;
			
		}   
    }   

    public static jdbcUnitUtilities getJdbcUnitUtilities( String jdbcUrl ) {
        return new jdbcUnitUtilities ( jdbcUrl );
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
	
	// vérifier dispo de la library postgreSQL
        try
	{
            Class.forName("org.postgresql.Driver");
        }
	catch (ClassNotFoundException e)
	{
            out.append("<p>La bibliothèque PostgreSQL est absente !</p>");
            e.printStackTrace();
        }

        // auto close connection
        /* try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/test", "postgres", "password")) { */
					
	try
	{
		Connection conn = DriverManager.getConnection( jdbcUrl );
		if (conn != null)
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
		
	return result ;
    }


	public static boolean loadDml ( String filePath)
	{
		return playSqlInsert ( readDml ( filePath ) ) ;
	}
	
    public static boolean playSqlInsert (String sql)
    {
		
		boolean result = true ;

		try
		{
            PreparedStatement pstmt = connection.prepareStatement(sql) ;
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
			result = false ;
        }
		
		return result ;
    }

    public static boolean deleteTableContent (String tableName)
    {
		
		boolean result = true ;
		String sql = "DELETE FROM " + tableName + ";" ;

		try
		{
            PreparedStatement pstmt = connection.prepareStatement(sql) ;
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
			result = false ;
        }
		return result ;
    }
	
	
	
    public static boolean dropTable (String tableName)
    {
		
		boolean result = true ;
		String sql = "DROP TABLE " + tableName + ";" ;

		try
		{
            PreparedStatement pstmt = connection.prepareStatement(sql) ;
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
			result = false ;
        }
		return result ;
    }	
	
    
	// readLineByLineJava8
	private static String readDml (String filePath) 
	{
		StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) 
		{
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return contentBuilder.toString();
	}
  
}
