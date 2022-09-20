package indigo

fun main() {
    val indigoGame = Game()
    indigoGame.play()
}
class Game {
    var gameOver = false

    fun play() {
        val deck = mutableListOf <String>()
        val tableCards =  mutableListOf <String>()
        var playFirst: String

        println("Indigo Card Game")
        Player().unpackNewDeck(deck)

        while (true) {
            print("Play first? \n> ")
            playFirst = readln()
            if (playFirst == "yes" || playFirst == "no" ) {
                break
            }
        }

        Player().shuffleDeck(deck)
        Player().putInitialCardsOnTable(deck, tableCards)
        whichPlayerIsFirst(playFirst, deck, tableCards)

        println("Game Over")
    }
    private fun whichPlayerIsFirst(playFirst: String, deck: MutableList<String>, tableCards: MutableList<String>) {
        val player = Player()
        val computer = Player()

        while (!gameOver) {
            when (playFirst) {
                "yes" -> {
                    if (player.cardsInHand.isEmpty()) player.takeDealtCards(deck)
                    if (computer.cardsInHand.isEmpty()) computer.takeDealtCards(deck)
                    if (!gameOver) playerPlay(computer, tableCards)
                    if (!gameOver) computerPlay(player, tableCards)
                }
                "no" -> {
                    if (computer.cardsInHand.isEmpty()) computer.takeDealtCards(deck)
                    if (player.cardsInHand.isEmpty()) player.takeDealtCards(deck)
                    if (!gameOver) computerPlay(player, tableCards)
                    if (!gameOver) playerPlay(player, tableCards)
                }
            }
        }

    }
    private fun computerPlay(computer: Player, tableCards: MutableList<String>) {
        val computerWinStack = mutableListOf<String>()

        if (tableCards.size < 52) {
            println()
            println("${tableCards.size} cards on the table, and the top card is " +
                    tableCards.last())

            val cardToThrow = computer.chooseCard(tableCards, computer.cardsInHand)

            println("Computer plays $cardToThrow")
            computer.winOrNot(computer, computerWinStack, tableCards, cardToThrow)
            println("Cards on the table: $tableCards")

        } else {
            println()
            println("${tableCards.size} cards on the table, and the top card is " +
                    tableCards.last())
            gameOver = true
        }
    }
    private fun playerPlay(player: Player, tableCards: MutableList<String>) {
        gameOver = if (tableCards.size < 52) {
            println()
            println("${tableCards.size} cards on the table, and the top card is " +
                    tableCards.last())
            println("Cards in hand: ${player.handForPrint.joinToString(" ")}")
            player.throwCard(tableCards)
        } else {
            println()
            println("${tableCards.size} cards on the table, and the top card is " +
                    tableCards.last())
            true
        }
    }
}

class Player : GameLogic(), CardDealer {
    var cardsInHand = mutableListOf<String>()
    var handForPrint = mutableListOf<String>()

    fun takeDealtCards(deck: MutableList<String>) {
        cardsInHand = sixCardsFromDeck(deck)
        for (index in 0..cardsInHand.lastIndex) {
            handForPrint.add(index, "${index + 1})${cardsInHand[index]}")
        }
    }
    fun throwCard(tableCards: MutableList<String>): Boolean {
        var cardNumber: String
        var condition = Game().gameOver

        while (true) {
            println("choose a card to play (1-${cardsInHand.size}):")
            print("> ")
            cardNumber = readln()

            if (cardNumber == "exit") {
                condition = true
                break
            } else if (cardNumber.isEmpty() || cardNumber.any {!it.isDigit()})  {
                continue
            } else {
                if (cardNumber.toInt() in 1..cardsInHand.size) {
                    val index = cardNumber.toInt() - 1
                    tableCards.add(cardsInHand[index])
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
    private fun sixCardsFromDeck(deck: MutableList<String>): MutableList<String> {
        val removedCards = mutableListOf<String>()
        for (i in 0 until 6) {
            if (deck.size > 0) {
                removedCards.add(deck.last())
                deck.remove(deck.last())
            }
        }
        return removedCards
    }
}

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
    fun putInitialCardsOnTable(deck: MutableList<String>, cardsOnTheTable: MutableList<String>) {
        print("Initial cards on the table: ")
        for (i in 0 until 4) {
            print("${deck.last()} ")
            cardsOnTheTable.add(deck.last())
            deck.remove(deck.last())
        }
        println()
    }
}

abstract class GameLogic {
    private var win = false

    fun chooseCard(tableCards: MutableList<String>, cardsInHand: MutableList<String>): String {
        val points = listOf("A", "10", "J", "Q", "K")
        val tableCardRank = tableCards.last().substring(0, tableCards.last().lastIndex)
        val tableCardSign = tableCards.last().last()
        val pointCardOnTable = points.contains(tableCardRank)
        var cardToThrow = ""

        println("c-hand: $cardsInHand")

        loop@ for (i in 0..cardsInHand.lastIndex) {
            val cardRank = cardsInHand[i].substring(0, cardsInHand[i].lastIndex)
            val cardSign = cardsInHand[i].last()
            val sameRank = cardRank == tableCardRank
            val sameSign = cardSign == tableCardSign

            if (pointCardOnTable && sameRank) {
                if (cardToThrow.isNotEmpty() && points.contains(cardToThrow)) {
                    continue
                } else {
                    win = true
                    cardToThrow = cardsInHand[i]
                    break@loop
                }
            } else if (sameSign || sameRank) {
                if (win && !points.contains(cardToThrow)) {
                    continue
                } else {
                    win = true
                    cardToThrow = cardsInHand[i]
                    break@loop
                }
            } else {
                if (win) {
                    continue
                } else {
                    win = false

                    for (j in 0..cardsInHand.lastIndex) {
                        if (!points.contains(cardRank)) {

                            cardToThrow = cardsInHand.last()
                            if (cardToThrow.substring(0, cardToThrow.lastIndex) < cardRank) {
                                cardToThrow = cardsInHand[j]
                            } else {
                                continue
                            }

                        } else {
                            cardToThrow = cardsInHand.last()
                        }
                    }
                }
            }
        }
        return cardToThrow
    }
    fun winOrNot(anyPlayer: Player, winStack: MutableList<String>, table: MutableList<String>, cardToThrow: String) {
        if (win) {
            table.add(cardToThrow)
            winStack.addAll(table)
            table.clear()
            anyPlayer.cardsInHand.remove(cardToThrow)
        } else {
            table.add(cardToThrow)
            anyPlayer.cardsInHand.remove(cardToThrow)
        }
    }
}
