package mobi.chouette.exchange.noptis.importer;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.stip.LineDAO;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.importer.util.NoptisReferential;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.exchange.validation.ValidationData;
import mobi.chouette.model.util.Referential;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;

@Log4j
@Stateless(name = NoptisInitImportCommand.COMMAND)
public class NoptisInitImportCommand implements Command, Constant {

    public static final String COMMAND = "NoptisInitImportCommand";

    @EJB
    private LineDAO lineDAO;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean execute(Context context) throws Exception {
        boolean result = ERROR;
        Monitor monitor = MonitorFactory.start(COMMAND);

        try {
            context.put(REFERENTIAL, new Referential());

            NoptisReferential noptisReferential = new NoptisReferential();
            context.put(NOPTIS_REFERENTIAL, noptisReferential);

            context.put(VALIDATION_DATA, new ValidationData());
            context.put(OPTIMIZED, false);

            ActionReporter reporter = ActionReporter.Factory.getInstance();
            reporter.addObjectReport(context, "merged", ActionReporter.OBJECT_TYPE.NETWORK, "networks", ActionReporter.OBJECT_STATE.OK, IO_TYPE.INPUT);
            reporter.addObjectReport(context, "merged", ActionReporter.OBJECT_TYPE.STOP_AREA, "stop areas", ActionReporter.OBJECT_STATE.OK, IO_TYPE.INPUT);
            reporter.addObjectReport(context, "merged", ActionReporter.OBJECT_TYPE.COMPANY, "companies", ActionReporter.OBJECT_STATE.OK, IO_TYPE.INPUT);
            reporter.addObjectReport(context, "merged", ActionReporter.OBJECT_TYPE.CONNECTION_LINK, "connection links", ActionReporter.OBJECT_STATE.OK, IO_TYPE.INPUT);
            reporter.addObjectReport(context, "merged", ActionReporter.OBJECT_TYPE.ACCESS_POINT, "access points", ActionReporter.OBJECT_STATE.OK, IO_TYPE.INPUT);
            reporter.addObjectReport(context, "merged", ActionReporter.OBJECT_TYPE.TIMETABLE, "calendars", ActionReporter.OBJECT_STATE.OK, IO_TYPE.INPUT);

            result = SUCCESS;
        } catch (Exception e) {
            log.error(e, e);
            throw e;
        } finally {
            log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
        }

        return result;
    }

    public static class DefaultCommandFactory extends CommandFactory {

        @Override
        protected Command create(InitialContext context) throws IOException {
            Command result = null;
            try {
                String name = "java:app/mobi.chouette.exchange.noptis/" + COMMAND;
                result = (Command) context.lookup(name);
            } catch (NamingException e) {
                String name = "java:module/" + COMMAND;
                try {
                    result = (Command) context.lookup(name);
                } catch (NamingException e1) {
                    log.error(e);
                }
            }
            return result;
        }
    }

    static {
        CommandFactory.factories.put(NoptisInitImportCommand.class.getName(), new NoptisInitImportCommand.DefaultCommandFactory());
    }

}
