package abc.music;

import abc.sound.Pitch;
import abc.sound.SequencePlayer;

class Note implements MusicPiece {

    // rep
    private final double numBeats;
    private final Pitch pitch;

    // Abstract function
    //      AF(numBeats, pitch) = a note of pitch pitch lasting numBeats beats
    // Rep variant
    //      numBeats > 0
    // Safety from rep exposure
    //      all fields are private, final, and immutable

    Note(double numBeats, Pitch pitch) {
        this.numBeats = numBeats;
        this.pitch = pitch;
        checkRep();
    }

    private void checkRep() {
        assert numBeats > 0;
    }

    Pitch pitch() {
        return pitch;
    }

    @Override
    public double numBeats() {
        return numBeats;
    }

    @Override
    public void addPiece(SequencePlayer player, double startBeat) {
        player.addNote(pitch.toMidiNote(), startBeat, numBeats());
    }

    @Override
    public MusicPiece factorTuplet(double tupletFactor) {
        return new Note(numBeats() * tupletFactor, pitch);
    }

    @Override
    public String toString() {
        return pitch.toString() + "(" + String.format("%.3f", numBeats) + ")";
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.on(this);
    }
}
