package abc.grammar;

import abc.music.*;
import abc.sound.Pitch;
import edu.mit.eecs.parserlib.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class AbcParser {

    // opt: header map DT
    /*final String NUMBER = "X";
    final String TITLE = "T";
    final String COMPOSER = "C";
    final String DEFAULT_LENGTH = "L";
    final String METER = "M";
    final String TEMPO = "Q";
    final String KEY = "K";*/

    // rep
    private final Map<AbcGrammar, String> headerMap = new HashMap<>();
    private Set<String> voiceSet = new HashSet<>();
    private int beatsPerMinute, keySignatureTranspose;
    private double meter, tempoNum, tempoLength, defaultLength;
    private String keySignatureNotes = "";

    private final Parser<AbcGrammar> musicParser;
    private final Parser<AbcGrammar> multiVoiceParser;
    private final Parser<AbcGrammar> voiceParser;
    private final Parser<AbcGrammar> barParser;
    // rep initializer
    {
        try {
            final String abcGrammar = "src/abc/grammar/AbcGrammar.g";
            musicParser = Parser.compile(new File(abcGrammar), AbcGrammar.MUSIC);
            multiVoiceParser = Parser.compile(new File(abcGrammar), AbcGrammar.MULTI_VOICE);
            voiceParser = Parser.compile(new File(abcGrammar), AbcGrammar.VOICE);
            barParser = Parser.compile(new File(abcGrammar), AbcGrammar.BAR_NOTES);
        } catch (UnableToParseException | IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("invalid grammar");
        }
    }

    //  safety from rep exposure argument
    //      All fields are private, final and immutable

    //////////////////
    // MAIN PARSER
    //////////////////
    /**
     * Parse notes to Music instance
     * @param notes An ABC-format notes
     * @return A Music instance parsed from notes
     */
    public Music parse(String notes) {
        try {
            // parse music
            ParseTree<AbcGrammar> tree = musicParser.parse(notes);
            // parse header
            parseHeader(tree.children().get(0));
            headerProcess();
            // body
            String body = tree.children().get(1).text();
            List<MusicPiece> voiceList;
            // parse single voice
            if (voiceSet.isEmpty())
                voiceList = Arrays.asList(parseVoice(voiceParser.parse(body)));
            // parse multi voices
            else {
                ParseTree<AbcGrammar> treeVoices = multiVoiceParser.parse(body);
                List<String> voices = parseMultiVoice(treeVoices);
                // parse voice
                voiceList = new ArrayList<>();
                ParseTree<AbcGrammar> treeVoice;
                for (String voice : voices) {
                    treeVoice = voiceParser.parse(voice);
                    voiceList.add(parseVoice(treeVoice));
                }
            }
            // build Music instance
            return new Music(voiceList, headerMap, beatsPerMinute);
        } catch (UnableToParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("invalid expression");
        }
    }

    //////////////////
    // HELPERS
    //////////////////
    void parseHeader(ParseTree<AbcGrammar> tree) {
        switch (tree.name()) {
            case HEADER:
            case OTHER_FIELDS:
                for (ParseTree<AbcGrammar> child : tree.children()) parseHeader(child);
                break;
            case FIELD_NUMBER:
                break;
            case FIELD_DEFAULT_LENGTH:
                defaultLength = parseNumber(tree.children().get(0));
                headerMap.put(tree.name(), tree.children().get(0).text());
                break;
            case FIELD_TEMPO:
                parseHeader(tree.children().get(0));
                headerMap.put(tree.name(), tree.children().get(0).text());
                break;
            case FIELD_METER:
                meter = parseNumber(tree.children().get(0));
                headerMap.put(tree.name(), tree.children().get(0).text());
                break;
            case FIELD_TITLE:
            case FIELD_COMPOSER:
            case FIELD_KEY:
                headerMap.put(tree.name(), tree.children().get(0).text());
                break;
            case TEMPO:
                tempoLength = parseNumber(tree.children().get(0));
                tempoNum = parseNumber(tree.children().get(1));
                break;
            case FIELD_VOICE:
                voiceSet.add(tree.children().get(0).text());
                break;
        }
    }

    List<String> parseMultiVoice(ParseTree<AbcGrammar> tree) {
        Map<String, StringBuilder> voiceMap = new HashMap<>();
        for (String voice_signature : voiceSet) voiceMap.put(voice_signature, new StringBuilder());
        for (ParseTree<AbcGrammar> child : tree.children()) {
            if (child.children().size() == 3) {
                String voice_signature = child.children().get(0).text();
                if (voiceSet.contains(voice_signature))
                    voiceMap.get(voice_signature).append(child.children().get(2).text());
                else throw new AssertionError("voice doesn't exist");
            }
        }
        return voiceMap.values().stream().map(StringBuilder::toString).collect(Collectors.toList());
    }

    MusicPiece parseVoice(ParseTree<AbcGrammar> tree) {
        switch (tree.name()) {
            // parse bar_notes to deal with accidentals
            case BAR_NOTES:
                try {
                    return parseBar(barParser.parse(processAccidental(tree.text())));
                } catch (UnableToParseException e) {
                    e.printStackTrace();
                    throw new IllegalArgumentException("invalid expression");
                }
            // process repeat
            case REPEAT:
                List<ParseTree<AbcGrammar>> repeated = Arrays.asList(
                        tree.children().get(0), tree.children().get(1),
                        tree.children().get(0), tree.children().get(1)
                );
                // Concat all parts
                return repeated.stream().map(this::parseVoice).reduce(MusicPiece::make).get();
            case NTH_REPEAT:
                List<ParseTree<AbcGrammar>> nthRepeated = Arrays.asList(
                        tree.children().get(0), tree.children().get(1),
                        tree.children().get(0), tree.children().get(2)
                );
                // Concat all parts
                return nthRepeated.stream().map(this::parseVoice).reduce(MusicPiece::make).get();
            // repeat
            case REPEAT_PHRASE: case FIRST_REPEAT: case SECOND_REPEAT:
            // voice
            case VOICE: case SECTION:
            // phrase
            case PHRASE:
            // bar
            case BAR_ELEMENT:
                // Concat all children
                return tree.children().stream().map(this::parseVoice).reduce(MusicPiece::make).get();
            default:
                throw new AssertionError("parseVoice: should never get here " + tree.name());
        }
    }

    String processAccidental(String barNotes) {
        final String regex = "(?<accidental>([\\^_]{1,2}|=)(?<note>[a-gA-G][,']*))(?<rest>[^\\r\\n]*)";
        final Matcher matcher = Pattern.compile(regex).matcher(barNotes);
        if (matcher.find()) {
            String rest = processAccidental(matcher.group("rest"));
            String regex_subst = "(?<![\\^_=])" + matcher.group("note");
            String replace = matcher.group("accidental")
                    + rest.replaceAll(regex_subst, matcher.group("accidental"));
            barNotes = matcher.replaceAll(replace);
        }
//            System.out.println(barNotes);
//            return barParser.parse(barNotes);
        return barNotes;
    }

    MusicPiece parseBar(ParseTree<AbcGrammar> tree) {
        switch (tree.name()) {
            case BAR_NOTES:
                // element
            case ELEMENT:
            case NOTE_ELEMENT:
                // Concat all children
                return tree.children().stream().map(this::parseBar).reduce(MusicPiece::make).get();
            case NOTE:
                // note length
                double lengthNote;
                if (tree.children().size() == 1) lengthNote = 1;
                else lengthNote = parseNumber(tree.children().get(1));
                // pitch or rest
                ParseTree<AbcGrammar> treeNote = tree.children().get(0).children().get(0);
                switch (treeNote.name()) {
                    case REST:
                        return MusicPiece.make(lengthNote);
                    case PITCH:
                        Pitch pitch = parsePitch(treeNote);
                        return MusicPiece.make(lengthNote, pitch);
                }
            case MULTI_NOTE:
                return MusicPiece.make(
                        tree.children().stream().map(this::parseBar).collect(Collectors.toList())
                );
            case TUPLET_ELEMENT:
                double tupletFactor = parseNumber(tree.children().get(0));
                return tree.children().subList(1, tree.children().size()).  // exclude tuplet_spec
                        stream().map(this::parseBar).map(x -> x.factorTuplet(tupletFactor)).
                        reduce(MusicPiece::make).get();
            default:
                throw new AssertionError("parseBar: should never get here " + tree.name());
        }
    }

    Pitch parsePitch(ParseTree<AbcGrammar> tree) {
        final String regex = "(?<accidental>([\\^_]{1,2}|=))?(?<note>[a-gA-G])(?<octave>[,']+)?";
        final Matcher matcher = Pattern.compile(regex).matcher(tree.text());
        if (matcher.find()) {
            int transpose = 0;
            String accidental = matcher.group("accidental");
            String note = matcher.group("note");
            String octave = matcher.group("octave");
            // accidental
            if (accidental != null)
                switch (accidental) {
                    case "__":  transpose --;
                    case "_":   transpose --; break;
                    case "^^":  transpose ++;
                    case "^":   transpose ++; break;
                    case "=":   break;
                    default: throw new AssertionError("wrong accidental format");
                }
                // else if affected by header key
            else if (keySignatureNotes.contains(note.toUpperCase()))
                transpose += keySignatureTranspose;
            // octave
            if (octave != null) {
                if (octave.contains(",")) transpose -= Pitch.OCTAVE * octave.length();
                else transpose += Pitch.OCTAVE * octave.length();
            }
            // note
            if (Character.isLowerCase(note.charAt(0))) transpose += Pitch.OCTAVE;
            // return pitch
            return new Pitch(note.toUpperCase().charAt(0)).transpose(transpose);
        }
        throw new AssertionError("wrong pitch format");
    }

    double parseNumber(ParseTree<AbcGrammar> tree) {
        switch (tree.name()) {
            case TUPLET_SPEC:
                switch (Integer.parseInt(tree.text().substring(1,2))) {
                    case 2: return 3.0/2;
                    case 3: return 2.0/3;
                    case 4: return 3.0/4;
                    default: return 1.0;
                }
            case NOTE_LENGTH:
                final String regex = "(?<numerator>\\d+)?(?<slash>/(?<denominator>\\d+)?)?";
                Matcher matcher = Pattern.compile(regex).matcher(tree.text());
                double length = 1.0;
                if (matcher.find()) {
                    String numerator = matcher.group("numerator");
                    String slash = matcher.group("slash");
                    String denominator = matcher.group("denominator");
                    if (numerator != null)
                        length *= Integer.parseInt(numerator);
                    if (slash != null) {
                        if (denominator != null)
                            length /= Integer.parseInt(denominator);
                        else length /= 2;
                    }
                }
                return length;
            case METER:
                if (tree.text().matches("C\\|?")) return 1;
                else return parseNumber(tree.children().get(0));
            case METER_FRACTION:
            case NOTE_LENGTH_STRICT:
                return parseNumber(tree.children().get(0)) / parseNumber(tree.children().get(1));
            case NUMBER:
                return Double.parseDouble(tree.text());
            default:
                throw new AssertionError("parseNumber: should never get here");
        }
    }

    //////////////////
    // HEADER
    //////////////////
    void headerProcess() {
        // Default setting
        // When the field M is omitted, the default meter is 4/4.
        // When the field L is omitted, a unit note length is set based on the meter. If the meter is less than 0.75, the default unit note length is a sixteenth note. If the meter is 0.75 or greater, it is an eighth note. For example, 2/4 = 0.5, so, the default unit note length is a sixteenth note, while for 4/4 = 1.0, or 6/8 = 0.75, or 3/4= 0.75, it is an eighth note. Notice that if neither M nor L fields are present, the default note length is an eighth.
        // When the field Q is omitted, the default tempo is 100 beats per minute, where a beat is the default note length given by field L. Notice, however, that when Q is specified, it includes its own beat length, and the tempo is not necessarily based on L.
        // When the field C is omitted, any reasonable string will suffice (e.g. Unknown).

        // default meter
        if (!headerMap.containsKey(AbcGrammar.FIELD_METER)) {
            headerMap.put(AbcGrammar.FIELD_METER, "4/4");
            meter = 1;
        }
        // default defaultLength
        if (!headerMap.containsKey(AbcGrammar.FIELD_DEFAULT_LENGTH)) {
            if (meter < 0.75) {
                headerMap.put(AbcGrammar.FIELD_DEFAULT_LENGTH, "1/16");
                defaultLength = 1.0/16;
            }
            else {
                headerMap.put(AbcGrammar.FIELD_DEFAULT_LENGTH, "1/8");
                defaultLength = 1.0/8;
            }
        }
        // default tempo
        if (!headerMap.containsKey(AbcGrammar.FIELD_TEMPO)) {
            headerMap.put(AbcGrammar.FIELD_TEMPO, headerMap.get(AbcGrammar.FIELD_DEFAULT_LENGTH) + "=100");
            tempoLength = defaultLength;
            tempoNum = 100;
        }
        // default composer
        if (!headerMap.containsKey(AbcGrammar.FIELD_COMPOSER))
            headerMap.put(AbcGrammar.FIELD_COMPOSER, "Unknown");
        // calculate beats per minute
        beatsPerMinute = (int) Math.round(tempoNum * tempoLength / defaultLength);
        // initialize key signature data
        switch (headerMap.get(AbcGrammar.FIELD_KEY)) {
            case "C#":  case "A#m": keySignatureNotes += "B";
            case "F#":  case "D#m": keySignatureNotes += "E";
            case "B":   case "G#m": keySignatureNotes += "A";
            case "E":   case "C#m": keySignatureNotes += "D";
            case "A":   case "F#m": keySignatureNotes += "G";
            case "D":   case "Bm":  keySignatureNotes += "C";
            case "G":   case "Em":  keySignatureNotes += "F"; keySignatureTranspose = 1; break;
            case "Cb":  case "Abm": keySignatureNotes += "F";
            case "Gb":  case "Ebm": keySignatureNotes += "C";
            case "Db":  case "Bbm": keySignatureNotes += "G";
            case "Ab":  case "Fm":  keySignatureNotes += "D";
            case "Eb":  case "Cm":  keySignatureNotes += "A";
            case "Bb":  case "Gm":  keySignatureNotes += "E";
            case "F":   case "Dm":  keySignatureNotes += "B"; keySignatureTranspose = -1; break;
            case "C":   case "Am":  break;
            default:    throw new AssertionError("wrong key signature format");
        }
    }

    String headerInfo() {
        return headerMap.toString();
    }




}







































