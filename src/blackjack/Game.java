package blackjack;

import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        playGame();
    }

    public static void playGame() {
        Player dealer = new Player(true);
        Player player = new Player(false);
        player.setInitialBalance(500);
        System.out.println("\n Welcome to Blackjack!");
        while (player.getCurrentBalance() > 0) {
            playRound(dealer, player);
        }

    }
    public static void playRound(Player dealer, Player player) {
        Deck deck = new Deck();
        deck.shuffleDeck();

        int potAmount = 0;
        int betAmount = 0;

        displayBalances(potAmount, player);

        betAmount += getBet(player);
        potAmount += betAmount;

        player.decreaseBalance(betAmount);
        displayBalances(potAmount, player);
        player.balanceToString();

        dealHand(deck, dealer, player);

    }

    public static void displayBalances(int potAmount, Player player) {
        System.out.println("Current pot: " + potAmount + " chips");
        player.balanceToString();
    }

    public static void dealHand(Deck deck, Player dealer, Player player) {
        player.drawCard(deck.drawCard());
        dealer.drawCard(deck.drawCard());
        player.drawCard(deck.drawCard());
        dealer.drawCard(deck.drawCard());
        System.out.println("\nDEALER'S HAND: " +
                (dealer.getHandTotal() - dealer.convertCardValueString(dealer.getCardValue(1))));
        dealer.renderHand(true);
        System.out.println("\nYOUR HAND: " + player.getHandTotal());
        player.renderHand(false);

    }

    public static int getBet(Player player) {
        Scanner scanner = new Scanner(System.in);
        int bet;
        while (true) {
            System.out.print("\nPlace your bet amount (increments of 5 only) Or enter 0 to exit: ");
            String userInput = scanner.nextLine();
            if (isNumeric(userInput)) {
                bet = Integer.parseInt(userInput);
                if (bet == 0) {
                    System.out.println("You cashed out with " + player.getCurrentBalance() + " chips. Goodbye!");
                } else if (bet % 5 == 0 && bet <= player.getCurrentBalance()) {
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

    public static boolean isNumeric(String amountInput) {
        try {
            Integer.parseInt(amountInput);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
}
