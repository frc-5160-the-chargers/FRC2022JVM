package frc.robot

class SuperPIDControllerGroup(val controllers: List<SuperPIDController>) {
    constructor(vararg controllers: SuperPIDController) : this(controllers.asList())

    fun stopAll() = controllers.forEach(SuperPIDController::stop)
    fun resetAll() = controllers.forEach(SuperPIDController::reset)
    fun allOnTarget(): Boolean = controllers.all(SuperPIDController::isOnTarget)
    fun maybeSetAllConstantsFromDash() = controllers.forEach(SuperPIDController::maybeSetConstantsFromDash)
    fun maybePutAllConstantsToDash() = controllers.forEach(SuperPIDController::maybePutConstantsToDash)

    fun executeAll() = controllers.forEach(SuperPIDController::execute)
}