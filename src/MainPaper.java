public class MainPaper {
    public float col,row;
    public int[][] matrix ;
    public boolean isRotated;
    public MainPaper(float _width,float _height){
        this.col = _width;
        this.row = _height;
        this.isRotated = false;
        this.matrix = new int[Math.round(_height)][Math.round(_width)];
    }
    public float area(){
        return this.col*this.row;
    }
    public MainPaper copy(){
        return new MainPaper(col,row);
    }
}
