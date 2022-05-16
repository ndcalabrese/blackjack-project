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

    public boolean getIsDealer() {
        return this.isDealer;
    }

    public int getCurrentBalance() {
        return this.balance;
    }

    public String getCardValue(int card) {
        return this.hand.get(card).getValue();
    }

    public ArrayList<Card> getHand() {
        return this.hand;
    }

    public int getHandTotal() {
        return this.handTotal;
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

        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            ArrayList<String> renderedCard = new ArrayList<>();
            String suitSymbol = null;
            String cardValue = card.getValue();

            switch (card.getSuit()) {
                case("spade")  -> suitSymbol = "♠";
                case("diamond")  -> suitSymbol = "♦";
                case("heart")  -> suitSymbol = "♥";
                case("club")  -> suitSymbol = "♣";
            }

            if (i == 1 && isSecondCardHidden) {
                renderedCard.add("┌─────────┐");
                renderedCard.add("│░░░░░░░░░│");
                renderedCard.add("│░░░░░░░░░│");
                renderedCard.add("│░░░░░░░░░│");
                renderedCard.add("│░░░░░░░░░│");
                renderedCard.add("│░░░░░░░░░│");
                renderedCard.add("└─────────┘");
            } else if (card.getValue().equals("10")) {
                renderedCard.add("┌─────────┐");
                renderedCard.add("│ 10      │");
                renderedCard.add("│         │");
                renderedCard.add("│    " + suitSymbol + "    │");
                renderedCard.add("│         │");
                renderedCard.add("│      10 │");
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
            switch (getCardValue(0)) {
                case "10":
                case "J":
                case "Q":
                case "K": {
                    // Peek at dealer's second card
                    if (getCardValue(1).equals("A")) {
                        System.out.println("\nDEALER'S HAND: " +
                                (getHandTotal()));
                        renderedCards = renderCards(false);
                    } else {
                        System.out.println("\nDEALER'S HAND: " +
                                (getHandTotal() - convertCardValueString(getCardValue(1))));
                        renderedCards = renderCards(true);
                    }
                    break;
                }
                case "A": {
                    // Peek at dealer's second card
                    switch (getCardValue(1)) {
                        case "10":
                        case "J":
                        case "Q":
                        case "K": {
                            System.out.println("\nDEALER'S HAND: " +
                                    (getHandTotal()));
                            renderedCards = renderCards(false);
                            break;
                        }
                        default: {
                            System.out.println("\nDEALER'S HAND: " +
                                    (getHandTotal() - convertCardValueString(getCardValue(1))));
                            renderedCards = renderCards(true);
                            break;
                        }
                    }
                }
                default: {
                    // Reveal hidden card if player is standing
                    if (isUserStanding) {
                        System.out.println("\nDEALER'S HAND: " +
                                (getHandTotal()));
                        renderedCards = renderCards(false);
                    // Keep second card hidden if player is not standing
                    } else {
                        System.out.println("\nDEALER'S HAND: " +
                                (getHandTotal() - convertCardValueString(getCardValue(1))));
                        renderedCards = renderCards(true);
                    }
                    break;
                }
            }
        } else {
            System.out.println("\nYOUR HAND: " + getHandTotal());
            renderedCards = renderCards(false);
        }



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


