package mobi.chouette.exchange.noptis.importer;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Chain;
import mobi.chouette.common.chain.ChainCommand;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.ProcessingCommands;
import mobi.chouette.exchange.ProcessingCommandsFactory;
import mobi.chouette.exchange.importer.CleanRepositoryCommand;
import mobi.chouette.exchange.importer.LineRegisterCommand;
import mobi.chouette.exchange.importer.UncompressCommand;
import mobi.chouette.exchange.noptis.Constant;

import javax.naming.InitialContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@Log4j
public class NoptisImporterProcessingCommands implements ProcessingCommands, Constant {

	public static class DefaultFactory extends ProcessingCommandsFactory {

		@Override
		protected ProcessingCommands create() throws IOException {
			ProcessingCommands result = new NoptisImporterProcessingCommands();
			return result;
		}
	}

	static {
		ProcessingCommandsFactory.factories.put(NoptisImporterProcessingCommands.class.getName(), new DefaultFactory());
	}

	@Override
	public List<? extends Command> getPreProcessingCommands(Context context, boolean withDao) {
		InitialContext initialContext = (InitialContext) context.get(INITIAL_CONTEXT);
		NoptisImportParameters parameters = (NoptisImportParameters) context.get(CONFIGURATION);
		List<Command> commands = new ArrayList<>();
		try {
			Chain initChain = (Chain) CommandFactory.create(initialContext, ChainCommand.class.getName());
			if (withDao && parameters.isCleanRepository()) {
				initChain.add(CommandFactory.create(initialContext, CleanRepositoryCommand.class.getName()));
			}
			initChain.add(CommandFactory.create(initialContext, UncompressCommand.class.getName())); // TODO remove...
			initChain.add(CommandFactory.create(initialContext, NoptisInitImportCommand.class.getName()));
			initChain.add(CommandFactory.create(initialContext, DaoNoptisSharedDataParserCommand.class.getName()));
			commands.add(initChain);
		} catch (Exception e) {
			log.error(e, e);
			throw new RuntimeException("unable to call factories");
		}

		return commands;
	}

	@Override
	public List<? extends Command> getLineProcessingCommands(Context context, boolean withDao) {
		InitialContext initialContext = (InitialContext) context.get(INITIAL_CONTEXT);
		NoptisImportParameters parameters = (NoptisImportParameters) context.get(CONFIGURATION);
		boolean level3validation = context.get(VALIDATION) != null;
		List<Command> commands = new ArrayList<>();

		try {
			if (withDao) {
				commands.add(CommandFactory.create(initialContext, DaoNoptisLineParserCommand.class.getName()));
				commands.add(CommandFactory.create(initialContext, DaoNoptisJourneyParserCommand.class.getName()));
			}
			if (withDao && !parameters.isNoSave()) {
				commands.add(CommandFactory.create(initialContext, LineRegisterCommand.class.getName()));
				//commands.add(CommandFactory.create(initialContext, CopyCommand.class.getName()));
			}
			if (level3validation) {
				//commands.add(CommandFactory.create(initialContext, ImportedLineValidatorCommand.class.getName()));
			}
		} catch (Exception e) {
			log.error(e, e);
			throw new RuntimeException("unable to call factories");
		}

		return commands;
	}

	@Override
	public List<? extends Command> getPostProcessingCommands(Context context, boolean withDao) {
		InitialContext initialContext = (InitialContext) context.get(INITIAL_CONTEXT);
		boolean level3validation = context.get(VALIDATION) != null;

		List<Command> commands = new ArrayList<>();


		try {

			if (level3validation) {
				// add shared data validation
				//commands.add(CommandFactory.create(initialContext, SharedDataValidatorCommand.class.getName()));
			}

		} catch (Exception e) {
			log.error(e, e);
			throw new RuntimeException("unable to call factories");
		}
		return commands;
	}

	@Override
	public List<? extends Command> getStopAreaProcessingCommands(Context context, boolean withDao) {
		InitialContext initialContext = (InitialContext) context.get(INITIAL_CONTEXT);
		NoptisImportParameters parameters = (NoptisImportParameters) context.get(CONFIGURATION);
		List<Command> commands = new ArrayList<>();

		try {
			//commands.add(CommandFactory.create(initialContext, NoptisStopAreaParserCommand.class.getName()));

			if (withDao && !parameters.isNoSave()) {
				//commands.add(CommandFactory.create(initialContext, StopAreaRegisterCommand.class.getName()));
			}
		} catch (Exception e) {
			log.error(e, e);
			throw new RuntimeException("unable to call factories");
		}

		return commands;
	}

	@Override
	public List<? extends Command> getDisposeCommands(Context context, boolean withDao) {
		List<Command> commands = new ArrayList<>();
		InitialContext initialContext = (InitialContext) context.get(INITIAL_CONTEXT);
		try {
		} catch (Exception e) {
			log.error(e, e);
			throw new RuntimeException("unable to call factories");
		}
		return commands;
	}

}
