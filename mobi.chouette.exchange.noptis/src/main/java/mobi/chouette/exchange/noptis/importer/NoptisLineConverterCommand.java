package mobi.chouette.exchange.noptis.importer;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.converter.NoptisLineConverter;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.model.stip.Line;
import mobi.chouette.model.util.Referential;

import javax.naming.InitialContext;
import java.io.IOException;

@Log4j
public class NoptisLineConverterCommand implements Command, Constant {

    public static final String COMMAND = "NoptisLineConverterCommand";

    @Override
    public boolean execute(Context context) throws Exception {
        boolean result = ERROR;
        Monitor monitor = MonitorFactory.start(COMMAND);
        ActionReporter reporter = ActionReporter.Factory.getInstance();

/*
        String fileName = path.getFileName().toString();
        reporter.addFileReport(context, fileName, IO_TYPE.INPUT);
        context.put(FILE_NAME, fileName);
*/

        try {
            Referential referential = (Referential) context.get(REFERENTIAL);
            if (referential != null) {
                referential.clear(true);
            }

            Line line = (Line) context.get(LINE);
            log.info("procesing line " + line.getGid());

            NoptisLineConverter converter = (NoptisLineConverter) ConverterFactory.create(NoptisLineConverter.class.getName());
            converter.convert(context);

            Context validationContext = (Context) context.get(VALIDATION_CONTEXT);
            //addStats(context, reporter, validationContext, referential);
            //reporter.setFileState(context, fileName, IO_TYPE.INPUT, ActionReporter.FILE_STATE.OK);
            result = SUCCESS;
        } catch (Exception e) {
            //reporter.addFileErrorInReport(context, fileName, ActionReporter.FILE_ERROR_CODE.INTERNAL_ERROR, e.toString());
            throw e;
        } finally {
            log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
        }

        return result;    }

    public static class DefaultCommandFactory extends CommandFactory {

        @Override
        protected Command create(InitialContext context) throws IOException {
            return new NoptisLineConverterCommand();
        }
    }

    static {
        CommandFactory.factories.put(NoptisLineConverterCommand.class.getName(), new DefaultCommandFactory());
    }
}
