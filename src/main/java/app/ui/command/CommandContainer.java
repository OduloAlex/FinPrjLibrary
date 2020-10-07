package app.ui.command;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;

/**
 * Holder for all commands.
 *
 * @author
 *
 */
public class CommandContainer {

    private static final Logger log = Logger.getLogger(CommandContainer.class);

    private static Map<String, Command> commands = new TreeMap<String, Command>();

    static {
        // common commands
        commands.put("login", new LoginCommand());
        commands.put("registration", new RegistrationCommand());
        commands.put("toRegistration", new ToRegistrationCommand());
        commands.put("logout", new LogoutCommand());
//        commands.put("noCommand", new NoCommand());
//        commands.put("viewSettings", new ViewSettingsCommand());
//        commands.put("updateSettings", new UpdateSettingsCommand());
//
//        // client commands
        commands.put("listCards", new ListCardsCommand());
        commands.put("listCatalog", new ListCatalogCommand());
        commands.put("listOrders", new ListOrdersCommand());
//
//        // admin commands
//        commands.put("listOrders", new ListOrdersCommand());

        log.debug("Command container was successfully initialized");
        log.trace("Number of commands --> " + commands.size());
    }

    /**
     * Returns command object with the given name.
     *
     * @param commandName
     *            Name of the command.
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