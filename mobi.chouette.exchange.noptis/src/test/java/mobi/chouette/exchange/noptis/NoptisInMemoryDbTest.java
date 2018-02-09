package mobi.chouette.exchange.noptis;

import com.google.common.reflect.ClassPath;
import mobi.chouette.exchange.noptis.model.DestinationDisplay;
import mobi.chouette.exchange.noptis.model.DirectionOfLine;
import mobi.chouette.exchange.noptis.model.Line;
import mobi.chouette.exchange.noptis.model.TransportAuthority;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NoptisInMemoryDbTest {

/*
    private static final String NOPTIS_PREFIX = "Noptis";
    private static NoptisInMemoryDb inMemoryDb;

    @BeforeClass
    static void init() throws Exception {
        inMemoryDb = new NoptisInMemoryDb();
        inMemoryDb.initDb();
        executeSqlFromFile("sql/dbaccesstest.sql");
    }

    public void testEntities_shouldHaveTablesDefined() throws Exception {
        ClassPath classPath = ClassPath.from(ClassLoader.getSystemClassLoader());
        final Set<ClassPath.ClassInfo> entityClasses = classPath.getTopLevelClasses("mobi.chouette.exchange.noptis.model");
        final EntityManager entityManager = getEntityManagerFactory().createEntityManager();

        Set<ClassPath.ClassInfo> concreteEntityClasses = entityClasses.stream()
                .filter(classInfo -> !classInfo.getSimpleName().startsWith(NOPTIS_PREFIX))
                .collect(Collectors.toSet());

        try {
            for (ClassPath.ClassInfo classInfo : concreteEntityClasses) {
                try {
                    entityManager.createQuery("select e from " + classInfo.getSimpleName() + " e").getResultList();
                } catch (Exception e) {
                    Assert.fail("Validation of entity " + classInfo + " failed.", e);
                }
            }
        } finally {
            entityManager.close();
        }
    }

    @Test
    public void getEntityManagerFactory_normalCase_shouldSucceed() {
        NoptisSqlQueryUtils queryUtils = new NoptisSqlQueryUtils(getEntityManagerFactory());
        Assert.assertNotNull(queryUtils.getEntityManagerFactory());
    }

    @Test
    public void query_noParametersDataExists_shouldReturnListOfDestinationDisplays() {
        NoptisSqlQueryUtils queryUtils = new NoptisSqlQueryUtils(getEntityManagerFactory());
        List<DestinationDisplay> destinationDisplays = queryUtils.query("SELECT dd FROM DestinationDisplay dd");
        Assert.assertNotNull(destinationDisplays);
        Assert.assertEquals(8, destinationDisplays.size());
    }

    @Test
    public void query_noParametersDataExists_shouldReturnListOfLines() {
        NoptisSqlQueryUtils queryUtils = new NoptisSqlQueryUtils(getEntityManagerFactory());
        List<Line> lines = queryUtils.query("SELECT l FROM Line l");
        Assert.assertNotNull(lines);
        Assert.assertEquals(10, lines.size());
    }

    @Test
    public void query_noParametersDataExists_shouldReturnListOfDirectionOfLines() {
        NoptisSqlQueryUtils queryUtils = new NoptisSqlQueryUtils(getEntityManagerFactory());
        List<DirectionOfLine> directionOfLines = queryUtils.query("SELECT dol FROM DirectionOfLine dol");
        Assert.assertNotNull(directionOfLines);
        Assert.assertEquals(3, directionOfLines.size());
    }

    @Test
    public void query_noParametersDataExists_shouldReturnListOfTransportAuthorities() {
        NoptisSqlQueryUtils queryUtils = new NoptisSqlQueryUtils(getEntityManagerFactory());
        List<TransportAuthority> transportAuthorities = queryUtils.query("SELECT ta FROM TransportAuthority ta");
        Assert.assertNotNull(transportAuthorities);
        Assert.assertEquals(16, transportAuthorities.size());
    }

    private static EntityManagerFactory getEntityManagerFactory() {
        return inMemoryDb.getEntityManagerFactory();
    }

    protected static void executeSqlFromFile(String sqlFilePath) throws Exception {
        inMemoryDb.executeSqlFromFile(sqlFilePath);
    }

    @AfterClass
    static void tearDown() throws Exception {
        inMemoryDb.tearDownDb();
    }
*/

}