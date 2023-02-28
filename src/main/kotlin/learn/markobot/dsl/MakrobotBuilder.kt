@file:Suppress("FunctionName")

package learn.markobot.dsl

import learn.markobot.api.*
import kotlin.properties.Delegates

fun робот(name: String, setting: MakrobotContext.() -> Unit): MakroBot {
    return with(MakrobotContext().apply(setting)) { MakroBot(name, head, body, hands, шасси) }
}

@MakroBotDsl
class MakrobotContext {

    internal lateinit var head: Head
    internal lateinit var body: Body
    internal lateinit var hands: Hands
    lateinit var шасси: Chassis

    fun голова(setting: HeadContext.()->Unit) {
        with(HeadContext().apply(setting)) { this@MakrobotContext.head = Head(материал, eyes, mouth) }
    }

    fun туловище(setting: BodyContext.()->Unit) {
        with(BodyContext().apply(setting)) { this@MakrobotContext.body = Body(материал, strings) }
    }

    fun руки(setting: HandsContext.()->Unit) {
        with(HandsContext().apply(setting)) { this@MakrobotContext.hands = Hands(материал, нагрузка.start, нагрузка.endInclusive) }
    }

    infix fun ChassisDsl.шириной(width: Int): Chassis {
        return when(this) {
            ChassisDsl.caterpillar -> Chassis.Caterpillar(width)
        }
    }

    fun колеса(setting: WheelContext.()->Unit): Chassis {
        return with(WheelContext().apply(setting)) { Chassis.Wheel(количество, диаметр) }
    }
}

// top-level declaration to avoid enum static import
val металл = MaterialDsl.metal
val пластик = MaterialDsl.plastik

enum class MaterialDsl {
    metal, plastik
}

internal interface Materialized {
    var материал: Material

    infix fun MaterialDsl.толщиной(thickness: Int) {
        материал = when(this) {
            MaterialDsl.metal -> Metal(thickness)
            MaterialDsl.plastik -> Plastik(thickness)
        }
    }
}

@MakroBotDsl
class HeadContext: Materialized {

    override lateinit var материал: Material
    internal lateinit var eyes: List<Eye>
    internal lateinit var mouth: Mouth

    fun глаза(setting: EyesContext.() -> Unit) {
        with(EyesContext().apply(setting)) { this@HeadContext.eyes = eyes }
    }

    fun рот(setting: MouthContext.() -> Unit) {
        with(MouthContext().apply(setting)) { this@HeadContext.mouth = Mouth(speaker) }
    }
}

@MakroBotDsl
class EyesContext {
    internal val eyes: MutableList<Eye> = arrayListOf()

    fun диоды(setting: EyeContext.() -> Unit) {
        val (ledEye, quantity) = with(EyeContext().apply(setting)) { LedEye(яркость) to количество }
        eyes.apply { repeat(quantity) { add(ledEye) } }
    }

    fun лампы(setting: EyeContext.() -> Unit) {
        val (lampEye, quantity) = with(EyeContext().apply(setting)) { LampEye(яркость) to количество }
        eyes.apply { repeat(quantity) { add(lampEye) } }
    }
}

@MakroBotDsl
class EyeContext {
    var количество by Delegates.notNull<Int>()
    var яркость by Delegates.notNull<Int>()
}

@MakroBotDsl
class MouthContext {
    internal var speaker: Speaker? = null

    fun динамик(setting: SpeakerContext.() -> Unit) {
        speaker = with(SpeakerContext().apply(setting)) { Speaker(мощность) }
    }
}

@MakroBotDsl
class SpeakerContext {
    var мощность by Delegates.notNull<Int>()
}

@MakroBotDsl
class BodyContext: Materialized {

    override lateinit var материал: Material
    internal val strings: MutableList<String> = arrayListOf()

    fun надпись(setting: LabelContext.() -> Unit) {
        with(LabelContext().apply(setting)) { this@BodyContext.strings.addAll(strings) }
    }
}

@MakroBotDsl
class LabelContext {
    internal val strings: MutableList<String> = arrayListOf()

    operator fun String.unaryPlus() {
        strings.add(this)
    }
}

val `очень легкая` = LoadClass.VeryLight
val легкая = LoadClass.Light
val средняя = LoadClass.Medium
val тяжелая = LoadClass.Heavy
val `очень тяжелая` = LoadClass.VeryHeavy
val ненормальная = LoadClass.Enormous

@MakroBotDsl
class HandsContext: Materialized {
    override lateinit var материал: Material
    lateinit var нагрузка: ClosedRange<LoadClass>
}

operator fun LoadClass.minus(arg: LoadClass): ClosedRange<LoadClass> = object : ClosedRange<LoadClass> {
    override val endInclusive: LoadClass
        get() = arg
    override val start: LoadClass
        get() = this@minus
}

enum class ChassisDsl {
    caterpillar
}

val гусеницы = ChassisDsl.caterpillar

typealias ноги = Chassis.Legs

@MakroBotDsl
class WheelContext {
    var диаметр by Delegates.notNull<Int>()
    var количество by Delegates.notNull<Int>()
}
