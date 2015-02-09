package mobi.chouette.exchange.neptune.importer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import lombok.ToString;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.FileUtils;
import mobi.chouette.common.chain.Chain;
import mobi.chouette.common.chain.ChainImpl;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.importer.UncompressCommand;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

@Stateless(name = NeptuneImporterCommand.COMMAND)
@ToString
@Log4j
public class NeptuneImporterCommand implements Command, Constant {

	public static final String COMMAND = "NeptuneImporterCommand";

	@Override
	public boolean execute(Context context) throws Exception {
		boolean result = ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);

		try {
			InitialContext ctx = (InitialContext) context.get(INITIAL_CONTEXT);

			// uncompress data
			Command command = CommandFactory.create(ctx,
					UncompressCommand.class.getName());
			command.execute(context);

			Chain chain = new ChainImpl();

			Path path = Paths.get(context.get(PATH).toString(), INPUT);
			List<Path> stream = FileUtils.listFiles(path, "*.xml");

			// validation
			for (Path file : stream) {
				context.put(FILE_URL, file.toUri().toURL().toExternalForm());
			
				Command validation = CommandFactory.create(ctx,
						NeptuneSAXParserCommand.class.getName());
				validation.execute(context)	;			
			}

			
			for (Path file : stream) {

				
				context.put(FILE_URL, file.toUri().toURL().toExternalForm());

				Chain transaction = new ChainImpl();

				// parser
				Command parser = CommandFactory.create(ctx,
						NeptuneParserCommand.class.getName());
				transaction.add(parser);
				

				// register
				// Command register = CommandFactory.create(ctx,
				// RegisterCommand.class.getName());
				// transac.add(register);
				
				
				chain.add(transaction);
			}

			chain.execute(context);

			result = SUCCESS;
		} catch (Exception e) {
			log.error(e);
		}

		log.info("[DSU] " + monitor.stop());
		return result;
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = null;
			try {
				String name = "java:app/mobi.chouette.exchange.neptune/"
						+ COMMAND;
				result = (Command) context.lookup(name);
			} catch (NamingException e) {
				log.error(e);
			}
			return result;
		}
	}

	static {
		CommandFactory factory = new DefaultCommandFactory();
		CommandFactory.factories.put(NeptuneImporterCommand.class.getName(),
				factory);
	}
}
