import RailWay.*;
import RailWay.utils.Point;
import RailWay.utils.Track;

import java.util.ArrayList;

//t 1 (1,1) -> (5,1) 5
//s 2 (5,1) -> (8,1),(5,3)
public class Launcher {
    public static void main(String[] args) {
//            depot.createEngine("electrical", "NMAclass", "001name", 3, true, true);
//            depot.createEngine("steam", "NMBclass", "002name", 5, true, true);
//            depot.createEngine("diesel", "NMCclass", "003name", 7, true, true);
//            depot.createCoach("passenger", 13, true, true);
//            depot.createCoach("freight", 23, true, true);
//            depot.createCoach("special", 33, true, true);
//            depot.createTrainSet("TSAclass", "101name", 53, true, false);
//            depot.createTrainSet("TSAclass", "102name", 73, true, true);
//            depot.createTrainSet("TSBclass", "103name", 93, false, true);
            RWState state = new RWState();
            Depot depot = new Depot();

            Point point1 = new Point(0, 0);
            Point point2 = new Point(3, 0);
            Point point3 = new Point(3, 3);
            Point point4 = new Point(6, 3);
            Point point5 = new Point(9, 3);
            Point point6 = new Point(9, 6);
            Point point7 = new Point(9, 9);
            Point point8 = new Point(6, 9);
            Point point9 = new Point(3, 9);
            Point point10 = new Point(3, 6);
            //
            Point point11 = new Point(12, 9);
            Point point12 = new Point(15, 9);
            Point point13 = new Point(15, 12);
            Point point14 = new Point(15, 15);
            Point point15 = new Point(12, 15);
            Point point16 = new Point(9, 15);
            Point point17 = new Point(9, 12);

            state.addTrack(point1, point2); //1
            state.addTrack(point3, point2); //2

            state.addSwitch(point3, point4, point10); //3
            state.addTrack(point5, point4); //4
            state.addTrack(point5, point6); //5
            state.addSwitch(point7, point6, point8); //6
            state.addTrack(point8, point9); //7
            state.addTrack(point9, point10); //8

            state.addSwitch(point7, point11, point17); //9
            state.addTrack(point11, point12); //10
            state.addTrack(point13, point12); //11
            state.addTrack(point13, point14); //12
            state.addTrack(point14, point15); //13
            state.addTrack(point15, point16); //14
            state.addTrack(point16, point17); //15

            state.setSwitch(3, point4);
            state.setSwitch(6, point6);
            state.setSwitch(9, point11);
            System.out.println(state.listTracks());
//            Point point1 = new Point(0, 0);
//            Point point2 = new Point(3, 0);
//            Point point3 = new Point(6, 0);
//            Point point4 = new Point(9, 0);
//
//

            depot.createTrainSet("TSAclass", "101name", 1, true, true);
            depot.createTrainSet("TSAclass", "102name", 1, true, true);
            depot.addTrain(1, "TSAclass-101name");
            depot.addTrain(1, "TSAclass-102name");
            depot.createTrainSet("TSAclass", "103name", 2, true, true);
            depot.createTrainSet("TSAclass", "104name", 1, true, true);
            depot.addTrain(2, "TSAclass-103name");
            depot.addTrain(2, "TSAclass-104name");
//            System.out.println(depot.listTrains());
//            state.putTrain(depot.getTrain(1), new Point(12, 9), new Point(-1, 0));
//            state.step((short)(-1));
            state.putTrain(depot.getTrain(1), new Point(5, 3), new Point(1, 0));
            //14, 15
            state.step((short)2);

    }
}
