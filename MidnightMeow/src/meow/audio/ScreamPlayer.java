package meow.audio;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class ScreamPlayer {

    private Clip clip;

    public void playScream() {
        try {
            InputStream audioStream = getClass().getResourceAsStream(
                    "/resources/sounds/scream.wav"
            );

            if (audioStream == null) {
                System.out.println("Scream file not found!");
                return;
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(audioStream)
            );

            clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();

        } catch (Exception e) {
        }
    }

    public void stopScream() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
    }
}
