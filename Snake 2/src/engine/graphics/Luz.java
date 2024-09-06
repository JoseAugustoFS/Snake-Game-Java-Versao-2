package engine.graphics;

public class Luz {

	private int raio, diametro, cor;
	private int[] mapaLuz;
	public static final int NADA = 0;
	public static final int CHEIO = 1;
	
	public Luz(int raio, int cor)
	{
		this.raio=raio;
		diametro=raio*2;
		this.cor=cor;
		mapaLuz = new int[diametro*diametro];
		
		int x,y;
		for(y=0;y<diametro;y++)
		{
			for(x=0;x<diametro;x++)
			{
				double distancia=Math.sqrt((x-raio)*(x-raio)+(y-raio)*(y-raio));
				
				if(distancia<raio)
				{
					double intencidade =1-(distancia/raio);
					
					mapaLuz[x+y*diametro]=((int)(((cor>>16)&0xff)*intencidade)<<16|(int)(((cor>>8)&0xff)*intencidade)<<8|(int)(((cor)&0xff)*intencidade));
				}
				else
				{
					mapaLuz[x+y*diametro]=0;
				}
			}
		}
	}
	public int getLuzValor(int x, int y)
	{
		if((x<0)||(x>=diametro)||(y<0)||(y>=diametro))
			return 0;
		return mapaLuz[x+y*diametro];
	}
	// |==============================================================================|
	// Metodos Gets

	public int getRaio() 
	{
		return raio;
	}
	public int getDiametro() 
	{
		return diametro;
	}
	public int getCor()
	{
		return cor;
	}
	public int[] getMapaLuz() 
	{
		return mapaLuz;
	}

	// |==============================================================================|
	// Metodos Sets
	
	public void setRaio(int raio) 
	{
		this.raio = raio;
	}
	public void setDiametro(int diametro) 
	{
		this.diametro = diametro;
	}
	public void setCor(int cor) 
	{
		this.cor = cor;
	}
	public void setMapaLuz(int[] mapaLuz) 
	{
		this.mapaLuz = mapaLuz;
	}
}
