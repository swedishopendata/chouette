package mobi.chouette.exchange.noptis.importer;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.stip.LineDAO;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.importer.util.NoptisImporterUtils;
import mobi.chouette.exchange.noptis.parser.NoptisLineParser;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.model.stip.Line;
import mobi.chouette.model.util.Referential;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Set;

@Log4j
@Stateless(name = NoptisLineParserCommand.COMMAND)
public class NoptisLineParserCommand implements Command, Constant {

    public static final String COMMAND = "NoptisLineParserCommand";

    @EJB
    private LineDAO lineDAO;

    @SuppressWarnings("unchecked")
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean execute(Context context) throws Exception {
        boolean result = ERROR;
        Monitor monitor = MonitorFactory.start(COMMAND);

        try {

            Referential referential = (Referential) context.get(REFERENTIAL);
            if (referential != null) {
                referential.clear(true);
            }

            Set<Long> lineGids = (Set<Long>) context.get(NOPTIS_LINE_GIDS);
            NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);
            ActionReporter reporter = ActionReporter.Factory.getInstance();

            short dataSourceId = NoptisImporterUtils.getDataSourceId(configuration.getObjectIdPrefix());

            // 1. TODO parse network

            // 2. TODO parse company

            // 3. TODO parse shared timetables

            Line line = lineDAO.findByGid(lineGids.iterator().next());

            log.info(line);

            NoptisLineParser noptisLineParser = (NoptisLineParser) ParserFactory.create(NoptisLineParser.class.getName());
            noptisLineParser.setNoptisLine(line);
            noptisLineParser.parse(context);

            result = SUCCESS;
        } catch (Exception e) {
            log.error("error parsing noptis data: ", e);
            throw e;
        }

        log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
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
        CommandFactory.factories.put(NoptisLineParserCommand.class.getName(),
                new NoptisLineParserCommand.DefaultCommandFactory());
    }

}
