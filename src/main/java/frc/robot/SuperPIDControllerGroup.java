package frc.robot;

import java.util.Arrays;

public class SuperPIDControllerGroup {
    SuperPIDController[] controllers;

    public SuperPIDControllerGroup(SuperPIDController[] controllers) {
        this.controllers = controllers;
    }

    public void stopAll() {
        Arrays.stream(controllers)
            .forEach(SuperPIDController::stop);
    }

    public void resetAll() {
        Arrays.stream(controllers)
            .forEach(SuperPIDController::reset);
    }

    public boolean allOnTarget() {
        return Arrays.stream(controllers)
            .allMatch(SuperPIDController::isOnTarget);
    }

    public void maybeSetAllConstantsFromDash() {
        Arrays.stream(controllers)
            .forEach(SuperPIDController::maybeSetConstantsFromDash);
    }

    public void maybePutAllConstantsToDash() {
        Arrays.stream(controllers)
            .forEach(SuperPIDController::maybePutConstantsToDash);
    }

    public void executeAll() {
        Arrays.stream(controllers)
            .forEach(SuperPIDController::execute);
    }
}
