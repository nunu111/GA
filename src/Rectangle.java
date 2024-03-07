public class Rectangle {
    public float col,row;
    public int index;
    public boolean isRotated;
    public Rectangle(float _width,float _height,int _index,boolean isRotated){
        this.col = _width;
        this.row = _height;
        this.index = _index;
        this.isRotated = isRotated;
    }
    public float area(){
        return this.col*this.row;
    }
    public Rectangle Rotate(){

        return new Rectangle(row,col,index,true);
    }

}
