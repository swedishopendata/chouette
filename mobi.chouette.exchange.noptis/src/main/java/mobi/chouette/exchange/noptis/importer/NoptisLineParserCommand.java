package mobi.chouette.exchange.noptis.importer;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.parser.NoptisLineParser;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.model.stip.Line;
import mobi.chouette.model.util.Referential;

import javax.naming.InitialContext;
import java.io.IOException;

@Log4j
public class NoptisLineParserCommand implements Command, Constant {

    public static final String COMMAND = "NoptisLineParserCommand";

    @Override
    public boolean execute(Context context) throws Exception {
        boolean result = ERROR;
        Monitor monitor = MonitorFactory.start(COMMAND);
        ActionReporter reporter = ActionReporter.Factory.getInstance();

        Referential referential = (Referential) context.get(REFERENTIAL);
        if (referential != null) {
            referential.clear(true);
        }

        try {
            Line line = (Line) context.get(LINE);
            log.info(line);

            //NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);
            //short dataSourceId = NoptisImporterUtils.getDataSourceId(configuration.getObjectIdPrefix());

            // 1. TODO parse network

            // 2. TODO parse company

            // 3. TODO parse shared timetables

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
            return new NoptisLineParserCommand();
        }
    }

    static {
        CommandFactory.factories.put(NoptisLineParserCommand.class.getName(), new DefaultCommandFactory());
    }

}
