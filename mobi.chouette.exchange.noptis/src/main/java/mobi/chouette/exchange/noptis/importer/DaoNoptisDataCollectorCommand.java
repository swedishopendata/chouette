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
import mobi.chouette.model.stip.Line;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;

@Log4j
@Stateless(name = DaoNoptisDataCollectorCommand.COMMAND)
public class DaoNoptisDataCollectorCommand implements Command, Constant {

    public static final String COMMAND = "DaoNoptisDataCollectorCommand";

    @Resource
    private SessionContext daoContext;

    @EJB
    private LineDAO lineDAO;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean execute(Context context) throws Exception {
        boolean result = ERROR;
        Monitor monitor = MonitorFactory.start(COMMAND);

        try {
            Long lineId = (Long) context.get(LINE_ID);
            Line line = lineDAO.find(lineId);
            context.put(LINE, line);

            InitialContext initialContext = (InitialContext) context.get(INITIAL_CONTEXT);
            Command dataCollectorCommand = CommandFactory.create(initialContext, NoptisDataCollectorCommand.class.getName());

            result = dataCollectorCommand.execute(context);
            daoContext.setRollbackOnly();
            lineDAO.clear();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
        CommandFactory.factories.put(DaoNoptisDataCollectorCommand.class.getName(), new DaoNoptisDataCollectorCommand.DefaultCommandFactory());
    }

}
