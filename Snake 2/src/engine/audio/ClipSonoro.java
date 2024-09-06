package engine.audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ClipSonoro {
	
	private Clip clip = null;
	private FloatControl controleGanho;
	
	public ClipSonoro(String diretorio)
	{
		try {
			InputStream fonteAudio = ClipSonoro.class.getResourceAsStream(diretorio);
			InputStream bufferEntrada = new BufferedInputStream(fonteAudio);
			AudioInputStream audio = AudioSystem.getAudioInputStream(bufferEntrada);
			AudioFormat formato = audio.getFormat();
			AudioFormat decodificado = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, formato.getSampleRate(),16,formato.getChannels(),formato.getChannels()*2,formato.getSampleRate(),false);
			AudioInputStream audioDecodificado = AudioSystem.getAudioInputStream(decodificado,audio);
			
			clip = AudioSystem.getClip();
			clip.open(audioDecodificado);
			
			controleGanho=(FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			
			
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void tocar()
	{
		if(clip==null)
			return;
		
		parar();
		clip.setFramePosition(0);
		while(!clip.isRunning())
		{
			clip.start();
		}
	}
	
	public void parar()
	{
		if(clip.isRunning())
			clip.stop();
	}
	
	public void fechar()
	{
		parar();
		clip.drain();
		clip.close();
	}
	
	public void loop()
	{
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		tocar();
	}
	
	public void setVolume(float valor)
	{
		controleGanho.setValue(valor);
	}
	
	public boolean isTocando()
	{
		return clip.isRunning();
	}

}
