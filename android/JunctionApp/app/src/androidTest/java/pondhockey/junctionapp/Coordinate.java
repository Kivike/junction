public class Coordinate{

    float north;
    float east;

    public Coordinate(float north, float east){
        this.north = north;
        this.east = east;
    }

    public String toString(){
        return new DecimalFormat("0.0000; -0.0000").format(this.north) + " " + new DecimalFormat("0.0000; -0.0000").format(this.east);
    }
}