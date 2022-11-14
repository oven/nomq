package no.kantega;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

@NotNull class PasswordRowMapper implements DB.RowMapper<Password> {
    @Override
    public Password mapRow(int rowNumber, ResultSet rs) throws SQLException {
        return new Password(
                rs.getInt("id"),
                rs.getString("hashed_password"),
                rs.getString("cleartext_password"),
                rs.getString("status")
        );
    }
}
