package indigo

interface PrintMessage {
    fun printComputerPlaysCard(computer: Player) = println("Computer plays ${computer.cardToThrow}")
    fun printCardsOnTable(table: MutableList<String>) {
        println()
        if (table.isEmpty()) {
            println("No cards on the table")
        } else {
            println("${table.size} cards on the table, and the top card is ${table.last()}")
        }
    }
    fun printScore(player: Player, computer: Player) {
        println("Score: Player ${player.score} - Computer ${computer.score}")
        println("Cards: Player ${player.winStack.size} - Computer ${computer.winStack.size}")
    }
    fun printPlayerCardsInHand(player: Player) {
        println("Cards in hand: ${player.handForPrint.joinToString(" ")}")
    }
}