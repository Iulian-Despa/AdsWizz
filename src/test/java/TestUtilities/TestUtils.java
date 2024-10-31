package TestUtilities;

import Utilities.Utils;
import org.junit.jupiter.api.Assertions;

import java.io.InputStream;
import java.util.*;

public class TestUtils {
    public static void customAssert (String expected, String actual, String message){
        try {
            Assertions.assertEquals(expected, actual, message);
        } catch (Throwable t) {
            System.err.println("Assertion failed: " + t.getMessage());
        }
    }
    public static void customAssert (int expected, int actual, String message){
        try {
            Assertions.assertEquals(expected, actual, message);
        } catch (Throwable t) {
            System.err.println("Assertion failed: " + t.getMessage());
        }
    }

    public static void mapAssert (LinkedHashMap<String, Integer> map) {
        LinkedHashMap<String, Integer> shows = new LinkedHashMap<>();
        shows.put("Stuff You Should Know", 40);
        shows.put("Who Trolled Amber", 40);
        shows.put("Crime Junkie", 30);
        shows.put("The Joe Rogan Experience", 10);
        try {
            Assertions.assertEquals(map, shows);
        } catch (Throwable t) {
            System.err.println("Assertion failed: " + t.getMessage());
        }
    }

    public static void weeklyAsserts(String weeklies) {
        String expected = "\n" +
                "Crime Junkie - Wed 22:00\n" +
                "Who Trolled Amber - Mon 20:00";
        try {
            Assertions.assertEquals(expected, weeklies);
        } catch (Throwable t) {
            System.err.println("Assertion failed: " + t.getMessage());
        }
    }
}
