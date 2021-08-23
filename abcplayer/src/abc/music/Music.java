package abc.music;

import abc.grammar.AbcGrammar;
import abc.grammar.AbcParser;
import abc.sound.SequencePlayer;

import java.util.List;
import java.util.Map;

public class Music {
    private final List<MusicPiece> voiceList;
    private final Map<AbcGrammar, String> header;   // todo opt: optimise the DT of header
    private final int beatsPerMinute;

    /**
     * construct a Music instance
     * @param voiceList List of Voices
     * todo opt: header DT
     * @param beatsPerMinute Number of beats per minute, >0
     */
    public Music(List<MusicPiece> voiceList, Map<AbcGrammar, String> header, int beatsPerMinute) {
        this.voiceList = voiceList;
        this.header = header;
        this.beatsPerMinute = beatsPerMinute;
    }

    /**
     * @return Music instance from an ABC-format ntoes
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

    public void addVoices(SequencePlayer player, double warmupBeat) {
        for (MusicPiece voice : voiceList) {
            voice.addPiece(player, warmupBeat);
        }
    }

    /**
     * @return String of the header of this music
     */
    public String header() {
        // todo last: implement header string method
        return "";
    }
}
