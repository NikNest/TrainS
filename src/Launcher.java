import RailWay.*;

//t 1 (1,1) -> (5,1) 5
//s 2 (5,1) -> (8,1),(5,3)
public class Launcher {
    public static void main(String[] args) {
            Depot depot = new Depot();
            System.out.println(depot.listEngines());
            System.out.println(depot.listCoaches());
            System.out.println(depot.listTrainSets());
            depot.createEngine("electrical", "NMAclass", "001name", 3, true, true);
            depot.createEngine("steam", "NMBclass", "002name", 5, true, true);
            depot.createEngine("diesel", "NMCclass", "003name", 7, true, true);
            depot.createCoach("passenger", 13, true, true);
            depot.createCoach("freight", 23, true, true);
            depot.createCoach("special", 33, true, true);
            depot.createTrainSet("TSAclass", "101name", 53, true, false);
            depot.createTrainSet("TSAclass", "102name", 73, true, true);
            depot.createTrainSet("TSBclass", "103name", 93, false, true);
            System.out.println();
//            depot.deleteRollingStock("NMAclass-001name");
//            depot.deleteRollingStock("3");
//            depot.deleteRollingStock("TSBclass-103name");
            System.out.println(depot.listEngines());
            System.out.println(depot.listCoaches());
            System.out.println(depot.listTrainSets());
            ///////////////////
            System.out.println();
            depot.addTrain(1, "NMAclass-001name");
            depot.addTrain(2, "TSAclass-102name");
            depot.addTrain(2, "TSAclass-101name");
            depot.addTrain(1, "W1");
            depot.addTrain(3, "NMBclass-002name");
            depot.addTrain(3, "W2");
            depot.addTrain(4, "NMCclass-003name");
            depot.addTrain(4, "W3");
            System.out.println(depot.listTrains());
            System.out.println(depot.listEngines());
            System.out.println(depot.listCoaches());
            System.out.println(depot.listTrainSets());
            /////////////////////
            depot.deleteTrain(3);
            System.out.println(depot.listTrains());
            depot.addTrain(3, "NMBclass-002name");
            depot.addTrain(3, "W2");
            depot.createCoach("passenger", 13, true, true);
            depot.createCoach("passenger", 13, true, true);
            depot.createCoach("passenger", 13, true, true);
            depot.deleteRollingStock("W4");
            System.out.println(depot.listCoaches());
            depot.createCoach("passenger", 13, true, true);
    }
}
