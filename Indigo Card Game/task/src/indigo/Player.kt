package indigo

class Player {

    var cardsInHand = mutableListOf<String>()
    var handForPrint = mutableListOf<String>()

    fun takeDealtCards() {
        cardsInHand = cardDeck.takeSixCardsFromDeck()
        for (index in 0..cardsInHand.lastIndex) {
            handForPrint.add(index, "${index + 1})${cardsInHand[index]}")
        }
    }

    fun throwCard(){
        var cardNumber: String
        while (true) {
            println("choose a card to play (1-${cardsInHand.size}):")
            print("> ")
            cardNumber = readln()
            println()
            if (cardNumber.toInt() in 1..cardsInHand.size) {
                val index = cardNumber.toInt() - 1
                cardDeck.cardsOnTheTable.add(cardsInHand[index])
                cardsInHand.removeAt(index)
                handForPrint.clear()
                for (i in 0..cardsInHand.lastIndex) {
                    handForPrint.add(i, "${i + 1})${cardsInHand[i]}")
                }
                break
            } else if (cardNumber == "exit") break
        }
    }
}