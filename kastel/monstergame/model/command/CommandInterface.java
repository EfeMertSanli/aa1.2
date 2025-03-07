package edu.kit.kastel.monstergame.model.command;

import edu.kit.kastel.monstergame.model.Action;
import edu.kit.kastel.monstergame.model.Monster;

import java.util.List;

/**
 *
 * @author uuifx
 */
public interface CommandInterface {
    Action selectAction(Monster monster, List<Monster> opponents);
}