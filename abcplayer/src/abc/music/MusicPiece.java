package abc.music;

import abc.sound.Pitch;
import abc.sound.SequencePlayer;

import java.util.List;
import java.util.function.Function;

/**
 * An immutable data type representing a music piece of:
 *      Concat (two consecutive music pieces),
 *      Chord (several music pieces to play at same time),
 *      Note, and Rest
 */
public interface MusicPiece {

    // Datatype Definition:
    //    Music = Note(numBeats:double, pitch: Pitch)
    //          + Rest(numBeats:double)
    //          + Chord(notes: List<MusicPiece>)
    //          + Concat(m1:MusicPiece, m2:MusicPiece)

    ///////////////
    // FACTORY
    ///////////////

    /**
     * MusicPiece factory: single piece
     * @param musicPiece instance of MusicPiece
     * @return same as parameter
     */
    static MusicPiece make(MusicPiece musicPiece) {
        return musicPiece;
    }

    /**
     * MusicPiece factory: Concat
     * @param m1 instance of MusicPiece
     * @param m2 instance of MusicPiece
     * @return instance of Concat with m1 and m2
     */
    static MusicPiece make(MusicPiece m1, MusicPiece m2) {
        return new Concat(m1, m2);
    }

    /**
     * MusicPiece factory: Chord
     * @param notes List of Note instances
     * @return instance of Chord consisting notes
     */
    static MusicPiece make(List<MusicPiece> notes) {
        return new Chord(notes);
    }

    /**
     * MusicPiece factory: Rest
     * @param numBeats number of beats to rest
     * @return instance of Rest lasting numBeats
     */
    static MusicPiece make(double numBeats) {
        return new Rest(numBeats);
    }

    /**MusicPiece factory: Note
     * @param numBeats number of beats of the note
     * @param pitch pitch of the note
     * @return instance of Note with pitch lasting numBeats
     */
    static MusicPiece make(double numBeats, Pitch pitch) {
        return new Note(numBeats, pitch);
    }

    ////////////////////
    // INSTANCE METHODS
    ////////////////////

    /**
     * @return total beats of this piece
     */
    double numBeats();

    /**
     * Add this piece to player
     * @param player player to play on
     * @param startBeat when to play
     */
    void addPiece(SequencePlayer player, double startBeat);

    /**
     * Adjust numBeats by tuplet factor before construct tuplet Concat
     * MusicPiece type must be Note or Chord
     * @param tupletFactor factor number for tuplet
     * @return new adjusted instance of MusicPiece
     */
    MusicPiece factorTuplet(double tupletFactor);

    // opt: update numBeats fields to have better toString representation （also for test)
    @Override
    String toString();

    ////////////////////
    // VISITOR METHODS
    ////////////////////

    /**
     * Add piece to player
     * @param piece MusicPiece to add to player
     * @param player player to play on
     * @param startBeat when to play
     */
    public static void addPiece(MusicPiece piece, SequencePlayer player, double startBeat) {
        piece.accept(makeVisitor(
                (Note note) -> {
                    player.addNote(note.pitch().toMidiNote(), startBeat, note.numBeats());
                    return null;
                },
                (Rest rest) -> null,
                (Chord chord) -> {
                    for (MusicPiece note : chord.notes()) MusicPiece.addPiece(note, player, startBeat);
                    return null;
                },
                (Concat concat) -> {
                    MusicPiece.addPiece(concat.m1(), player, startBeat);
                    MusicPiece.addPiece(concat.m2(), player, startBeat + concat.m1().numBeats());
                    return null;
                }
        ));
    }

    /////////////////////////
    // VISITOR INFRASTRUCTURE
    /////////////////////////

    /**
     * Represents a function on different kinds of {@link MusicPiece}s
     * @param <R> the type of the result of the function
     */
    interface Visitor<R> {
        R on(Note note);
        R on(Rest rest);
        R on(Chord chord);
        R on(Concat concat);
    }

    /**
     * Call a function on this Formula.
     * @param <R> the type of the result
     * @param visitor the function to call
     * @return function applied to this
     */
    <R> R accept(Visitor<R> visitor);

    /**
     * @return a visitor object whose on(T) method calls the onT function parameter,
     *         for all T that are concrete variants of Formula
     */
    public static <R> Visitor<R> makeVisitor(
            Function<Note,R> onVariable,
            Function<Rest,R> onNot,
            Function<Chord,R> onAnd,
            Function<Concat,R> onOr
    ) {
        return new Visitor<R>() {
            public R on(Note note) { return onVariable.apply(note); }
            public R on(Rest rest) { return onNot.apply(rest); }
            public R on(Chord chord) { return onAnd.apply(chord); }
            public R on(Concat concat) { return onOr.apply(concat); }
        };
    }



}

























