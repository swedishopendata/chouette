package mobi.chouette.exchange.noptis;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.exception.LockException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.log4j.Log4j;
import org.h2.jdbcx.JdbcDataSource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j
public class NoptisInMemoryDb {

    private static final String DATASOURCE_URL = "jdbc:h2:mem:stip-inmemory;INIT=CREATE SCHEMA IF NOT EXISTS dbo;";
    private Database liquibaseDb;
    private Liquibase liquibase;
    private EntityManagerFactory entityManagerFactory;

    public NoptisInMemoryDb() {
        entityManagerFactory = Persistence.createEntityManagerFactory("stip-inmemory");
    }

    public void initDb() throws LiquibaseException, SQLException {
        // Create h2 datasource
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl(DATASOURCE_URL);

        log.debug("Setting up in-memory database.");
        liquibaseDb = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()));
        liquibaseDb.setDefaultSchemaName("dbo");
        log.debug("Default schema is set");
        liquibase = new Liquibase("org/liquibase/stip.changelog.xml", new ClassLoaderResourceAccessor(), liquibaseDb);
        log.debug("Liquibase created");
        log.debug("Now calling Liquibase update...");
        liquibase.update("test,!prod");
        log.debug("Liquibase update finished");

    }

    public void tearDownDb() throws DatabaseException, LockException {
        entityManagerFactory.close();
        log.debug("Tearing down in-memory database.");
        liquibase.dropAll();
        liquibaseDb.close();
    }

    public void executeSqlFromFile(String sqlFilePath) throws URISyntaxException, IOException {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            URI uri = ClassLoader.getSystemResource(sqlFilePath).toURI();
            log.debug("Running database script " + sqlFilePath + " on classpath");

            final EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            try (Stream<String> sqlStream = Files.lines(Paths.get(uri))) {
                String sql = sqlStream
                        .filter(line -> !line.startsWith("--"))
                        .collect(Collectors.joining("\n"));
                entityManager.createNativeQuery(sql).executeUpdate();
            }
            transaction.commit();
        } finally {
            entityManager.close();
        }
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }


}
