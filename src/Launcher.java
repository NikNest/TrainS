import RailWay.*;
import RailWay.utils.Point;

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

            Point point1 = new Point(0, 0);
            Point point2 = new Point(1, 0);
            Point point3 = new Point(1, 1);
            Point point4 = new Point(2, 1);
            Point point5 = new Point(3, 1);
            Point point6 = new Point(3, 2);
            Point point7 = new Point(3, 3);
            Point point8 = new Point(2, 3);
            Point point9 = new Point(1, 3);
            Point point10 = new Point(1, 2);
            state.addTrack(point1, point2); //1
            state.addTrack(point2, point3); //2
            state.addSwitch(point3, point4, point10); //3
            state.addTrack(point4, point5); //4
            state.addTrack(point5, point6); //5
            state.addSwitch(point7, point6, point8); //6
            state.addTrack(point8, point9); //7
            state.addTrack(point9, point10); //8

            state.setSwitch(3, point4);
            state.setSwitch(6, point8);


            System.out.println(state.listTracks());

    }
}
