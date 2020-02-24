import RailWay.*;
import RailWay.utils.Point;
import RailWay.utils.Track;

public class Launcher {
    public static void main(String[] args) {
            Depot depot = new Depot();

            depot.createEngine("electrical", "NMAclass", "001name", 3, true, true);
            depot.createEngine("steam", "NMBclass", "002name", 5, true, true);
            depot.createEngine("diesel", "NMCclass", "003name", 7, true, true);
            depot.createCoach("passenger", 13, true, true);
            depot.createCoach("freight", 23, true, true);
            depot.createCoach("special", 33, true, true);
            depot.createTrainSet("TSAclass", "101name", 53, true, false);
            depot.createTrainSet("TSAclass", "102name", 73, true, true);
            depot.createTrainSet("TSBclass", "103name", 93, false, true);
//            RWState state = new RWState();
//
//            Point point1 = new Point(0, 0);
//            Point point2 = new Point(3, 0);
//            Point point3 = new Point(3, 3);
//            Point point4 = new Point(6, 3);
//            Point point5 = new Point(9, 3);
//            Point point6 = new Point(9, 6);
//            Point point7 = new Point(9, 9);
//            Point point8 = new Point(6, 9);
//            Point point9 = new Point(3, 9);
//            Point point10 = new Point(3, 6);
//            //
//            Point point11 = new Point(12, 9);
//            Point point12 = new Point(15, 9);
//            Point point13 = new Point(15, 12);
//            Point point14 = new Point(15, 15);
//            Point point15 = new Point(12, 15);
//            Point point16 = new Point(9, 15);
//            Point point17 = new Point(9, 12);
//
//            state.addTrack(point1, point2); //1
//            state.addTrack(point3, point2); //2
//
//            state.addSwitch(point3, point4, point10); //3
//            state.addTrack(point5, point4); //4
//            state.addTrack(point5, point6); //5
//            state.addSwitch(point7, point6, point8); //6
//            state.addTrack(point8, point9); //7
//            state.addTrack(point9, point10); //8
//
//            state.addSwitch(point7, point11, point17); //9
//            state.addTrack(point11, point12); //10
//            state.addTrack(point13, point12); //11
//            state.addTrack(point13, point14); //12
//            state.addTrack(point14, point15); //13
//            state.addTrack(point15, point16); //14
//            state.addTrack(point16, point17); //15
//
//            state.setSwitch(3, point4);
//            state.setSwitch(6, point6);
//            state.setSwitch(9, point11);
//            System.out.println(state.listTracks());
//
//            depot.createTrainSet("TSAclass", "101name", 1, true, true);
//            depot.createTrainSet("TSAclass", "102name", 1, true, true);
//            depot.addTrain(1, "TSAclass-101name");
//            depot.addTrain(1, "TSAclass-102name");
//            depot.createTrainSet("TSAclass", "103name", 1, true, true);
//            depot.createTrainSet("TSAclass", "104name", 1, true, true);
//            depot.addTrain(2, "TSAclass-103name");
//            depot.addTrain(2, "TSAclass-104name");
//            depot.createTrainSet("TSAclass", "105name", 1, true, true);
//            depot.createTrainSet("TSAclass", "106name", 1, true, true);
//            depot.addTrain(3, "TSAclass-105name");
//            depot.addTrain(3, "TSAclass-106name");
//            depot.createTrainSet("TSAclass", "107name", 1, true, true);
//            depot.createTrainSet("TSAclass", "108name", 1, true, true);
//            depot.addTrain(4, "TSAclass-107name");
//            depot.addTrain(4, "TSAclass-108name");
//            depot.createTrainSet("TSAclass", "109name", 1, true, true);
//            depot.createTrainSet("TSAclass", "1010name", 1, true, true);
//            depot.addTrain(5, "TSAclass-109name");
//            depot.addTrain(5, "TSAclass-1010name");
//            depot.createTrainSet("TSAclass", "1011name", 1, true, true);
//            depot.createTrainSet("TSAclass", "1012name", 1, true, true);
//            depot.addTrain(6, "TSAclass-1011name");
//            depot.addTrain(6, "TSAclass-1012name");
//            depot.createTrainSet("TSAclass", "1013name", 1, true, true);
//            depot.createTrainSet("TSAclass", "1014name", 1, true, true);
//            depot.addTrain(7, "TSAclass-1013name");
//            depot.addTrain(7, "TSAclass-1014name");
//            depot.createTrainSet("TSAclass", "1015name", 1, true, true);
//            depot.createTrainSet("TSAclass", "1016name", 1, true, true);
//            depot.addTrain(8, "TSAclass-1015name");
//            depot.addTrain(8, "TSAclass-1016name");
//
//            //TODO COLLECTIONS.SORT испоьзовать лямбда выражения
//            state.putTrain(depot.getTrain(1), new Point(4, 3), new Point(1, 0));
////            state.putTrain(depot.getTrain(2), new Point(8, 3), new Point(1, 0));
////            state.putTrain(depot.getTrain(5), new Point(9, 4), new Point(0, -1));
////            state.putTrain(depot.getTrain(4), new Point(11, 9), new Point(1, 0));
////            state.putTrain(depot.getTrain(3), new Point(9, 8), new Point(0, 1));
////            state.putTrain(depot.getTrain(6), new Point(12, 9), new Point(-1, 0));
////            state.putTrain(depot.getTrain(7), new Point(0, 0), new Point(-1, 0));
////            state.putTrain(depot.getTrain(8), new Point(3, 2), new Point(0, 1));
////
////            state.step((short)1);
////            state.step((short)1);
////            state.deleteTrack(15);
////            state.deleteTrack(14);
////            state.deleteTrack(13);
//            state.setSwitch(3, point4);
//            state.step((short)1);

    }
}
