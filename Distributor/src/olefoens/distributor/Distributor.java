package olefoens.distributor;

import lejos.nxt.Sound;

import java.io.File;
import java.util.Map;

public class Distributor
{
    private static final float MOTOR_CM_TO_ROTATION_ANGLE_FACTOR = 29f; // 29 rotations to move 1cm
    private final StallableDriveMotor bridgeMotor;
    private final StallableDriveMotor carMotor;
    private final CarTray tray;
    private Map<Integer, Point> trayCoordinateSystem_DropOffPoints;
    private Point trayToDistributorOffset;

    public Distributor(StallableDriveMotor carMotor, StallableDriveMotor bridgeMotor, CarTray tray,
                       Map<Integer, Point> trayCoordinateSystem_DropOffPoints, Point trayToDistributorOffset)
    {
        this.carMotor = carMotor;
        this.bridgeMotor = bridgeMotor;
        this.trayToDistributorOffset = trayToDistributorOffset;
        this.trayCoordinateSystem_DropOffPoints = trayCoordinateSystem_DropOffPoints;
        this.tray = tray;

        carMotor.setAcceleration(3000);
        carMotor.setSpeed((int)carMotor.getMaxSpeed());

        bridgeMotor.setAcceleration(3000);
        bridgeMotor.setSpeed((int) bridgeMotor.getMaxSpeed());

    }

    public void dropOffPieceAt(int position)
    {
        Point targetInTrayCoordSys = trayCoordinateSystem_DropOffPoints.get(position);
        Point targetInDistributorCoordSys =
                new Point(targetInTrayCoordSys.x + trayToDistributorOffset.x,
                          targetInTrayCoordSys.y + trayToDistributorOffset.y);

        int bridgeMotorRotationAngle = -1*cmToRotationAngle(targetInDistributorCoordSys.x);
        bridgeMotor.rotate(bridgeMotorRotationAngle, true);

        int carMotorRotationAngle = -1*cmToRotationAngle(targetInDistributorCoordSys.y);
        carMotor.rotate(carMotorRotationAngle, true);

        bridgeMotor.waitComplete();
        carMotor.waitComplete();

        Sound.playSample(new File("wupii.wav"));
        tray.releaseItem();

        carMotor.rotate(Math.max(-carMotorRotationAngle-cmToRotationAngle(2f),0), true);
        bridgeMotor.rotate(Math.max(-bridgeMotorRotationAngle-cmToRotationAngle(2f),0), true);

        bridgeMotor.waitComplete();
        carMotor.waitComplete();

        resetPosition();
    }

    public void resetPosition()
    {
        bridgeMotor.forwardUntilStalledAsync();
        carMotor.forwardUntilStalledAsync();

        carMotor.waitForDriveStall();
        bridgeMotor.waitForDriveStall();
    }

    public void setTrayCoordinateSystem_DropOffPoints(Map<Integer, Point> trayCoordinateSystem_DropOffPoints)
    {
        this.trayCoordinateSystem_DropOffPoints = trayCoordinateSystem_DropOffPoints;
    }

    public void setTrayToDistributorOffset(Point trayToDistributorOffset)
    {
        this.trayToDistributorOffset = trayToDistributorOffset;
    }

    private static int cmToRotationAngle(float cm)
    {
        return Math.round(MOTOR_CM_TO_ROTATION_ANGLE_FACTOR*cm);
    }
}
