package edu.kit.kastel.monstergame.model.command.impl;
import edu.kit.kastel.monstergame.model.command.CommandHandler;
import edu.kit.kastel.monstergame.model.command.CommandInterface;
import edu.kit.kastel.monstergame.model.Action;
import edu.kit.kastel.monstergame.model.Monster;
import java.util.List;


/**
 * Console-based implementation of the command interface.
 * @author uuifx
 */
public class ConsoleCommandInterface implements CommandInterface {
    private final CommandHandler commandHandler;

    public ConsoleCommandInterface(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public Action selectAction(Monster monster, List<Monster> opponents) {
        // Set the current monster in the command handler
        commandHandler.setCurrentMonster(monster);
        return null;
    }
}