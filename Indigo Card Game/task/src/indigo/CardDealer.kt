package indigo

interface CardDealer {
    fun unpackNewDeck(deck: MutableList<String>) {
        val cardRanks = listOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        val cardSuits = listOf("♦", "♥", "♠", "♣")
        var cardCounter = 0

        for (i in cardSuits.indices) {
            for (j in cardRanks.indices) {
                deck.add(cardCounter, cardRanks[j] + cardSuits[i])
                cardCounter++
            }
        }
    }
    fun shuffleDeck(deck: MutableList<String>) {
        deck.shuffle()
    }
    fun putInitialCardsOnTable(deck: MutableList<String>, table: MutableList<String>) {
        print("Initial cards on the table: ")
        for (i in 0 until 4) {
            print("${deck.last()} ")
            table.add(deck.last())
            deck.remove(deck.last())
        }
        println()
    }
}