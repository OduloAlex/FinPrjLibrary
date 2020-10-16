package app.ui.command;

import app.ui.command.admin.*;
import app.ui.command.librarian.*;
import app.ui.command.reader.ListCardsCommand;
import app.ui.command.reader.ListCatalogCommand;
import app.ui.command.reader.ListOrdersCommand;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;

/**
 * Holder for all commands.
 *
 * @author Alex Odulo
 */
public class CommandContainer {

    private static final Logger log = Logger.getLogger(CommandContainer.class);

    private static Map<String, Command> commands = new TreeMap<String, Command>();

    static {
        // common commands
        commands.put("login", new LoginCommand());
        commands.put("registration", new RegistrationCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("error", new ErrorCommand());

        // reader commands
        commands.put("listCards", new ListCardsCommand());
        commands.put("listCatalog", new ListCatalogCommand());
        commands.put("listOrders", new ListOrdersCommand());

        // librarian commands
        commands.put("listLibReaders", new ListLibReadersCommand());
        commands.put("listLibOrders", new ListLibOrdersCommand());
        commands.put("listLibCards", new ListLibCardsCommand());
        commands.put("makeLibCard", new MakeLibCardCommand());
        commands.put("listLibCatalog", new ListLibCatalogCommand());

        // admin commands
        commands.put("listAdminCatalog", new ListAdminCatalogCommand());
        commands.put("listAdminUsers", new ListAdminUsersCommand());
        commands.put("listAdminAuthors", new ListAdminAuthorsCommand());
        commands.put("editAdminBook", new EditAdminBookCommand());
        commands.put("settingsAdminBook", new SettingsAdminBookCommand());
        commands.put("makeAdminBook", new MakeAdminBookCommand());
        commands.put("listAdminPublishings", new ListAdminPublishingsCommand());

        //
        log.debug("Command container was successfully initialized");
        log.trace("Number of commands --> " + commands.size());
    }

    /**
     * Returns command object with the given name.
     *
     * @param commandName Name of the command.
     * @return Command object.
     */
    public static Command get(String commandName) {
        if (commandName == null || !commands.containsKey(commandName)) {
            log.trace("Command not found, name --> " + commandName);
            return commands.get("noCommand");
        }

        return commands.get(commandName);
    }

}