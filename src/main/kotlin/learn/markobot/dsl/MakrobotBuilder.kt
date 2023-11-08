package learn.markobot.dsl

import learn.markobot.api.*
import kotlin.properties.Delegates

fun robot(name: String, setting: MakrobotContext.() -> Unit): MakroBot {
    return with(MakrobotContext().apply(setting)) { MakroBot(name, head, body, hands, chassis) }
}

@MakroBotDsl
class MakrobotContext {

    internal lateinit var head: Head
    internal lateinit var body: Body
    internal lateinit var hands: Hands
    lateinit var chassis: Chassis

    fun head(setting: HeadContext.()->Unit) {
        with(HeadContext().apply(setting)) { this@MakrobotContext.head = Head(material, eyes, mouth) }
    }

    fun body(setting: BodyContext.()->Unit) {
        with(BodyContext().apply(setting)) { this@MakrobotContext.body = Body(material, strings) }
    }

    fun hands(setting: HandsContext.()->Unit) {
        with(HandsContext().apply(setting)) { this@MakrobotContext.hands = Hands(material, load.start, load.endInclusive) }
    }

    infix fun ChassisDsl.withWidth(width: Int): Chassis {
        return when(this) {
            ChassisDsl.Caterpillar -> Chassis.Caterpillar(width)
        }
    }

    fun wheels(setting: WheelContext.()->Unit): Chassis {
        return with(WheelContext().apply(setting)) { Chassis.Wheel(quantity, diameter) }
    }
}

// top-level declaration to avoid enum static import
val metal = MaterialDsl.Metal
val plastic = MaterialDsl.Plastic

enum class MaterialDsl {
    Metal, Plastic
}

internal interface Materialized {
    var material: Material

    infix fun MaterialDsl.withThickness(thickness: Int) {
        material = when(this) {
            MaterialDsl.Metal -> Metal(thickness)
            MaterialDsl.Plastic -> Plastik(thickness)
        }
    }
}

@MakroBotDsl
class HeadContext: Materialized {

    override lateinit var material: Material
    internal lateinit var eyes: List<Eye>
    internal lateinit var mouth: Mouth

    fun eyes(setting: EyesContext.() -> Unit) {
        with(EyesContext().apply(setting)) { this@HeadContext.eyes = eyes }
    }

    fun mouth(setting: MouthContext.() -> Unit) {
        with(MouthContext().apply(setting)) { this@HeadContext.mouth = Mouth(speaker) }
    }
}

@MakroBotDsl
class EyesContext {
    internal val eyes: MutableList<Eye> = arrayListOf()

    fun diods(setting: EyeContext.() -> Unit) {
        val (ledEye, quantity) = with(EyeContext().apply(setting)) { LedEye(brightness) to quantity }
        eyes.apply { repeat(quantity) { add(ledEye) } }
    }

    fun lamps(setting: EyeContext.() -> Unit) {
        val (lampEye, quantity) = with(EyeContext().apply(setting)) { LampEye(brightness) to quantity }
        eyes.apply { repeat(quantity) { add(lampEye) } }
    }
}

@MakroBotDsl
class EyeContext {
    var quantity by Delegates.notNull<Int>()    // why lateinit is not possible here?
    var brightness by Delegates.notNull<Int>()
}

@MakroBotDsl
class MouthContext {
    internal var speaker: Speaker? = null

    fun speaker(setting: SpeakerContext.() -> Unit) {
        speaker = with(SpeakerContext().apply(setting)) { Speaker(power) }
    }
}

@MakroBotDsl
class SpeakerContext {
    var power by Delegates.notNull<Int>()
}

@MakroBotDsl
class BodyContext: Materialized {

    override lateinit var material: Material
    internal val strings: MutableList<String> = arrayListOf()

    fun label(setting: LabelContext.() -> Unit) {
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

val `very light` = LoadClass.VeryLight
val light = LoadClass.Light
val intermediate = LoadClass.Medium
val heavy = LoadClass.Heavy
val `very heavy` = LoadClass.VeryHeavy
val enormous = LoadClass.Enormous

@MakroBotDsl
class HandsContext: Materialized {
    override lateinit var material: Material
    lateinit var load: ClosedRange<LoadClass>
}

operator fun LoadClass.minus(arg: LoadClass): ClosedRange<LoadClass> = object : ClosedRange<LoadClass> {
    override val endInclusive: LoadClass
        get() = arg
    override val start: LoadClass
        get() = this@minus
}

enum class ChassisDsl {
    Caterpillar
}

val caterpillar = ChassisDsl.Caterpillar

typealias legs = Chassis.Legs

@MakroBotDsl
class WheelContext {
    var diameter by Delegates.notNull<Int>()
    var quantity by Delegates.notNull<Int>()
}
