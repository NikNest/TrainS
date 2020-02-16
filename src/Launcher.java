import RailWay.*;
import RailWay.utils.Point;

//t 1 (1,1) -> (5,1) 5
//s 2 (5,1) -> (8,1),(5,3)
public class Launcher {
    public static void main(String[] args) {
            Depot depot = new Depot();
            System.out.println(depot.listEngines());
            System.out.println(depot.listCoaches());
            System.out.println(depot.listTrainSets());
            depot.createEngine("electrical", "NMAclass", "001name", 3, false, true);
            depot.createEngine("steam", "NMBclass", "002name", 5, true, true);
            depot.createEngine("diesel", "NMCclass", "003name", 7, false, false);
            System.out.println(depot.listEngines());
            depot.createCoach("passenger", 13, false, true);
            depot.createCoach("freight", 23, true, true);
            depot.createCoach("special", 33, false, false);
            System.out.println(depot.listCoaches());
            depot.createTrainSet("TSAclass", "101name", 53, true, false);
            depot.createTrainSet("TSBclass", "102name", 73, true, true);
            depot.createTrainSet("TSCclass", "103name", 93, false, true);
            System.out.println(depot.listTrainSets());
            /////////////////
            System.out.println();
            depot.deleteRollingStock("NMAclass-001name");
            depot.deleteRollingStock("3");
            depot.deleteRollingStock("TSCclass-103name");
            System.out.println(depot.listEngines());
            System.out.println(depot.listCoaches());
            System.out.println(depot.listTrainSets());
            ///////////////////
            System.out.println();
            depot.addTrainWrap(1, "NMCclass-003name");
            depot.addTrainWrap(2, "NMBclass-002name");
            depot.addTrainWrap(2, "TSBclass-102name");
            depot.addTrainWrap(4, "TSBclass-102name");
            depot.addTrainWrap(1, "TSBclass-102name");
            depot.addTrainWrap(1, "W4");
            depot.addTrainWrap(1, "4-4");
            System.out.println(depot.listTrains());
            depot.addTrainWrap(3, "W1");
            depot.addTrainWrap(3, "W2");
            depot.addTrainWrap(3, "W2");
            depot.addTrainWrap(3, "TSBclass-102name");
            depot.addTrainWrap(3, "TSAclass-101name");
            System.out.println(depot.listTrains());
            ///////////////////
    }
}
