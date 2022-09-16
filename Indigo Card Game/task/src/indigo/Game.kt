package indigo

class Game {

    private val player = Player()
    private val computer = Player()
    private lateinit var playFirst: String

    fun play() {

        while (true) {
            print("Play first? (enter yes or no): \n> ")
            playFirst = readln()
            if (playFirst == "yes" || playFirst == "no") break
        }
        cardDeck.putInitialCardsOnTable()
        println()

        when (playFirst) {
            "yes" -> playerIsFirst()
            "no" -> computerIsFirst()
        }
    }

    private fun playerIsFirst() {
        while (cardDeck.cardsOnTheTable.size < 53) {
            if (player.cardsInHand.isEmpty()) player.takeDealtCards()
            if (computer.cardsInHand.isEmpty()) computer.takeDealtCards()
            playerPlay()
            computerPlay()
        }
    }

    private fun computerIsFirst(){
        while (cardDeck.cardsOnTheTable.size < 53) {
            if (computer.cardsInHand.isEmpty()) computer.takeDealtCards()
            if (player.cardsInHand.isEmpty()) player.takeDealtCards()
            computerPlay()
            playerPlay()
        }
    }

    private fun computerPlay() {
        if (cardDeck.cardsOnTheTable.size < 53) {
            println(
                "${cardDeck.cardsOnTheTable.size} cards on the table, and the top card is " +
                        cardDeck.cardsOnTheTable.last()
            )
            println("Computer plays ${computer.cardsInHand.last()}")
            cardDeck.cardsOnTheTable.add(computer.cardsInHand.last())
            computer.cardsInHand.removeLast()
            println()
        }
    }

    private fun playerPlay() {
        if (cardDeck.cardsOnTheTable.size < 53) {
            println(
                "${cardDeck.cardsOnTheTable.size} cards on the table, and the top card is " +
                        cardDeck.cardsOnTheTable.last()
            )
            println("Cards in hand: ${player.handForPrint.joinToString(" ")}")
            player.throwCard()
        }
    }
}