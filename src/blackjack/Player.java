package blackjack;

import java.util.ArrayList;

public class Player {

    private boolean isDealer;
    private int balance;
    private ArrayList<Card> hand;
    private int handTotal;

    Player(boolean isDealer) {
        hand = new ArrayList<>();
        this.isDealer = isDealer;
    }

    public void setInitialBalance(int initialBalance) {
        this.balance = initialBalance;
    }

//    public boolean getIsDealer() {
//        return this.isDealer;
//    }

    public int getCurrentBalance() {
        return this.balance;
    }

    public String getCardValue(int card) {
        return this.hand.get(card).getValue();
    }

//    public ArrayList<Card> getHand() {
//        return this.hand;
//    }

    public int getHandTotal() {
        return this.handTotal;
    }

    public int getHandSize() {
        return this.hand.size();
    }

    public void balanceToString() {
        System.out.println("Your balance: " +  balance + " chips");
    }

    public void drawCard(Card card) {
        hand.add(card);
        sumCardValues();
    }

    ArrayList<ArrayList<String>> renderCards(boolean isSecondCardHidden) {
        ArrayList<ArrayList<String>> renderedCards = new ArrayList<>();
        String ANSI_RED = "\u001B[31m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_PURPLE = "\u001B[35m";
        String ANSI_RESET = "\u001B[0m";

        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            ArrayList<String> renderedCard = new ArrayList<>();
            String suitSymbol = null;
            String cardValue = null;

            switch (card.getSuit()) {
                case("spade")  -> {
                    suitSymbol = ANSI_BLUE + "♠" + ANSI_RESET;
                    cardValue = ANSI_BLUE + card.getValue() + ANSI_RESET;
                }
                case("diamond")  -> {
                    suitSymbol = ANSI_RED + "♦" + ANSI_RESET;
                    cardValue = ANSI_RED + card.getValue() + ANSI_RESET;
                }
                case("heart")  -> {
                    suitSymbol = ANSI_RED + "♥" + ANSI_RESET;
                    cardValue = ANSI_RED + card.getValue() + ANSI_RESET;
                }
                case("club")  -> {
                    suitSymbol = ANSI_BLUE + "♣" + ANSI_RESET;
                    cardValue = ANSI_BLUE + card.getValue() + ANSI_RESET;
                }
            }


            if (i == 1 && isSecondCardHidden) {
                renderedCard.add("┌─────────┐");
                renderedCard.add("│" + ANSI_PURPLE + "░░░░░░░░░" + ANSI_RESET + "│");
                renderedCard.add("│" + ANSI_PURPLE + "░░░░░░░░░" + ANSI_RESET + "│");
                renderedCard.add("│" + ANSI_PURPLE + "░░░░░░░░░" + ANSI_RESET + "│");
                renderedCard.add("│" + ANSI_PURPLE + "░░░░░░░░░" + ANSI_RESET + "│");
                renderedCard.add("│" + ANSI_PURPLE + "░░░░░░░░░" + ANSI_RESET + "│");
                renderedCard.add("└─────────┘");
            } else if (card.getValue().equals("10")) {
                renderedCard.add("┌─────────┐");
                renderedCard.add("│ " + cardValue + "      │");
                renderedCard.add("│         │");
                renderedCard.add("│    " + suitSymbol + "    │");
                renderedCard.add("│         │");
                renderedCard.add("│      " + cardValue + " │");
                renderedCard.add("└─────────┘");
            } else {
                renderedCard.add("┌─────────┐");
                renderedCard.add("│ " + cardValue + "       │");
                renderedCard.add("│         │");
                renderedCard.add("│    " + suitSymbol + "    │");
                renderedCard.add("│         │");
                renderedCard.add("│      " + cardValue + "  │");
                renderedCard.add("└─────────┘");
            }
            renderedCards.add(renderedCard);
        }

        return renderedCards;
    }

    public void renderHand(boolean isUserStanding) {
        ArrayList<ArrayList<String>> renderedCards;
        // If dealer's first card is a 10, face card, or ace, check for natural blackjack
        if (isDealer) {
            // Hide dealer's second card if there is no natural blackjack
            // and the user is not standing
            if (!isUserStanding) {
                switch (getCardValue(0)) {
                    case "10":
                    case "J":
                    case "Q":
                    case "K": {
                        // Peek at dealer's second card if the first card is a 10 or face card
                        // If it is natural blackjack, show both cards
                        if (getCardValue(1).equals("A")) {
                            System.out.println("\nDEALER'S HAND: " +
                                    (getHandTotal()));
                            renderedCards = renderCards(false);
                            printHand(renderedCards);
                        } else {
                            System.out.println("\nDEALER'S HAND: " +
                                    (getHandTotal() - convertCardValueString(getCardValue(1))));
                            renderedCards = renderCards(true);
                            printHand(renderedCards);
                        }
                        break;
                    }
                    case "A": {
                        // Peek at dealer's second card if the first card is an ace
                        // If it is natural blackjack, show both cards
                        switch (getCardValue(1)) {
                            case "10":
                            case "J":
                            case "Q":
                            case "K": {
                                System.out.println("\nDEALER'S HAND: " +
                                        (getHandTotal()));
                                renderedCards = renderCards(false);
                                printHand(renderedCards);
                                break;
                            }
                            default: {
                                System.out.println("\nDEALER'S HAND: " +
                                        (getHandTotal() - convertCardValueString(getCardValue(1))));
                                renderedCards = renderCards(true);
                                printHand(renderedCards);
                                break;
                            }
                        }
                        break;
                    }
                    default: {
                        System.out.println("\nDEALER'S HAND: " +
                                (getHandTotal() - convertCardValueString(getCardValue(1))));
                        renderedCards = renderCards(true);
                        printHand(renderedCards);
                        break;
                    }
                }
            // Show dealer's second card if user is standing
            } else {
                System.out.println("\nDEALER'S HAND: " +
                        (getHandTotal()));
                renderedCards = renderCards(false);
                printHand(renderedCards);
            }
        // Show both cards if the player is the user
        } else {
            System.out.println("\nYOUR HAND: " + getHandTotal());
            renderedCards = renderCards(false);
            printHand(renderedCards);
        }
    }

    public void printHand (ArrayList<ArrayList<String>> renderedCards) {
        if (hand.size() == 2) {
            for (int i = 0 ; i < renderedCards.get(0).size(); i++) {
                System.out.printf("%s\t%s\n", renderedCards.get(0).get(i), renderedCards.get(1).get(i));
            }
        } else if (hand.size() == 3) {
            for (int i = 0 ; i < renderedCards.get(0).size(); i++) {
                System.out.printf("%s\t%s\t%s\n", renderedCards.get(0).get(i), renderedCards.get(1).get(i),
                        renderedCards.get(2).get(i));
            }
        } else if (hand.size() == 4) {
            for (int i = 0 ; i < renderedCards.get(0).size(); i++) {
                System.out.printf("%s\t%s\t%s\t%s\n", renderedCards.get(0).get(i), renderedCards.get(1).get(i),
                        renderedCards.get(2).get(i), renderedCards.get(3).get(i));
            }
        } else if (hand.size() == 5) {
            for (int i = 0 ; i < renderedCards.get(0).size(); i++) {
                System.out.printf("%s\t%s\t%s\t%s\t%s\n", renderedCards.get(0).get(i), renderedCards.get(1).get(i),
                        renderedCards.get(2).get(i), renderedCards.get(3).get(i), renderedCards.get(4).get(i));
            }
        } else if (hand.size() == 6) {
            for (int i = 0 ; i < renderedCards.get(0).size(); i++) {
                System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\n", renderedCards.get(0).get(i),
                        renderedCards.get(1).get(i), renderedCards.get(2).get(i), renderedCards.get(3).get(i),
                        renderedCards.get(4).get(i), renderedCards.get(5).get(i));
            }
        } else if (hand.size() == 7) {
            for (int i = 0 ; i < renderedCards.get(0).size(); i++) {
                System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\n", renderedCards.get(0).get(i),
                        renderedCards.get(1).get(i), renderedCards.get(2).get(i), renderedCards.get(3).get(i),
                        renderedCards.get(4).get(i), renderedCards.get(5).get(i), renderedCards.get(6).get(i));
            }
        } else if (hand.size() == 8) {
            for (int i = 0 ; i < renderedCards.get(0).size(); i++) {
                System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n", renderedCards.get(0).get(i),
                        renderedCards.get(1).get(i), renderedCards.get(2).get(i), renderedCards.get(3).get(i),
                        renderedCards.get(4).get(i), renderedCards.get(5).get(i), renderedCards.get(6).get(i),
                        renderedCards.get(7).get(i));
            }
        } else if (hand.size() == 9) {
            for (int i = 0 ; i < renderedCards.get(0).size(); i++) {
                System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n", renderedCards.get(0).get(i),
                        renderedCards.get(1).get(i), renderedCards.get(2).get(i), renderedCards.get(3).get(i),
                        renderedCards.get(4).get(i), renderedCards.get(5).get(i), renderedCards.get(6).get(i),
                        renderedCards.get(7).get(i), renderedCards.get(8).get(i));
            }
        } else if (hand.size() == 10) {
            for (int i = 0 ; i < renderedCards.get(0).size(); i++) {
                System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n", renderedCards.get(0).get(i),
                        renderedCards.get(1).get(i), renderedCards.get(2).get(i), renderedCards.get(3).get(i),
                        renderedCards.get(4).get(i), renderedCards.get(5).get(i), renderedCards.get(6).get(i),
                        renderedCards.get(7).get(i), renderedCards.get(8).get(i), renderedCards.get(9).get(i));
            }
        } else if (hand.size() == 11) {
            for (int i = 0 ; i < renderedCards.get(0).size(); i++) {
                System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n", renderedCards.get(0).get(i),
                        renderedCards.get(1).get(i), renderedCards.get(2).get(i), renderedCards.get(3).get(i),
                        renderedCards.get(4).get(i), renderedCards.get(5).get(i), renderedCards.get(6).get(i),
                        renderedCards.get(7).get(i), renderedCards.get(8).get(i), renderedCards.get(9).get(i),
                        renderedCards.get(10).get(i));
            }
        }
    }

    public void clearHand() {
        hand = new ArrayList<>();
    }

    public void sumCardValues() {
        handTotal = 0;
        for (int i = 0; i < hand.size(); i++) {
            String cardValueString = hand.get(i).getValue();
            handTotal += convertCardValueString(cardValueString);
        }
    }

    public int convertCardValueString(String cardValueString) {
        int cardValueInt;
        switch (cardValueString) {
            case "J", "Q", "K" -> cardValueInt = 10;
            case "A" -> cardValueInt = handTotal > 21
                    ? 1
                    : 11;
            default -> cardValueInt = Integer.parseInt(cardValueString);
        }

        return cardValueInt;
    }

    /**
     * Player wins hand or there is a push
     * @param amount added to player balance
     */
    public void addToBalance(int amount) {
        this.balance += amount;
    }

    /**
     * Player loses hand
     * @param amount subtracted from player balance
     */
    public void subtractFromBalance(int amount) {
        this.balance -= amount;
    }


}


