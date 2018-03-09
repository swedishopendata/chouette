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
import mobi.chouette.exchange.noptis.parser.NoptisTransportAuthorityParser;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.model.Network;
import mobi.chouette.model.stip.Line;
import mobi.chouette.model.stip.TransportAuthority;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

import javax.naming.InitialContext;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

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

            NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);
            //short dataSourceId = NoptisImporterUtils.getDataSourceId(configuration.getObjectIdPrefix());

            // Network
            if (Objects.requireNonNull(referential).getSharedPTNetworks().isEmpty()) {
                createPTNetwork(referential, configuration);
                reporter.addObjectReport(context, "merged", ActionReporter.OBJECT_TYPE.NETWORK,
                        "networks", ActionReporter.OBJECT_STATE.OK, IO_TYPE.INPUT);
                reporter.setStatToObjectReport(context, "merged", ActionReporter.OBJECT_TYPE.NETWORK,
                        ActionReporter.OBJECT_TYPE.NETWORK, referential.getSharedPTNetworks().size());
            }

            TransportAuthority transportAuthority = (TransportAuthority) context.get(TRANSPORT_AUTHORITY);
            log.info(transportAuthority);

            // Company
            if (referential.getSharedCompanies().isEmpty()) {
                NoptisTransportAuthorityParser transportAuthorityParser = (NoptisTransportAuthorityParser)
                        ParserFactory.create(NoptisTransportAuthorityParser.class.getName());
                transportAuthorityParser.setTransportAuthority(transportAuthority);
                transportAuthorityParser.parse(context);
                reporter.addObjectReport(context, "merged", ActionReporter.OBJECT_TYPE.COMPANY,
                        "companies", ActionReporter.OBJECT_STATE.OK, IO_TYPE.INPUT);
                reporter.setStatToObjectReport(context, "merged", ActionReporter.OBJECT_TYPE.COMPANY,
                        ActionReporter.OBJECT_TYPE.COMPANY, referential.getSharedCompanies().size());
            }

            // 3. TODO parse shared timetables

            // TODO A calendar in noptis is based on specific vehicle journeys (vehicle journey id) and should be processed during vehicle journey parser only

            // Timetable
            if (referential.getSharedTimetables().isEmpty()) {
                //GtfsCalendarParser gtfsCalendarParser = (GtfsCalendarParser) ParserFactory.create(GtfsCalendarParser.class.getName());
                //gtfsCalendarParser.parse(context);
                reporter.addObjectReport(context, "merged", ActionReporter.OBJECT_TYPE.TIMETABLE,
                        "time tables", ActionReporter.OBJECT_STATE.OK, IO_TYPE.INPUT);
                reporter.setStatToObjectReport(context, "merged", ActionReporter.OBJECT_TYPE.TIMETABLE,
                        ActionReporter.OBJECT_TYPE.TIMETABLE, referential.getSharedTimetables().size());
            }

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

    private Network createPTNetwork(Referential referential, NoptisImportParameters configuration) {
        String prefix = configuration.getObjectIdPrefix();
        String ptNetworkId = prefix + ":" + Network.PTNETWORK_KEY + ":" + prefix;
        Network ptNetwork = ObjectFactory.getPTNetwork(referential, ptNetworkId);
        ptNetwork.setVersionDate(Calendar.getInstance().getTime());
        ptNetwork.setName(prefix);
        ptNetwork.setRegistrationNumber(prefix);
        ptNetwork.setSourceName("NOPTIS");
        return ptNetwork;
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
