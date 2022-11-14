package no.kantega;

import org.actioncontroller.actions.POST;
import org.actioncontroller.values.ContentBody;

import java.sql.Connection;

public class MyApiController {
    @POST("/password")
    public void submitPassword(@ContentBody String body) throws Exception {
        String[] passwords = body.split("\n");
        for (String password : passwords) {
            try (Connection connection = App.datasource.getConnection()) {
                DB.executeUpdate(connection,
                        "insert into password (hashed_password, status) values (?, 'todo')",
                        password.trim());
                connection.commit();
            }
        }
    }
}
