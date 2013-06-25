package olefoens.distributor;

import lejos.nxt.*;
import lejos.util.Delay;
import olefoens.communication.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main
{
    private static final Point axleTrayToDistributorOffset = new Point(-4.1f, -2);
    private static final Point beamTrayToDistributorOffset = new Point(0, -5);
    private static Map<Integer,Point> axleTrayCoordinateSystem_dropOffPoints;
    private static Map<Integer,Point> beamTrayCoordinateSystem_dropOffPoints;

    public static void main(String[] args) throws InterruptedException
    {
        Button.ENTER.addButtonListener(new ButtonListener()
        {
            @Override
            public void buttonPressed(Button b)
            {
                System.exit(0);
            }

            @Override
            public void buttonReleased(Button b)
            {

            }
        });

        setupDropOffPositions();

        Sound.setVolume(100);

        StallableDriveMotor carMotor = new StallableDriveMotor(MotorPort.C);
        Distributor distributor = new Distributor(carMotor, new StallableDriveMotor(MotorPort.B),
                new CarTray(Motor.A), beamTrayCoordinateSystem_dropOffPoints, axleTrayToDistributorOffset);

        distributor.resetPosition();

        try
        {
            Communicator communicator = Communicator.acceptConnectionFromMaster(ClientRole.Distributor);

            while(true)
            {
                Packet packet = communicator.readPacket();
                switch(packet.type)
                {
                    case Reset:
                        distributor.resetPosition();
                        communicator.sendReady();
                        break;

                    case LegoDescription:
                        LCD.clear();
                        LegoItemDescription d = (LegoItemDescription) packet.content;
                        Sound.playSample(new File("yay.wav"));
                        if (d.type == LegoBrickType.Axle)
                        {
                            distributor.setTrayCoordinateSystem_DropOffPoints(axleTrayCoordinateSystem_dropOffPoints);
                            distributor.setTrayToDistributorOffset(axleTrayToDistributorOffset);
                        }
                        else
                        {
                            distributor.setTrayCoordinateSystem_DropOffPoints(beamTrayCoordinateSystem_dropOffPoints);
                            distributor.setTrayToDistributorOffset(beamTrayToDistributorOffset);
                        }

                        LCD.drawString(d.type.name(), 0, 0);
                        LCD.drawInt(d.length, 3, 0, 1);
                        distributor.dropOffPieceAt(d.length);
                        communicator.sendReady();
                        break;

                    default:
                        throw new RuntimeException();
                }
            }
        } catch(IOException e)
        {
            LCD.clear();
            LCD.drawString("IOException!", 0, 0);
            Delay.msDelay(10000);
        }
    }

    private static void setupDropOffPositions() {
        axleTrayCoordinateSystem_dropOffPoints = new HashMap<Integer, Point>(11);
        axleTrayCoordinateSystem_dropOffPoints.put(2, new Point(4.1f, 2f));
        axleTrayCoordinateSystem_dropOffPoints.put(3, new Point(12.3f, 2f));
        axleTrayCoordinateSystem_dropOffPoints.put(4, new Point(20.5f, 2f));
        axleTrayCoordinateSystem_dropOffPoints.put(5, new Point(4.1f, 10.4f));
        axleTrayCoordinateSystem_dropOffPoints.put(6, new Point(12.3f, 10.0f));
        axleTrayCoordinateSystem_dropOffPoints.put(7, new Point(20.5f, 9.6f));
        axleTrayCoordinateSystem_dropOffPoints.put(8, new Point(4.1f, 19.8f));
        axleTrayCoordinateSystem_dropOffPoints.put(9, new Point(12.3f, 19.4f));
        axleTrayCoordinateSystem_dropOffPoints.put(10, new Point(20.5f, 19.0f));
        axleTrayCoordinateSystem_dropOffPoints.put(12, new Point(4.1f, 28.4f));
        // error
        axleTrayCoordinateSystem_dropOffPoints.put(11, new Point(45.1f, 18.4f));

        beamTrayCoordinateSystem_dropOffPoints = new HashMap<Integer, Point>(8);
        beamTrayCoordinateSystem_dropOffPoints.put(2, new Point(5.2f, 5f));
        beamTrayCoordinateSystem_dropOffPoints.put(3, new Point(15.6f, 5f));
        beamTrayCoordinateSystem_dropOffPoints.put(5, new Point(26f, 5f));
        beamTrayCoordinateSystem_dropOffPoints.put(7, new Point(5.2f, 14.3f));
        beamTrayCoordinateSystem_dropOffPoints.put(9, new Point(15.6f, 14.3f));
        beamTrayCoordinateSystem_dropOffPoints.put(11, new Point(26f, 14.3f));
        beamTrayCoordinateSystem_dropOffPoints.put(13, new Point(5.2f, 28.6f));
        beamTrayCoordinateSystem_dropOffPoints.put(15, new Point(15.6f, 28.6f));
    }
}
