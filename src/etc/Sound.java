package etc;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	private Clip clip;
	
	public Sound(String path) {
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(getClass().getResource(path));		
			
			AudioFormat format = audio.getFormat();
			AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), format.getChannels()*2, format.getSampleRate(), false);			
			
			AudioInputStream decodedAudio = AudioSystem.getAudioInputStream(decodeFormat, audio);
			
			clip = AudioSystem.getClip();
			clip.open(decodedAudio);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		if(clip == null)
			return;
		stop();
		clip.setFramePosition(0);
		clip.start();
	}
	
	public void stop() {
		if(clip.isRunning())
			clip.stop();
	}
	
	public void close() {
		stop();
		clip.close();
	}
}
