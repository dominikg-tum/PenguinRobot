package pgdp.robot;

public class LeftHandSensor extends Sensor<Character> {

    @Override
    /* Gets the BLock to the left of out Pingu Robot. */
    public Character getData() {
        Position ogPos = owner.getPosition();
        double direction = owner.getDirection();
        //turn the original direction to the left by 90°
        if (direction == Math.toRadians(0)) { //if looking east -> turn north
            direction = Math.toRadians(270);
        } else {
            direction -= Math.toRadians(90);
        }
        //move the position to the spot to the left of the current position
        ogPos.moveBy(1.00, direction);
        //and return the block at that spot
        return owner.getWorld().getTerrain(ogPos);
    }
}
