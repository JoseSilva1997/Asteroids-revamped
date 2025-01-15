package org.example.asteroidsrevamped;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * This class manages the game's sound effects and background music. It uses a singleton pattern
 * to ensure only one instance exists and provides methods to play and stop various sounds.
 *
 * @author Jose Silva
 */
public class SoundPool {

    /**
     * The single instance of the SoundManager class.
     */
    private static SoundPool instance;

    // Background music. Accessible throughout the application
    private final URL backgroundMusicUrl;
    private Clip backgroundMusicClip;

    // Asteroid colliding with spaceship
    private final Clip[] collisionClipPool = new Clip[3];
    private int currentCollisionClipIndex = 0;

    // Pool of sounds for asteroids being destroyed
    private final Clip[] destructionClipPool = new Clip[3];
    private int currentDestructionClipIndex = 0;

    // Leveling up.
    private static Clip levelUpClip;

    // Pool of laser sounds
    private final Clip[] laserSoundPool = new Clip[5];
    private int currentLaserClipIndex = 0;

    // Pause sounds
    private Clip pauseClip;
    private Clip unpauseClip;

    // Game over sound
    private Clip gameOverClip;

    /**
     * Private constructor to enforce singleton pattern and initialize sound pools.
     *
     * @throws UnsupportedAudioFileException if the audio file format is unsupported
     * @throws IOException                   if an I/O error occurs during file reading
     * @throws LineUnavailableException      if a line cannot be opened due to resource restrictions
     */
    private SoundPool() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // Background music
        backgroundMusicUrl = SoundPool.class.getResource("/sounds/background-music.wav");

        // Initialize other sound pools but DON'T start background music automatically
        initializeSoundPools();
    }

    /**
     * Returns the singleton instance of {@code SoundPool}.
     *
     * @return the singleton instance
     * @throws UnsupportedAudioFileException if the audio file format is unsupported
     * @throws IOException                   if an I/O error occurs during file reading
     * @throws LineUnavailableException      if a line cannot be opened due to resource restrictions
     */
    public static synchronized SoundPool getInstance() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (instance == null) {
            instance = new SoundPool();
        }
        return instance;
    }

    /**
     * Initializes all sound pools and individual sound clips.
     *
     * @throws UnsupportedAudioFileException if the audio file format is unsupported
     * @throws IOException                   if an I/O error occurs during file reading
     * @throws LineUnavailableException      if a line cannot be opened due to resource restrictions
     */
    private void initializeSoundPools() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // Initialize collision sound pool
        URL collisionClipPath = SoundPool.class.getResource("/sounds/collision.wav");
        for (int i = 0; i < collisionClipPool.length; i++) {
            collisionClipPool[i] = createClip(Objects.requireNonNull(collisionClipPath));
        }

        // Initialize destruction sound pool
        URL asteroidDestructionClipPath = SoundPool.class.getResource("/sounds/asteroid-destruction.wav");
        for (int i = 0; i < destructionClipPool.length; i++) {
            destructionClipPool[i] = createClip(Objects.requireNonNull(asteroidDestructionClipPath));
        }

        // Initialize level-up sound
        URL levelUpClipPath = SoundPool.class.getResource("/sounds/level-up.wav");
        levelUpClip = createClip(Objects.requireNonNull(levelUpClipPath));
        adjustVolume(levelUpClip, -10.0f);

        // Initialize laser sound pool
        URL laserClipPath = SoundPool.class.getResource("/sounds/laser-sound.wav");
        for (int i = 0; i < laserSoundPool.length; i++) {
            laserSoundPool[i] = createClip(Objects.requireNonNull(laserClipPath));
        }

        // Initialize pause sound
        pauseClip = createClip(Objects.requireNonNull(SoundPool.class.getResource("/sounds/pause.wav")));

        // Initialize unpause sound
        unpauseClip = createClip(Objects.requireNonNull(SoundPool.class.getResource("/sounds/unpause.wav")));

        // Initialize game-over sound
        gameOverClip = createClip(Objects.requireNonNull(SoundPool.class.getResource("/sounds/game-over.wav")));
    }

    /**
     * Plays the background music on a continuous loop.
     */
    public void playBackgroundMusicClip() {
        try {
            stopBackgroundMusic();

            AudioInputStream backgroundMusicInputStream = AudioSystem.getAudioInputStream(backgroundMusicUrl);
            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(backgroundMusicInputStream);
            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl volume = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(-10.0f);

        } catch (Exception e) {
            System.err.println("Failed to start background music: " + e.getMessage());
        }
    }

    /**
     * Plays the next destruction sound from the pool.
     */
    public void playDestructionClip() {
        playClipFromPool(destructionClipPool, currentDestructionClipIndex);
        currentDestructionClipIndex = (currentDestructionClipIndex + 1) % destructionClipPool.length;
    }

    /**
     * Plays the level-up sound effect.
     */
    public static void playLevelUpClip() {
        playClip(levelUpClip);
    }

    /**
     * Plays the next collision sound from the pool.
     */
    public void playCollisionClip() {
        playClipFromPool(collisionClipPool, currentCollisionClipIndex);
        currentCollisionClipIndex = (currentCollisionClipIndex + 1) % collisionClipPool.length;
    }

    /**
     * Plays the next laser sound from the pool.
     */
    public void playLaserSound() {
        playClipFromPool(laserSoundPool, currentLaserClipIndex);
        currentLaserClipIndex = (currentLaserClipIndex + 1) % laserSoundPool.length;
    }

    /**
     * Plays the pause sound effect.
     */
    public void playPauseClip() {
        playClip(pauseClip);
    }

    /**
     * Plays the unpause sound effect.
     */
    public void playUnpauseClip() {
        playClip(unpauseClip);
    }

    /**
     * Plays the game-over sound effect.
     */
    public void playGameOverClip() {
        playClip(gameOverClip);
    }

    /**
     * Stops the background music if it is playing.
     */
    public void stopBackgroundMusic() {
        if (backgroundMusicClip != null) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
            backgroundMusicClip = null;
        }
    }

    /**
     * Cleans up resources by stopping and closing all sound clips.
     */
    public void cleanup() {

        stopAndCloseClips(collisionClipPool);
        stopAndCloseClips(destructionClipPool);
        stopAndCloseClips(laserSoundPool);

        if (levelUpClip != null) {
            levelUpClip.stop();
            levelUpClip.close();
        }

        stopBackgroundMusic();
        System.out.println("SoundPool cleanup complete");
    }

    // Helper Methods

    /**
     * Creates and returns a clip for the given file.
     */
    private Clip createClip(URL url) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(url));
        Clip clip = AudioSystem.getClip();
        clip.open(inputStream);
        return clip;
    }

    /**
     * Adjusts the volume of a clip.
     */
    private void adjustVolume(Clip clip, float value) {
        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(value);
    }

    /**
     * Plays a clip from a pool at the given index.
     */
    private void playClipFromPool(Clip[] pool, int index) {
        Clip clip = pool[index];
        playClip(clip);
    }

    /**
     * Plays a single clip.
     */
    private static void playClip(Clip clip) {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    /**
     * Stops and closes all clips in the given pool.
     */
    private void stopAndCloseClips(Clip[] pool) {
        for (Clip clip : pool) {
            if (clip != null) {
                clip.stop();
                clip.close();
            }
        }
    }
}
