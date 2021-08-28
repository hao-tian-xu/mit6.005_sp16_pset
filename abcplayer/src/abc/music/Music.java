package abc.music;

import abc.grammar.AbcGrammar;
import abc.grammar.AbcParser;
import abc.sound.SequencePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * An immutable data type representing a music consisting:
 *
 * <ul>
 *
 *   <li><p> one or multiple voices of {@link MusicPiece} instance,
 *   <li><p> header information,
 *   <li><p> and number of beats per minute
 *
 * </ul>
 *
 * @author Xu Hao-Tian
 */
public class Music {

    // Rep
    // OPT-Priority：Map and List to immutable type
    private final List<MusicPiece> voiceList;
    private final Map<AbcGrammar, String> header;   // opt: optimise the DT of header
    private final int beatsPerMinute;

    // Priority-Doc: AF, RI, RE

    /**
     * construct a Music instance
     * @param voiceList List of {@link MusicPiece} instances
     * @param header header info from {@link AbcParser}
     * @param beatsPerMinute Number of beats per minute, >0
     */
    public Music(List<MusicPiece> voiceList, Map<AbcGrammar, String> header, int beatsPerMinute) {
        this.voiceList = voiceList;
        this.header = new HashMap<>(header);
        this.beatsPerMinute = beatsPerMinute;
    }

    /**
     * @param notes an ABC-format notes
     * @return Music instance from notes
     */
    public static Music parseNotes(String notes) throws Exception {
        return new AbcParser().parse(notes);
    }

    /**
     * @return Number of beats per minute in this music
     */
    public int beatsPerMinute() {
        return beatsPerMinute;
    }

    /**
     * add list of voices to player starting at warmupBeats
     * @param player player to add voiceList to
     * @param warmupBeat number of beats to wait to start play
     */
    public void addVoices(SequencePlayer player, double warmupBeat) {
        for (MusicPiece voice : voiceList) {
            voice.addPiece(player, warmupBeat);
        }
    }

    /**
     * @return music header in printable format
     */
    public String header() {
        return "Title: "        + header.get(AbcGrammar.FIELD_TITLE) +
               "\nComposer: "   + header.get(AbcGrammar.FIELD_COMPOSER) +
               "\nKey: "        + header.get(AbcGrammar.FIELD_KEY);
    }
}
