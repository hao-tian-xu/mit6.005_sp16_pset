package abc.music;

import abc.sound.SequencePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Chord implements MusicPiece {

    // Rep
    private final List<MusicPiece> notes;

    // Abstract function
    //      AF(notes) = chord of notes
    // Rep invariant
    //      true
    // Safety from rep exposure
    //      All fields are private and final
    //      notes is a mutable List, so constructor and notes() makes a defensive copy from parameter

    Chord(List<MusicPiece> notes) {
        this.notes = new ArrayList<>(notes);
    }

    List<MusicPiece> notes() {
        return new ArrayList<>(notes);
    }

    @Override
    public double numBeats() {
        return notes.get(0).numBeats();
    }

    @Override
    public void addPiece(SequencePlayer player, double startBeat) {
        for (MusicPiece note: notes) note.addPiece(player, startBeat);
    }

    @Override
    public MusicPiece factorTuplet(double tupletFactor) {
        return new Chord(notes.stream().map(x -> x.factorTuplet(tupletFactor)).collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return "Chord("
             + notes.stream().map(MusicPiece::toString).reduce((x, y) -> x + " " + y).get()
             + ")";
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.on(this);
    }
}
