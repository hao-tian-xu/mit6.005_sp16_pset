package abc.music;

import abc.sound.Pitch;
import abc.sound.SequencePlayer;

class Note implements MusicPiece {

    // rep
    private final double numBeats;
    private final Pitch pitch;

    public Note(double numBeats, Pitch pitch) {
        this.numBeats = numBeats;
        this.pitch = pitch;
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
}
