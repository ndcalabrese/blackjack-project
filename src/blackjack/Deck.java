package blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;

public class Deck {

    private ArrayList<Card> deck;

    public Deck() {
        deck = new ArrayList<>();
        String[] suits = { "spade", "diamond", "heart", "club" };
        String[] values = {"2", "3", "4", "5", "6", "7",
                "8", "9", "10", "J", "Q", "K", "A" };
        for (String suit : suits) {
            for (String value : values) {
                deck.add(new Card(suit, value));
            }
        }
    }

    public void shuffleDeck() {
        Collections.shuffle(deck, new Random());
    }

//    public Card getCard(int i){
//        return deck.get(i);
//    }

//    public int getDeckSize() {
//        return deck.size();
//    }

    public Card drawCard() {
        // Gets a random number between 0 and deck.size() - 1
        int randomNumber = ThreadLocalRandom.current().nextInt(0, deck.size());
        Card drawnCard = deck.get(randomNumber);
        this.deck.remove(randomNumber);

        return drawnCard;
    }

//    public void removeCard(int i){
//        this.deck.remove(i);
//    }

}
