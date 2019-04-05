package rocks.milspecsg.msparties.common.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class CommonBaseDal {

    private HikariConfig config = new HikariConfig();
    private HikariDataSource ds;

    public CommonBaseDal() {
        config.setJdbcUrl("jdbc_url");
        config.setUsername("database_username");
        config.setPassword("database_password");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }



}
