package no.kantega;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordCrackerTest {

    @Test
    void can_encrypt_and_decrypt() {
        String cleartext = "this is a test";
        String ciphertext = PasswordCracker.crack(cleartext);
        System.out.println(ciphertext);
        assertNotEquals(cleartext, ciphertext);
        assertEquals(cleartext, PasswordCracker.crack(ciphertext));
    }
}
