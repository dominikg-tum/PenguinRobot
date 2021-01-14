package pgdp.robot;

public class TerrainSensor extends Sensor<Character> {

    @Override
    public Character getData() {
        Position roboterPos = owner.getPosition();
        return owner.getWorld().getTerrain(roboterPos);
    }

}
