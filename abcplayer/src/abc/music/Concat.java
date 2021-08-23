package abc.music;

import abc.sound.SequencePlayer;

class Concat implements MusicPiece {

    // rep
    private final MusicPiece m1;
    private final MusicPiece m2;

    //  safety from rep exposure argument
    //      All fields are private, final and immutable

    public Concat(MusicPiece m1, MusicPiece m2) {
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
