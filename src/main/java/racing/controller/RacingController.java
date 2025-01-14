package racing.controller;

import racing.common.exception.InvalidInputFormatException;
import racing.domain.player.Car;
import racing.domain.gamerule.GameRule;
import racing.domain.player.RacingPlayer;
import racing.view.InputView;
import racing.view.OutputView;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public final class RacingController implements GameController {
    private final InputView inputView = new InputView();
    private List<RacingPlayer> racingPlayers;
    private Integer numberOfTurns;

    public void run() {
        try {
            createPlayers(getNamesFromUser());
            getNumberOfTurnsFromUser();
        } catch (InvalidInputFormatException e) {
            System.out.println(e.getLocalizedMessage());
            return;
        }

        runWholeGame();
    }
    private List<String> getNamesFromUser() {
        OutputView.askForNames();
        return inputView.getPlayerNames();
    }

    private void getNumberOfTurnsFromUser() {
        OutputView.askForNumberOfTurns();
        numberOfTurns = inputView.getNumberOfTurns();
    }

    private void runWholeGame() {
        OutputView.printResultTitle();
        OutputView.printCurrentStatus(racingPlayers);
        for (int i = 0; i < numberOfTurns; i++) {
            sleepForMillis(500);
            runSingleTurn();
            OutputView.printCurrentStatus(racingPlayers);
        }

        printResult(racingPlayers);
    }

    private void printResult(List<RacingPlayer> racingPlayers) {
        List<RacingPlayer> winners;

        try {
            winners = GameRule.getWinners(racingPlayers);
        } catch (InvalidInputFormatException e) {
            System.out.println(e.getLocalizedMessage());
            return;
        }
        OutputView.printWinners(winners);
    }

    private void createPlayers(@NotNull List<String> playerNames) {
        racingPlayers = playerNames.stream()
            .map(Car::new)
            .collect(Collectors.toList());
    }

    private void runSingleTurn() {
        racingPlayers
            .forEach(racingPlayer -> racingPlayer.proceedNextTurn(GameRule.isAbleToProceed()));
    }
    
    private void sleepForMillis(Integer milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch(InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
