package game;

public class Colissoes {
	
	public boolean Parede(int xCabeca, int yCabeca)
	{
		boolean colidiu=false;
		
		if((xCabeca<1)||(xCabeca>22)||(yCabeca<1)||(yCabeca>14))
			colidiu=true;
		
		return colidiu;
	}
	
	public boolean Corpo(int xCabeca, int yCabeca, int[] xCorpo, int[] yCorpo, int tamanho)
	{
		boolean colidiu=false;
		
		
		int posicao_atual;
		for(posicao_atual=1;posicao_atual<=tamanho;posicao_atual++)
		{
			if((xCabeca==xCorpo[posicao_atual])&&(yCabeca==yCorpo[posicao_atual]))
				colidiu=true;
		}
		
		return colidiu;
	}

}
