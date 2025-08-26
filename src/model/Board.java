package model;

public class Board {

    public record iPair(int i, int j){
    }
    public static class GraphCoord {
        private iPair coord;

        public iPair getCoord(){
            return coord;
        }
        public void setCoord(iPair coord) {
            this.coord = coord;
        }

        public GraphCoord(iPair coord) {
            this.coord = coord;
        }

    }
}