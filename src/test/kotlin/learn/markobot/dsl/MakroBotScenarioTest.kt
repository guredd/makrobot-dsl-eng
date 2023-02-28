package learn.markobot.dsl

import learn.markobot.api.*
import learn.markobot.api.WeekDay.*
import org.junit.jupiter.api.Test

class MakroBotScenarioTest {

    @Test
    fun turnBack() {
        val wallE = MakroBot("Wall-E",
                Head(Plastik(2), listOf(LampEye(10), LampEye(10)), Mouth(Speaker(3))),
                Body(Metal(1), listOf("I don't want to survive.", "I want live.")),
                Hands(Plastik(3), LoadClass.Light, LoadClass.Medium),
                Chassis.Caterpillar(10)
        )

        scenario {
            wallE {                             // invoke operator overload
                speed = 2                                   // initialization DSL
                power = 3
            }

            wallE forward 3                                // infix functions
            wallE.sing {
                +"We're coming down to the ground"
                +"To hear the birds sing in the trees"
                +"And the land will be looked after"
                +"To send the seeds out in the deep"
            }
            wallE.turnBack()
            wallE backward 3

            schedule {                          // context-based high level function with context-lambda

                //wall_e forward 3                        // control methods availability with @DslMarker

                repeat(mon at 10, tue at 12)     // typealias, infix functions, vararg
                except(13)
                repeat(wed..fri at 11)
            }

        }.launchNow()
                .resetSchedule()               // calls chaining
                .schedule {
                    repeat(fri at 23)
                }

        val (name, speed) = wallE               // destructuring declarations
    }
}