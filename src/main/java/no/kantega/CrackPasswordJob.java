package no.kantega;

import java.sql.Connection;

public class CrackPasswordJob implements Runnable {

    private final Password password;

    public CrackPasswordJob(Password password) {
        this.password = password;
    }

    @Override
    public void run() {
        String cleartextPassword = PasswordCracker.crack(password.getHashedPassword());
        System.out.println("Cracked password: " + password.getHashedPassword() + " was " + cleartextPassword + " in clear text");

        try (Connection connection = App.datasource.getConnection()) {
            DB.executeUpdate(connection, """
                    update password
                    set    cleartext_password = ?, status = 'done'
                    where  id = ?
                    """, cleartextPassword, password.getId());
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
