package olefoens.feeder;

import lejos.nxt.NXTRegulatedMotor;
import lejos.util.Delay;

public class Elevator
{
	private final NXTRegulatedMotor elevator;
	private static final int rotationsToMoveElevatorUp = -330;

	public Elevator(NXTRegulatedMotor elevator)
	{
		this.elevator = elevator;
		goToStartPosition();
	}

	private void goToStartPosition()
	{
		elevator.setSpeed(60);
		elevator.setStallThreshold(3, 5);
		elevator.forward();
		elevator.waitComplete();
		elevator.flt();
		Delay.msDelay(100);
		elevator.stop();
		Delay.msDelay(50);
		elevator.flt();
		Delay.msDelay(200);
		elevator.rotate(-20);
		elevator.setStallThreshold(25, 50);
		elevator.setSpeed(elevator.getMaxSpeed());
		elevator.resetTachoCount();
	}

	public void elevatorUp()
	{
		elevator.rotate(rotationsToMoveElevatorUp);
	}

	public void elevatorDown()
	{
		elevator.rotate(-rotationsToMoveElevatorUp);
	}
}
