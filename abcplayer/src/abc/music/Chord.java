package abc.music;

import abc.sound.SequencePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Chord implements MusicPiece {

    // rep
    private final List<MusicPiece> notes;

    //  safety from rep exposure argument
    //      All fields are private and final
    //      notes is a mutable List, so constructor makes a defensive copy from parameter

    Chord(List<MusicPiece> notes) {
        this.notes = new ArrayList<>(notes);
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
}
