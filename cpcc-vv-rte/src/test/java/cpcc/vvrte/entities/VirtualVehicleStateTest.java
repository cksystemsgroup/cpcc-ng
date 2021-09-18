// This code is part of the CPCC-NG project.
//
// Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software Foundation,
// Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

package cpcc.vvrte.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * VirtualVehicleStateTest
 */
class VirtualVehicleStateTest
{
    /*
     * VirtualVehicleState.INIT
     */
    static Stream<Arguments> validTransitionsFromInitDataprovider()
    {
        return Stream.of(
            arguments(VirtualVehicleState.RUNNING));
    }

    @ParameterizedTest
    @MethodSource("validTransitionsFromInitDataprovider")
    void shouldDetectValidTransitionsFromInit(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.INIT.canTraverseTo(state);
        assertThat(result).isTrue();
    }

    static Stream<Arguments> invalidTransitionsFromInitDataprovider()
    {
        return Stream.of(
            arguments(VirtualVehicleState.INIT),
            //            arguments(VirtualVehicleState.WAITING),
            arguments(VirtualVehicleState.MIGRATION_AWAITED_SND),
            arguments(VirtualVehicleState.MIGRATING_SND),
            arguments(VirtualVehicleState.MIGRATION_INTERRUPTED_SND),
            arguments(VirtualVehicleState.MIGRATION_COMPLETED_SND),
            arguments(VirtualVehicleState.FINISHED),
            arguments(VirtualVehicleState.INTERRUPTED),
            arguments(VirtualVehicleState.TASK_COMPLETION_AWAITED),
            arguments(VirtualVehicleState.DEFECTIVE));
    }

    @ParameterizedTest
    @MethodSource("invalidTransitionsFromInitDataprovider")
    void shouldDetectInvalidTransitionsFromInit(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.INIT.canTraverseTo(state);
        assertThat(result).isFalse();
    }

    /*
     * VirtualVehicleState.RUNNING
     */
    static Stream<Arguments> validTransitionsFromRunningDataprovider()
    {
        return Stream.of(
            //            arguments(VirtualVehicleState.WAITING),
            arguments(VirtualVehicleState.FINISHED));
    }

    @ParameterizedTest
    @MethodSource("validTransitionsFromRunningDataprovider")
    void shouldDetectValidTransitionsFromRunning(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.RUNNING.canTraverseTo(state);
        assertThat(result).isTrue();
    }

    static Stream<Arguments> invalidTransitionsFromRunningDataprovider()
    {
        return Stream.of(
            arguments(VirtualVehicleState.INIT),
            arguments(VirtualVehicleState.RUNNING),
            arguments(VirtualVehicleState.MIGRATION_AWAITED_SND),
            arguments(VirtualVehicleState.MIGRATING_SND),
            arguments(VirtualVehicleState.MIGRATION_INTERRUPTED_SND),
            arguments(VirtualVehicleState.MIGRATION_COMPLETED_SND),
            arguments(VirtualVehicleState.INTERRUPTED),
            arguments(VirtualVehicleState.TASK_COMPLETION_AWAITED),
            arguments(VirtualVehicleState.DEFECTIVE));
    }

    @ParameterizedTest
    @MethodSource("invalidTransitionsFromRunningDataprovider")
    void shouldDetectInvalidTransitionsFromRunning(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.RUNNING.canTraverseTo(state);
        assertThat(result).isFalse();
    }

    /*
     * VirtualVehicleState.WAITING
     */
    static Stream<Arguments> validTransitionsFromWaitingDataprovider()
    {
        return Stream.of(
            arguments(VirtualVehicleState.RUNNING),
            arguments(VirtualVehicleState.MIGRATING_SND));
    }

    //    @ParameterizedTest
    //    @MethodSource("validTransitionsFromWaitingDataprovider")
    //    void shouldDetectValidTransitionsFromWaiting(VirtualVehicleState state)
    //    {
    //        boolean result = VirtualVehicleState.WAITING.canTraverseTo(state);
    //        assertThat(result).isTrue();
    //    }

    //    static Stream<Arguments> invalidTransitionsFromWaitingDataprovider()
    //    {
    //        return Stream.of(
    //            arguments(VirtualVehicleState.INIT),
    //            //            arguments(VirtualVehicleState.WAITING),
    //            arguments(VirtualVehicleState.MIGRATION_AWAITED_SND),
    //            arguments(VirtualVehicleState.MIGRATION_INTERRUPTED_SND),
    //            arguments(VirtualVehicleState.MIGRATION_COMPLETED_SND),
    //            arguments(VirtualVehicleState.FINISHED),
    //            arguments(VirtualVehicleState.INTERRUPTED),
    //            arguments(VirtualVehicleState.TASK_COMPLETION_AWAITED),
    //            arguments(VirtualVehicleState.DEFECTIVE));
    //    }

    //    @ParameterizedTest
    //    @MethodSource("invalidTransitionsFromWaitingDataprovider")
    //    void shouldDetectInvalidTransitionsFromWaiting(VirtualVehicleState state)
    //    {
    //        boolean result = VirtualVehicleState.WAITING.canTraverseTo(state);
    //        assertThat(result).isFalse();
    //    }

    /*
     * VirtualVehicleState.MIGRATION_AWAITED
     */
    static Stream<Arguments> validTransitionsFromMigrationAwaitedDataprovider()
    {
        return Stream.of(
            arguments(VirtualVehicleState.MIGRATING_SND));
    }

    @ParameterizedTest
    @MethodSource("validTransitionsFromMigrationAwaitedDataprovider")
    void shouldDetectValidTransitionsFromMigrationAwaited(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.MIGRATION_AWAITED_SND.canTraverseTo(state);
        assertThat(result).isTrue();
    }

    static Stream<Arguments> invalidTransitionsFromMigrationAwaitedDataprovider()
    {
        return Stream.of(
            arguments(VirtualVehicleState.INIT),
            arguments(VirtualVehicleState.RUNNING),
            //            arguments(VirtualVehicleState.WAITING),
            arguments(VirtualVehicleState.MIGRATION_AWAITED_SND),
            arguments(VirtualVehicleState.MIGRATION_INTERRUPTED_SND),
            arguments(VirtualVehicleState.MIGRATION_COMPLETED_SND),
            arguments(VirtualVehicleState.FINISHED),
            arguments(VirtualVehicleState.INTERRUPTED),
            arguments(VirtualVehicleState.TASK_COMPLETION_AWAITED),
            arguments(VirtualVehicleState.DEFECTIVE));
    }

    @ParameterizedTest
    @MethodSource("invalidTransitionsFromMigrationAwaitedDataprovider")
    void shouldDetectInvalidTransitionsFromMigrationAwaited(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.MIGRATION_AWAITED_SND.canTraverseTo(state);
        assertThat(result).isFalse();
    }

    /*
     * VirtualVehicleState.MIGRATING
     */
    static Stream<Arguments> validTransitionsFromMigratingDataprovider()
    {
        return Stream.of(
            //            arguments(VirtualVehicleState.WAITING),
            arguments(VirtualVehicleState.MIGRATION_INTERRUPTED_SND));
    }

    @ParameterizedTest
    @MethodSource("validTransitionsFromMigratingDataprovider")
    void shouldDetectValidTransitionsFromMigrating(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.MIGRATING_SND.canTraverseTo(state);
        assertThat(result).isTrue();
    }

    static Stream<Arguments> invalidTransitionsFromMigratingDataprovider()
    {
        return Stream.of(
            arguments(VirtualVehicleState.INIT),
            arguments(VirtualVehicleState.RUNNING),
            arguments(VirtualVehicleState.MIGRATION_AWAITED_SND),
            arguments(VirtualVehicleState.MIGRATING_SND),
            arguments(VirtualVehicleState.MIGRATION_COMPLETED_SND),
            arguments(VirtualVehicleState.FINISHED),
            arguments(VirtualVehicleState.INTERRUPTED),
            arguments(VirtualVehicleState.TASK_COMPLETION_AWAITED),
            arguments(VirtualVehicleState.DEFECTIVE));
    }

    @ParameterizedTest
    @MethodSource("invalidTransitionsFromMigratingDataprovider")
    void shouldDetectInvalidTransitionsFromMigrating(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.MIGRATING_SND.canTraverseTo(state);
        assertThat(result).isFalse();
    }

    /*
     * VirtualVehicleState.MIGRATION_INTERRUPTED
     */
    static Stream<Arguments> validTransitionsFromMigrationInterruptedDataprovider()
    {
        return Stream.of(
            arguments(VirtualVehicleState.MIGRATION_AWAITED_SND)
        //            arguments(VirtualVehicleState.WAITING),
        );
    }

    @ParameterizedTest
    @MethodSource("validTransitionsFromMigrationInterruptedDataprovider")
    void shouldDetectValidTransitionsFromMigrationInterrupted(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.MIGRATION_INTERRUPTED_SND.canTraverseTo(state);
        assertThat(result).isTrue();
    }

    static Stream<Arguments> invalidTransitionsFromMigrationInterruptedDataprovider()
    {
        return Stream.of(
            arguments(VirtualVehicleState.INIT),
            arguments(VirtualVehicleState.RUNNING),
            arguments(VirtualVehicleState.MIGRATING_SND),
            arguments(VirtualVehicleState.MIGRATION_INTERRUPTED_SND),
            arguments(VirtualVehicleState.MIGRATION_COMPLETED_SND),
            arguments(VirtualVehicleState.FINISHED),
            arguments(VirtualVehicleState.INTERRUPTED),
            arguments(VirtualVehicleState.TASK_COMPLETION_AWAITED),
            arguments(VirtualVehicleState.DEFECTIVE));
    }

    @ParameterizedTest
    @MethodSource("invalidTransitionsFromMigrationInterruptedDataprovider")
    void shouldDetectInvalidTransitionsFromMigrationInterrupted(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.MIGRATION_INTERRUPTED_SND.canTraverseTo(state);
        assertThat(result).isFalse();
    }

    static Stream<Arguments> allStatesDataprovider()
    {
        return Stream.of(
            arguments(VirtualVehicleState.INIT),
            arguments(VirtualVehicleState.RUNNING),
            //            arguments(VirtualVehicleState.WAITING),
            arguments(VirtualVehicleState.MIGRATION_AWAITED_SND),
            arguments(VirtualVehicleState.MIGRATING_SND),
            arguments(VirtualVehicleState.MIGRATION_INTERRUPTED_SND),
            arguments(VirtualVehicleState.MIGRATION_COMPLETED_SND),
            arguments(VirtualVehicleState.MIGRATING_RCV),
            arguments(VirtualVehicleState.MIGRATION_COMPLETED_RCV),
            arguments(VirtualVehicleState.FINISHED),
            arguments(VirtualVehicleState.INTERRUPTED),
            arguments(VirtualVehicleState.TASK_COMPLETION_AWAITED),
            arguments(VirtualVehicleState.DEFECTIVE));
    }

    /*
     * VirtualVehicleState.MIGRATION_COMPLETED
     */
    @ParameterizedTest
    @MethodSource("allStatesDataprovider")
    void shouldDetectInvalidTransitionsFromMigrationCompleted(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.MIGRATION_COMPLETED_SND.canTraverseTo(state);
        assertThat(result).isFalse();
    }

    /*
     * VirtualVehicleState.FINISHED
     */
    @ParameterizedTest
    @MethodSource("allStatesDataprovider")
    void shouldDetectInvalidTransitionsFromFinished(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.FINISHED.canTraverseTo(state);
        assertThat(result).isFalse();
    }

    /*
     * VirtualVehicleState.INTERRUPTED
     */
    @ParameterizedTest
    @MethodSource("allStatesDataprovider")
    void shouldDetectValidTransitionsFromInterrupted(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.INTERRUPTED.canTraverseTo(state);
        assertThat(result).isTrue();
    }

    /*
     * VirtualVehicleState.TASK_COMPLETION_AWAITED
     */
    @ParameterizedTest
    @MethodSource("allStatesDataprovider")
    void shouldDetectValidTransitionsFromTaskCompletionAwaited(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.TASK_COMPLETION_AWAITED.canTraverseTo(state);
        assertThat(result).isTrue();
    }

    /*
     * VirtualVehicleState.DEFECTIVE
     */
    @ParameterizedTest
    @MethodSource("allStatesDataprovider")
    void shouldDetectInvalidTransitionsFromDefective(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.DEFECTIVE.canTraverseTo(state);
        assertThat(result).isFalse();
    }

}
