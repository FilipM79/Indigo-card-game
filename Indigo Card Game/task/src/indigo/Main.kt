package indigo

fun main() {
    val indigoGame = Game()
    indigoGame.play()
}

class Game {
    var gameOver = false

    fun play() {
        val deck = mutableListOf <String>()
        val table =  mutableListOf <String>()
        var playFirst: String

        println("Indigo Card Game")
        Player().unpackNewDeck(deck)

        while (true) {
            print("Play first? \n> ")
            playFirst = readln()
            if (playFirst == "yes" || playFirst == "no") {
                break
            }
        }

        Player().shuffleDeck(deck)
        Player().putInitialCardsOnTable(deck, table)
        whichPlayerIsFirst(playFirst, deck, table)

        println("Game Over")
    }

    private fun whichPlayerIsFirst(playFirst: String, deck: MutableList<String>, table: MutableList<String>) {

        val player = Player()
        val computer = Player()
        val computerWinStack = mutableListOf<String>()
        val playerWinStack = mutableListOf<String>()

        mainLoop@ while (!gameOver) {
            when (playFirst) {
                "yes" -> {
                    if (player.cardsInHand.isEmpty()) player.takeDealtCards(deck)
                    if (computer.cardsInHand.isEmpty()) computer.takeDealtCards(deck)

                    if (!gameOver) {
                        gameOver = playerPlay(player, table, playerWinStack, computerWinStack.size)
                    } else {
                        break@mainLoop
                    }

                    if (!gameOver) {
                        gameOver = computerPlay(computer,table,computerWinStack,playerWinStack.size)
                    } else break@mainLoop
                }

                "no" -> {
                    if (computer.cardsInHand.isEmpty()) computer.takeDealtCards(deck)
                    if (player.cardsInHand.isEmpty()) player.takeDealtCards(deck)

                    if (!gameOver) {
                        gameOver = computerPlay(computer,table,computerWinStack,playerWinStack.size)
                    } else break@mainLoop

                    if (!gameOver) {
                        gameOver = playerPlay(player, table, playerWinStack, computerWinStack.size)
                    } else break@mainLoop
                }
            }
        }
    }

    private fun computerPlay(computer: Player, table: MutableList<String>, winStack: MutableList<String>,
                             playerWinStackSize: Int): Boolean {

        var gameOver = false
        val cardToThrow: String

        if (table.isEmpty() && playerWinStackSize + winStack.size < 22) {

            println()
            println("No cards on the table")

            cardToThrow = computer.chooseCard(table, computer.cardsInHand)
            println("Computer plays $cardToThrow")
            table.add(cardToThrow)
            computer.cardsInHand.remove(cardToThrow)

        } else {

            if (playerWinStackSize + winStack.size + table.size < 22) {
                println()
                println(
                    "${table.size} cards on the table, and the top card is " +
                            table.last()
                )

                cardToThrow = computer.chooseCard(table, computer.cardsInHand)
                println("Computer plays $cardToThrow")

                val cardRank = cardToThrow.substring(0, cardToThrow.lastIndex)
                val cardSign = cardToThrow.last()
                val tableRank = table.last().substring(0, table.last().lastIndex)
                val tableSign = table.last().last()

                if (tableRank == cardRank || tableSign == cardSign) {
                    table.add(cardToThrow)
                    winStack.addAll(table)
                    table.clear()
                    computer.cardsInHand.remove(cardToThrow)
                } else {
                    table.add(cardToThrow)
                    computer.cardsInHand.remove(cardToThrow)
                }

            } else {
                gameOver = true
            }
        }
        return gameOver
    }
    private fun playerPlay(player: Player, table: MutableList<String>, winStack: MutableList<String>,
                           computerWinStackSize: Int): Boolean {

        val cardToThrow: String
        val tableRank: String
        val tableSign: String
        var gameOver = false

        if (table.isEmpty() && computerWinStackSize + winStack.size < 22) {
            println()
            println("No cards on the table")
            println("Cards in hand: ${player.handForPrint.joinToString(" ")}")
            println(player.cardsInHand)

            cardToThrow = player.playerThrowCard()
            if (cardToThrow == "exit") {
                gameOver = true
            } else {
                table.add(cardToThrow)
            }

        } else if (computerWinStackSize + winStack.size + table.size < 22) {
            println()
            println("${table.size} cards on the table, and the top card is ${table.last()}")
            println("Cards in hand: ${player.handForPrint.joinToString(" ")}")
            println(player.cardsInHand)

            cardToThrow = player.playerThrowCard()
            if (cardToThrow == "exit") {
                gameOver = true
            } else {
                tableRank = table.last().substring(0, table.last().lastIndex)
                tableSign = table.last().last().toString()

                val cardRank: String = cardToThrow.substring(0, cardToThrow.lastIndex)
                val cardSign: String = cardToThrow.last().toString()

                if (tableRank == cardRank || tableSign == cardSign) {
                    table.add(cardToThrow)
                    winStack.addAll(table)
                    table.clear()
                    player.cardsInHand.remove(cardToThrow)
                } else {
                    table.add(cardToThrow)
                    player.cardsInHand.remove(cardToThrow)
                }
            }
        }
        return gameOver
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
    fun playerThrowCard(): String {
        var cardNumber: String
        val cardToThrow: String

        loop@ while (true) {
            println("choose a card to play (1-${cardsInHand.size}):")
            print("> ")
            cardNumber = readln()

            if (cardNumber == "exit") {
                cardToThrow = cardNumber
                Game().gameOver = true
                break@loop

            } else if (cardNumber.isEmpty() || cardNumber.any {!it.isDigit()})  {
                continue

            } else {
                if (cardNumber.toInt() in 1..cardsInHand.size) {
                    val index = cardNumber.toInt() - 1
                    cardToThrow = cardsInHand[index]
                    cardsInHand.removeAt(index)
                    handForPrint.clear()

                    for (i in 0..cardsInHand.lastIndex) {
                        handForPrint.add(i, "${i + 1})${cardsInHand[i]}")
                    }
                    break
                }
            }
        }
        return cardToThrow
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


