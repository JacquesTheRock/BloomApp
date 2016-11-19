package tech.bloomgenetics.bloomapp;

/**
 * Created by mdric on 11/19/2016.
 */

public class Candidate {

        private int id;
        private String name;
        private int[] traits;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setTraits(int[] traits) {

            this.traits = traits;

        }

        public int[] getTraits() {
        return traits;
    }
}