package engine.graphics;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Imagem {

	private int largura, altura, blocoLuz=Luz.NADA;
	private int[] pixel;
	private boolean alpha = false;
	
	
	public Imagem(String diretorio)
	{
		BufferedImage imagem = null;
		
		try {
			imagem = ImageIO.read(Image.class.getResourceAsStream(diretorio));
		} catch (IOException e) {
			e.printStackTrace();
		}
		largura = imagem.getWidth();
		altura = imagem.getHeight();
		pixel = imagem.getRGB(0, 0, largura, altura, null, 0, largura);
		
		imagem.flush();
	}
	
	public Imagem(int[] pixel, int largura, int altura)
	{
		this.pixel=pixel;
		this.largura=largura;
		this.altura=altura;
	}
	
	public void setPrimeiroPlano(boolean alpha)
	{
		this.alpha=alpha;
	}
	
	// |==============================================================================|
	// Metodos Gets
	
	public int getLargura()
	{
		return largura;
	}
	public int getAltura()
	{
		return altura;
	}
	public int[] getPixel()
	{
		return pixel;
	}
	public int getBlocoLuz()
	{
		return blocoLuz;
	}
	
	// |==============================================================================|
	// Metodos Sets
	
	public void setLargura(int largura)
	{
		this.largura=largura;
	}
	public void setAltura(int altura)
	{
		this.altura=altura;
	}
	public void setPixel(int[] pixel)
	{
		this.pixel=pixel;
	}
	public void setBlocoLuz(int blocoLuz)
	{
		this.blocoLuz=blocoLuz;
	}
	
	// |==============================================================================|
	// Metodos Is
	
	public boolean isAlpha()
	{
		return alpha;
	}
}
