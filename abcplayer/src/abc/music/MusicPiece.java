package abc.music;

import abc.sound.Pitch;
import abc.sound.SequencePlayer;

import java.util.List;

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

    /**
     * MusicPiece factory
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
}
