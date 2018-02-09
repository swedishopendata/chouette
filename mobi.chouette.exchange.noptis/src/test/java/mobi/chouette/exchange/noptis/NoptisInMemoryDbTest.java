package mobi.chouette.exchange.noptis;

import com.google.common.reflect.ClassPath;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Set;
import java.util.stream.Collectors;

public class NoptisInMemoryDbTest {

    private static final String NOPTIS_PREFIX = "Noptis";
    private static NoptisInMemoryDb inMemoryDb;

    @BeforeClass
    static void init() throws Exception {
        inMemoryDb = new NoptisInMemoryDb();
        inMemoryDb.initDb();
        executeSqlFromFile("sql/dbaccesstest.sql");
    }

    @Test
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

}