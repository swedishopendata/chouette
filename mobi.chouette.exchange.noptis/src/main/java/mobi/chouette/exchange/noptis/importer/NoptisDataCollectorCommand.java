package mobi.chouette.exchange.noptis.importer;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.model.stip.Line;
import mobi.chouette.model.util.Referential;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;

@Log4j
@Stateless(name = NoptisDataCollectorCommand.COMMAND)
public class NoptisDataCollectorCommand implements Command, Constant {

    public static final String COMMAND = "NoptisDataCollectorCommand";

    @EJB
    private NoptisDataCollector collector;

    @Override
    public boolean execute(Context context) throws Exception {
        boolean result = ERROR;
        Monitor monitor = MonitorFactory.start(COMMAND);
        ActionReporter reporter = ActionReporter.Factory.getInstance();
        NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);

        try {
            Referential referential = (Referential) context.get(REFERENTIAL);
            if (referential != null) {
                referential.clear(true);
            }

            Line line = (Line) context.get(LINE);
            log.info("procesing line with gid : " + line.getGid());
            context.put(NOPTIS_DATA_CONTEXT, line);

            ImportableNoptisData collection = (ImportableNoptisData) context.get(IMPORTABLE_NOPTIS_DATA);
            if (collection == null) {
                collection = new ImportableNoptisData();
                context.put(IMPORTABLE_NOPTIS_DATA, collection);
            } else {
                collection.clear();
            }

            boolean isValidData = collector.collect(collection, line, configuration.getObjectIdPrefix());
            if (isValidData) {
                result = SUCCESS;
            } else {
                reporter.addErrorToObjectReport(context, String.valueOf(line.getGid()), ActionReporter.OBJECT_TYPE.LINE,
                        ActionReporter.ERROR_CODE.NO_DATA_FOUND, "no data found");
                result = ERROR;
            }
        } catch (Exception e) {
            //reporter.addFileErrorInReport(context, fileName, ActionReporter.FILE_ERROR_CODE.INTERNAL_ERROR, e.toString());
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
                String name = "java:app/mobi.chouette.exchange.netexprofile/" + COMMAND;
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
        CommandFactory.factories.put(NoptisDataCollectorCommand.class.getName(), new NoptisDataCollectorCommand.DefaultCommandFactory());
    }
}
