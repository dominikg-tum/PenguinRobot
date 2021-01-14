package pgdp.robot;

import java.util.ArrayList;
import java.util.List;

public class User {

    public static Robot makeLineFollower() {
        Robot pinguRobo = new Robot("roboBot", 0, 0.7);
        //create memory
        Memory<Character> terrain = pinguRobo.createMemory(new Memory<>("terrain", ' '));
        //create and attach sensors & processor
        pinguRobo.attachSensor(new TerrainSensor().setProcessor(terrain::set));
        //program the robot
        pinguRobo.setProgram(robot -> {
            List<Command> commands = new ArrayList<>();
            switch (terrain.get()) {
                case '^' -> commands.add(r -> r.turnTo(Math.toRadians(270))); //north
                case '>' -> commands.add(r -> r.turnTo(Math.toRadians(0))); //east
                case 'v' -> commands.add(r -> r.turnTo(Math.toRadians(90))); //south
                case '<' -> commands.add(r -> r.turnTo(Math.toRadians(180))); //west
                default -> {
                    //just keep going straight forward
                }
            }
            //do one step
            commands.add(r -> r.go(1));
            return commands;
        });
        return pinguRobo;
    }

    /* we always spawn next to a wall and we can go "outside" of the maze -> zulip (Joong-Won Seo)! */
    public static Robot makeMazeRunner() {
        Robot mazePingu = new Robot("mazeBoi", 0, 0.7);
        //create memory
        Memory<Character> terrain = mazePingu.createMemory(new Memory<>("terrain", ' '));
        Memory<Character> terrainLeft = mazePingu.createMemory(new Memory<>("left", ' '));
        Memory<Character> terrainInFront = mazePingu.createMemory(new Memory<>("front", ' '));
        //create and attach sensors & processor
        mazePingu.attachSensor(new LeftHandSensor().setProcessor(terrainLeft::set));
        mazePingu.attachSensor(new FrontSensor().setProcessor(terrainInFront::set));
        mazePingu.attachSensor(new TerrainSensor().setProcessor(terrain::set));
        //program the robot -> Strategy is "left hand rule"
        mazePingu.setProgram(robot -> {
            List<Command> commands = new ArrayList<>();

            //if we find de $, Pingu is happy :)
            if (terrain.get() == '$')
                commands.add(r -> r.say("yaay :)"));

            //direction for turning right
            double Rdirection = robot.getDirection();
            if (Rdirection == Math.toRadians(270)) { //if looking north -> turn east
                Rdirection = Math.toRadians(0);
            } else {
                Rdirection += Math.toRadians(90);
            }
            //direction for turning left
            double Ldirection = robot.getDirection();
            if (Ldirection == Math.toRadians(0)) { //if looking east -> turn north
                Ldirection = Math.toRadians(270);
            } else {
                Ldirection -= Math.toRadians(90);
            }

            //if theres a wall to the left of us
            if (terrainLeft.get() == '#') {
                //check if the block in front of us is a wall -> turn right
                if (terrainInFront.get() == '#') {
                    double finalDirection = Rdirection;
                    commands.add(r -> r.turnTo(finalDirection)); //turn right
                } else {
                    //do one step
                    commands.add(r -> r.go(1));
                }
            } else {
                //turn left
                double finalDirection = Ldirection;
                commands.add(r -> r.turnTo(finalDirection)); //turn left
                //and go there
                commands.add(r -> r.go(1));
            }

            return commands;
        });
        return mazePingu;
    }

    //   public static void main(String[] args) {
//        Robot mazePingu = makeMazeRunner();
//        World world = new World("""
//                ##############
//                #  0   #   ##  $
//                #  #  ##   #   #
//                ####   # ### ###
//                #      #       #
//                #  #      ##   #
//                ################""");
//
//        mazePingu.spawnInWorld(world, '0');
//        world.run();

//        Robot followPinguBot = makeLineFollower();
//        World world = new World("""
//                ################
//                #v<#           #
//                #v^#   #v<< #  #
//                #v^<<<<<<0^ #  #
//                #v   # >>>^#   #
//                #v###  ^# ^<<<<##
//                #>>>>>>      ^ #
//                ################""");
//
//        followPinguBot.spawnInWorld(world, '0');
//        world.run();

//        Robot panicPenguin = new Robot("robi", 0, 0.5);
//
//        //create memory
//        Memory<Character> terrain = panicPenguin.createMemory(new Memory<>("terrain", ' '));
//
//        //create and attach sensors
//        panicPenguin.attachSensor(new TerrainSensor().setProcessor(terrain::set));
//
//        //program the robot
//        panicPenguin.setProgram(robot -> {
//            List<Command> commands = new ArrayList<>();
//
//            commands.add(r -> r.say(terrain.get().toString()));
//            commands.add(r -> r.turnBy(Math.toRadians(5)));
//            commands.add(r -> r.go(0.1));
//
//            return commands;
//        });

//        //run the simulation
//        String map = "################\n" +
//                "#  #0     1    #\n" +
//                "#  #   ##   #  #\n" +
//                "#  ###   #T #  #\n" +
//                "#   3# a ### W #\n" +
//                "# ###   # 2   ##\n" +
//                "#      #       #\n" +
//                "###########$$$##";
//        World world = new World(map);
//        new Robot("Pengu", Math.toRadians(90), 1).spawnInWorld(world, '3');
//        world.run();
    //   }
}
