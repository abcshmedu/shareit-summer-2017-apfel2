/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */

package edu.hm.models;

/**
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-04-19
 */
public class Disc extends Medium {
    private final String barcode;
    private final String director;
    private final int fsk;

    /**
     * private default constructor is needed for reflection
     */
    private Disc() {
        super("");
        barcode = "";
        director = "";
        fsk = 0;
    }

    public Disc(String title, String barcode, String director, int fsk) {
        super(title);
        this.barcode = barcode;
        this.director = director;
        this.fsk = fsk;
    }

    @Override
    public String toString() {
        return "Disc{" +
                super.toString() +
                ", barcode='" + barcode + '\'' +
                ", director='" + director + '\'' +
                ", fsk=" + fsk +
                '}';
    }

    public String getBarcode() {
        return barcode;
    }

    public String getDirector() {
        return director;
    }

    public int getFsk() {
        return fsk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Disc disc = (Disc) o;

        if (getFsk() != disc.getFsk()) return false;
        if (getBarcode() != null ? !getBarcode().equals(disc.getBarcode()) : disc.getBarcode() != null) return false;
        return getDirector() != null ? getDirector().equals(disc.getDirector()) : disc.getDirector() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getBarcode() != null ? getBarcode().hashCode() : 0);
        result = 31 * result + (getDirector() != null ? getDirector().hashCode() : 0);
        result = 31 * result + getFsk();
        return result;
    }
}
