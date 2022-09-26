package indigo
fun main() {
    val indigoGame = Game()
    indigoGame.play()
}
class Game : PrintMessage {
    var gameOver = false
    private val deck = mutableListOf <String>()
    private val table = mutableListOf <String>()
    private var whoWonLast = ""
    private var playFirst = ""

    fun play() {
        println("Indigo Card Game")
        Player().unpackNewDeck(deck)
        while (true) {
            print("Play first? \n> ")
            playFirst = readln()
            if (playFirst == "yes" || playFirst == "no") {
                break
            }
        }

        Player().shuffleDeck(deck = deck)
        Player().shuffleDeck(deck = deck)
        Player().putInitialCardsOnTable(deck = deck, table = table)
        val player = Player()
        val computer = Player()

        orderOfMoves(player = player, computer = computer) // This is where the game happens ...
        gameEnding(player = player, computer = computer)
    }
    private fun orderOfMoves(player: Player, computer: Player) {
        mainLoop@ while (true) {
            when (playFirst) {
                "yes" -> {
                    if (!gameOver) playerIsOnTheMove(player = player, computer = computer) else break@mainLoop
                    if (!gameOver) computerIsOnTheMove(player = player, computer = computer) else break@mainLoop
                }
                "no" -> {
                    if (!gameOver) computerIsOnTheMove(player = player, computer = computer) else break@mainLoop
                    if (!gameOver) playerIsOnTheMove(player = player, computer = computer) else break@mainLoop

                }
            }
        }
    }
    private fun computerIsOnTheMove(player: Player, computer: Player) {
        if (computer.cardsInHand.isEmpty()) computer.takeDealtCards(deck)
        gameOver = computerPlay(player = player, computer = computer)
        computer.score = score(computer.winStack)
        val totalWinStackSize = computer.winStack.size + player.winStack.size + table.size

        if (table.isEmpty() && totalWinStackSize < 52 && computer.cardToThrow != "exit") {
            println("Computer wins cards")
            printScore(player = player, computer = computer)
        }
    }
    private fun playerIsOnTheMove(player: Player, computer: Player) {
        if (player.cardsInHand.isEmpty()) player.takeDealtCards(deck)
        gameOver = playerPlay(player = player, computer = computer)
        player.score = score(winStack = player.winStack)
        val totalWinStackSize = computer.winStack.size + player.winStack.size + table.size

        if (table.isEmpty() && totalWinStackSize < 52 && player.cardToThrow != "exit") {
            println("Player wins cards")
            printScore(player = player, computer = computer)
        }
    }
    private fun score (winStack: MutableList<String>): Int {
        var score = 0
        for (card in winStack) {
            val points = listOf("A", "10", "J", "Q", "K")
            val cardRank = card.substring(0, card.lastIndex)
            if (points.contains(cardRank)) score++
        }
        return score
    }
    private fun computerPlay(player: Player, computer: Player): Boolean {
        var gameOver = false
        val cardRank: String
        val cardSign: String
        val tableRank: String
        val tableSign: String
        val stackSize = player.winStack.size + computer.winStack.size

        if (table.isEmpty() && stackSize < 52 && computer.cardToThrow != "exit") {
            printCardsOnTable(table)
            computer.cardToThrow = computer.chooseCard(table = table, cardsInHand = computer.cardsInHand)
            printComputerPlaysCard(computer = computer)
            table.add(computer.cardToThrow)
            computer.cardsInHand.remove(computer.cardToThrow)
        } else if (stackSize + table.size < 52 && computer.cardToThrow != "exit") {
            printCardsOnTable(table = table)
            computer.cardToThrow = computer.chooseCard(table = table, cardsInHand = computer.cardsInHand)
            printComputerPlaysCard(computer = computer)

            cardRank = computer.cardToThrow.substring(0, computer.cardToThrow.lastIndex)
            cardSign = computer.cardToThrow.last().toString()
            tableRank = table.last().substring(0, table.last().lastIndex)
            tableSign = table.last().last().toString()

            if (tableRank == cardRank || tableSign == cardSign) {
                table.add(computer.cardToThrow)
                computer.winStack.addAll(table)
                table.clear()
                computer.cardsInHand.remove(computer.cardToThrow)
                whoWonLast = "computer" // needed to decide who gets the last cards on the table ...
            } else {
                table.add(computer.cardToThrow)
                computer.cardsInHand.remove(computer.cardToThrow)
            }

            //What to do with last cards on table:
        } else if ((player.cardsInHand.size < 1 || computer.cardsInHand.size < 1) && deck.size == 0) {
            gameOver = whoGetsTheLastTable(player = player, computer = computer)
        }
        return gameOver
    }
    private fun playerPlay(player: Player, computer: Player): Boolean {
        val tableRank: String
        val tableSign: String
        val cardRank: String
        val cardSign: String
        var gameOver = false
        val stackSize = computer.winStack.size + player.winStack.size

        if (table.isEmpty() && stackSize < 52) {
            printCardsOnTable(table = table)
            printPlayerCardsInHand(player = player)
            player.cardToThrow = player.playerThrowCard()

            if (player.cardToThrow == "exit") {
                gameOver = true
            } else {
                table.add(player.cardToThrow)
            }

        } else if (stackSize + table.size < 52) {
            printCardsOnTable(table = table)
            printPlayerCardsInHand(player = player)
            player.cardToThrow = player.playerThrowCard()

            if (player.cardToThrow == "exit") {
                gameOver = true
            } else {
                cardRank = player.cardToThrow.substring(0, player.cardToThrow.lastIndex)
                cardSign = player.cardToThrow.last().toString()
                tableRank = table.last().substring(0, table.last().lastIndex)
                tableSign = table.last().last().toString()

                if (tableRank == cardRank || tableSign == cardSign) {
                    table.add(player.cardToThrow)
                    player.winStack.addAll(table)
                    table.clear()
                    player.cardsInHand.remove(player.cardToThrow)
                    whoWonLast = "player"
                } else {
                    table.add(player.cardToThrow)
                    player.cardsInHand.remove(player.cardToThrow)
                }
            }

            //What to do with last cards on table:
        } else if ((computer.cardsInHand.size < 1 || player.cardsInHand.size < 1) && deck.isEmpty()) {
            gameOver = whoGetsTheLastTable(player = player, computer = computer)
        }
        return gameOver
    }
    private fun whoGetsTheLastTable(player: Player, computer: Player): Boolean {
        if(whoWonLast == "player") {
            player.winStack.addAll(table)
            gameOver = true
        } else if (whoWonLast == "computer") {
            computer.winStack.addAll(table)
            gameOver = true
        } else if (playFirst == "yes") {
            println("main wtf3")
            player.winStack.addAll(table)
            gameOver = true
        } else if (playFirst == "no") {
            println("main wtf4")
            computer.winStack.addAll(table)
            gameOver = true
        }
        return gameOver
    }
    private fun gameEnding(player: Player, computer: Player) {
        if (player.cardToThrow == "exit" || computer.cardToThrow == "exit") {
            gameOver = true
        } else {
            if (computer.winStack.size > player.winStack.size) {
                ////
//                player.score = score(player.winStack) + 3
//                computer.score = score(computer.winStack)
                ////
                player.score = score(player.winStack)
                computer.score = score(computer.winStack) + 3
                ////
            } else if (player.winStack.size > computer.winStack.size) {
                player.score = score(player.winStack) + 3
                computer.score = score(computer.winStack)
            } else {
                when (playFirst) {
                    "yes" -> player.score += 3
                    "no" -> computer.score += 3
                }
            }
            printCardsOnTable(table = this.table)
            printScore(player = player, computer = computer)
        }
        println("Game over")
    }
}

class Player : GameLogic(), CardDealer {
    var cardsInHand = mutableListOf<String>()
    var handForPrint = mutableListOf<String>()
    var score = 0
    val winStack = mutableListOf<String>()
    var cardToThrow = ""
    fun takeDealtCards(deck: MutableList<String>) {
        cardsInHand = sixCardsFromDeck(deck = deck)
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
            } else if (cardNumber.any {it.isDigit()})  {
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
            } else {
                continue
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


