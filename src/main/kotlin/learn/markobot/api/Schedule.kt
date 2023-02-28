@file:Suppress("EnumEntryName")
package learn.markobot.api

import learn.markobot.dsl.MakroBotDsl

enum class WeekDay {
    mon, tue, wed, thu, fri, sat, sun
}

@MakroBotDsl
class Schedule {

    val timePoints = arrayListOf<Pair<WeekDay, Int>>()
    val exceptDaysOfMonth = arrayListOf<Int>()

    override fun toString(): String {
        return buildString {
            append(timePoints.joinToString(prefix = "Schedule: ") { "${it.first} at ${it.second}h" })
            if (exceptDaysOfMonth.isNotEmpty()) {
                append(exceptDaysOfMonth.joinToString(prefix = " except: ", postfix = " days of month"))
            }
        }
    }
}
