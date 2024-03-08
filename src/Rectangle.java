public class Rectangle {
    public float col,row;
    public boolean isRotated;
    public Rectangle(float _width,float _height,boolean isRotated){
        this.col = _width;
        this.row = _height;
        this.isRotated = isRotated;
    }

    public Rectangle Rotate(){

        return new Rectangle(row,col,true);
    }

}
