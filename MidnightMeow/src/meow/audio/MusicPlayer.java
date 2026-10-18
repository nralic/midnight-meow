package meow.audio;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MusicPlayer {

    private Clip clip;

    public MusicPlayer() {
        try {
            InputStream audioStream = getClass().getResourceAsStream(
                    "/resources/sounds/halloween.wav"
            );

            if (audioStream == null) {
                System.out.println("Music file not found!");
                return;
            }

            try (AudioInputStream audio = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(audioStream))) {

                clip = AudioSystem.getClip();
                clip.open(audio);
            }

        } catch (Exception e) {
            System.out.println("Error loading music:");
            e.printStackTrace();
        }
    }

    public void playMusic() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0);
        }
    }
}
