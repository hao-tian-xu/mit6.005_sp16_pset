package abc.music;

import abc.sound.SequencePlayer;

class Concat implements MusicPiece {

    // rep
    private final MusicPiece m1;
    private final MusicPiece m2;

    // Abstract function
    //      AF(m1, m2) = a music piece composed of two consecutive music pieces m1 and m2
    // Rep invariant
    //      true
    // Safety from rep exposure
    //      All fields are private, final and immutable

    Concat(MusicPiece m1, MusicPiece m2) {
        this.m1 = m1;
        this.m2 = m2;
    }

    @Override
    public double numBeats() {
        return m1.numBeats() + m2.numBeats();
    }

    @Override
    public void addPiece(SequencePlayer player, double startBeat) {
        m1.addPiece(player, startBeat);
        m2.addPiece(player, startBeat+m1.numBeats());
    }

    @Override
    public MusicPiece factorTuplet(double tupletFactor) {
        throw new AssertionError("Tuplet Concat: wrong tuplet element");
    }

    @Override
    public String toString() {
        return m1.toString() + ", " + m2.toString();
    }
}
