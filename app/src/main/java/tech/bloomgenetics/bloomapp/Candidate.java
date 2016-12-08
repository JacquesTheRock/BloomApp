package tech.bloomgenetics.bloomapp;

/**
 * Created by mdric on 11/19/2016.
 */

public class Candidate {

        private int id;
        private String name;
        private int[] traits;
        private int iid;

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

        public int getImgId() {
        return iid;
    }

        public void setImgId(int iid) {
        this.iid = iid;
    }

        public void setTraits(int[] traits) {

            this.traits = traits;

        }

        public int[] getTraits() {
        return traits;
    }

        public String toString() {
            String out = "{";
            out += "\"id\": " + getId() + ",";
            out += "\"traits\": [";
            for (int i = 0; i < traits.length; i++) {
                Trait t = new Trait();
                t.setId(traits[i]);
                if (i > 0)
                    out += ", " + t.toString();
                else
                    out += t.toString();
            }
            out += "]";

            out += ", \"imageID\": " + getImgId() + "";

            out += "}";
            return out;
        }
}