package abc.grammar;

import abc.music.Music;
import abc.sound.Pitch;
import abc.sound.SequencePlayer;
import abc.sound.SequencePlayerTest;
import edu.mit.eecs.parserlib.ParseTree;
import edu.mit.eecs.parserlib.Parser;
import edu.mit.eecs.parserlib.UnableToParseException;
import org.junit.Test;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class AbcParserTest {

    // init test data
    final static AbcParser abcParser = new AbcParser();

    final String file1 = "sample_abc/piece1.abc";
    final String musicSheet1 = new String(Files.readAllBytes(Paths.get(file1)));
    final ParseTree<AbcGrammar> musicTree1 = makeTree(AbcGrammar.MUSIC, musicSheet1);

    final String file2 = "sample_abc/piece2.abc";
    final String musicSheet2 = new String(Files.readAllBytes(Paths.get(file2)));
    final ParseTree<AbcGrammar> musicTree2 = makeTree(AbcGrammar.MUSIC, musicSheet2);

    final String file3 = "sample_abc/sample3.abc";
    final String musicSheet3 = new String(Files.readAllBytes(Paths.get(file3)));
    final ParseTree<AbcGrammar> musicTree3 = makeTree(AbcGrammar.MUSIC, musicSheet3);

    final String file4 = "sample_abc/fur_elise.abc";
    final String musicSheet4 = new String(Files.readAllBytes(Paths.get(file4)));
    final ParseTree<AbcGrammar> musicTree4 = makeTree(AbcGrammar.MUSIC, musicSheet4);

    public AbcParserTest() throws IOException {}

    @Test
    public void testParse1() throws MidiUnavailableException, InvalidMidiDataException, IOException {
        String musicSheet = new String(Files.readAllBytes(Paths.get(file1)));
        Music music = abcParser.parse(musicSheet);
        SequencePlayer player = new SequencePlayer(music.beatsPerMinute(), 12);
        music.addVoices(player, 0);
        SequencePlayer expected = SequencePlayerTest.player1();
        assertEquals(expected.toString(), player.toString());
    }

    @Test
    public void testParse2() throws MidiUnavailableException, InvalidMidiDataException, IOException {
        String musicSheet = new String(Files.readAllBytes(Paths.get(file3)));
        Music music = abcParser.parse(musicSheet);
        SequencePlayer player = new SequencePlayer(music.beatsPerMinute(), 12);
        music.addVoices(player, 0);
        player.play();
        /*SequencePlayer expected = SequencePlayerTest.player1();
        assertEquals(expected.toString(), player.toString());*/
    }

    @Test
    public void testParseHeader() {
        abcParser.parseHeader(musicTree1.children().get(0));
    }

    @Test
    public void testParseMultiVoice1() {
        abcParser.parseHeader(musicTree3.children().get(0));
        abcParser.headerProcess();
        System.out.println(abcParser.parseMultiVoice(makeTree(AbcGrammar.MULTI_VOICE, musicTree3.children().get(1).text())));
    }

    @Test
    public void testParseMultiVoice2() {
        abcParser.parseHeader(musicTree4.children().get(0));
        abcParser.headerProcess();
        System.out.println(abcParser.parseMultiVoice(makeTree(AbcGrammar.MULTI_VOICE, musicTree4.children().get(1).text())).get(0));
    }

    @Test
    public void testParseVoice1() {
        String result = abcParser.parseVoice(makeTree(AbcGrammar.VOICE, musicTree1.children().get(1).text())).toString();
        String expected = "C(1.000), C(1.000), C(0.750), D(0.250), E(1.000), E(0.750), D(0.250), E(0.750), F(0.250), G(2.000), C'(0.333), C'(0.333), C'(0.333), G(0.333), G(0.333), G(0.333), E(0.333), E(0.333), E(0.333), C(0.333), C(0.333), C(0.333), G(0.750), F(0.250), E(0.750), D(0.250), C(2.000)";
        assertEquals(expected, result);
    }

    @Test
    public void testParseVoice2() {
        String result = abcParser.parseVoice(makeTree(AbcGrammar.VOICE, musicTree2.children().get(1).text())).toString();
        String expected = "Chord(^F(0.500) E'(0.500)), Chord(^F(0.500) E'(0.500)), Rest(0.500), Chord(^F(0.500) E'(0.500)), Rest(0.500), Chord(^F(0.500) C'(0.500)), Chord(^F(1.000) E'(1.000)), Chord(G(1.000) B(1.000) G'(1.000)), Rest(1.000), G(1.000), Rest(1.000), C'(1.500), G(0.500), Rest(1.000), E(1.000), E(0.500), A(1.000), B(1.000), ^A(0.500), A(1.000), G(0.667), E'(0.667), G'(0.667), A'(1.000), F'(0.500), G'(0.500), Rest(0.500), E'(1.000), C'(0.500), D'(0.500), B(0.750), Rest(0.750)";
        assertEquals(expected, result);
    }

    @Test
    public void testProcessAccidental() {
        String result = abcParser.processAccidental(makeTree(AbcGrammar.BAR_NOTES, "c d ^c' =b, c' D")).text();
        assertEquals("c d ^c' =b, ^c' D", result);
    }

    @Test
    public void testParseBar() {
        String result = abcParser.parseBar(makeTree(AbcGrammar.BAR_NOTES, "c/8 d ^c' =b, ^c' D")).toString();
        String expected = "C'(0.125), D'(1.000), ^C''(1.000), B(1.000), ^C''(1.000), D(1.000)";
        assertEquals(expected, result);
    }

    @Test
    public void testParsePitch() {
        String note = "^^c";
        ParseTree<AbcGrammar> tree = makeTree(AbcGrammar.PITCH, note);
        Pitch pitch = abcParser.parsePitch(tree);
        assertEquals(74, pitch.toMidiNote());
    }

    @Test
    public void testParseNumber() {
        // TUPLET_SPEC
        assertEquals(2.0/3, abcParser.parseNumber(makeTree(AbcGrammar.TUPLET_SPEC, "(3")), 0.0);
        // NOTE_LENGTH
        assertEquals(0.25, abcParser.parseNumber(makeTree(AbcGrammar.NOTE_LENGTH, "/4")), 0.0);
        assertEquals(0.5, abcParser.parseNumber(makeTree(AbcGrammar.NOTE_LENGTH, "/")), 0.0);
        assertEquals(2, abcParser.parseNumber(makeTree(AbcGrammar.NOTE_LENGTH, "2")), 0.0);
        assertEquals(0.125, abcParser.parseNumber(makeTree(AbcGrammar.NOTE_LENGTH, "1/8")), 0.0);
        // METER
        assertEquals(1, abcParser.parseNumber(makeTree(AbcGrammar.METER, "C")), 0.0);
        assertEquals(1, abcParser.parseNumber(makeTree(AbcGrammar.METER, "C|")), 0.0);
        assertEquals(0.5, abcParser.parseNumber(makeTree(AbcGrammar.METER, "2/4")), 0.0);
        // METER_FRACTION
        assertEquals(0.5, abcParser.parseNumber(makeTree(AbcGrammar.METER_FRACTION, "2/4")), 0.0);
        // NOTE_LENGTH_STRICT
        assertEquals(0.5, abcParser.parseNumber(makeTree(AbcGrammar.NOTE_LENGTH_STRICT, "2/4")), 0.0);
        // NUMBER
        assertEquals(8, abcParser.parseNumber(makeTree(AbcGrammar.NUMBER, "8")), 0.0);
    }

    @Test
    public void testProcessHeader() {
        abcParser.parseHeader(musicTree1.children().get(0));
        abcParser.headerProcess();
    }

    /**
     * @param root the desired root non-terminal in the grammar
     * @param notes notes to parse
     * @return ParseTree from parsed notes
     */
    public ParseTree<AbcGrammar> makeTree(AbcGrammar root, String notes) {
        try {
            final String abcGrammar = "src/abc/grammar/AbcGrammar.g";
            Parser<AbcGrammar> parser = Parser.compile(new File(abcGrammar), root);
            return parser.parse(notes);
        } catch (UnableToParseException | IOException e) {
            e.printStackTrace();
        }
        throw new AssertionError("unable to make ParseTree");
    }








}
