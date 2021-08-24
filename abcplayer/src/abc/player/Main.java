package abc.player;

import abc.music.Music;
import abc.sound.SequencePlayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Main entry point of your application.
 */
public class Main {

    private static final int TICKS_PER_BEAT = 12;

    public static void main(String[] args) throws Exception {
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("name of file to play >");

            final String input = in.readLine();
            if (input.isEmpty()) return;
            String path = "sample_abc/" + input;

            File f = new File(path);
            if (f.exists()) play(path);
            else System.out.println("file doesn't exist");
        }
    }

    /**
     * Plays the input file using Java MIDI API and displays
     * header information to the standard output stream.
     * 
     * (Your code should not exit the application abnormally using
     * System.exit().)
     * 
     * @param file the name of input abc file
     */
    public static void play(String file) throws Exception {
        // read file
        String musicSheet = new String(Files.readAllBytes(Paths.get(file)));
        // parse AST from file
        Music music = Music.parseNotes(musicSheet);
        // use parsed Music instance to build track
        SequencePlayer player = new SequencePlayer(music.beatsPerMinute(), TICKS_PER_BEAT);
        music.addVoices(player, 1);
        // print header
        System.out.println(music.header());
        // play track
        player.play();
    }
}
