import org.junit.Test;
import static org.junit.Assert.* ;

public class unitTestTxnScript2 {

   // init cnx SGBD et base
   private static txnscript txn = txnscript.getTxnscript() ;


   Integer id = txn.getIdVilleByNom ( "MARRAKECH" ) ;
   Integer codePostalAvant = txn.getCodePostalVilleById ( id ) ;
   String display2 = txn.updateVille ( id, "MARRAKECH", 78000 ) ;
   Integer codePostalApres = txn.getCodePostalVilleById ( id ) ;
   
   @Test
   public void testCodePostalaChange() {
	   System.out.println( "" ) ;
	   System.out.println( "codePostalAvant : " + codePostalAvant ) ;
	   System.out.println( "codePostalApres : " + codePostalApres ) ;
	   assertFalse(codePostalAvant.equals ( codePostalApres ) );
   }
   
   @Test
   public void testCodePostalestCeluiPrevu() {
	   assertTrue(codePostalApres == 78000);
   }

}
