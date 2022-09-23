package indigo

import java.util.*

abstract class GameLogic {
    private var win = false

    fun chooseCard(table: MutableList<String>, cardsInHand: MutableList<String>): String {
        val points = listOf("A", "10", "J", "Q", "K")
        var cardToThrow = ""

        println(cardsInHand.joinToString(" "))

        val mapOfPointCards = mutableMapOf<String, Int>()
        val mapOfOtherCards = mutableMapOf<String, Int>()
        val pointCards = mutableListOf<String>()
        val allCardSigns = hashSetOf<String>()
        val pointCardSigns = mutableListOf<String>()
        val otherCardSigns = mutableListOf<String>()
        val otherCards = mutableListOf<String>()
        var preferredPointCardSign = ""
        var preferredOtherCardSign = ""
        var preferredAllCardSign = ""
        val allCardsRanks = mutableListOf<String>()

        for (card in cardsInHand) {
            val cardRank = card.substring(0, card.lastIndex)
            val cardSign = card.last().toString()

            allCardsRanks.add(card.substring(0, card.lastIndex))
            allCardSigns.add(cardSign)

            if (points.contains(cardRank)) {
                pointCards.add(card)
                pointCardSigns.add(cardSign)
            } else {
                otherCards.add(card)
                otherCardSigns.add(cardSign)
            }
        }

        for (i in pointCardSigns.distinct()) mapOfPointCards[i] = Collections.frequency(pointCardSigns, i)
        for (i in otherCardSigns.distinct()) mapOfOtherCards[i] = Collections.frequency(otherCardSigns, i)

        if (cardsInHand.isNotEmpty()) {
            preferredAllCardSign = mapOfPointCards.toList().maxByOrNull { (_, value) -> value }!!.first
        }

        if (mapOfPointCards.isNotEmpty()) {
            preferredPointCardSign = mapOfPointCards.toList().maxByOrNull { (_, value) -> value }!!.first
        }

        if (mapOfOtherCards.isNotEmpty()) {
            preferredOtherCardSign = mapOfOtherCards.toList().maxByOrNull { (_, value) -> value }!!.first
        }

        print("pointSign: $preferredPointCardSign, otherSign: $preferredOtherCardSign ")
        println(", allCardsSign: $preferredAllCardSign")


        // Start if deciding which card to throw ...
        // Card to throw when table is empty...
        if (table.isEmpty()) {
            if (otherCards.isNotEmpty()) {
                for (card in otherCards) {
                    val cardSign = card.last().toString()
                    if (cardSign == preferredOtherCardSign) {
                        cardToThrow = card
                        break
                    }
                }

            } else if (pointCards.isNotEmpty()) {
                for (card in pointCards) {
                    val cardSign = card.last().toString()
                    if (cardSign == preferredPointCardSign) {
                        cardToThrow = card
                        break
                    }
                }

            } else {
                cardToThrow = cardsInHand.random()
                println("Random card when table is empty")
            }
            println("cardToThrow when table is empty: $cardToThrow")

        }  else { //if table is not empty
            val tableCardRank: String
            val tableCardSign: String

            if (table.size == 1) {
                tableCardRank = table[0].substring(0, table[0].lastIndex)
                tableCardSign = table[0].last().toString()
            } else {
                tableCardRank = table.last().substring(0, table.last().lastIndex)
                tableCardSign = table.last().last().toString()
            }

            println(pointCards)
            println(otherCards)

            val pointCardOnTable: Boolean = points.contains(tableCardRank)

            // looping through cardsInHand if table is not empty ...
            loop@ for (card in cardsInHand) {
                val cardRank = card.substring(0, card.lastIndex)
                val cardSign = card.last().toString()

                if (!allCardSigns.contains(tableCardSign)) {

                    if (allCardsRanks.contains(tableCardRank)) {
                        if (pointCards.isNotEmpty() && pointCards.contains(card) && preferredAllCardSign == cardSign) {
                            println("First")
                            cardToThrow = card
                            break@loop

                        } else if (otherCards.isNotEmpty() && otherCards.contains(card)
                            && preferredAllCardSign == cardSign) {

                            println("Second")
                            cardToThrow = card
                            break@loop
                        }
                    } else {
                        if (otherCards.isNotEmpty() && otherCards.contains(card) && preferredAllCardSign == cardSign) {
                            cardToThrow = card
                            println("Third")

                        } else if (pointCards.isNotEmpty() && pointCards.contains(card)
                            && preferredAllCardSign == cardSign) {

                            cardToThrow = card
                            println("Fourth")
                        }
                    }

                    // 1. if top table card IS a pointCard and there are pointCards in hand...
                } else if (pointCardOnTable && pointCards.isNotEmpty() && pointCards.contains(card)) {

                        // 1.
                    if (cardSign == tableCardSign && cardSign == preferredPointCardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih je na tabli. Nosim sa stihom u istom preferiranom znaku.")
                        break@loop

                        // 2.
                    } else if (cardSign == tableCardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih je na tabli. Nosim sa stihom u istom znaku.")
                        break@loop

                        // 3.
                    } else if (cardRank == tableCardRank && preferredAllCardSign == cardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih je na tabli. Nosim sa stihom u istom broju sa preferiranim znakom.")
                        break@loop

                        // 4.
                    } else if (cardRank == tableCardRank) {
                        cardToThrow = card
                        win = true
                        println("Stih je na tabli.  Nosim sa stihom u istom broju.")
                        break@loop

                    }

                    // 2. if top table card IS a pointCard, but no PointCards have the same sign or rank ...
                } else if (pointCardOnTable && otherCards.isNotEmpty() && otherCards.contains(card)) {

                        // 5.
                    if (cardSign == tableCardSign && cardSign == preferredOtherCardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih je na tabli. Nosim sa obicnom kartom u istom preferiranom znaku.")
                        break@loop

                        // 6.
                    } else if (cardSign == tableCardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih je na tabli. Nosim sa obicnom kartom u istom znaku.")
                        break@loop

                        // 7.
                    } else if (cardRank == tableCardRank && preferredAllCardSign == cardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih je na tabli. Nosim sa obicnom kartom u istom broju sa preferiranim znakom.")
                        break@loop

                        // 8.
                    } else if (cardRank == tableCardRank) {
                        cardToThrow = card
                        win = true
                        println("Stih je na tabli.  Nosim sa obicnom kartom u istom broju.")
                        break@loop

                    }

                    // 3. if top table card is NOT a pointCard and there are pointCards in hand...
                } else if (pointCards.isNotEmpty() && !pointCardOnTable && pointCards.contains(card)) {

                        // 9.
                    if (cardSign == tableCardSign && cardSign == preferredPointCardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih nije na tabli. Nosim sa stihom u istom preferiranom znaku.")
                        break@loop

                        // 10.
                    } else if (cardSign == tableCardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih nije na tabli. Nosim sa stihom u istom znaku.")
                        break@loop

                        // 11.
                    } else if (cardRank == tableCardRank && preferredAllCardSign == cardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih nije na tabli. Nosim sa stihom u istom broju sa preferiranim znakom.")
                        break@loop

                        // 12.
                    } else if (cardRank == tableCardRank) {
                        cardToThrow = card
                        win = true
                        println("Stih nije na tabli.  Nosim sa stihom u istom broju.")
                        break@loop

                    }

                    // 4. if top table card is NOT a pointCard, but no PointCards have the same sign or rank ...
                } else if (otherCards.isNotEmpty() && !pointCardOnTable && otherCards.contains(card)) {

                        // 13.
                    if (cardSign == tableCardSign && cardSign == preferredPointCardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih nije na tabli. Nosim sa obicnom kartom u istom preferiranom znaku.")
                        break@loop

                        // 14.
                    } else if (cardSign == tableCardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih nije na tabli. Nosim sa obicnom kartom u istom znaku.")
                        break@loop

                        // 15.
                    } else if (cardRank == tableCardRank && preferredAllCardSign == cardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih nije na tabli. Nosim sa obicnom kartom u istom broju sa preferiranim znakom.")
                        break@loop

                        // 16.
                    } else if (cardRank == tableCardRank) {
                        cardToThrow = card
                        win = true
                        println("Stih nije na tabli.  Nosim sa obicnom kartom u istom broju.")
                        break@loop
                    }

                    // Top table card has no same sign or rank as any cards in hands...
                } else if (!allCardsRanks.contains(tableCardRank) || !allCardSigns.contains(tableCardSign)){

                        // 17
                    if (otherCards.isNotEmpty() && otherCards.contains(card)
                        && preferredOtherCardSign == cardSign) {

                        println("Nemam kartu sa kojom bih nosio. Bacam obicnu kartu sa pozeljnim znakom.")
                        cardToThrow = card
                        break@loop

                        // 18
                    } else if (otherCards.isNotEmpty() && otherCards.contains(card)
                        && preferredAllCardSign == cardSign) {

                        println("Nemam kartu sa kojom bih nosio. Bacam obicnu kartu sa pozeljnim glavnim znakom.")
                        cardToThrow = card
                        break@loop

                        // 19
                    } else if(pointCards.isNotEmpty() && pointCards.contains(card)
                        && preferredPointCardSign == cardSign) {

                        println("Nemam kartu sa kojom bih nosio. Bacam stih sa pozeljnim znakom.")
                        cardToThrow = card
                        break@loop

                        // 20
                    } else if(pointCards.isNotEmpty() && pointCards.contains(card)
                        && preferredAllCardSign == cardSign) {

                        println("Nemam kartu sa kojom bih nosio. Bacam stih sa pozeljnim glavnim znakom.")
                        cardToThrow = card
                        break@loop

                        // 21
                    }

                    // 22
                } else if (otherCards.isNotEmpty() && otherCards.contains(card)) {

                    println("Nemam kartu sa kojom bih nosio. Bacam obicnu kartu.")
                    cardToThrow = card
                    break@loop

                    // 23
                } else if(pointCards.isNotEmpty() && pointCards.contains(card)) {

                    println("Nemam kartu sa kojom bih nosio. Bacam stih.")
                    cardToThrow = card
                    break@loop

                    // 24
                } else if (pointCards.isEmpty() && preferredAllCardSign == cardSign && otherCards.contains(card)){
                    println("Just all card sign 1")
                    cardToThrow = card
                    break@loop

                    // 25
                } else if (otherCards.isEmpty() && preferredAllCardSign == cardSign && pointCards.contains(card)){
                    println("Just all card sign 2")
                    cardToThrow = card
                    break@loop

                    // 26
                } else {
                    println("wtf")
                    cardToThrow = cardsInHand.random()
                }
            }
        }
        return cardToThrow
    }
}