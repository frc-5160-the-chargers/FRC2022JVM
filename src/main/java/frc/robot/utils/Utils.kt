package frc.robot.utils

val ClosedRange<Double>.spread: Double get() =
    endInclusive - start

fun Double.scaleBetweenRanges(fromRange: ClosedRange<Double>, toRange: ClosedRange<Double>): Double {
    val clampedValue = coerceIn(fromRange)
    val proportionIntoRange: Double = (clampedValue - fromRange.start) / fromRange.spread
    val distanceIntoToRange: Double = proportionIntoRange * toRange.spread
    return toRange.start + distanceIntoToRange
}
