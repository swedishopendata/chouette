package mobi.chouette.exchange.noptis.importer;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.stip.LineDAO;
import mobi.chouette.dao.stip.StopAreaDAO;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.importer.util.NoptisImporterUtils;
import mobi.chouette.exchange.noptis.parser.NoptisLineParser;
import mobi.chouette.exchange.noptis.parser.NoptisStopAreaParser;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.model.stip.Line;
import mobi.chouette.model.stip.StopArea;
import mobi.chouette.model.util.Referential;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Log4j
@Stateless(name = NoptisStopAreaParserCommand.COMMAND)
public class NoptisStopAreaParserCommand implements Command, Constant {

    public static final String COMMAND = "NoptisStopAreaParserCommand";

    @EJB
    private StopAreaDAO stopAreaDAO;

    @Override
    public boolean execute(Context context) throws Exception {
        boolean result = ERROR;
        Monitor monitor = MonitorFactory.start(COMMAND);

        try {
            Referential referential = (Referential) context.get(REFERENTIAL);
            if (referential != null) {
                referential.clear(true);
            }

            NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);
            short dataSourceId = NoptisImporterUtils.getDataSourceId(configuration.getObjectIdPrefix());

            List<StopArea> stopAreas = stopAreaDAO.findByDataSourceId(dataSourceId);

            // StopArea
            if (Objects.requireNonNull(referential).getSharedStopAreas().isEmpty()) {
                NoptisStopAreaParser noptisStopAreaParser = (NoptisStopAreaParser) ParserFactory.create(NoptisStopAreaParser.class.getName());
                noptisStopAreaParser.setNoptisStopAreas(stopAreas);
                noptisStopAreaParser.parse(context);
            }

            // ConnectionLink
/*
            if (importer.hasTransferImporter()) {
                if (referential.getSharedConnectionLinks().isEmpty()) {
                    GtfsTransferParser gtfsTransferParser = (GtfsTransferParser) ParserFactory
                            .create(GtfsTransferParser.class.getName());
                    gtfsTransferParser.parse(context);
                }
            }
            if (configuration.getMaxDistanceForCommercial() > 0) {
                CommercialStopGenerator commercialStopGenerator = new CommercialStopGenerator();
                commercialStopGenerator.createCommercialStopPoints(context);
            }

            if (configuration.getMaxDistanceForConnectionLink() > 0) {
                ConnectionLinkGenerator connectionLinkGenerator = new ConnectionLinkGenerator();
                connectionLinkGenerator.createConnectionLinks(context);

            }

            addStats(context, referential);
*/

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
        CommandFactory.factories.put(NoptisStopAreaParserCommand.class.getName(),
                new NoptisStopAreaParserCommand.DefaultCommandFactory());
    }

}
