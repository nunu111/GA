import java.util.Arrays;
import java.util.List;

public class function {
    public Rectangle[] appendToFront(Rectangle[] array, Rectangle element) {
        int length = array.length;

        // Create a new array with increased size
        Rectangle[] newArray = Arrays.copyOf(array, length + 1);

        // Insert the new element at the beginning
        newArray[0] = element;

        return newArray;
    }
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
