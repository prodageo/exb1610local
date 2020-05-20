import org.junit.Test;
import static org.junit.Assert.* ;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;



// pour tester le transaction script
public class unitTestTxnScript {

   // init cnx SGBD et base
   private static txnscript txn = txnscript.getTxnscript() ;
   private static String jdbcUrl = txnscript.getJdbcUrl () ;

   // tester la méthode list (en fonction du contenu du DML)
   String display = txn.list () ;
   
/*
   @Test
   public void testdBConnection() {
	   assertTrue( testCnx );
   } 
*/

   @Test
   public void testListWithMarrakech() {
	   assertTrue(display.contains("MARRAKECH"));
   }

   @Test
   public void testListIsWithParis() {
	   assertTrue(display.contains("PARIS"));
   }


   @Test
   public void testListMissingWashington() {
	   assertFalse(display.contains("WASHINGTON"));
   }

   @Test
   public void testAppelTableFunction() {
	List <dtoVille> list = txn.getVilles() ;
	Integer n = list.size() ;
	System.out.println ( n ) ;
	assertTrue( n == 5 ) ;
   }

}