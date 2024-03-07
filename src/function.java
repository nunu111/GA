import java.util.Arrays;
import java.util.List;

public class function {

    public int genesGenerator (MainPaper main_rectangle, List<Rectangle> rectangles){


        float smallest = rectangles.getFirst().row* rectangles.getFirst().col;
        for (int i = 1; i < rectangles.size(); i++) {
            if (rectangles.get(i).col* rectangles.get(i).row < smallest) {
                smallest = rectangles.get(i).col* rectangles.get(i).row;
            }
        }

        return (int)(main_rectangle.col* main_rectangle.row/smallest);
    }
}
