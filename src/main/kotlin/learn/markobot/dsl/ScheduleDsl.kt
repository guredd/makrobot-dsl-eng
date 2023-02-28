package learn.markobot.dsl

import learn.markobot.api.Schedule
import learn.markobot.api.WeekDay

fun MakroBotScenario.schedule(scheduleFun: Schedule.() -> Unit): MakroBotScenario {
    this.schedule = Schedule().apply(scheduleFun)
    return this
}

fun MakroBotScenario.resetSchedule(): MakroBotScenario {
    this.schedule = null
    return this
}

typealias time = Pair<WeekDay, Int>

infix fun WeekDay.at(hour: Int) = time(this, hour)

fun Schedule.repeat(vararg timePointsToAdd: time) {
    timePoints.addAll(timePointsToAdd)
}

infix fun ClosedRange<WeekDay>.at(hour: Int): List<time> {
    return WeekDay.values().filter { it in this }.map { time(it, hour) }           // can't iterate over ClosedRange
}

fun Schedule.repeat(timePointsToAdd: List<time>) = this@repeat.repeat(*timePointsToAdd.toTypedArray())

fun Schedule.except(vararg daysOfMonth: Int) {
    exceptDaysOfMonth.addAll(daysOfMonth.toList())
}
