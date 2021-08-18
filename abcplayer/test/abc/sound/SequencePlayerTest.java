package abc.sound;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.util.concurrent.TimeUnit;

public class SequencePlayerTest {

    // TODO: warmup #2

    @Test
    public void test1() {
        int startTick = 0;
        int numTicks = 0;
        try {
            SequencePlayer player = new SequencePlayer(140, 12);
            // 1st
            startTick += numTicks; numTicks = 12;
            player.addNote(new Pitch('C').toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 12;
            player.addNote(new Pitch('C').toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 9;
            player.addNote(new Pitch('C').toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 3;
            player.addNote(new Pitch('D').toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 12;
            player.addNote(new Pitch('E').toMidiNote(), startTick, numTicks);
            //2nd
            startTick += numTicks; numTicks = 9;
            player.addNote(new Pitch('E').toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 3;
            player.addNote(new Pitch('D').toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 9;
            player.addNote(new Pitch('E').toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 3;
            player.addNote(new Pitch('F').toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 24;
            player.addNote(new Pitch('G').toMidiNote(), startTick, numTicks);
            // 3rd
            for (int i=0; i<3; i++) {
                startTick += numTicks; numTicks = 4;
                player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), startTick, numTicks);
            }
            for (int i=0; i<3; i++) {
                startTick += numTicks; numTicks = 4;
                player.addNote(new Pitch('G').toMidiNote(), startTick, numTicks);
            }
            for (int i=0; i<3; i++) {
                startTick += numTicks; numTicks = 4;
                player.addNote(new Pitch('E').toMidiNote(), startTick, numTicks);
            }
            for (int i=0; i<3; i++) {
                startTick += numTicks; numTicks = 4;
                player.addNote(new Pitch('C').toMidiNote(), startTick, numTicks);
            }
            // 4th
            startTick += numTicks; numTicks = 9;
            player.addNote(new Pitch('G').toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 3;
            player.addNote(new Pitch('F').toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 9;
            player.addNote(new Pitch('E').toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 3;
            player.addNote(new Pitch('D').toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 24;
            player.addNote(new Pitch('C').toMidiNote(), startTick, numTicks);

            System.out.println(player);
            player.play();

            TimeUnit.SECONDS.sleep(8);

        } catch (MidiUnavailableException | InvalidMidiDataException | InterruptedException mue) {
            mue.printStackTrace();
        }
    }

    @Test
    public void test2() {
        int startTick = 0;
        int numTicks = 0;
        try {
            SequencePlayer player = new SequencePlayer(200, 12);
            // 1st
            startTick += numTicks; numTicks = 6;
            player.addNote(new Pitch('F').transpose(1).toMidiNote(), startTick, numTicks);
            player.addNote(new Pitch('E').transpose(Pitch.OCTAVE).toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 6;
            player.addNote(new Pitch('F').toMidiNote(), startTick, numTicks);
            player.addNote(new Pitch('E').transpose(Pitch.OCTAVE).toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 6;
            player.addRest(startTick, numTicks);
            startTick += numTicks; numTicks = 6;
            player.addNote(new Pitch('F').toMidiNote(), startTick, numTicks);
            player.addNote(new Pitch('E').transpose(Pitch.OCTAVE).toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 6;
            player.addRest(startTick, numTicks);
            startTick += numTicks; numTicks = 6;
            player.addNote(new Pitch('F').toMidiNote(), startTick, numTicks);
            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 12;
            player.addNote(new Pitch('F').toMidiNote(), startTick, numTicks);
            player.addNote(new Pitch('E').transpose(Pitch.OCTAVE).toMidiNote(), startTick, numTicks);
            // 2nd
            startTick += numTicks; numTicks = 12;
            player.addNote(new Pitch('G').toMidiNote(), startTick, numTicks);
            player.addNote(new Pitch('B').toMidiNote(), startTick, numTicks);
            player.addNote(new Pitch('G').transpose(Pitch.OCTAVE).toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 12;
            player.addRest(startTick, numTicks);
            startTick += numTicks; numTicks = 12;
            player.addNote(new Pitch('G').toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 12;
            player.addRest(startTick, numTicks);
            // 3rd
            startTick += numTicks; numTicks = 18;
            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 12;
            player.addRest(startTick, numTicks);
            startTick += numTicks; numTicks = 12;
            player.addNote(new Pitch('E').toMidiNote(), startTick, numTicks);
            // 4th
            startTick += numTicks; numTicks = 6;
            player.addNote(new Pitch('E').toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 12;
            player.addNote(new Pitch('A').toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 12;
            player.addNote(new Pitch('B').toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 6;
            player.addNote(new Pitch('B').transpose(-1).toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 12;
            player.addNote(new Pitch('A').toMidiNote(), startTick, numTicks);
            // 5th
            startTick += numTicks; numTicks = 8;
            player.addNote(new Pitch('G').toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 8;
            player.addNote(new Pitch('E').transpose(Pitch.OCTAVE).toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 8;
            player.addNote(new Pitch('G').transpose(Pitch.OCTAVE).toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 12;
            player.addNote(new Pitch('A').transpose(Pitch.OCTAVE).toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 6;
            player.addNote(new Pitch('F').transpose(Pitch.OCTAVE).toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 6;
            player.addNote(new Pitch('G').transpose(Pitch.OCTAVE).toMidiNote(), startTick, numTicks);
            // 6th
            startTick += numTicks; numTicks = 6;
            player.addRest(startTick, numTicks);
            startTick += numTicks; numTicks = 12;
            player.addNote(new Pitch('E').transpose(Pitch.OCTAVE).toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 6;
            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 6;
            player.addNote(new Pitch('D').transpose(Pitch.OCTAVE).toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 9;
            player.addNote(new Pitch('B').toMidiNote(), startTick, numTicks);
            startTick += numTicks; numTicks = 9;
            player.addRest(startTick, numTicks);

            System.out.println(player);
            player.play();

            TimeUnit.SECONDS.sleep(8);

        } catch (MidiUnavailableException | InvalidMidiDataException | InterruptedException mue) {
            mue.printStackTrace();
        }
    }

}
