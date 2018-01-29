package mobi.chouette.exchange.noptis.importer;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.LineDAO;
import mobi.chouette.dao.VehicleJourneyDAO;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ObjectReport;
import mobi.chouette.exchange.report.ReportConstant;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.Line;
import mobi.chouette.model.util.Referential;
import mobi.chouette.persistence.hibernate.ContextHolder;
import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

@Log4j
public class NoptisImportTests extends Arquillian implements Constant, ReportConstant {

    @EJB
    LineDAO lineDao;

    @EJB
    VehicleJourneyDAO vehicleJourneyDao;

    @PersistenceContext(unitName = "referential")
    EntityManager em;

    @Inject
    UserTransaction utx;

    @Deployment
    public static EnterpriseArchive createDeployment() {
        return null;
    }

    private static InitialContext initialContext;

    protected void init() {
        Locale.setDefault(Locale.ENGLISH);
        if (initialContext == null) {
            try {
                initialContext = new InitialContext();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
    }

    protected Context initImportContext() {
        init();
        ContextHolder.setContext("chouette_gui"); // set tenant schema

        Context context = new Context();
        context.put(INITIAL_CONTEXT, initialContext);
        context.put(REPORT, new ActionReport());
        context.put(VALIDATION_REPORT, new ValidationReport());

/*
        NeptuneImportParameters configuration = new NeptuneImportParameters();
        context.put(CONFIGURATION, configuration);
        configuration.setName("name");
        configuration.setUserName("userName");
        configuration.setNoSave(true);
        configuration.setCleanRepository(true);
        configuration.setOrganisationName("organisation");
        configuration.setReferentialName("test");

        JobDataTest jobData = new JobDataTest();
        context.put(JOB_DATA,jobData);
        jobData.setPathName("target/referential/test");
        File f = new File("target/referential/test");

        if (f.exists())
            try {
                FileUtils.deleteDirectory(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        f.mkdirs();

        jobData.setReferential("chouette_gui");
        jobData.setAction(IMPORTER);
        jobData.setType( "neptune");
        context.put("testng", "true");
        context.put(OPTIMIZED, Boolean.FALSE);
*/

        return context;
    }

    @Test(groups = { "ImportLine" }, description = "Import Plugin should import file")
    public void verifyImportLine() throws Exception {
        Context context = initImportContext();
        NoptisImporterCommand command = (NoptisImporterCommand) CommandFactory.create(initialContext, NoptisImporterCommand.class.getName());
    }

}
