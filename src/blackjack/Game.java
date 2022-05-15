package blackjack;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Game {
    public static void main(String[] args) throws InterruptedException {
        playGame();
    }

    public static void playGame() throws InterruptedException {
        Player dealer = new Player(true);
        Player user = new Player(false);
        user.setInitialBalance(500);
        System.out.println("\n\t\t   W  E  L  C  O  M  E       T  O");
        System.out.println("""
                 _      _               _       _               _   \s
                | |    | |             | |     (_)             | |  \s
                | |__  | |  __ _   ___ | | __   _   __ _   ___ | | __
                | '_ \\ | | / _` | / __|| |/ /  | | / _` | / __|| |/ /
                | |_) || || (_| || (__ |   <   | || (_| || (__ |   <\s
                |_.__/ |_| \\__,_| \\___||_|\\_\\  | | \\__,_| \\___||_|\\_\\
                                              _/ |                  \s
                                             |__/                   \s
                """);

        while (user.getCurrentBalance() > 0) {
            playRound(dealer, user);
        }

        System.out.println("\nNo more chips left! Thanks for playing!");

    }
    public static void playRound(Player dealer, Player user) throws InterruptedException {

        Deck deck = new Deck();
        deck.shuffleDeck();
        System.out.print("\nShuffling deck");

        for (int i = 0; i < 3; i++) {
            System.out.print(" .");
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println("\n");

        int potAmount = 0;
        int betAmount;

        displayBalances(potAmount, user);

        betAmount = getBet(user);
        potAmount += betAmount;

        user.subtractFromBalance(betAmount);
        displayBalances(potAmount, user);

        // Deal two cards to dealer and player, and display them
        dealHand(deck, dealer, user);
        if (checkScores(dealer, user, potAmount, false)) {
            return;
        }

        // User decides to hit or stand
        boolean isFirstHit = true;

        while (true) {
            // Array of booleans: willHit and willDoubleDown
            boolean[] userChoices = userHitOrStand(user, isFirstHit,
                    betAmount);
            // Player hits
            if (userChoices[0]) {
                if (userChoices[1]) {
                        potAmount += betAmount;
                        user.subtractFromBalance(betAmount);
                        displayBalances(potAmount, user);
                    // Player does not double down
                }
                isFirstHit = false;
                user.drawCard(deck.drawCard());
                dealer.renderHand(false);
                user.renderHand(false);
                if (checkScores(dealer, user, potAmount, false)) {
                    break;
                }
            // Player stands
            } else {
                dealerHitOrStand(deck, dealer);
                dealer.renderHand(true);
                user.renderHand(true);
                if (checkScores(dealer, user, potAmount, true)) {
                    break;
                }
            }
        }


    }

    public static void displayBalances(int potAmount, Player user) {
        System.out.println("\nCurrent pot: " + potAmount + " chips");
        user.balanceToString();
    }

    public static void dealHand(Deck deck, Player dealer, Player user) {
        user.clearHand();
        dealer.clearHand();
        user.drawCard(deck.drawCard());
        dealer.drawCard(deck.drawCard());
        user.drawCard(deck.drawCard());
        dealer.drawCard(deck.drawCard());
        dealer.renderHand(false);
        user.renderHand(false);
//        switch (dealer.getCardValue(0)) {
//            case "10":
//            case "J":
//            case "Q":
//            case "K":
//            case "A": {
//                System.out.println("\nDEALER'S HAND: " +
//                        (dealer.getHandTotal()));
//                dealer.renderHand(false);
//                break;
//            }
//            default: {
//                System.out.println("\nDEALER'S HAND: " +
//                        (dealer.getHandTotal() - dealer.convertCardValueString(dealer.getCardValue(1))));
//                dealer.renderHand(true);
//                break;
//            }
//        }
    }

    public static int getBet(Player user) {
        int bet;
        while (true) {
            System.out.print("\nPlace your bet amount (increments of 5 only) Or enter 0 to exit: ");
            String userInput = getUserInput();
            if (isNumeric(userInput)) {
                bet = Integer.parseInt(userInput);
                if (bet == 0) {
                    System.out.println("\nYou cashed out with " + user.getCurrentBalance() + " chips. Goodbye!");
                } else if (bet % 5 == 0 && bet <= user.getCurrentBalance()) {
                    break;
                } else {
                    System.out.println("\nPlease enter a multiple of 5.");
                }
            } else {
                System.out.println("\nInvalid input.");
            }
        }

        return bet;
    }

    // Returns true if game has ended.
    public static boolean checkScores(Player dealer, Player user, int potAmount, boolean isStanding) {
        boolean isRoundOver = false;
        // Both dealer and user have blackjack
        if (dealer.getHandTotal() == 21 && user.getHandTotal() == 21) {
            System.out.println("\nPush.");
            user.addToBalance(potAmount);
            isRoundOver = true;
        // Dealer has blackjack
        } else if (dealer.getHandTotal() == 21) {
            System.out.println("\nDealer got blackjack. Dealer wins");
            isRoundOver = true;
        // User has blackjack
        } else if (user.getHandTotal() == 21) {
            System.out.println("\nYou got blackjack! You win " + (potAmount * 2) +
                    " chips!");
            user.addToBalance(potAmount * 2);
            isRoundOver = true;
        // Dealer busts
        } else if (dealer.getHandTotal() > 21 && user.getHandTotal() <= 21) {
            System.out.println("\nDealer busts. You win " + (potAmount * 2) +
                    " chips!");
            user.addToBalance(potAmount * 2);
            isRoundOver = true;
        // User busts
        } else if (dealer.getHandTotal() <= 21 && user.getHandTotal() > 21) {
            System.out.println("\nBust! Dealer wins.");
            isRoundOver = true;
        } else if (dealer.getHandTotal() < 21 && user.getHandTotal() < 21 && isStanding) {
            if (dealer.getHandTotal() > user.getHandTotal()) {
                System.out.println("\nDealer wins.");
                isRoundOver = true;
            } else if (dealer.getHandTotal() < user.getHandTotal()) {
                System.out.println("\nYou win " + (potAmount * 2) +
                        " chips!");
                user.addToBalance(potAmount * 2);
                isRoundOver = true;
            } else if (dealer.getHandTotal() == user.getHandTotal()) {
                System.out.println("Push.");
                user.addToBalance(potAmount);
                isRoundOver = true;
            }
        }
        return isRoundOver;
    }

    public static boolean[] userHitOrStand(Player user, boolean isFirstHit,
                                           int betAmount) {
        boolean willHit = false;
        boolean willDoubleDown = false;
        int userChoiceHitOrStand;
        while (true) {
            System.out.println("""
                1 - Hit
                2 - Stand""");
            System.out.print("Your response: ");
            String userInputHitOrStand = getUserInput();
            if (isNumeric(userInputHitOrStand)) {
                userChoiceHitOrStand = Integer.parseInt(userInputHitOrStand);
                if (userChoiceHitOrStand == 1) {
                    willHit = true;
                    if (isFirstHit && (user.getCurrentBalance() >= (betAmount * 2))) {
                        while (true) {
                            System.out.print("Double down? Y/N: ");
                            String userInputDoubleDown = getUserInput();
                            if (userInputDoubleDown.equalsIgnoreCase("Y")) {
                                willDoubleDown = true;
                                break;
                            } else if (userInputDoubleDown.equalsIgnoreCase("N")) {
                                break;
                            } else {
                                System.out.println("\nInvalid input.");
                            }
                        }
                    }
                    break;
                } else if (userChoiceHitOrStand == 2) {
                    break;
                } else {
                    System.out.println("\nInvalid input.");
                }
            } else {
                System.out.println("\nInvalid input.");
            }
        }
        return new boolean[] { willHit, willDoubleDown };
    }

    public static void dealerHitOrStand (Deck deck, Player dealer) {
        while (dealer.getHandTotal() < 17) {
            dealer.drawCard(deck.drawCard());
        }
    }

    public static String getUserInput() {
        Scanner scanner = new Scanner(System.in);

        return scanner.nextLine();
    }

    public static boolean isNumeric(String amountInput) {
        try {
            Integer.parseInt(amountInput);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
}
