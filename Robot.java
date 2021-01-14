package pgdp.robot;

import java.util.*;
import java.util.function.Function;

public class Robot {
    /// Attributes
    //final attributes
    private final String name;
    private final double size;

    //internal states
    private Position position = new Position();
    private double direction;
    private World world;

    //memory, sensor, command attributes
    private List<Memory<?>> memory = new ArrayList<>();
    private List<Sensor<?>> sensors = new ArrayList<>();
    private Queue<Command> todo = new ArrayDeque<>();
    private Function<Robot, List<Command>> program; //TODO implement?

    /// Methods
    public Robot(String name, double direction, double size) {
        this.name = name;
        this.direction = direction;
        this.size = Math.min(Math.max(0.5, size), 1);
    }

    public void setProgram(Function<Robot, List<Command>> newProgram) {
        this.program = newProgram;
    }

    public void work() {
        if (todo.isEmpty()) {
            this.sense();
            this.think();
        }
        this.act();
    }

    private void sense() {
        for (Sensor<?> sens : sensors) {
            fooSensor(sens);
        }
    }
    /* Hilfsmethode */
    private <T> void fooSensor(Sensor<T> sensor) {
        sensor.processor.accept(sensor.getData());
    }

    private void think() {
        List<Command> newCommands = program.apply(this);
        todo.addAll(newCommands);
    }

    private void act() {
        while (!todo.isEmpty()) {
            if (!todo.remove().execute(this)) {
                break;
            }
        }
    }

    public void attachSensor(Sensor<?> sensor) {
        sensors.add(sensor);
        sensor.setOwner(this);
    }

    public <T> Memory<T> createMemory(Memory<T> newMemory) {
        memory.add(newMemory);
        return newMemory;
    }

    public String memoryToString() {
        StringBuilder sB = new StringBuilder();
        for (Memory<?> mem : memory) {
            sB.append("[").append(mem.toString()).append("]"); //TODO: check if < > also needed!
        }
        return sB.toString();
    }

    /// Pre-programmed Commands
    public boolean go(double distance) {
        //step can be negative if the penguin walks backwards
        double sign = Math.signum(distance);
        distance = Math.abs(distance);
        //penguin walks, each step being 0.2m
        while (distance > 0) {
            position.moveBy(sign * Math.min(distance, 0.2), direction);
            world.resolveCollision(this, position);
            distance -= 0.2;
        }
        return true;
    }

    public boolean turnBy(double deltaDirection) {
        direction += deltaDirection;
        return true;
    }

    public boolean turnTo(double newDirection) {
        direction = newDirection;
        return true;
    }

    public boolean say(String text) {
        world.say(this, text);
        return true;
    }

    public boolean paintWorld(Position pos, char blockType) {
        world.setTerrain(pos, blockType);
        return true;
    }


    /// Getters and Setters
    public String getName() {
        return name;
    }

    public double getSize() {
        return size;
    }

    public Position getPosition() {
        return new Position(position);
    }

    public double getDirection() {
        return direction;
    }

    public World getWorld() {
        return world;
    }

    public void spawnInWorld(World world, char spawnMarker) {
        this.world = world;
        this.position = new Position(world.spawnRobotAt(this, spawnMarker));
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "\"%s\" position=%s direction=%.2f°", name, position, Math.toDegrees(direction));
    }
}
