package indigo

class Player {

    var cardsInHand = mutableListOf<String>()
    var handForPrint = mutableListOf<String>()

    fun takeDealtCards() {
        cardsInHand = cardDealer.takeSixCardsFromDeck()
        for (index in 0..cardsInHand.lastIndex) {
            handForPrint.add(index, "${index + 1})${cardsInHand[index]}")
        }
    }
    fun throwCard(): Boolean {
        var cardNumber: String
        var condition = gameOver

        while (true) {
            println("choose a card to play (1-${cardsInHand.size}):")
            print("> ")
            cardNumber = readln()
            println()

            if (cardNumber == "exit") {
                condition = true
                break
            } else if (cardNumber.isEmpty() || cardNumber.any {!it.isDigit()})  {
                condition = false
            } else {
                if (cardNumber.toInt() in 1..cardsInHand.size) {
                    val index = cardNumber.toInt() - 1
                    cardDealer.cardsOnTheTable.add(cardsInHand[index])
                    cardsInHand.removeAt(index)
                    handForPrint.clear()
                    for (i in 0..cardsInHand.lastIndex) {
                        handForPrint.add(i, "${i + 1})${cardsInHand[i]}")
                    }
                    break
                }
            }
        }
        return condition
    }
    fun takeNewHand() = cardDealer.takeSixCardsFromDeck()
    fun shuffleDeck() = cardDealer.shuffleDeck()
    fun putInitialCardsOnTable() = cardDealer.putInitialCardsOnTable()
}