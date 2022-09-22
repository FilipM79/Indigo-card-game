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
        val pointCardSigns = mutableListOf<String>()
        val otherCardSigns = mutableListOf<String>()
        val otherCards = mutableListOf<String>()
        var preferredPointCardSign = ""
        var preferredOtherCardSign = ""

        for (card in cardsInHand) {
            val cardRank = card.substring(0, card.lastIndex)
            val cardSign = card.last().toString()

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

        if (mapOfPointCards.isNotEmpty()) {
            preferredPointCardSign = mapOfPointCards.toList().maxByOrNull { (_, value) -> value }!!.first
        }

        if (mapOfOtherCards.isNotEmpty()) {
            preferredOtherCardSign = mapOfOtherCards.toList().maxByOrNull { (_, value) -> value }!!.first
        }

        println("pointSign: $preferredPointCardSign, otherSign: $preferredOtherCardSign")
        if (table.isEmpty()) {
            if (otherCards.isNotEmpty()) {
                for (card in otherCards) {
                    val cardSign = card.last().toString()
                    if (cardSign == preferredOtherCardSign) cardToThrow = card; break
                }
            } else if (pointCards.isNotEmpty()) {
                for (card in pointCards) {
                    val cardSign = card.last().toString()
                    if (cardSign == preferredOtherCardSign) cardToThrow = card; break
                }
            }else {
                cardToThrow = cardsInHand.random()
                println("Random card when table is empty")
            }

            println("cardToThrow when table is empty: $cardToThrow")

        }  else {
            val tableCardRank: String
            val tableCardSign: String

            if (table.size == 1) {
                tableCardRank = table[0].substring(0, table[0].lastIndex)
                tableCardSign = table[0].last().toString()
            } else {
                tableCardRank = table.last().substring(0, table.last().lastIndex)
                tableCardSign = table.last().last().toString()
            }

            println("tableCardRank: $tableCardRank, tableCardSign: $tableCardSign")
            println(pointCards)
            println(otherCards)

            val pointCardOnTable: Boolean = points.contains(tableCardRank)

            loop@ for (card in cardsInHand) {
                val cardRank = card.substring(0, card.lastIndex)
                val cardSign = card.last().toString()

                // if top table card is a pointCard and there are pointCards in hand...
                if (pointCardOnTable && pointCards.contains(card) && pointCards.isNotEmpty()) {
                    if (cardSign == preferredPointCardSign && cardSign == tableCardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih je na tabli, imam stihova u rukama. Nosim sa stihom u istom pref. znaku.")
                        break@loop

                    } else if (cardSign == tableCardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih je na tabli, imam stihova u rukama. Nosim sa stihom u istom znaku.")
                        break@loop

                    } else if (cardRank == tableCardRank) {
                        cardToThrow = card
                        win = true
                        println("Stih je na tabli, imam stihova u rukama. Nosim sa stihom u istom broju.")
                        break@loop

                    } else continue

                // If top table card is not a pointCard, but there are pointCards in hand ...
                } else if (!pointCardOnTable && pointCards.contains(card)){
                    if (cardSign == preferredPointCardSign && cardSign == tableCardSign && pointCards.isNotEmpty()) {
                        cardToThrow = card
                        win = true
                        println("Stih nije na tabli, imam stihova u rukama. Nosim sa stihom u istom pref. znaku.")
                        break@loop

                    } else if (cardSign == tableCardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih nije na tabli, imam stihova u rukama. Nosim sa stihom u istom znaku.")
                        break@loop

                    } else if (cardRank == tableCardRank) {
                        cardToThrow = card
                        win = true
                        println("Stih nije na tabli, imam stihova u rukama. Nosim sa stihom u istom broju.")
                        break@loop

                    } else if (otherCards.isEmpty() && cardSign == preferredPointCardSign) {
                        cardToThrow = card
                        println("Bacam stih jer nemam cime da nosim, i nema obicnih karata")
                        break@loop
                    }
                }

                // If top table card is a pointCard, but no pointCards in hand ...
                else if (pointCardOnTable && otherCards.contains(card) && otherCards.isNotEmpty()) {
                    if (cardSign == preferredOtherCardSign && cardSign == tableCardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih je na tabli, nemam stih u rukama. Nosim obicnom kartom u istom pref. znaku.")
                        break@loop

                    } else if (cardSign == tableCardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih je na tabli, nemam stih u rukama. Nosim sa obicnom kartom u istom znaku.")
                        break@loop

                    } else if (cardRank == tableCardRank) {
                        cardToThrow = card
                        win = true
                        println("Stih je na tabli, nemam stih u rukama. Nosim sa obicnom kartom u istom broju.")
                        break@loop
                    }

                // If top table card is not a pointCard, and there are no point cards in hand ...
                } else if (!pointCardOnTable && otherCards.contains(card) && otherCards.isNotEmpty()) {
                    if (cardSign == preferredOtherCardSign && cardSign == tableCardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih nije na tabli, nemam stih u rukama. Nosim sa obicnom kartom u istom znaku.")
                        break@loop

                    } else if (cardSign == tableCardSign) {
                        cardToThrow = card
                        win = true
                        println("Stih nije na tabli, nemam stih u rukama. Nosim sa obicnom kartom u istom znaku.")
                        break@loop

                    } else if (cardRank == tableCardRank) {
                        cardToThrow = card
                        win = true
                        println("Stih nije na tabli, nemam stih u rukama. Nosim sa obicnom kartom u istom broju.")
                        break@loop
                    }

                } else {

                    if (otherCards.isNotEmpty() && cardSign == preferredOtherCardSign && otherCards.contains(card)) {
                        cardToThrow = card
                        println("Nemam kartu sa kojom bih nosio. Bacam obicnu kartu sa pozeljnim znakom.")
                        break@loop

                    } else if (pointCards.isNotEmpty() && cardSign == preferredPointCardSign
                        && pointCards.contains(card)) {

                        cardToThrow = card
                        println("Nemam kartu sa kojom bih nosio. Bacam stih sa pozeljnim znakom.")
                        break@loop

//                    } else if (){
//                        ////////////////////////////////////
                    } else {
                        if (otherCards.isNotEmpty() && otherCards.contains(card)) {
                            cardToThrow = card
                            break@loop
                        } else if(pointCards.isNotEmpty() && pointCards.contains(card)) {
                            cardToThrow = card
                            break@loop
                        } else {
                            cardToThrow = cardsInHand.random()
                            println("Random card")
                            break@loop
                        }
                    }
                }
            }
        }
        print("Card is: $cardToThrow")
        println()
        return cardToThrow
    }
}