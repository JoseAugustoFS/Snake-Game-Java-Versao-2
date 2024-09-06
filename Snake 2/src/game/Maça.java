package game;

import java.util.Random;

public class Maça {

	private int x,y;
	private Random aleatorio;
	public Maça()
	{
		aleatorio= new Random();
		x=-30;
		y=-30;
	}
	
	public void DefinirLocal(int[] xCorpo, int[] yCorpo, int tamanho)
	{
		boolean tentar =true;
		while(tentar)
		{
			x=aleatorio.nextInt(22)+1;
			y=aleatorio.nextInt(14)+1;
			int local_atual,sucessos=0;
			for(local_atual=0;local_atual<=tamanho;local_atual++)
			{
				if((x!=xCorpo[local_atual])&&(y!=yCorpo[local_atual]))
				{
					sucessos++;
				}
			}
			if(sucessos==tamanho)
				tentar=false;
			else
				sucessos=0;
		}
	}
	
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	
}
