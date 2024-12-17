package com.mycompany.codereview;

import com.mycompany.codeReview.AwesomePasswordChecker;
/*import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;*/
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
/*import java.io.IOException;*/
import org.junit.jupiter.api.Test;

/**
 * Test class for AwesomePasswordChecker.
 */
public class AwesomePasswordCheckerTest {

    /**
     * Test of getInstance method with file parameter.
     *
     * @throws Exception if an error occurs during test execution
     */
    @Test
    public void testGetInstance_File() throws Exception {
        System.out.println("getInstance");

        File file = new File("src/test/resources/cluster_centers_HAC_aff.csv");
        assertTrue(file.exists(), "Le fichier de centres de clusters doit exister pour le test.");

        AwesomePasswordChecker result = AwesomePasswordChecker.getInstance(file);
        assertNotNull(result, "L'instance obtenue ne doit pas être null.");

        AwesomePasswordChecker secondResult = AwesomePasswordChecker.getInstance(file);
        assertSame(result, secondResult, "La méthode getInstance doit retourner la même instance.");
    }

    /**
     * Test of getInstance method without arguments.
     *
     * @throws Exception if an error occurs during test execution
     */
    /*@Test
    public void testGetInstance_0args() throws Exception {
        System.out.println("getInstance with no arguments");

        AwesomePasswordChecker result = AwesomePasswordChecker.getInstance();
        assertNotNull(result, "L'instance obtenue ne doit pas être null.");

        AwesomePasswordChecker secondResult = AwesomePasswordChecker.getInstance();
        assertSame(result, secondResult, "La méthode getInstance doit retourner la même instance.");
    }

    /**
     * Test of maskAff method.
     *
     * @throws IOException if an error occurs during initialization
     */
    /*
    @Test
    public void testMaskAff() throws IOException {
        System.out.println("maskAff");

        String password = "Password123!";
        AwesomePasswordChecker instance = AwesomePasswordChecker.getInstance();

        int[] expectedMask = {4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 6, 6};
        int[] result = instance.maskAff(password);

        assertArrayEquals(expectedMask, result, "Le masque généré doit correspondre au masque attendu.");
    }*/

    /**
     * Test of getDistance method.
     *
     * @throws Exception if an error occurs during initialization
     */
    /*
    @Test
    public void testGetDistance() throws Exception {
        System.out.println("getDistance");

        String password = "Password123!";
        AwesomePasswordChecker instance = AwesomePasswordChecker.getInstance();

        double result = instance.getDistance(password);
        assertTrue(result > 0, "La distance calculée doit être supérieure à zéro.");
    }*/

    /**
     * Test of computeMd5 method.
     */
    /*  @Test
    public void testComputeMd5() {
    System.out.println("computeMd5");
    
    // Entrée et résultat attendu
    String input = "Password123!";
    String expectedMd5 = "42f749ade7f9e195bf475f37a44cafcb";
    
    String result = AwesomePasswordChecker.computeMd5(input);
    assertEquals(expectedMd5, result, "Le hash MD5 calculé doit correspondre au résultat attendu.");
    }*/


    /**
     * Test of main method.
     */
    @Test
    public void testMain() {
        System.out.println("main");

        String[] args = {};
        assertDoesNotThrow(() -> AwesomePasswordChecker.main(args),
                "La méthode main doit s'exécuter sans erreur.");
    }
}
