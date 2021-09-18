package cpcc.vvrte.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;

import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import cpcc.core.entities.RealVehicle;

class VirtualVehicleTest
{
    private VirtualVehicle sut;

    @BeforeEach
    void setUp()
    {
        sut = new VirtualVehicle();
    }

    static Stream<Arguments> dataProvider()
    {
        return Stream.of(
            arguments(
                100660,
                "lLZs7A",
                "",
                996405,
                "",
                VirtualVehicleState.TASK_COMPLETION_AWAITED,
                VirtualVehicleState.MIGRATING_RCV,
                mock(RealVehicle.class),
                mock(RealVehicle.class),
                450149,
                new Date(1305171714389L),
                new byte[]{-117, 1, -8, 97, 104, 37, -108, -77,},
                new Date(1204845255813L),
                new Date(1258217243487L),
                new Date(1291098933084L),
                "AtaOg2uCI",
                mock(Task.class),
                false),
            arguments(
                603078,
                "6I",
                "BEzvS",
                456316,
                "wVZFS0Y6L",
                VirtualVehicleState.MIGRATION_INTERRUPTED_RCV,
                VirtualVehicleState.MIGRATING_RCV,
                mock(RealVehicle.class),
                mock(RealVehicle.class),
                29353,
                new Date(1288988903687L),
                new byte[]{74, -88, -40, 21, -79, 106, -18,},
                new Date(1365327950136L),
                new Date(1272923951964L),
                new Date(1452214008267L),
                "CBZ6Nvsc",
                mock(Task.class),
                true),
            arguments(
                299694,
                "rFTuwnGZe",
                "VSYuNI",
                326234,
                "",
                VirtualVehicleState.MIGRATION_AWAITED_SND,
                VirtualVehicleState.TASK_COMPLETION_AWAITED,
                mock(RealVehicle.class),
                mock(RealVehicle.class),
                93484,
                new Date(1432730208219L),
                new byte[]{101, -23,},
                new Date(1432581601335L),
                new Date(1372218945368L),
                new Date(1431115854424L),
                "0RNPsukEM",
                mock(Task.class),
                true),
            arguments(
                752125,
                "BuW",
                "ZL",
                413748,
                "2cRksJw",
                VirtualVehicleState.MIGRATING_RCV,
                VirtualVehicleState.TASK_COMPLETION_AWAITED,
                mock(RealVehicle.class),
                mock(RealVehicle.class),
                898991,
                new Date(1323254177780L),
                new byte[]{-80,},
                new Date(1354142254788L),
                new Date(1447826788701L),
                new Date(1474831422133L),
                "G9VyLTYx",
                mock(Task.class),
                true),
            arguments(
                318107,
                "",
                "zXu",
                337977,
                "WjTzXDnn",
                VirtualVehicleState.FINISHED,
                VirtualVehicleState.INIT,
                mock(RealVehicle.class),
                mock(RealVehicle.class),
                866772,
                new Date(1328921631167L),
                new byte[]{119, 89, 17, -50, 91, 56, -76,},
                new Date(1414606049490L),
                new Date(1414853421762L),
                new Date(1474372843867L),
                "Si7CfytJ",
                mock(Task.class),
                true),
            arguments(
                13235,
                "5h",
                "X3T",
                146695,
                "l0XVJ9",
                VirtualVehicleState.INTERRUPTED,
                VirtualVehicleState.MIGRATION_AWAITED_SND,
                mock(RealVehicle.class),
                mock(RealVehicle.class),
                193834,
                new Date(1363292185729L),
                new byte[]{-102, 22, -59, 39, 123,},
                new Date(1378722383358L),
                new Date(1470803736383L),
                new Date(1336409666671L),
                "D",
                mock(Task.class),
                true),
            arguments(
                902587,
                "fbehB",
                "Zyj7fyJIC",
                176830,
                "ojchL0To",
                VirtualVehicleState.MIGRATION_INTERRUPTED_SND,
                VirtualVehicleState.MIGRATING_SND,
                mock(RealVehicle.class),
                mock(RealVehicle.class),
                80267,
                new Date(1214252635004L),
                new byte[]{-46,},
                new Date(1242375981484L),
                new Date(1383515623282L),
                new Date(1377109698144L),
                "",
                mock(Task.class),
                true),
            arguments(
                846657,
                "UAg2G",
                "feX67L",
                202425,
                "0NmN3",
                VirtualVehicleState.FINISHED,
                VirtualVehicleState.TASK_COMPLETION_AWAITED,
                mock(RealVehicle.class),
                mock(RealVehicle.class),
                49785,
                new Date(1282485790335L),
                new byte[]{90, 81, -101, -100, 40, -46, 17, 30,},
                new Date(1471165977312L),
                new Date(1216368376570L),
                new Date(1469462427851L),
                "9fDdKiaJb",
                mock(Task.class),
                false),
            arguments(
                392718,
                "",
                "oCKiYW",
                441,
                "6pp8B",
                VirtualVehicleState.FINISHED,
                VirtualVehicleState.INIT,
                mock(RealVehicle.class),
                mock(RealVehicle.class),
                4220,
                new Date(1253345946284L),
                new byte[]{},
                new Date(1252930646493L),
                new Date(1273799105351L),
                new Date(1304262481292L),
                "",
                mock(Task.class),
                false),
            arguments(
                152599,
                "iIoghfu",
                "YC",
                303034,
                "WrAMg",
                VirtualVehicleState.MIGRATION_COMPLETED_RCV,
                VirtualVehicleState.TASK_COMPLETION_AWAITED,
                mock(RealVehicle.class),
                mock(RealVehicle.class),
                122680,
                new Date(1252552058334L),
                new byte[]{-85, 123, -91, -113,},
                new Date(1323755708018L),
                new Date(1206330650723L),
                new Date(1363976786170L),
                "keLLf",
                mock(Task.class),
                false));
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    void shouldStoreAndRetrieveValues(Integer id, String uuid, String name, Integer apiVersion, String code,
        cpcc.vvrte.entities.VirtualVehicleState state, cpcc.vvrte.entities.VirtualVehicleState preMigrationState,
        cpcc.core.entities.RealVehicle migrationDestination, cpcc.core.entities.RealVehicle migrationSource,
        Integer chunkNumber, java.util.Date migrationStartTime, byte[] continuation, java.util.Date startTime,
        java.util.Date endTime, java.util.Date updateTime, String stateInfo, cpcc.vvrte.entities.Task task,
        boolean selected)
    {
        sut.setId(id);
        sut.setUuid(uuid);
        sut.setName(name);
        sut.setApiVersion(apiVersion);
        sut.setCode(code);
        sut.setState(state);
        sut.setPreMigrationState(preMigrationState);
        sut.setMigrationDestination(migrationDestination);
        sut.setMigrationSource(migrationSource);
        sut.setChunkNumber(chunkNumber);
        sut.setMigrationStartTime(migrationStartTime);
        sut.setContinuation(continuation);
        sut.setStartTime(startTime);
        sut.setEndTime(endTime);
        sut.setUpdateTime(updateTime);
        sut.setStateInfo(stateInfo);
        sut.setTask(task);
        sut.setSelected(selected);

        assertThat(sut.getId()).isEqualTo(id);
        assertThat(sut.getUuid()).isEqualTo(uuid);
        assertThat(sut.getName()).isEqualTo(name);
        assertThat(sut.getApiVersion()).isEqualTo(apiVersion);
        assertThat(sut.getCode()).isEqualTo(code);
        assertThat(sut.getState()).isEqualTo(state);
        assertThat(sut.getPreMigrationState()).isEqualTo(preMigrationState);
        assertThat(sut.getMigrationDestination()).isEqualTo(migrationDestination);
        assertThat(sut.getMigrationSource()).isEqualTo(migrationSource);
        assertThat(sut.getChunkNumber()).isEqualTo(chunkNumber);
        assertThat(sut.getMigrationStartTime()).isEqualTo(migrationStartTime);
        assertThat(sut.getContinuation()).isEqualTo(continuation);
        assertThat(sut.getStartTime()).isEqualTo(startTime);
        assertThat(sut.getEndTime()).isEqualTo(endTime);
        assertThat(sut.getUpdateTime()).isEqualTo(updateTime);
        assertThat(sut.getStateInfo()).isEqualTo(stateInfo);
        assertThat(sut.getTask()).isEqualTo(task);
        assertThat(sut.isSelected()).isEqualTo(selected);
    }
}