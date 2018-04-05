package mobi.chouette.exchange.noptis.importer;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.exchange.ProcessingCommands;
import mobi.chouette.exchange.ProgressionCommand;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.model.stip.DataSource;
import mobi.chouette.model.util.Referential;

import javax.ejb.EJB;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static mobi.chouette.exchange.noptis.Constant.NOPTIS_DATA_SOURCE_ID;

@Log4j
public class AbstractNoptisImporterCommand implements Constant {

    @EJB
    private NoptisDaoReader daoReader;

    protected enum Mode {
        line, stopareas
    }

    @SuppressWarnings("unchecked")
    public boolean process(Context context, ProcessingCommands commands, ProgressionCommand progression,
                           boolean continueProcesingOnError, Mode mode) throws Exception {
        boolean result = ERROR;
        boolean disposeResult = SUCCESS;
        ActionReporter reporter = ActionReporter.Factory.getInstance();
        NoptisImportParameters parameters = (NoptisImportParameters) context.get(CONFIGURATION);

        try {
            // Initialization
            List<? extends Command> preProcessingCommands = commands.getPreProcessingCommands(context, true);
            progression.initialize(context, preProcessingCommands.size() + 1);
            for (Command importCommand : preProcessingCommands) {
                result = importCommand.execute(context);
                if (!result) {
                    if (!reporter.hasActionError(context))
                        reporter.setActionError(context, ActionReporter.ERROR_CODE.NO_DATA_FOUND, "no data to import");
                    progression.execute(context);
                    return ERROR;
                }
                progression.execute(context);
            }

            if (mode.equals(Mode.line)) {
                Referential referential = (Referential) context.get(REFERENTIAL);
                if (referential != null) {
                    referential.clear(true);
                }

                DataSource dataSource = daoReader.loadDataSource(parameters.getObjectIdPrefix());
                short dataSourceId = (short) dataSource.getId().longValue();
                context.put(NOPTIS_DATA_SOURCE_ID, dataSourceId);

                // process stop areas
                List<? extends Command> stopProcessingCommands = commands.getStopAreaProcessingCommands(context, true);
                progression.start(context, stopProcessingCommands.size());

                for (Command command : stopProcessingCommands) {
                    result = command.execute(context);
                    if (!result) {
                        return ERROR;
                    }
                    progression.execute(context);
                }

                // process lines
                Set<Long> lineIds = daoReader.loadLineIds(dataSourceId);
                if (lineIds.isEmpty()) {
                    reporter.setActionError(context, ActionReporter.ERROR_CODE.NO_DATA_FOUND, "no data selected");
                    return ERROR;
                }
                progression.execute(context);

                List<? extends Command> lineProcessingCommands = commands.getLineProcessingCommands(context, true);

                int lineCount = 0;

                for (Long lineId : lineIds) {

                    context.put(LINE_ID, lineId);
                    boolean importFailed = false;

                    if (lineProcessingCommands.size() > 0) {
                        progression.start(context, lineProcessingCommands.size());
                        for (Command importCommand : lineProcessingCommands) {
                            result = importCommand.execute(context);
                            if (!result) {
                                importFailed = true;
                                break;
                            }
                        }
                    }
                    progression.execute(context);
                    if (!importFailed) {
                        lineCount++;
                    } else if (!continueProcesingOnError) {
                        return ERROR;
                    }
                }

                if (lineCount == 0) {
                    progression.execute(context);
                    return ERROR;
                }

                // check if CopyCommands ended (with timeout to 5 minutes >
                // transaction timeout)
                if (context.containsKey(COPY_IN_PROGRESS)) {
                    long timeout = 5;
                    TimeUnit unit = TimeUnit.MINUTES;
                    List<Future<Void>> futures = (List<Future<Void>>) context.get(COPY_IN_PROGRESS);
                    for (Future<Void> future : futures) {
                        if (!future.isDone()) {
                            log.info("waiting for CopyCommand");
                            future.get(timeout, unit);
                        }
                    }
                }

            } else {
                // get stop info
                List<? extends Command> stopProcessingCommands = commands.getStopAreaProcessingCommands(context, true);
                progression.start(context, stopProcessingCommands.size());
                for (Command command : stopProcessingCommands) {
                    result = command.execute(context);
                    if (!result) {
                        return ERROR;
                    }
                    progression.execute(context);
                }

            }
            // post processing
            List<? extends Command> postProcessingCommands = commands.getPostProcessingCommands(context, true);
            if (postProcessingCommands.isEmpty()) {
                progression.terminate(context, 1);
                progression.execute(context);
            } else {
                progression.terminate(context, postProcessingCommands.size());
                for (Command command : postProcessingCommands) {
                    result = command.execute(context);
                    if (!result) {
                        return ERROR;
                    }
                    progression.execute(context);
                }
            }

            if (mode.equals(Mode.line) && !reporter.hasInfo(context, OBJECT_TYPE.LINE)) {
                if (!reporter.hasActionError(context))
                    reporter.setActionError(context, ActionReporter.ERROR_CODE.NO_DATA_FOUND, "no data");
            }
        } finally {
            // call dispose commmands
            try {
                List<? extends Command> disposeCommands = commands.getDisposeCommands(context, true);
                for (Command command : disposeCommands) {
                    disposeResult = command.execute(context);
                    if (!disposeResult) {
                        break;
                    }
                }
            } catch (Exception e) {
                log.warn("problem on dispose commands " + e.getMessage());
            }
            context.remove(CACHE);
        }
        return result ; // && disposeResult;
    }

}
