package blackjack;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Game {
    public static void main(String[] args) throws InterruptedException {
        playGame();
    }

    public static void playGame() throws InterruptedException {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";
        renderSplashMessage();

        Player dealer = new Player(true);
        Player user = new Player(false);

        user.setInitialBalance(500);

        while (user.getCurrentBalance() > 0) {
            playRound(dealer, user);
        }

        System.out.println(ANSI_RED + "\nNo more chips left! Thanks for playing, sucker!" + ANSI_RESET);

    }

    public static void renderSplashMessage() {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_PURPLE = "\u001B[35m";
        String ANSI_CYAN = "\u001B[36m";

        String[] colorArray = { ANSI_RED, ANSI_GREEN, ANSI_YELLOW, ANSI_BLUE, ANSI_PURPLE, ANSI_CYAN };
        // Gets a random number between 0 and colorArray.length - 1
        int randomNumber = ThreadLocalRandom.current().nextInt(0, colorArray.length);

        System.out.println("\n\t\t   W  E  L  C  O  M  E       T  O");
        System.out.println(colorArray[randomNumber]);
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
        System.out.print(ANSI_RESET);
    }

    public static void playRound(Player dealer, Player user) throws InterruptedException {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_YELLOW = "\u001B[33m";

        Deck deck = new Deck();
        deck.shuffleDeck();
        System.out.print(ANSI_YELLOW + "\nShuffling deck");

        for (int i = 0; i < 3; i++) {
            System.out.print(" .");
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(ANSI_RESET + "\n");

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
                // Player doubles down
                if (userChoices[1]) {
                    potAmount += betAmount;
                    user.subtractFromBalance(betAmount);
                    displayBalances(potAmount, user);
                    isFirstHit = false;
                    user.drawCard(deck.drawCard());
                    dealerHitOrStand(deck, dealer);
                    dealer.renderHand(true);
                    user.renderHand(true);
                    if (checkScores(dealer, user, potAmount, true)) {
                        break;
                    }
                } else {
                    isFirstHit = false;
                    user.drawCard(deck.drawCard());
                    dealer.renderHand(user.getHandTotal() >= 21);
                    user.renderHand(false);
                    if (checkScores(dealer, user, potAmount, false)) {
                        break;
                    }
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
        String ANSI_RESET = "\u001B[0m";
        String ANSI_PURPLE = "\u001B[35m";
        System.out.println("\nCurrent pot: " + ANSI_PURPLE + potAmount + ANSI_RESET + " chips");
        user.balanceToString();
    }

    public static void dealHand(Deck deck, Player dealer, Player user) {
        user.clearHand();
        dealer.clearHand();
        user.drawCard(deck.drawCard());
        dealer.drawCard(deck.drawCard());
        user.drawCard(deck.drawCard());
        dealer.drawCard(deck.drawCard());
        // If dealer has natural blackjack, user stands
//        dealer.renderHand(dealer.getHandTotal() == 21);
        dealer.renderHand(false);
        user.renderHand(false);
    }

    public static int getBet(Player user) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_CYAN = "\u001B[36m";
        int bet;
        while (true) {
            System.out.print("\nPlace your bet amount (increments of 5 only) Or enter 0 to "
                    + ANSI_GREEN + "cash out " + ANSI_RESET + "and exit: ");
            String userInput = getUserInput();
            if (isNumeric(userInput)) {
                bet = Integer.parseInt(userInput);
                // User enters 0 and cashes out
                if (bet == 0) {
                    System.out.println(ANSI_GREEN + "\nYou cashed out with "
                            + user.getCurrentBalance() + " chips. " + ANSI_RESET + "Goodbye!");
                    System.exit(0);
                // User enters a positive number that is a multiple of 5 and <= their total chips
                } else if (bet % 5 == 0 && bet <= user.getCurrentBalance() && bet > 0) {
                    break;
                // User enters a positive number that is not a multiple of 5
                } else if (bet <= user.getCurrentBalance() && bet > 0) {
                    System.out.println(ANSI_RED + "\nPlease enter a multiple of 5." + ANSI_RESET);
                // User enters a number that is > their total chips
                } else if (bet > user.getCurrentBalance()) {
                    System.out.println(ANSI_RED + "\nInsufficient chips." + ANSI_RESET);
                // User enters a negative number.
                } else if (bet < 0) {
                    System.out.println(ANSI_RED + "\nInvalid input." + ANSI_RESET);
                }
            } else {
                System.out.println(ANSI_RED + "\nInvalid input." + ANSI_RESET);
            }
        }

        return bet;
    }

    // Returns true if game has ended.
    public static boolean checkScores(Player dealer, Player user, int potAmount, boolean isStanding) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_PURPLE = "\u001B[35m";
        String ANSI_CYAN = "\u001B[36m";

        boolean isRoundOver = false;
        // Both dealer and user have natural blackjack
        if (dealer.getHandTotal() == 21 && user.getHandTotal() == 21
                && dealer.getHandSize() == 2 && user.getHandSize() == 2) {
            System.out.println(ANSI_PURPLE + "\nPush. Chips returned." + ANSI_RESET);
            user.addToBalance(potAmount);
            isRoundOver = true;
        // Dealer gets natural blackjack and user doesn't
        } else if (dealer.getHandTotal() == 21 && dealer.getHandSize() == 2) {
            System.out.println(ANSI_YELLOW + "\nDealer" + ANSI_RESET + " got blackjack. "
                    + ANSI_YELLOW + "Dealer " + ANSI_RESET + "wins, " + ANSI_YELLOW
                    + dealer.getHandTotal() + ANSI_RESET + " to " + ANSI_CYAN
                    + user.getHandTotal() + ANSI_RESET + ". Dealer takes " + ANSI_RED + potAmount
                    + ANSI_RESET + " chips.");
            isRoundOver = true;
        // User gets natural blackjack and dealer doesn't
        // Blackjack pays 3:2
        } else if (user.getHandTotal() == 21 && user.getHandSize() == 2) {
            int userWinnings = (int)Math.round(potAmount * 2.5);
            System.out.println(ANSI_CYAN + "\nYou " + ANSI_RESET + "got blackjack! "
                    + ANSI_CYAN + "You " + ANSI_RESET + "win " + ANSI_GREEN + userWinnings +
                    ANSI_RESET + " chips!");
            user.addToBalance(userWinnings);
            isRoundOver = true;
        // Both dealer and user reach 21
        } else if (dealer.getHandTotal() == 21 && user.getHandTotal() == 21) {
            System.out.println(ANSI_PURPLE + "\nPush. Chips returned." + ANSI_RESET);
            user.addToBalance(potAmount);
            isRoundOver = true;
        // Dealer reaches 21 and user does not
        } else if (dealer.getHandTotal() == 21) {
            System.out.println(ANSI_YELLOW + "\nDealer" + ANSI_RESET + " got 21. "
                    + ANSI_YELLOW + "Dealer " + ANSI_RESET + "wins. " + ANSI_YELLOW
                    + "Dealer " + ANSI_RESET + "takes " + ANSI_RED + potAmount
                    + ANSI_RESET + " chips.");
            isRoundOver = true;
        // User reaches 21 and dealer does not.
        } else if (user.getHandTotal() == 21) {
            System.out.println(ANSI_CYAN + "\nYou " + ANSI_RESET + "got 21. "
                    + ANSI_CYAN + "You " + ANSI_RESET + "win " + ANSI_GREEN + (potAmount * 2) +
                    ANSI_RESET + " chips!");
            user.addToBalance(potAmount * 2);
            isRoundOver = true;
        // Dealer busts
        } else if (dealer.getHandTotal() > 21 && user.getHandTotal() <= 21) {
            System.out.println(ANSI_YELLOW + "\nDealer " + ANSI_RESET + "busted. " + ANSI_CYAN +
                    "You " + ANSI_RESET + "win " + ANSI_GREEN + (potAmount * 2) +
                    ANSI_RESET + " chips!");
            user.addToBalance(potAmount * 2);
            isRoundOver = true;
        // User busts
        } else if (dealer.getHandTotal() <= 21 && user.getHandTotal() > 21) {
            System.out.println(ANSI_CYAN + "\nYou " + ANSI_RESET + "busted! " + ANSI_YELLOW
                    + "Dealer " + ANSI_RESET + "wins. " + ANSI_YELLOW + "Dealer " + ANSI_RESET
                    + "takes " + ANSI_RED + potAmount + ANSI_RESET + " chips.");
            isRoundOver = true;
        // User stands and dealer stops hitting
        } else if (dealer.getHandTotal() < 21 && user.getHandTotal() < 21 && isStanding) {
            // Dealer has a higher hand value
            if (dealer.getHandTotal() > user.getHandTotal()) {
                System.out.println(ANSI_YELLOW + "\nDealer " + ANSI_RESET + "wins, " + ANSI_YELLOW
                        + dealer.getHandTotal() + ANSI_RESET + " to " + ANSI_CYAN + user.getHandTotal()
                        + ANSI_RESET + ". " + ANSI_YELLOW + "Dealer " + ANSI_RESET + "takes " + ANSI_RED
                        + potAmount + ANSI_RESET + " chips.");
                isRoundOver = true;
            // User has a higher hand value
            } else if (dealer.getHandTotal() < user.getHandTotal()) {
                System.out.println(ANSI_CYAN + "\nYou " + ANSI_RESET + "win, " + ANSI_CYAN
                        + user.getHandTotal() + ANSI_RESET + " to " + ANSI_YELLOW + dealer.getHandTotal()
                        + ANSI_RESET + ". " + ANSI_CYAN + "You " + ANSI_RESET + "win " + ANSI_GREEN
                        + (potAmount * 2) + ANSI_RESET + " chips!");
                user.addToBalance(potAmount * 2);
                isRoundOver = true;
            // Both dealer and user have equal hand value
            } else if (dealer.getHandTotal() == user.getHandTotal()) {
                System.out.println(ANSI_PURPLE + "\nPush. Chips returned." + ANSI_RESET);
                user.addToBalance(potAmount);
                isRoundOver = true;
            }
        }
        return isRoundOver;
    }

    public static boolean[] userHitOrStand(Player user, boolean isFirstHit,
                                           int betAmount) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";

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
                                System.out.println(ANSI_RED + "\nInvalid input." + ANSI_RESET);
                            }
                        }
                    }
                    break;
                } else if (userChoiceHitOrStand == 2) {
                    break;
                } else {
                    System.out.println(ANSI_RED + "\nInvalid input." + ANSI_RESET);
                }
            } else {
                System.out.println(ANSI_RED + "\nInvalid input." + ANSI_RESET);
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
