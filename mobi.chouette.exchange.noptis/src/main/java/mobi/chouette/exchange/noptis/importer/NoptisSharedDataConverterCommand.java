package mobi.chouette.exchange.noptis.importer;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.stip.TransportAuthorityDAO;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.importer.util.NoptisImporterUtils;
import mobi.chouette.model.stip.TransportAuthority;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.List;

@Log4j
@Stateless(name = NoptisSharedDataConverterCommand.COMMAND)
public class NoptisSharedDataConverterCommand implements Command, Constant {

    public static final String COMMAND = "NoptisSharedDataConverterCommand";

    @Resource
    private SessionContext daoContext;

    @EJB
    private TransportAuthorityDAO transportAuthorityDAO;

    @Override
    public boolean execute(Context context) throws Exception {
        boolean result = ERROR;
        Monitor monitor = MonitorFactory.start(COMMAND);

        try {
            NoptisImportParameters parameters = (NoptisImportParameters) context.get(Constant.CONFIGURATION);
            short dataSourceId = NoptisImporterUtils.getDataSourceId(parameters.getObjectIdPrefix());
            List<TransportAuthority> transportAuthorities = transportAuthorityDAO.findByDataSourceId(dataSourceId);

            for (TransportAuthority transportAuthority : transportAuthorities) {
                log.info(transportAuthority.toString());
            }

            result = SUCCESS;
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
        CommandFactory.factories.put(NoptisSharedDataConverterCommand.class.getName(), new NoptisSharedDataConverterCommand.DefaultCommandFactory());
    }

}
