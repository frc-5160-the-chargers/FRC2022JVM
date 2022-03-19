package frc.robot.utils

interface MotorConfig<M> {
    fun configure(motor: M)
}

fun <M> M.configure(config: MotorConfig<M>) = config.configure(this)