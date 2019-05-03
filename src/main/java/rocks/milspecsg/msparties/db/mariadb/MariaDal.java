package rocks.milspecsg.msparties.db.mariadb;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import rocks.milspecsg.msparties.model.exceptions.InvalidMaxSizeException;
import rocks.milspecsg.msparties.model.core.Party;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MariaDal {

    private HikariConfig config = new HikariConfig();
    private final HikariDataSource ds;

    public MariaDal() {
        config.setJdbcUrl("jdbc:mariadb://localhost:3306");
        config.setUsername("test");
        config.setPassword("testpass");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setDriverClassName("org.mariadb.jdbc.Driver");
        config.setMaximumPoolSize(20);
        //config.setAutoCommit(false);
        ds = new HikariDataSource(config);
//        ds.setJdbcUrl("jdbc:mariadb://localhost:3306/db");
//        ds.addDataSourceProperty("user", "root");
//        ds.addDataSourceProperty("password", "myPassword");


        try {
            ds.getConnection().prepareStatement("show databases;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public List<Party> fetchData() throws SQLException {
        String SQL_QUERY = "select * from msparties.t";
        List<Party> parties = null;
        try (Connection con = ds.getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_QUERY);
             ResultSet rs = pst.executeQuery();) {
            parties = new ArrayList<>();
            Party party;
            while (rs.next()) {
                party = new Party();

                party.setMaxSize(rs.getInt("empno"));
                parties.add(party);
            }
        } catch (InvalidMaxSizeException e) {
            e.printStackTrace();
        }
        return parties;
    }


    public CompletableFuture<Boolean> insertOneAsync(String database, String collection) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                //ds.getConnection().prepareStatement("INSERT 'test' into ");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
}
