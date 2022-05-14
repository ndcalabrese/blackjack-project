package blackjack;

import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        playGame();
    }

    public static void playGame() {
        Player dealer = new Player(true);
        Player user = new Player(false);
        user.setInitialBalance(500);
        System.out.println("\n Welcome to Blackjack!");
        while (user.getCurrentBalance() > 0) {
            playRound(dealer, user);
        }

    }
    public static void playRound(Player dealer, Player user) {
        Deck deck = new Deck();
        deck.shuffleDeck();

        int potAmount = 0;
        int betAmount = 0;

        displayBalances(potAmount, user);

        betAmount = getBet(user);
        potAmount += betAmount;

        user.decreaseBalance(betAmount);
        displayBalances(potAmount, user);

        // Round 1
        // Deal two cards to dealer and player, and display them
        dealHand(deck, dealer, user);

        // User decides to hit or stand
        boolean isSecondRound = true;
        while (true) {
            // Array of booleans, willHit, and willDoubleDown
            boolean[] userChoices = userHitOrStand(deck, user, isSecondRound);
            // Player hits
            if (userChoices[0]) {
                // Player doubles down
                if (userChoices[1]) {
                    potAmount += betAmount;
                    user.decreaseBalance(betAmount);
                    displayBalances(potAmount, user);
                    isSecondRound = false;
                    user.drawCard(hit(deck, user));
                    break;
                // Player does not double down
                } else {
                    isSecondRound = false;
                    user.drawCard(hit(deck, user));
                }
            // Player stands
            } else {
                isSecondRound = false;
                dealerHitOrStand(deck, dealer, false);
                break;
            }
        }

    }

    public static void displayBalances(int potAmount, Player user) {
        System.out.println("Current pot: " + potAmount + " chips");
        user.balanceToString();
    }

    public static void dealHand(Deck deck, Player dealer, Player user) {
        user.drawCard(deck.drawCard());
        dealer.drawCard(deck.drawCard());
        user.drawCard(deck.drawCard());
        dealer.drawCard(deck.drawCard());
        System.out.println("\nDEALER'S HAND: " +
                (dealer.getHandTotal() - dealer.convertCardValueString(dealer.getCardValue(1))));
        dealer.renderHand(true);
        System.out.println("\nYOUR HAND: " + user.getHandTotal());
        user.renderHand(false);
    }

    public static int getBet(Player user) {
        int bet;
        while (true) {
            System.out.print("\nPlace your bet amount (increments of 5 only) Or enter 0 to exit: ");
            String userInput = getUserInput();
            if (isNumeric(userInput)) {
                bet = Integer.parseInt(userInput);
                if (bet == 0) {
                    System.out.println("You cashed out with " + user.getCurrentBalance() + " chips. Goodbye!");
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

    public static boolean[] userHitOrStand(Deck deck, Player player, boolean isSecondRound) {
        boolean willHit = false;
        boolean willDoubleDown = false;
        int userChoiceHitOrStand;
        while (true) {
            System.out.println("""
                1 - Hit
                2 - Stand""");
            String userInputHitOrStand = getUserInput();
            if (isNumeric(userInputHitOrStand)) {
                userChoiceHitOrStand = Integer.parseInt(userInputHitOrStand);
                if (userChoiceHitOrStand == 1) {
                    willHit = true;
                    if (isSecondRound) {
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

    public static void hit(Deck deck, Player player) {

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
