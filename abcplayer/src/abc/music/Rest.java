package abc.music;

import abc.sound.SequencePlayer;

class Rest implements MusicPiece {

    //rep
    private final double numBeats;

    //  safety from rep exposure argument
    //      All fields are private, final and immutable

    public Rest(double numBeats) {
        this.numBeats = numBeats;
    }

    @Override
    public double numBeats() {
        return numBeats;
    }

    @Override
    public void addPiece(SequencePlayer player, double startBeat) {
        // do nothing
    }

    @Override
    public MusicPiece factorTuplet(double tupletFactor) {
        throw new AssertionError("Tuplet Concat: wrong tuplet element");
    }

    @Override
    public String toString() {
        return "Rest(" + String.format("%.3f", numBeats) + ")";
    }
}
