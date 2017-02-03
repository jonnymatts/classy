package com.jonnymatts.classy

class Randomizer {

    fun <T> randomEntry(list: List<T>): T {
        if(list.isEmpty()) throw IllegalArgumentException("List cannot be empty")
        return list[(Math.random() * list.size).toInt()]
    }
}