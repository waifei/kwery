package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class V14__ implements JdbcMigration {
    @Override
    public void migrate(Connection connection) throws Exception {
        try (PreparedStatement ps = connection.prepareStatement("insert into installation_detail (installation_epoch) values (?)")) {
            ps.setLong(1, System.currentTimeMillis());
            ps.executeUpdate();
        }
    }
}