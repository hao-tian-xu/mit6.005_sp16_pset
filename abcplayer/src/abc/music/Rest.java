package abc.music;

import abc.sound.SequencePlayer;

class Rest implements MusicPiece {

    //rep
    private final double numBeats;

    // Abstract Function
    //      AF(numBeats) = a rest lasting numBeats beats
    // Rep invariant
    //      numBeats > 0;
    // Safety from rep exposure
    //      All fields are private, final and immutable

    Rest(double numBeats) {
        this.numBeats = numBeats;
        checkRep();
    }

    private void checkRep() {
        assert numBeats > 0;
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
