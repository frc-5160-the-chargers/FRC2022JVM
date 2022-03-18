package frc.robot.utils

import edu.wpi.first.math.controller.PIDController

/**
 * A data class representing the various constants needed to configure a PID controller.
 */
data class PIDConstants(
    /**
     * The constant that weights the proportional PID term.
     */
    @JvmField
    val kP: Double,
    /**
     * The constant that weights the integral PID term.
     */
    @JvmField
    val kI: Double,
    /**
     * The constant that weights the derivative PID term.
     */
    @JvmField
    val kD: Double
) {
    companion object {
        /**
         * Applies the values of a [PIDConstants] object to an existing [PIDController].
         */
        @Deprecated("Prefer the extension method.", ReplaceWith("controller.updateConstants(pidConstants)"))
        @JvmStatic
        fun updateController(controller: PIDController, pidConstants: PIDConstants) =
            controller.setConstants(pidConstants)
    }
}

fun PIDController.setConstants(constants: PIDConstants) =
    setPID(constants.kP, constants.kI, constants.kD)