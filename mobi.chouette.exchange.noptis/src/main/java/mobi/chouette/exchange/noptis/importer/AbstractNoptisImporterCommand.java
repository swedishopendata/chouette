package mobi.chouette.exchange.noptis.importer;

import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.exchange.ProcessingCommands;
import mobi.chouette.exchange.ProgressionCommand;
import mobi.chouette.exchange.noptis.importer.util.NoptisImporterUtils;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.model.util.Referential;

import javax.ejb.EJB;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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

                short dataSourceId = NoptisImporterUtils.getDataSourceId(parameters.getObjectIdPrefix());

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
                int maxLines = 10;
                int index = 0;

                for (Long lineId : lineIds) {

                    if (index >= maxLines) {
                        break;
                    }

                    // TODO remove below check when production
                    Set<Long> excludeIds = Sets.newHashSet(
                            14010000238160607L
                            , 33010000000570941L
                            , 33010000000570956L
                            , 33010000000570971L
                            , 33010000000570986L
                            , 33010000000571001L
                            , 33010000000571016L
                            , 33010000000571031L
                            , 33010000000571046L
                            , 33010000000571061L
                            , 33010000000719952L
                            , 33010000000719967L
                            , 33010000000720012L
                            , 33010000000720027L
                            , 33010000006688400L
                            , 33010000006688415L
                            , 33010000006688445L
                            , 33010000006689380L
                            , 33010000022768923L
                            , 33010000022768937L
                            , 33010000032745794L
                            , 33010000041585342L
                            , 33010000046738093L
                            , 33010000046738173L
                            , 14010000046885939L
                            , 33010000000658688L
                            , 33010000000658703L
                            , 33010000000719652L
                            , 33010000000719667L
                            , 33010000000719682L
                            , 33010000000719742L
                            , 33010000000719757L
                            , 33010000000719772L
                            , 33010000000719787L
                            , 33010000000719817L
                            , 33010000000720207L
                            , 33010000000720297L
                            , 33010000000720327L
                            , 33010000006688535L
                            , 33010000006688550L
                            , 33010000006688565L
                            , 33010000006689705L
                            , 33010000006690080L
                            , 33010000006690125L
                            , 33010000006690170L
                            , 33010000022764420L
                            , 33010000032745749L
                            , 33010000039000055L
                            , 33010000047691644L
                    );

                    if (excludeIds.contains(lineId)) {
                        continue;
                    }

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

                    index++;
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
