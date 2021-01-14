package pgdp.robot;

public class FrontSensor extends Sensor<Character> {

    @Override
    /* Gets the Block in Front of our Pingu Robot. */
    public Character getData() {
        Position pos = owner.getPosition();
        double dir = owner.getDirection();
        //move the position to the desired spot -> one ahead in the same direction
        pos.moveBy(1.00, dir);
        //and return the block at that spot
        return owner.getWorld().getTerrain(pos);
    }
}
