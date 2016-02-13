import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class PokemonSound implements Runnable {

	public ByteArrayOutputStream out;
	public static void main(String[] args) throws InterruptedException, IOException {
		PokemonSound ps = new PokemonSound();
		Thread t = new Thread(ps);
		t.start();
		System.out.println("---");
		PokemonTest pt = new PokemonTest();
		while(true){
			if(ps.out != null){
				System.out.println("here");
				pt.compareFingerPrintStream(ps.out.toByteArray());
			}
			System.out.println("aaa");
		}
	}
	
	public static void FFT(){
		
	}

	public static boolean running = false;

	public AudioFormat getFormat() {
		return new AudioFormat(44100.0F, 16, 1, true, false);
	}

	@Override
	public void run() {

		final AudioFormat format = getFormat(); // Fill AudioFormat with the
												// wanted settings
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		TargetDataLine line;
		try {
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start();

			byte buffer[] = new byte[8000];
			// In another thread I start:

			out = new ByteArrayOutputStream();
			running = true;

			try {
				while (running) {
					int count = line.read(buffer, 0, buffer.length);
					if (count > 0) {
						out.write(buffer, 0, count);
						System.out.println(out.toByteArray() != null);
					}
				}
				out.close();
				
			} catch (IOException e) {
				System.err.println("I/O problems: " + e);
				System.exit(-1);
			}
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
