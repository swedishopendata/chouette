package mobi.chouette.exchange.noptis.importer;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.LineDAO;
import mobi.chouette.dao.StopAreaDAO;
import mobi.chouette.dao.VehicleJourneyDAO;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.DummyChecker;
import mobi.chouette.exchange.noptis.JobDataTest;
import mobi.chouette.exchange.noptis.NoptisTestUtils;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ReportConstant;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.persistence.hibernate.ContextHolder;
import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.testng.Assert.assertTrue;

@Log4j
public class NoptisImportIT extends Arquillian implements Constant, ReportConstant {

	protected static InitialContext initialContext;

	@EJB
	private LineDAO lineDao;

	@EJB
	private VehicleJourneyDAO vehicleJourneyDao;

	@EJB
	private StopAreaDAO stopAreaDAO;

	@PersistenceContext(unitName = "referential")
	private EntityManager em;

	@Inject
	UserTransaction utx;

	@Deployment
	public static EnterpriseArchive createDeployment() {
		EnterpriseArchive result;
		File[] files = Maven.resolver().loadPomFromFile("pom.xml").resolve("mobi.chouette:mobi.chouette.exchange.noptis").withTransitivity().asFile();

		List<File> jars = new ArrayList<>();
		List<JavaArchive> modules = new ArrayList<>();

		for (File file : files) {
			if (file.getName().startsWith("mobi.chouette.exchange")) {
				String name = file.getName().split("\\-")[0] + ".jar";
				JavaArchive archive = ShrinkWrap.create(ZipImporter.class, name).importFrom(file).as(JavaArchive.class);
				modules.add(archive);
			} else {
				jars.add(file);
			}
		}

		File[] filesDao = Maven.resolver().loadPomFromFile("pom.xml").resolve("mobi.chouette:mobi.chouette.dao").withTransitivity().asFile();

		if (filesDao.length == 0) {
			throw new NullPointerException("no dao");
		}

		for (File file : filesDao) {
			if (file.getName().startsWith("mobi.chouette.dao")) {
				String name = file.getName().split("\\-")[0] + ".jar";
				JavaArchive archive = ShrinkWrap.create(ZipImporter.class, name).importFrom(file).as(JavaArchive.class);
				modules.add(archive);

				if (!modules.contains(archive))
					modules.add(archive);
			} else {
				if (!jars.contains(file))
					jars.add(file);
			}
		}

		final WebArchive testWar = ShrinkWrap.create(WebArchive.class, "test.war").addAsWebInfResource("postgres-ds.xml").addAsWebInfResource("mssql-ds.xml")//.addAsWebInfResource("h2-ds.xml")
				.addClass(NoptisImportIT.class).addClass(NoptisTestUtils.class).addClass(DummyChecker.class).addClass(JobDataTest.class);

		result = ShrinkWrap.create(EnterpriseArchive.class, "test.ear").addAsLibraries(jars.toArray(new File[0]))
				.addAsModules(modules.toArray(new JavaArchive[0])).addAsModule(testWar).addAsResource(EmptyAsset.INSTANCE, "beans.xml");
		return result;
	}

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
		NoptisImportParameters configuration = new NoptisImportParameters();
		context.put(CONFIGURATION, configuration);
		configuration.setName("name");
		configuration.setUserName("userName");
		configuration.setNoSave(true);
		configuration.setCleanRepository(true);
		configuration.setOrganisationName("organisation");
		configuration.setReferentialName("test");
		configuration.setObjectIdPrefix("ULA");
		JobDataTest jobData = new JobDataTest();
		context.put(JOB_DATA, jobData);
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
		jobData.setType("noptis");
		context.put("testng", "true");
		context.put(OPTIMIZED, Boolean.TRUE);
		return context;
	}

	@Test(groups = { "ImportLine" }, description = "Import Plugin should import file")
	public void verifyImportLine() throws Exception {
		Context context = initImportContext();

		Command command = CommandFactory.create(initialContext, NoptisImporterCommand.class.getName());

		NoptisTestUtils.copyFile("sample_line.xml");

		JobDataTest jobData = (JobDataTest) context.get(JOB_DATA);
		jobData.setInputFilename("sample_line.xml");

		NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);
		configuration.setNoSave(false);
		configuration.setCleanRepository(true);

		boolean result;
		try {
			result = command.execute(context);
		} catch (Exception ex) {
			log.error("test failed", ex);
			throw ex;
		}

		ActionReport report = (ActionReport) context.get(REPORT);
		assertTrue(true);
	}

	@Test(enabled = false, groups = { "ImportLine" }, description = "Import Plugin should import file")
	public void verifyImportCompressedLine() throws Exception {
		Context context = initImportContext();

		NoptisImporterCommand command = (NoptisImporterCommand) CommandFactory.create(
				initialContext, NoptisImporterCommand.class.getName());

		NoptisTestUtils.copyFile("single_line.zip");

		JobDataTest jobData = (JobDataTest) context.get(JOB_DATA);
		jobData.setInputFilename("single_line.zip");

		NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);
		configuration.setNoSave(false);
		configuration.setCleanRepository(true);

		boolean result;
		try {
			result = command.execute(context);
		} catch (Exception ex) {
			log.error("test failed", ex);
			throw ex;
		}

		ActionReport report = (ActionReport) context.get(REPORT);
		assertTrue(true);
	}

	@Test(enabled = false, groups = { "ImportLine" }, description = "Import Plugin should import file")
	public void verifyImportCompressedMultipleLines() throws Exception {
		Context context = initImportContext();

		NoptisImporterCommand command = (NoptisImporterCommand) CommandFactory.create(
				initialContext, NoptisImporterCommand.class.getName());

		NoptisTestUtils.copyFile("multiple_lines.zip");

		JobDataTest jobData = (JobDataTest) context.get(JOB_DATA);
		jobData.setInputFilename("multiple_lines.zip");

		NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);
		configuration.setNoSave(false);
		configuration.setCleanRepository(true);

		boolean result;
		try {
			result = command.execute(context);
		} catch (Exception ex) {
			log.error("test failed", ex);
			throw ex;
		}

		ActionReport report = (ActionReport) context.get(REPORT);
		assertTrue(true);
	}

}
