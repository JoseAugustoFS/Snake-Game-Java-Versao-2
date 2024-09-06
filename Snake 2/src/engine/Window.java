package engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Window {

	private JFrame frame;
	private BufferedImage image;
	private Canvas tela;
	private BufferStrategy buffer;
	private Graphics graficos;
	
	public Window(EngineCore nucleo)
	{
		image = new BufferedImage(nucleo.getLargura(), nucleo.getAltura(), BufferedImage.TYPE_INT_RGB);
		
		tela = new Canvas();
		Dimension tamanho = new Dimension((int)(nucleo.getLargura() * nucleo.getEscala()), (int)(nucleo.getAltura() * nucleo.getEscala()));
		tela.setPreferredSize(tamanho);
		tela.setMaximumSize(tamanho);
		tela.setMinimumSize(tamanho);
		
		frame = new JFrame(nucleo.getTitulo());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(tela,BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		tela.createBufferStrategy(2);
		buffer = tela.getBufferStrategy();
		graficos = buffer.getDrawGraphics();
	}
	
	public void update()
	{
		graficos.drawImage(image, 0, 0, tela.getWidth(), tela.getHeight(), null);
		buffer.show();
	}
	
	// |==============================================================================|
	// Metodos Gets
	
	public Canvas getTela()
	{
		return tela;
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	public JFrame getFrame()
	{
		return frame;
	}

}
