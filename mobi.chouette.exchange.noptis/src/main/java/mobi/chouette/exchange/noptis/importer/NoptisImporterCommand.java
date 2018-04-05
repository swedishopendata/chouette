package mobi.chouette.exchange.noptis.importer;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.CommandCancelledException;
import mobi.chouette.exchange.ProcessingCommands;
import mobi.chouette.exchange.ProcessingCommandsFactory;
import mobi.chouette.exchange.ProgressionCommand;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.model.util.Referential;

import javax.naming.InitialContext;
import java.io.IOException;

@Log4j
public class NoptisImporterCommand extends AbstractNoptisImporterCommand implements Command, Constant {

    public static final String COMMAND = "NoptisImporterCommand";

    @Override
    public boolean execute(Context context) throws Exception {
        boolean result = SUCCESS;
        Monitor monitor = MonitorFactory.start(COMMAND);

        InitialContext initialContext = (InitialContext) context.get(INITIAL_CONTEXT);
        ActionReporter actionReporter = ActionReporter.Factory.getInstance();

        context.put(REFERENTIAL, new Referential());

        // initialize reporting and progression
        ProgressionCommand progression = (ProgressionCommand) CommandFactory.create(initialContext,
                ProgressionCommand.class.getName());

        try {

            Object configuration = context.get(CONFIGURATION);
            if (!(configuration instanceof NoptisImportParameters)) {
                // fatal wrong parameters
                log.error("invalid parameters for noptis import " + configuration.getClass().getName());
                actionReporter.setActionError(context, ActionReporter.ERROR_CODE.INVALID_PARAMETERS, "invalid parameters for noptis import " + configuration.getClass().getName());
                return false;
            }

            ProcessingCommands commands = ProcessingCommandsFactory.create(NoptisImporterProcessingCommands.class.getName());
            result = process(context, commands, progression, true, Mode.line);

        } catch (CommandCancelledException e) {
            actionReporter.setActionError(context, ActionReporter.ERROR_CODE.INTERNAL_ERROR, "Command cancelled");
            log.error(e.getMessage());
        } catch (Exception e) {
            String fileName = (String) context.get(FILE_NAME);
            log.error("Error parsing Noptis file " + fileName + ": " + e.getMessage(), e);
            actionReporter.setActionError(context, ActionReporter.ERROR_CODE.INTERNAL_ERROR, "Internal error while parsing Noptis files: " + e.toString());
        } finally {
            progression.dispose(context);
            log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        }

        return result;
    }

    public static class DefaultCommandFactory extends CommandFactory {

        @Override
        protected Command create(InitialContext context) throws IOException {
            Command result = new NoptisImporterCommand();
            return result;
        }
    }

    static {
        CommandFactory.factories.put(NoptisImporterCommand.class.getName(), new DefaultCommandFactory());
    }

}
